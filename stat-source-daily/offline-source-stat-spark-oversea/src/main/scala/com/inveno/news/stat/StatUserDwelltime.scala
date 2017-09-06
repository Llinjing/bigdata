package com.inveno.news.stat

import org.apache.spark.rdd.RDD
import org.apache.spark.SparkContext
import scala.collection.mutable.HashMap
import scala.collection.mutable.LinkedList
import com.inveno.news.parser.ReformatDataParser
import com.inveno.news.common.CombineUtil
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions
import org.apache.spark.broadcast.Broadcast
import scala.collection.Map
import org.slf4j.LoggerFactory

/**
 * @author Administrator
 */
object StatUserDwelltime {
  
   private val CLASS_NAME = StatUserDwelltime.getClass.getSimpleName

   private val logger = LoggerFactory.getLogger(CLASS_NAME)
  
  def dwelltime(dwelltimeInput: String, sc: SparkContext, br: Broadcast[Map[String, String]]): RDD[(String, ((Long, Long), (Long, Long), (Long, Long), (Long, Long)))] = {
    val inputRdd = sc.textFile(dwelltimeInput)
    
    val joinRdd = inputRdd.mapPartitions{iter => {
      var ret_list = List[String]()
      while (iter.hasNext) {
        val str = iter.next
        val data = ReformatDataParser.parseDwelltime(str)
        if (!data.isEmpty) {
          val arr = data.get
          val content_id = arr.apply(0)
          val tmp_str = arr.apply(1)
          val m = br.value
          if (m.contains(content_id)) {
            ret_list ::= m.get(content_id).get+("##")+tmp_str
          }
        }
      }
      ret_list.toIterator
    } }
    
    val processRdd = joinRdd.mapPartitions(processPartition)

    val statRdd = processRdd.map(x => (x._1, x._2._1)).reduceByKey(_ + _, 100).map(x => (x._1, (x._2, 0L)))
    val outputRdd = statRdd.map(x => (x._1, ((0L, 0L), (0L, 0L), (0L, 0L), x._2)))
    outputRdd
  }
  
  def processPartition(iter: Iterator[String]): Iterator[(String, (Long, LinkedList[String]))] = {
    val map = new HashMap[String, (Long, LinkedList[String])]()

    var list = new java.util.ArrayList[String]()
    var help_arr = new Array[String](32);
    
    while (iter.hasNext) {
      val str = iter.next
      val arr = str.split("##")
      val uid = arr.apply(arr.length-2)
      var dwelltime = 0L
      try {
        dwelltime = arr.apply(arr.length - 1).toLong
      } catch {
        case ex: Exception => logger.warn("dwelltime is : {} ", dwelltime)
      }
      if (dwelltime > 1000L) {
        dwelltime = 1000L
      } else if (dwelltime < 0L) {
        dwelltime = 0L
      }
      
      list.clear()
      CombineUtil.combine(arr, help_arr, 0, arr.length - 2, list)
      for (i <- 0 until list.size) {
        val key = list.get(i)
        map(key) = merge(( dwelltime, LinkedList(uid)), map.get(key).getOrElse((0L, LinkedList())) )
      }
    }
    
    map.toIterator
  }

  def merge(t1: (Long, LinkedList[String]), t2: (Long, LinkedList[String])): (Long, LinkedList[String]) = {
    (t1._1 + t2._1, t1._2.append(t2._2))
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
  }

  def statPartition(iter: Iterator[(String, LinkedList[String])]): Iterator[(String, Long)] = {
    val map = new HashMap[String, Long]

    while (iter.hasNext) {
      val line = iter.next
      val data = line._2
      data.foreach(ele => {
        map(ele) = map.get(ele).getOrElse(0L) + 1L
      })
    }

    map.toIterator
  }
}