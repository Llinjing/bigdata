package com.inveno.news.base.stat

import org.slf4j.LoggerFactory
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions

/**
 * @author Administrator
 */
object MergeUserStatInfo {
  
  private val CLASS_NAME = MergeUserStatInfo.getClass.getSimpleName

  private val logger = LoggerFactory.getLogger(CLASS_NAME)
  
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    val sc = new SparkContext(conf)
    
    val profileRdd = StatProfile.getProfileUser(args.apply(0), sc)
    
    val broadcastRdd1 = sc.broadcast(profileRdd.collect())
    val requestRdd = StatUserRequest.request(args.apply(1), sc, broadcastRdd1)
    
    val broadcastRdd2 = sc.broadcast(profileRdd.collect())
    val impressionRdd = StatUserImpression.impression(args.apply(2), sc, broadcastRdd2)
    
    val broadcastRdd3 = sc.broadcast(profileRdd.collect())
    val clickRdd = StatUserClick.click(args.apply(3), sc, broadcastRdd3)
    
    val broadcastRdd4 = sc.broadcast(profileRdd.collect())
    val dwelltimeRdd = StatUserDwelltime.dwelltime(args.apply(4), sc, broadcastRdd4)
    
    val broadcastRdd5 = sc.broadcast(profileRdd.collect())
    val listpageDwelltimeRdd = StatUserListpageDwelltime.listpageDwelltime(args.apply(5), sc, broadcastRdd5)
    
    val broadcastRdd6 = sc.broadcast(profileRdd.collect())
    val impressionAllRdd = StatUserImpressionAll.impression(args.apply(2), args.apply(3), sc, broadcastRdd6)
    
    val unionRdd = requestRdd.union(impressionRdd).union(clickRdd).union(dwelltimeRdd).union(listpageDwelltimeRdd).union(impressionAllRdd).reduceByKey((a, b) => 
      ((a._1._1 + b._1._1, a._1._2 + b._1._2), 
        (a._2._1 + b._2._1, a._2._2 + b._2._2), 
        (a._3._1 + b._3._1, a._3._2 + b._3._2), 
        (a._4._1 + b._4._1, a._4._2 + b._4._2), 
        (a._5._1 + b._5._1, a._5._2 + b._5._2)))
    
    val ressultRdd = unionRdd.map(x => (x._1, 
        x._2._1._1, x._2._1._2, 
        x._2._2._1, x._2._2._2, 
        x._2._3._1, x._2._3._2, 
        x._2._4._1, x._2._4._2, 
        x._2._5._1, x._2._5._2))
    ressultRdd.saveAsTextFile(args.apply(6)) 
  }  
  
}