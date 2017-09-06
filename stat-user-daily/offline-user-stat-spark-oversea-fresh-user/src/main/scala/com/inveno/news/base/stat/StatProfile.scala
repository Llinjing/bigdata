package com.inveno.news.base.stat

import org.apache.spark.rdd.RDD
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions
import org.apache.spark.SparkContext

/**
 * @author Administrator
 */
object StatProfile {
  
    def getProfileUser(profilePath: String, sc: SparkContext): RDD[String] = { 
      val profileInputRdd = sc.textFile(profilePath)
      val profileRdd = profileInputRdd.mapPartitions(processPartition).distinct().repartition(100)
      profileRdd
    }
    
    def processPartition(iter: Iterator[String]): Iterator[String] = {
      var ret_list = List[String]()
      while (iter.hasNext) {
        val uid = iter.next
        ret_list ::= (uid)
      }
      ret_list.toIterator
  }
  
}