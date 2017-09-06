package com.inveno.news.base.stat

import org.apache.spark.rdd.RDD
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions
import org.apache.spark.SparkContext
import com.inveno.news.base.parser.ProfileParser
import scala.collection.mutable.HashMap
import scala.collection.Map

/**
 * @author Administrator
 */
object StatProfile {
  
  def getProfileUser(profilePath: String, appsflyerPath: String, sc: SparkContext): RDD[(String, String)] = { 
    val appsflyerInputRdd = sc.textFile(appsflyerPath)
    val appsflyerRdd = appsflyerInputRdd.flatMap(x => {
      var map = HashMap[String, String]()
      val arr = x.split(",")
      val aid = arr.apply(61)
      val partner = arr.apply(11)
      val media_source = arr.apply(12)
      var promotion = ""
      if (!partner.isEmpty() && !media_source.isEmpty()) {
          promotion = "gp_"+media_source.replace(" ", "")+"_"+partner.replace(" ", "")
      } else {
          if (!partner.isEmpty()) {
            promotion = "gp_"+partner.replace(" ", "")
          } else if (!media_source.isEmpty()) {
            promotion = "gp_"+media_source.replace(" ", "")
          }
      }
      map(aid) = promotion
      map.iterator
    }).collectAsMap
    
    val br = sc.broadcast(appsflyerRdd)
    
    val profileInputRdd = sc.textFile(profilePath)
    val profileRdd = profileInputRdd.mapPartitions{iter => {
      val map = new HashMap[String, String]()
      val appsflyermap = br.value
      while (iter.hasNext) {
        val str = iter.next
        val data = ProfileParser.parseProfile(str, appsflyermap)
        if (!data.isEmpty) {
          val obj = data.get
          val uid = obj._1
          val promotion = obj._2
          map(uid) = promotion
        }  
      }
      map.iterator
    } }.repartition(10).distinct
    profileRdd
  }
  
}