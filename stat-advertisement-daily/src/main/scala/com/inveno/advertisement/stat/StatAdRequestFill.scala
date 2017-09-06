package com.inveno.advertisement.stat

import org.apache.spark.rdd.RDD
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions
import org.slf4j.LoggerFactory
import com.inveno.advertisement.parser.ReformatDataParser
import com.inveno.advertisement.common.CommonUtil

/**
 * @author Administrator
 */
object StatAdRequestFill {

    private val CLASS_NAME = StatAdRequestFill.getClass.getSimpleName

    private val logger = LoggerFactory.getLogger(CLASS_NAME)
  
    def adRequestFill(input: String, sc: SparkContext): RDD[(String, (Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long))] = {
        val inputRdd = sc.textFile(input)
        val processRdd = inputRdd.mapPartitions(processPartition)
        val statRdd = processRdd.map(x => (x._1, x._2)).reduceByKey(_ + _, 50).map(x => (x._1, (0L, 0L, 0L, 0L, 0L, 0L, 0L, x._2, 0L, 0L, 0L)))
        statRdd
    }
    
    def processPartition(iter: Iterator[String]): Iterator[(String, Long)] = {
      var list = List[(String, Long)]()
      while (iter.hasNext) {
        val str = iter.next
        val data = ReformatDataParser.parseAdRequestFill(str)
        if (!data.isEmpty) {
          val arr = data.get
          val key = CommonUtil.mergeKey(arr)
          list ::= (key, 1L)
        }
      }
      list.iterator
    }  
  
}