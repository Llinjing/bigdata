package com.inveno.advertise.nearline.feedback

import com.alibaba.fastjson.{JSON, JSONObject}
import com.github.panhongan.util.db.JedisUtil
import com.github.panhongan.util.sparkstreaming.StreamingFactory
import com.inveno.advertise.nearline.common.{FeedBackUtil, ReformatParser}
import com.inveno.advertise.nearline.config.AdvConfig
import com.inveno.advertise.nearline.filter.InfoProcess
import inveno.bigdata.common.{ReduceUtil, TimeUtil}
import inveno.bigdata.kafka.BatchProducer
import inveno.bigdata.log.LogSupport
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Minutes, StreamingContext, Time}
import redis.clients.jedis.Jedis


object AdvertiseFeedback extends LogSupport with InfoProcess {

  val CLASS_NAME = AdvertiseFeedback.getClass.getSimpleName

  def updateRedis(dataSet: DStream[(String, (Long, Long, Long))], flag: Boolean = true): Unit = {
    try {
      var jdClient: Jedis = null
      val hash_key = AdvConfig.redisGateInfo.hashkey
      dataSet.foreachRDD(rdd => {

        rdd.foreachPartition(partition => {
          try {

            jdClient = if (flag) new Jedis(AdvConfig.redisMalaccaInfo.server, AdvConfig.redisMalaccaInfo.port)
            else new Jedis(AdvConfig.redisGateInfo.server, AdvConfig.redisGateInfo.port)

            jdClient.connect()
            if (jdClient != null) {

              partition.foreach((info: (String, (Long, Long, Long))) => {
                val fields = info._1.split(FeedBackUtil.KEY_TAG)

                val ad_id = fields.head
                val daily_count_limit = fields(11)
                val ad_source = fields(2)
                val pay_model = fields(16)
                val log_type = fields.last

                val (request, impression, click) = info._2

                val count = if (checkPayModel(pay_model)) {
                  click
                } else if (checkLogType(log_type)) {
                  impression
                } else {
                  request
                }

                if (ad_source == "self_ad" && daily_count_limit.toInt > 0) {

                  val jdInfo = jdClient.hget(hash_key, ad_id)

                  val count_amount = if (jdInfo != null && jdInfo != "") {
                    val json: JSONObject = JSON.parseObject(jdInfo)
                    json.getString("count")
                  } else "0"

                  val pvObj = new JSONObject()
                  pvObj.put("count", count + count_amount.toInt)
                  pvObj.put("pay_model", pay_model)
                  pvObj.put("log_type", log_type)
                  pvObj.put("daily_count_limit", daily_count_limit.toInt)

                  jdClient.hset(hash_key, ad_id, pvObj.toString)
                }
              })
            }
          } catch {
            case e: Exception => log.warn(e.getMessage, e)
          }

          JedisUtil.closeJedis(jdClient)
        })
      })
    } catch {
      case ex: Exception => log.warn(ex.getMessage, ex)
    }
  }

