package com.inveno.news.nearline.feedback

import com.github.panhongan.util.sparkstreaming.StreamingFactory
import com.inveno.news.nearline.common.{FeedBackUtil, ReformatParser}
import com.inveno.news.nearline.config.NativeConfig
import inveno.bigdata.common.{ReduceUtil, TimeUtil}
import inveno.bigdata.kafka.BatchProducer
import inveno.bigdata.log.LogSupport
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming._


object ArticleFeedback extends LogSupport {

  val CLASS_NAME = ArticleFeedback.getClass.getSimpleName

  def handler(window_size: Int, window_num: Int, kafka_param: Map[String, String], output_topic: String, ssc: StreamingContext) = {
    val offset_base = NativeConfig.offset_basepath
    val zk_hosts = NativeConfig.zk_hosts
    // (key, (request, impression, click, dwelltime))
    // request
    val requestStream = StreamingFactory.createDirectStreamByOffset(
      zk_hosts,
      offset_base,
      NativeConfig.requestTopic.name,
      NativeConfig.requestTopic.partition,
      kafka_param,
      ssc,
      true)
    val requestWindow = requestStream.window(Minutes(window_size * window_num), Minutes(window_size))
    val requestUniq = requestWindow.map(msg => (msg._2, msg._1)).reduceByKey(ReduceUtil.reduceUniqText _, 16)
    val requestStat = requestUniq.flatMap(msg => {
      val info = ReformatParser.parseRequest(msg._1)
      info.isValid match {
        case true =>
          val keys = FeedBackUtil.makeNativeArticleKeys(info)
          keys.map(key => (key, 1L))
        case _ => Nil
      }
    }).reduceByKey(ReduceUtil.reduceAddLong _, 16).map(x => (x._1, (x._2, 0L, 0L, 0L)))

    // impression
    val impressionStream = StreamingFactory.createDirectStreamByOffset(
      zk_hosts,
      offset_base,
      NativeConfig.impressionTopic.name,
      NativeConfig.impressionTopic.partition,
      kafka_param,
      ssc,
      true)
    val impressionWindow = impressionStream.window(Minutes(window_size * window_num), Minutes(window_size))
    val impressionUniq = impressionWindow.map(msg => (msg._2, msg._1)).reduceByKey(ReduceUtil.reduceUniqText _, 16)
    val impressionStat = impressionUniq.flatMap(msg => {
      val info = ReformatParser.parseImpression(msg._1)
      info.isValid match {
        case true =>
          val keys = FeedBackUtil.makeNativeArticleKeys(info)
          keys.map(key => (key, 1L))
        case _ => Nil
      }
    }).reduceByKey(ReduceUtil.reduceAddLong _, 16).map(x => (x._1, (0L, x._2, 0L, 0L)))

    // click
    val clickStream = StreamingFactory.createDirectStreamByOffset(
      zk_hosts,
      offset_base,
      NativeConfig.clickTopic.name,
      NativeConfig.clickTopic.partition,
      kafka_param,
      ssc,
      true)
    val clickWindow = clickStream.window(Minutes(window_size * window_num), Minutes(window_size))
    val clickUniq = clickWindow.map(msg => (msg._2, msg._1)).reduceByKey(ReduceUtil.reduceUniqText _, 16)
    val clickStat = clickUniq.flatMap(msg => {
      val info = ReformatParser.parseClick(msg._1)
      info.isValid match {
        case true =>
          val keys = FeedBackUtil.makeNativeArticleKeys(info)
          keys.map(key => (key, 1L))
        case _ => Nil
      }
    }).reduceByKey(ReduceUtil.reduceAddLong _, 16).map(x => (x._1, (0L, 0L, x._2, 0L)))

    // dwelltime
    val dwelltimeStream = StreamingFactory.createDirectStreamByOffset(
      zk_hosts,
      offset_base,
      NativeConfig.dwelltimeTopic.name,
      NativeConfig.dwelltimeTopic.partition,
      kafka_param,
      ssc,
      true)
    val dwelltimeWindow = dwelltimeStream.window(Minutes(window_size * window_num), Minutes(window_size))
    val dwelltimeUniq = dwelltimeWindow.map(msg => (msg._2, msg._1)).reduceByKey(ReduceUtil.reduceUniqText _, 16)
    val dwelltimeStat = dwelltimeUniq.flatMap(msg => {
      val info = ReformatParser.parseDwelltime(msg._1)
      info.isValid match {
        case true =>
          val keys = FeedBackUtil.makeNativeArticleKeys(info)
          var timeLen = 0L
          try {
            timeLen = info.dwelltime.toLong
            if (timeLen > 300L) {
              timeLen = 300L
            }
          } catch {
            case ex: Exception =>
          }
          keys.map(key => (key, timeLen))
        case _ => Nil
      }
    }).reduceByKey(ReduceUtil.reduceAddLong _, 16).map(x => (x._1, (0L, 0L, 0L, x._2)))

    val result = requestStat.union(impressionStat).union(clickStat).union(dwelltimeStat)
      .reduceByKey((a, b) => (a._1 + b._1, a._2 + b._2, a._3 + b._3, a._4 + b._4))

    result.foreachRDD((rdd: RDD[(String, (Long, Long, Long, Long))]) => {
      val timestamp = TimeUtil.getCurrTimeIndex()

      rdd.foreachPartition(partitionOfRecords => {

        val producer = new BatchProducer(zk_hosts, kafka_param.get("metadata.broker.list").get, NativeConfig.kafka_batchSize)

        partitionOfRecords.foreach(keyValue => {
          val (key, value) = keyValue
          val fields = key.split(FeedBackUtil.KEY_TAG)

          try {
            val output_json = FeedBackUtil.makeNativeArticleResult(timestamp, fields, value)
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
                             output_topic: String, checkpointDir: String): StreamingContext = {
    val conf = new SparkConf()
    val ssc = new StreamingContext(conf, Minutes(window_size))
    handler(window_size, window_num, kafka_param, output_topic, ssc)
    ssc.checkpoint(checkpointDir)
    ssc
  }

  def usage() {
    println(CLASS_NAME + " <group_id> <window_size> <window_num> <output_topic> <checkpoint_dir>")
  }

  def main(args: Array[String]) {
    if (args.length != 5) {
      usage()
      System.exit(1)
    }

    val Array(group_id, window_size, window_num, output_topic, checkpoint_dir) = args
    var kafka_param = Map[String, String]()
    kafka_param += ("metadata.broker.list" -> NativeConfig.kafka_brokers)
    kafka_param += ("group.id" -> group_id)
    kafka_param += ("auto.offset.reset" -> "largest")

    val ssc = StreamingContext.getOrCreate(checkpoint_dir, () => {
      createStreamingContext(window_size.toInt, window_num.toInt, kafka_param, output_topic, checkpoint_dir)
    })

    try {
      ssc.start()
      ssc.awaitTermination()
    } finally {
      ssc.stop()
    }
  }

}