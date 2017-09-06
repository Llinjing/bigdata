package com.inveno.extend.service

import com.inveno.extend.common.{FeedBackUtil, ReformatDataParser,ExtendEventConfig}
import inveno.bigdata.common.TimeUtil
import inveno.bigdata.log.LogSupport
import inveno.bigdata.kafka.BatchProducer
import org.apache.spark.rdd.RDD

import org.apache.spark.{SparkContext, SparkConf}

/**
 * Created by dory on 2017/2/10.
 */
object ShortVideoFeedback extends LogSupport {

  def main(args: Array[String]) {

    val sparkConf = new SparkConf()

    sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")

    val sc = new SparkContext(sparkConf)

    process(sc)

    sc.stop()

  }

  def process(sc: SparkContext): Unit = {

    val extendDataSet: RDD[String] = sc.textFile(s"${ExtendEventConfig.extend_hdfs}/${TimeUtil.getToday()}/${TimeUtil.getHour()}/*_${TimeUtil.getHour()}${TimeUtil.getMin()}_*")

    val clickDataSet = sc.textFile(s"${ExtendEventConfig.click_hdfs}/${TimeUtil.getToday()}/${TimeUtil.getHour()}/*_${TimeUtil.getHour()}${TimeUtil.getMin()}_*")

    val extendResult = extendDataSet.
      map(ReformatDataParser.parseExtend).
      filter(_.isValid).
      map(info => s"${info.uid}#${info.content_id}#${info.product_id}#${info.app_lan}").distinct().
      map(line => {
      (line.split("#").drop(2).mkString("", "#", ""), 1L)
    }).reduceByKey(_ + _).map(x=>(x._1,(x._2, 0L)))

    val clickResult = clickDataSet.
      map(ReformatDataParser.parseClick).
      filter(_.isValid).
      map(info => {
      (s"${info.product_id}#${info.app_lan}", (0L, 1L))
    }).reduceByKey((e1, e2) => (e1._1 + e2._1, e1._2 + e2._2))

    val unionResult = extendResult.union(clickResult).reduceByKey((e1, e2) => (e1._1 + e2._1, e1._2 + e2._2))

    unionResult.map(FeedBackUtil.shortVideoResult).foreachPartition(partition => {

      val producer = new BatchProducer(ExtendEventConfig.zk_hosts, ExtendEventConfig.kafka_brokers, ExtendEventConfig.kafka_batchSize)

      partition.foreach((line: String) => {
        try {
          producer.send(ExtendEventConfig.output_topic, line)
        } catch {
          case ex: Exception => {
            log.warn(ex.getMessage, ex)
          }
        }
      })

      producer.close()

    })

  }

}
