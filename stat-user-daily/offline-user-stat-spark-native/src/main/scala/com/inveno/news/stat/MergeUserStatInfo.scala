package com.inveno.news.stat

import org.slf4j.LoggerFactory
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import com.inveno.news.common.Constant

import com.github.panhongan.util.conf.Config
import com.github.panhongan.util.db.MysqlUtil
import com.github.panhongan.util.db.MysqlSession

/**
 * @author Administrator
 */
object MergeUserStatInfo {
  
  private val CLASS_NAME = MergeUserStatInfo.getClass.getSimpleName

  private val logger = LoggerFactory.getLogger(CLASS_NAME)
  
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    val sc = new SparkContext(conf)
    
    val requestRdd = StatUserRequest.request(args.apply(0), sc);
    
    val impressionRdd = StatUserImpression.impression(args.apply(1), sc);
    
    val clickRdd = StatUserClick.click(args.apply(2), sc);
    
    val dwelltimeRdd = StatUserDwelltime.dwelltime(args.apply(3), sc);
    
    val unionRdd = requestRdd.union(impressionRdd).union(clickRdd).union(dwelltimeRdd).reduceByKey((a, b) => ((a._1._1 + b._1._1, a._1._2 + b._1._2), (a._2._1 + b._2._1, a._2._2 + b._2._2), (a._3._1 + b._3._1, a._3._2 + b._3._2), (a._4._1 + b._4._1, a._4._2 + b._4._2)))
    
    val ressultRdd = unionRdd.map(x => (x._1, x._2._1._1, x._2._1._2, 
        x._2._2._1, x._2._2._2, 
        x._2._3._1, x._2._3._2, 
        x._2._4._1, x._2._4._2))
    ressultRdd.saveAsTextFile(args.apply(4))    
    
//    ressultRdd.foreachPartition(writeDataTodb)
    
  }
  
  def writeDataTodb(iter: Iterator[(String, Long , Long, Long, Long, Long, Long, Long, Long)]): Unit = {
    var session: Option[MysqlSession] = None

    val mysql_config = new Config()
    if (!mysql_config.parse("")) {
      logger.warn("parse mysql config file failed : {}" + "")
      return
    }
    
    try {
      session = Option(new MysqlSession(MysqlUtil.createConnection(mysql_config)))
      if (!session.isEmpty) {
          while (iter.hasNext) {
            val data = iter.next()
            var sql = "";
            val ret = session.get.executeUpdate(sql)
            if (!ret) {
              logger.warn("insert failed : {}", sql)
            }
          }
      } else {
        logger.warn("create mysql session failed")
      }
    } catch {
      case ex: Exception => logger.warn(ex.getMessage, ex)
    } finally {
      MysqlUtil.closeMysqlSession(session.getOrElse(null))
    }
  }
  
}