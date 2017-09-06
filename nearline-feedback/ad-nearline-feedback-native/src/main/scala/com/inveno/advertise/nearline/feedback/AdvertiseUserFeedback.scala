package com.inveno.advertise.nearline.feedback

import com.inveno.advertise.nearline.common._
import com.inveno.advertise.nearline.kafka.BatchProducer
import com.inveno.advertise.nearline.log.LogSupport
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Minutes, StreamingContext, Time}


object AdvertiseUserFeedback extends LogSupport {

  val CLASS_NAME = AdvertiseUserFeedback.getClass.getSimpleName

  def handler(window_size: Int, window_num: Int, kafka_param: Map[String, String], output_topic: String, ssc: StreamingContext) = {

    val offsetPath = NearLineConfig.offset_basepath
    val zkHosts = NearLineConfig.zk_hosts

    val requestStream = StreamingFactory.createDirectStreamByOffset(
      zkHosts,
      offsetPath,
      NearLineConfig.advertiseRequestTopic.partition,
      NearLineConfig.advertiseRequestTopic.name,
      kafka_param,
      ssc)
    val requestWindow = requestStream.window(Minutes(window_size * window_num), Minutes(window_size))
    val requestStat = requestWindow.map(msg => {
      val info = ReformatDataParser.parseAdvertiseRequest(msg._2)
      info.length match {
        case len if len > 0 =>
          val key = FeedBackUtil.generateUserKey(info)
          (key, 1L)
        case _ => ("", 0L)
      }
    }).filter(e => e._1 != "").reduceByKey(ReduceUtil.reduceAddLong _, 8).map(e => (e._1, (e._2, 0L, 0L)))

    val impressionStream = StreamingFactory.createDirectStreamByOffset(
      zkHosts,
      offsetPath,
      NearLineConfig.advertiseImpressionTopic.partition,
      NearLineConfig.advertiseImpressionTopic.name,
      kafka_param,
      ssc)
    val impressionWindow = impressionStream.window(Minutes(window_size * window_num), Minutes(window_size))
    val impressionStat = impressionWindow.map(msg => {
      val info = ReformatDataParser.parseAdvertiseImpression(msg._2)
      info.length match {
        case len if len > 0 =>
          val key = FeedBackUtil.generateUserKey(info)
          (key, 1L)
        case _ => ("", 0L)
      }
    }).filter(e => e._1 != "").reduceByKey(ReduceUtil.reduceAddLong _, 8).map(e => (e._1, (0L, e._2, 0L)))

    val clickStream = StreamingFactory.createDirectStreamByOffset(
      zkHosts,
      offsetPath,
      NearLineConfig.advertiseClickTopic.partition,
      NearLineConfig.advertiseClickTopic.name,
      kafka_param,
      ssc)
    val clickWindow = clickStream.window(Minutes(window_size * window_num), Minutes(window_size))
    val clickStat = clickWindow.map(msg => {
      val info = ReformatDataParser.parseAdvertiseClick(msg._2)
      info.length match {
        case len if len > 0 =>
          val key = FeedBackUtil.generateUserKey(info)
          (key, 1L)
        case _ => ("", 0L)
      }
    }).filter(e => e._1 != "").reduceByKey(ReduceUtil.reduceAddLong _, 8).map(e => (e._1, (0L, 0L, e._2)))

    val unionResult = requestStat.union(impressionStat).union(clickStat).reduceByKey((e1, e2) => (e1._1 + e2._1, e1._2 + e2._2, e1._3 + e2._3))

    unionResult.foreachRDD((rdd, time: Time) => {
      val timestamp = TimeUtil.getCurrTimeIndex()
      rdd.foreachPartition(partitionOfRecords => {

        val producer = new BatchProducer(NearLineConfig.zk_hosts, kafka_param.get("metadata.broker.list").get)

        partitionOfRecords.foreach(keyValue => {
          val (key, value) = keyValue
          val fields = key.split(Constant.KEY_TAG)
          try {
            val output_json = FeedBackUtil.makeUserFeedBackResult(timestamp, fields, value)
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
    ssc.checkpoint(checkpoint_dir)
    ssc
  }

  def usage() {
    println(CLASS_NAME + " <group_id> <window_size> <window_num> <checkpoint_dir>")
  }

  def main(args: Array[String]) {
    if (args.length != 4) {
      usage()
      System.exit(1)
    }

    val Array(group_id, window_size, window_num, checkpoint_dir) = args
    var kafka_param = Map[String, String]()
    kafka_param += ("metadata.broker.list" -> NearLineConfig.kafka_brokers)
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