  def handler(window_size: Int, window_num: Int, kafka_param: Map[String, String], output_topic: String, ssc: StreamingContext) = {

    val offsetPath = AdvConfig.offset_basepath
    val zkHosts = AdvConfig.zk_hosts

    val requestStream = StreamingFactory.createDirectStreamByOffset(
      zkHosts,
      offsetPath,
      AdvConfig.advertiseRequestTopic.name,
      AdvConfig.advertiseRequestTopic.partition,
      kafka_param,
      ssc,
      true)
    val requestWindow = requestStream.window(Minutes(window_size * window_num), Minutes(window_size))
    val requestStat = requestWindow.map(msg => {
      val info = ReformatParser.parseAdvertiseRequest(msg._2)
      info.length match {
        case len if len > 0 =>
          val key = FeedBackUtil.generateAdvertiseKey(info)
          (key, 1L)
        case _ => ("", 0L)
      }
    }).filter(e => e._1 != "").reduceByKey(ReduceUtil.reduceAddLong _, 8).map(e => (e._1, (e._2, 0L, 0L)))

    val impressionStream = StreamingFactory.createDirectStreamByOffset(
      zkHosts,
      offsetPath,
      AdvConfig.advertiseImpressionTopic.name,
      AdvConfig.advertiseImpressionTopic.partition,
      kafka_param,
      ssc,
      true)
    val impressionWindow = impressionStream.window(Minutes(window_size * window_num), Minutes(window_size))
    val impressionStat = impressionWindow.map(msg => {
      val info = ReformatParser.parseAdvertiseImpression(msg._2)
      info.length match {
        case len if len > 0 =>
          val key = FeedBackUtil.generateAdvertiseKey(info)
          (key, 1L)
        case _ => ("", 0L)
      }
    }).filter(e => e._1 != "").reduceByKey(ReduceUtil.reduceAddLong _, 8).map(e => (e._1, (0L, e._2, 0L)))

    val clickStream = StreamingFactory.createDirectStreamByOffset(
      zkHosts,
      offsetPath,
      AdvConfig.advertiseClickTopic.name,
      AdvConfig.advertiseClickTopic.partition,
      kafka_param,
      ssc,
      true)
    val clickWindow = clickStream.window(Minutes(window_size * window_num), Minutes(window_size))
    val clickStat = clickWindow.map(msg => {
      val info = ReformatParser.parseAdvertiseClick(msg._2)
      info.length match {
        case len if len > 0 =>
          val key = FeedBackUtil.generateAdvertiseKey(info)
          (key, 1L)
        case _ => ("", 0L)
      }
    }).filter(e => e._1 != "").reduceByKey(ReduceUtil.reduceAddLong _, 8).map(e => (e._1, (0L, 0L, e._2)))

    val unionResult = requestStat.union(impressionStat).union(clickStat).reduceByKey((e1, e2) => (e1._1 + e2._1, e1._2 + e2._2, e1._3 + e2._3))

    val malaccaResult = unionResult.filter(ds => {
      ds._1.contains("malacca-ad")
    })
    val gateResult = unionResult.filter(ds => {
      !ds._1.contains("malacca-ad")
    })

    updateRedis(malaccaResult)

    updateRedis(gateResult, false)

    unionResult.foreachRDD((rdd, time: Time) => {
      val timestamp = TimeUtil.getCurrTimeIndex()
      rdd.foreachPartition(partitionOfRecords => {

        val producer = new BatchProducer(AdvConfig.zk_hosts, kafka_param.get("metadata.broker.list").get, AdvConfig.kafka_batchSize)

        partitionOfRecords.foreach(keyValue => {
          val (key, value) = keyValue
          val fields = key.split(FeedBackUtil.KEY_TAG)
          try {
            val output_json = FeedBackUtil.makeAdvertiseFeedBackResult(timestamp, fields, value)
            producer.send(output_topic, output_json)
          } catch {
            case ex: Exception => {
              log.warn(ex.getMessage, ex)
            }
          }
        })

        producer.close()

      })
    })

  }

  def createStreamingContext(window_size: Int, window_num: Int,
                             kafka_param: Map[String, String], output_topic: String,
                             checkpoint_dir: String): StreamingContext = {
    val conf = new SparkConf()
    val ssc = new StreamingContext(conf, Minutes(window_size))
    handler(window_size, window_num, kafka_param, output_topic, ssc)
    ssc
  }

  def usage() {
    println(CLASS_NAME + "<group_id> <window_size> <window_num> <checkpoint_dir>")
  }

  def main(args: Array[String]) {
    if (args.length != 4) {
      usage()
      System.exit(1)
    }

    val Array(group_id, window_size, window_num, checkpoint_dir) = args
    var kafka_param = Map[String, String]()
    kafka_param += ("metadata.broker.list" -> AdvConfig.kafka_brokers)
    kafka_param += ("group.id" -> group_id)
    kafka_param += ("auto.offset.reset" -> "largest")

    val ssc = StreamingContext.getOrCreate(checkpoint_dir, () => {
      createStreamingContext(window_size.toInt, window_num.toInt, kafka_param, group_id, checkpoint_dir)
    })

    try {
      ssc.start()
      ssc.awaitTermination()
    } finally {
      ssc.stop()
    }
  }

}
