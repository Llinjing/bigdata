package com.inveno.news.stat

import org.apache.spark.rdd.RDD
import org.apache.spark.SparkContext
import scala.collection.mutable.HashMap
import scala.collection.mutable.LinkedList
import com.inveno.news.parser.ReformatDataParser
import com.inveno.news.common.CombineUtil
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions

/**
 * @author Administrator
 */
object StatUserImpression {
  
  def impression(impressionInput: String, sc: SparkContext): RDD[(String, ((Long, Long), (Long, Long), (Long, Long), (Long, Long)))] = {
    val inputRdd = sc.textFile(impressionInput)
    
    val processRdd = inputRdd.mapPartitions(processPartition)
    
    val statRdd = processRdd.map(x => (x._1, x._2._1)).reduceByKey(_ + _, 100).map(x => (x._1, (x._2, 0L)))

    val statUserRdd = processRdd.mapPartitions(userPartition)
      .reduceByKey((a, b) => a.append(b).distinct)
      .mapPartitions(statPartition)
      .reduceByKey(_+_, 100).map(x => (x._1, (0L, x._2)))
      
    val impressionRdd = statRdd.union(statUserRdd).reduceByKey((a, b) => (a._1 + b._1, a._2 + b._2) )
    
    //key:product_id##app_ver##config_id##content_type##strategy##ad_configid##biz_configid##scenario##uid
    //value:((request, requestUser), (impression, impressionUser), (click, clickUser), (dwelltime, dwelltimeUser))
    val outputRdd = impressionRdd.map(x => (x._1, ((0L, 0L), x._2, (0L, 0L), (0L, 0L))))
    outputRdd
  }    

  def userPartition(iter: Iterator[(String, (Long, LinkedList[String]))]): Iterator[(String, LinkedList[String])] = {
    val map = new HashMap[String, LinkedList[String]]()
    
    while (iter.hasNext) {
      val line = iter.next
      val key = line._1
      val data = line._2._2.distinct
      data.foreach(ele => {
        map(ele) = LinkedList(key).append(map.get(ele).getOrElse(LinkedList()))
      })
    }
    map.toIterator
    /*
    var list = List[(String, LinkedList[String])]()
    map.foreach(ele => {
      list ::= (ele._1, (ele._2.distinct))
    })
    
    list.toIterator
    */
  }
  
  def statPartition(iter: Iterator[(String, LinkedList[String])]): Iterator[(String, Long)] = {
    val map = new HashMap[String, Long]
    
    while (iter.hasNext) {
      val line = iter.next
      val data =  line._2
      data.foreach(ele => {
        map(ele) = map.get(ele).getOrElse(0L) + 1L
      })
    }  
    
    map.toIterator
  }
  

  def processPartition(iter: Iterator[String]): Iterator[(String, (Long, LinkedList[String]))] = {
    val map = new HashMap[String, (Long, LinkedList[String])]()

    var list = new java.util.ArrayList[String]()
    var help_arr = new Array[String](32);
    
    while (iter.hasNext) {
      val str = iter.next
      val data = ReformatDataParser.parseImpression(str)
      if (!data.isEmpty) {
        val arr = data.get
        val uid = arr.apply(8)
        
        list.clear()
        CombineUtil.combine(arr, help_arr, 0, arr.length - 1, list)
        
        for (i <- 0 until list.size) {
          val key = list.get(i)
          map(key) = merge(( 1L, LinkedList(uid)), map.get(key).getOrElse((0L, LinkedList())) )
        }
      }
    }
    map.toIterator
    /*
    var ret_list = List[(String, (Long, LinkedList[String]))]()
    map.foreach(ele => {
      ret_list ::= (ele._1, (ele._2._1, ele._2._2.distinct))
    })

    ret_list.toIterator
    */
  }
  
  def merge(t1: (Long, LinkedList[String]), t2: (Long, LinkedList[String])): (Long, LinkedList[String]) = {
    (t1._1 + t2._1, t1._2.append(t2._2))
  }

}