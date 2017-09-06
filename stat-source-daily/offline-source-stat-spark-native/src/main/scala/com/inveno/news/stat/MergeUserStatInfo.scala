package com.inveno.news.stat

import org.slf4j.LoggerFactory
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

/**
 * @author Administrator
 */
object MergeUserStatInfo {
  
  private val CLASS_NAME = MergeUserStatInfo.getClass.getSimpleName

  private val logger = LoggerFactory.getLogger(CLASS_NAME)
  
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    val sc = new SparkContext(conf)
    
     val articleInfoRdd = StatArticleInfo.getArticleInfo(args.apply(0), args.apply(1), sc).collectAsMap
     
     val broadcastRdd1 = sc.broadcast(articleInfoRdd)
     val requestRdd = StatUserRequest.request(args.apply(2), sc, broadcastRdd1)
     
     val broadcastRdd2 = sc.broadcast(articleInfoRdd)
     val impressionRdd = StatUserImpression.impression(args.apply(3), sc, broadcastRdd2)
  
     
     val broadcastRdd3 = sc.broadcast(articleInfoRdd)
     val clickRdd = StatUserClick.click(args.apply(4), sc, broadcastRdd3)
     
     val unionRdd = requestRdd.union(impressionRdd).union(clickRdd).reduceByKey((a, b) => ((a._1._1 + b._1._1, a._1._2 + b._1._2), (a._2._1 + b._2._1, a._2._2 + b._2._2), (a._3._1 + b._3._1, a._3._2 + b._3._2)))
     
     val ressultRdd = unionRdd.map(x => (x._1, x._2._1._1, x._2._1._2, 
         x._2._2._1, x._2._2._2, 
         x._2._3._1, x._2._3._2))
     ressultRdd.saveAsTextFile(args.apply(5))
    
  }
  
}