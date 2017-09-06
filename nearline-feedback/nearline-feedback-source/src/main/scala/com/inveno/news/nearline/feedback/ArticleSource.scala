package com.inveno.news.nearline.feedback

import com.inveno.news.nearline.common._
import inveno.bigdata.log.LogSupport
import org.apache.spark.SparkConf
import org.apache.spark.streaming._
import com.github.panhongan.util.sparkstreaming.StreamingFactory
import inveno.bigdata.common.{ReduceUtil, TimeUtil}
import inveno.bigdata.kafka.BatchProducer
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming._


object ArticleSource extends LogSupport {

  val CLASS_NAME = ArticleSource.getClass.getSimpleName

  def handler(window_size: Int, window_num: Int, kafka_param: Map[String, String], output_topic: String, ssc: StreamingContext) = {
    val offset_base = SourceConfig.offset_basepath
    val zk_hosts = SourceConfig.zk_hosts
    // (key, (request, impression, click, dwelltime))
    // request
    val requestStream = StreamingFactory.createDirectStreamByOffset(
      zk_hosts,
      offset_base,
      SourceConfig.requestTopic.name,
      SourceConfig.requestTopic.partition,
      kafka_param,
      ssc,
      true)
    val requestWindow = requestStream.window(Minutes(window_size * window_num), Minutes(window_size))
    val requestUniq = requestWindow.map(msg => (msg._2, msg._1)).reduceByKey(ReduceUtil.reduceUniqText _, 16)
    val requestStat = requestUniq.flatMap(msg => {
      val info = ReformatDataParser.parseRequest(msg._1)
      info.isValid match {
        case true =>
          val key = FeedBackUtil.makeArticleCombinationKey(info)
          List((key, 1L))
        case _ => Nil
      }
    }).reduceByKey(ReduceUtil.reduceAddLong _, 16).map(x => (x._1, (x._2, 0L, 0L, 0L)))

    // impression
    val impressionStream = StreamingFactory.createDirectStreamByOffset(
      zk_hosts,
      offset_base,
      SourceConfig.impressionTopic.name,
      SourceConfig.impressionTopic.partition,
      kafka_param,
      ssc,
      true)
    val impressionWindow = impressionStream.window(Minutes(window_size * window_num), Minutes(window_size))
    val impressionUniq = impressionWindow.map(msg => (msg._2, msg._1)).reduceByKey(ReduceUtil.reduceUniqText _, 16)
    val impressionStat = impressionUniq.flatMap(msg => {
      val info = ReformatDataParser.parseImpression(msg._1)
      info.isValid match {
        case true =>
          val key = FeedBackUtil.makeArticleCombinationKey(info)
          List((key, 1L))
        case _ => Nil
      }
    }).reduceByKey(ReduceUtil.reduceAddLong _, 16).map(x => (x._1, (0L, x._2, 0L, 0L)))

    // click
    val clickStream = StreamingFactory.createDirectStreamByOffset(
      zk_hosts,
      offset_base,
      SourceConfig.clickTopic.name,
      SourceConfig.clickTopic.partition,
      kafka_param,
      ssc,
      true)
    val clickWindow = clickStream.window(Minutes(window_size * window_num), Minutes(window_size))
    val clickUniq = clickWindow.map(msg => (msg._2, msg._1)).reduceByKey(ReduceUtil.reduceUniqText _, 16)
    val clickStat = clickUniq.flatMap(msg => {
      val info = ReformatDataParser.parseClick(msg._1)
      info.isValid match {
        case true =>
          val key = FeedBackUtil.makeArticleCombinationKey(info)
          List((key, 1L))
        case _ => Nil
      }
    }).reduceByKey(ReduceUtil.reduceAddLong _, 16).map(x => (x._1, (0L, 0L, x._2, 0L)))

    // dwelltime
    val dwelltimeStream = StreamingFactory.createDirectStreamByOffset(
      zk_hosts,
      offset_base,
      SourceConfig.dwelltimeTopic.name,
      SourceConfig.dwelltimeTopic.partition,
      kafka_param,
      ssc,
      true)
    val dwelltimeWindow = dwelltimeStream.window(Minutes(window_size * window_num), Minutes(window_size))
    val dwelltimeUniq = dwelltimeWindow.map(msg => (msg._2, msg._1)).reduceByKey(ReduceUtil.reduceUniqText _, 16)
    val dwelltimeStat = dwelltimeUniq.flatMap(msg => {
      val info = ReformatDataParser.parseDwelltime(msg._1)
      info.isValid match {
        case true =>
          val key = FeedBackUtil.makeArticleCombinationKey(info)
          var timeLen = 0L
          try {
            timeLen = info.dwelltime.toLong
            if (timeLen > 300L) {
              timeLen = 300L
            }
          } catch {
            case ex: Exception =>
          }
          List((key, timeLen))
        case _ => Nil
      }
    }).reduceByKey(ReduceUtil.reduceAddLong _, 16).map(x => (x._1, (0L, 0L, 0L, x._2)))

    val result = requestStat.union(impressionStat).union(clickStat).union(dwelltimeStat)
      .reduceByKey((a, b) => (a._1 + b._1, a._2 + b._2, a._3 + b._3, a._4 + b._4))

    result.foreachRDD(rdd => {
      val timestamp = TimeUtil.getCurrTimeIndex()

      rdd.foreachPartition(partitionOfRecords => {

        val producer = new BatchProducer(zk_hosts, kafka_param.get("metadata.broker.list").get, SourceConfig.kafka_batchSize)

        partitionOfRecords.foreach(keyValue => {
          val (key, value) = keyValue
          val fields = key.split(FeedBackUtil.KEY_TAG)

          try {
            val output_json = FeedBackUtil.makeArticleFeedBackResult(timestamp, fields, value)
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
    println(CLASS_NAME + " <group_id> <window_size> <window_num> <checkpoint_dir>")
  }

  def main(args: Array[String]) {
    if (args.length != 5) {
      usage()
      System.exit(1)
    }

    val Array(group_id, window_size, window_num, output_topic, checkpoint_dir) = args
    var kafka_param = Map[String, String]()
    kafka_param += ("metadata.broker.list" -> SourceConfig.kafka_brokers)
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
