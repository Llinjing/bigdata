package com.inveno.news.stat

import org.apache.spark.rdd.RDD
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions
import org.slf4j.LoggerFactory
import com.inveno.news.parser.ReformatDataParser
import com.inveno.news.common.CommonUtil

/**
 * @author Administrator
 */
object StatArticleDwelltime {
  
    private val CLASS_NAME = StatArticleDwelltime.getClass.getSimpleName

    private val logger = LoggerFactory.getLogger(CLASS_NAME)
  
    def dwelltime(dwelltimeInput: String, sc: SparkContext): RDD[(String, (Long, Long, Long, Long))] = {
        val inputRdd = sc.textFile(dwelltimeInput)
        val processRdd = inputRdd.mapPartitions(processPartition)
        val statRdd = processRdd.map(x => (x._1, x._2)).reduceByKey(_ + _, 100).map(x => (x._1, (0L, 0L, 0L, x._2)))
        statRdd
    }
    
    def processPartition(iter: Iterator[String]): Iterator[(String, Long)] = {
      var list = List[(String, Long)]()
      while (iter.hasNext) {
        val str = iter.next
        val data = ReformatDataParser.parseDwelltime(str)
        if (!data.isEmpty) {
          val obj = data.get
          val arr = obj._1
          val key = CommonUtil.mergeKey(arr)
          val dwelltime_str = obj._2
          var dwelltime = 0L
          try {
            dwelltime = dwelltime_str.toLong
          } catch {
            case ex: Exception => logger.warn("dwelltime is : {} ", dwelltime)
          }
          if (dwelltime > 1000L) {
            dwelltime = 1000L
          } else if (dwelltime < 0L) {
            dwelltime = 0L
          }
          
          list ::= (key, dwelltime)
        }
      }
      list.iterator
    }
  
}