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
object StatAdRequestResponse {

    private val CLASS_NAME = StatAdRequestResponse.getClass.getSimpleName

    private val logger = LoggerFactory.getLogger(CLASS_NAME)
  
    def adRequestResponse(input: String, sc: SparkContext): RDD[(String, (Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long))] = {
        val inputRdd = sc.textFile(input)
        val processRdd = inputRdd.mapPartitions(processPartition)
        val statRdd = processRdd.map(x => (x._1, (x._2, x._3))).reduceByKey((a, b) => (a._1 + b._1, a._2 + b._2), 50).map(x => (x._1, (0L, x._2._1, x._2._2, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L)))
        statRdd
    }
    
    def processPartition(iter: Iterator[String]): Iterator[(String, Long, Long)] = {
      var list = List[(String, Long, Long)]()
      while (iter.hasNext) {
        val str = iter.next
        val data = ReformatDataParser.parseAdRequestResponse(str)
        if (!data.isEmpty) {
          val obj = data.get
          val arr = obj._1
          val key = CommonUtil.mergeKey(arr)
          val value = obj._2
          
          var request_count = 0L
          try {
            request_count = value._1.toLong
          } catch {
            case ex: Exception => logger.warn("request_count is : {} ", request_count)
          }
          
          var response_count = 0L
          try {
            response_count = value._2.toLong
          } catch {
            case ex: Exception => logger.warn("response_count is : {} ", response_count)
          }
          
          list ::= (key, request_count, response_count)
        }
      }
      list.iterator
    }
}