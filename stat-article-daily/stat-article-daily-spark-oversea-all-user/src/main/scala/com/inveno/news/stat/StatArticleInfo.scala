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
object StatArticleInfo {
      private val CLASS_NAME = StatArticleInfo.getClass.getSimpleName

      private val logger = LoggerFactory.getLogger(CLASS_NAME)
    
      def info(infoInput: String, infoOutput: String, sc: SparkContext) = {
          val inputRdd = sc.textFile(infoInput)
          val processRdd = inputRdd.mapPartitions(processPartition).distinct.repartition(50)
          processRdd.saveAsTextFile(infoOutput)
      }
      
      def processPartition(iter: Iterator[String]): Iterator[String] = {
        var list = List[String]()
        while (iter.hasNext) {
          val str = iter.next
          val data = ReformatDataParser.parseArticle(str)
          if (!data.isEmpty) {
            val arr = data.get
            val key = CommonUtil.mergeKey(arr)
            list ::= (key)
          }
        }
        list.iterator
      }
      
}