package com.inveno.news.nearline.feedback


import com.github.panhongan.util.db.{MysqlUtil, MysqlSession}
import com.github.panhongan.util.mapreduce.ReduceUtil
import com.github.panhongan.util.sparkstreaming.StreamingFactory
import com.inveno.news.nearline.common.{Constant, ReformatParser}
import com.inveno.news.nearline.config.ShareConfig
import inveno.bigdata.common.TimeUtil
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Minutes, StreamingContext}


import org.apache.spark.streaming.dstream.DStream
import org.slf4j.LoggerFactory

/**
 * Created by dory on 2017/2/23.
 */
object ArticleFeedback {

  private val CLASS_NAME = ArticleFeedback.getClass.getSimpleName

  private val logger = LoggerFactory.getLogger(CLASS_NAME)


  def writeStatInfo(stat_info: DStream[(String, String, String, String, String, Long)]) {
  stat_info.foreachRDD(rdd => {
    val timestamp = TimeUtil.getCurrCharTimeIndex()

    rdd.foreachPartition(partition => {
      var session: Option[MysqlSession] = None
      try {
        session = Option(new MysqlSession(MysqlUtil.createConnection(ShareConfig.mysql_server, ShareConfig.mysql_port, ShareConfig.mysql_db, ShareConfig.mysql_user, ShareConfig.mysql_pwd, ShareConfig.mysql_chst)))
        if (!session.isEmpty) {
          partition.foreach(x => {
            val (product_id, platform, action_name, content_id, from, num) = x

              val sql = "insert into t_10min_app_share_test(timestamp, product_id, platform, action_name, content_id, share_place, num) values(" +
                "'" + timestamp + "'," +
                "'" + product_id + "'," +
                "'" + platform + "'," +
                "'" + action_name + "'," +
                "'" + content_id + "'," +
                "'" + from + "'," + num + ");"

              val ret = session.get.executeUpdate(sql)
              if (!ret) {
                logger.warn("insert failed : {}", sql)
              }

          })
        } else {
          logger.warn("create mysql session failed")
        }
      } catch {
        case ex: Exception => logger.warn(ex.getMessage, ex)
      } finally {
        MysqlUtil.closeMysqlSession(session.getOrElse(null))
      }
    })
  })
  }

  def handle(window_size: Int, window_num: Int,
             kafka_param: Map[String, String], ssc: StreamingContext) = {

    val share_stream = StreamingFactory.createDirectStreamByOffset(
    ShareConfig.zk_hosts,
    ShareConfig.offset_basepath,
    ShareConfig.shareExtendTopic.topic,
    ShareConfig.shareExtendTopic.partition,
    kafka_param,
    ssc,
    true
    )
    val share_window = share_stream.window(Minutes(window_size * window_num), Minutes(window_size))
    val stat_result = share_window.flatMap(msg => {
      val parse_result = ReformatParser.parse(msg._2)
      var key = ""
      if (!parse_result.isEmpty) {
        key = parse_result.mkString(Constant.KEY_TAG)
      }
      List((key, 1L))
    }).reduceByKey(ReduceUtil.reduceAddLong).filter(x=>x._1.length>0).map(x=>{
      val arr = x._1.split(Constant.KEY_TAG)
      if(arr.length >=5) {
        (arr(0), arr(1), arr(2), arr(3), arr(4), x._2)
      }else{
        ("","","","","",0L)
      }
    })

    writeStatInfo(stat_result)

  }

  def createStreamingContext(kafka_param: Map[String, String], window_size: Int, window_num: Int): StreamingContext = {


    val spark_conf = new SparkConf()
    val ssc = new StreamingContext(spark_conf, Minutes(window_size))
    handle(window_size, window_num, kafka_param, ssc)
    ssc
  }

  def help: Unit ={
    println("need 3 params: group_id window_num window_size ")
  }
  def main(args: Array[String]) {

    if(args.length < 3){
      help
    }

    val Array(group_id, window_num, window_size) = args

    var kafka_param = Map[String, String]()
    kafka_param += ("metadata.broker.list" -> ShareConfig.kafka_brokers )
    kafka_param += ("group.id" -> group_id)
    kafka_param += ("auto.offset.reset" -> "largest")

    val ssc = StreamingContext.getActiveOrCreate(() => { createStreamingContext(kafka_param, window_size.toInt, window_num.toInt) })

    try {
      ssc.start()
      ssc.awaitTermination()
    } finally {
      ssc.stop()
    }
  }

}
