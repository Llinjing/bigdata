package com.inveno.bigdata.stat

import org.apache.spark.rdd.RDD
import org.apache.spark.SparkContext
import scala.collection.mutable.HashMap
import scala.collection.mutable.LinkedList
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions
import org.slf4j.LoggerFactory
import com.inveno.bigdata.common.DataParser

/**
 * @author Administrator
 */
object H5Share {
    
    private val CLASS_NAME = H5Share.getClass.getSimpleName 
    private val logger = LoggerFactory.getLogger(CLASS_NAME)
  
    def h5Share(h5ShareInput: String, sc: SparkContext): RDD[(String, Long)] = {
        val inputRdd = sc.textFile(h5ShareInput)
        val processRdd = inputRdd.mapPartitions(processPartition)
        val outputRdd = processRdd.map(x => (x._1, x._2)).reduceByKey(_ + _, 18)
        outputRdd
    }
  
    def processPartition(iter: Iterator[String]): Iterator[(String, Long)] = {
      var ret_list:List[(String, Long)] = List()
  
      while (iter.hasNext) {
          val str = iter.next
          val data = DataParser.parseH5Share(str)
          if (!data.isEmpty) {
              ret_list ::= ((data, 1L))
          }
      }
  
        ret_list.toIterator
    }
  
  }