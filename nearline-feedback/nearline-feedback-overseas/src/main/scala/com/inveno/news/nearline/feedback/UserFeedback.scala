package com.inveno.news.nearline.feedback

import com.github.panhongan.util.sparkstreaming.StreamingFactory
import com.inveno.news.nearline.common.{FeedBackUtil, ReformatParser}
import com.inveno.news.nearline.config.OverseasConfig
import inveno.bigdata.common.{ReduceUtil, TimeUtil}
import inveno.bigdata.kafka.BatchProducer
import inveno.bigdata.log.LogSupport
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming._


object UserFeedback extends LogSupport {

  val CLASS_NAME = UserFeedback.getClass.getSimpleName

  def handler(window_size: Int, window_num: Int, kafka_param: Map[String, String], output_topic: String, ssc: StreamingContext) = {
    // (key, (request, impression, click, dwelltime))
    val offset_base = OverseasConfig.offset_basePath
    val zk_hosts = OverseasConfig.zk_hosts
    // request
    val requestStream = StreamingFactory.createDirectStreamByOffset(
      zk_hosts,
      offset_base,
      OverseasConfig.requestTopic.name,
      OverseasConfig.requestTopic.partition,
      kafka_param,
      ssc,
      true)
    val requestWindow = requestStream.window(Minutes(window_size * window_num), Minutes(window_size))
    val requestDistinct = requestWindow.map(msg => (msg._2, msg._1)).reduceByKey(ReduceUtil.reduceUniqText _, 16)
    val requestStat = requestDistinct.flatMap(msg => {
      val info = ReformatParser.parseOverSeasRequest(msg._1)
      info.isValid match {
        case true =>
          val key = FeedBackUtil.makeOverseasUserKey(info)
          List((key, 1L))
        case _ => Nil
      }
    }).reduceByKey(ReduceUtil.reduceAddLong _, 16).map(x => (x._1, (x._2, 0L, 0L, 0L))) //.checkpoint(Minutes(window_size))

    // impression
    val impressionStream = StreamingFactory.createDirectStreamByOffset(
      zk_hosts,
      offset_base,
      OverseasConfig.impressionTopic.name,
      OverseasConfig.impressionTopic.partition,
      kafka_param,
      ssc,
      true)
    val impressionWindow = impressionStream.window(Minutes(window_size * window_num), Minutes(window_size))
    val impressionDistinct = impressionWindow.map(msg => (msg._2, msg._1)).reduceByKey(ReduceUtil.reduceUniqText _, 16)
    val impressionStat = impressionDistinct.flatMap(msg => {
      val info = ReformatParser.parseOverSeasImpression(msg._1)
      info.isValid match {
        case true =>
          val key = FeedBackUtil.makeOverseasUserKey(info)
          List((key, 1L))
        case _ => Nil
      }
    }).reduceByKey(ReduceUtil.reduceAddLong _, 16).map(x => (x._1, (0L, x._2, 0L, 0L))) //.checkpoint(Minutes(window_size))

    // click
    val clickStream = StreamingFactory.createDirectStreamByOffset(
      zk_hosts,
      offset_base,
      OverseasConfig.clickTopic.name,
      OverseasConfig.clickTopic.partition,
      kafka_param,
      ssc,
      true)
    val clickWindow = clickStream.window(Minutes(window_size * window_num), Minutes(window_size))
    val clickDistinct = clickWindow.map(msg => (msg._2, msg._1)).reduceByKey(ReduceUtil.reduceUniqText _, 16)
    val clickStat = clickDistinct.flatMap(msg => {
      val info = ReformatParser.parseOverSeasClick(msg._1)
      info.isValid match {
        case true =>
          val key = FeedBackUtil.makeOverseasUserKey(info)
          List((key, 1L))
        case _ => Nil
      }
    }).reduceByKey(ReduceUtil.reduceAddLong _, 16).map(x => (x._1, (0L, 0L, x._2, 0L))) //.checkpoint(Minutes(window_size))

    //dwelltime
    val dwelltimeStream = StreamingFactory.createDirectStreamByOffset(
      zk_hosts,
      offset_base,
      OverseasConfig.dwellTimeTopic.name,
      OverseasConfig.dwellTimeTopic.partition,
      kafka_param,
      ssc,
      true)
    val dwelltimeWindow = dwelltimeStream.window(Minutes(window_size * window_num), Minutes(window_size))
    val dwelltimeDistinct = dwelltimeWindow.map(msg => (msg._2, msg._1)).reduceByKey(ReduceUtil.reduceUniqText _, 16)
    val dwelltimeStat = dwelltimeDistinct.flatMap(msg => {
      val info = ReformatParser.parseOverSeasDwelltime(msg._1)
      info.isValid match {
        case true =>
          val key = FeedBackUtil.makeOverseasUserKey(info)
          var timeLen = 0L
          try {
            timeLen = info.dwelltime.toLong
            if (timeLen > 300L) {
              timeLen = 300L
            }
          } catch {
            case ex: Exception => {}
          }
          List((key, timeLen))
        case _ => Nil
      }
    }).reduceByKey(ReduceUtil.reduceAddLong _, 16).map(x => (x._1, (0L, 0L, 0L, x._2))) //.checkpoint(Minutes(window_size))

    val result = requestStat.union(impressionStat).union(clickStat).union(dwelltimeStat)
      .reduceByKey((a, b) => (a._1 + b._1, a._2 + b._2, a._3 + b._3, a._4 + b._4))

    result.foreachRDD((rdd: RDD[(String, (Long, Long, Long, Long))], time: Time) => {
      val timestamp = TimeUtil.getCurrTimeIndex()

      rdd.foreachPartition(partitionOfRecords => {

        val producer = new BatchProducer(zk_hosts, kafka_param.get("metadata.broker.list").get, OverseasConfig.kafka_batchSize)

        partitionOfRecords.foreach(keyValue => {
          val (key, value) = keyValue
          val fields = key.split(FeedBackUtil.KEY_TAG)
          try {
            val output_json = FeedBackUtil.makeOverseasUserResult(timestamp, fields, value)
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

  def createStreamingContext(window_size: Int, window_num: Int, kafka_param: Map[String, String],
                             output_topic: String): StreamingContext = {
    val conf = new SparkConf()
    val ssc = new StreamingContext(conf, Minutes(window_size))
    handler(window_size, window_num, kafka_param, output_topic, ssc)
    //ssc.checkpoint(checkpoint_dir)
    ssc
  }

  def usage() {
    println(CLASS_NAME + " <group_id> <window_size> <window_num> <output_topic>")
  }

  def main(args: Array[String]) {

    if (args.length != 4) {
      usage()
      System.exit(1)
    }

    val Array(group_id, window_size, window_num, output_topic) = args
    var kafka_param = Map[String, String]()
    kafka_param += ("metadata.broker.list" -> OverseasConfig.kafka_brokers)
    kafka_param += ("group.id" -> group_id)
    kafka_param += ("auto.offset.reset" -> "largest")

    val ssc = StreamingContext.getActiveOrCreate(() => {
      createStreamingContext(window_size.toInt, window_num.toInt, kafka_param, output_topic)
    })

    try {
      ssc.start()
      ssc.awaitTermination()
    } finally {
      ssc.stop()
    }
  }

}

