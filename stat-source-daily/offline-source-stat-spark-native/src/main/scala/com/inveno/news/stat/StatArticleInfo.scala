 package com.inveno.news.stat
 
 import org.apache.spark.SparkContext
 import scala.collection.mutable.HashMap
 import scala.collection.mutable.HashSet
 import com.inveno.news.parser.ArticleInfoParser
 import org.apache.spark.rdd.RDD
 import org.apache.spark.rdd.RDD.rddToPairRDDFunctions
 import org.slf4j.LoggerFactory
 
 /**
  * @author Administrator
  */
 object StatArticleInfo {
   
   private val CLASS_NAME = StatArticleInfo.getClass.getSimpleName
 
   private val logger = LoggerFactory.getLogger(CLASS_NAME)
   
   def getArticleInfo(paidMediaPath: String, articleInfoPath: String, sc: SparkContext): RDD[(String, String)] = {
     val paidMediaRdd = sc.textFile(paidMediaPath)
     
     val br = sc.broadcast(paidMediaRdd.collect())
     logger.info("br length={}", br.value.length)
     
     val articleInfoInputRdd = sc.textFile(articleInfoPath)
     val articleInfoRdd = articleInfoInputRdd.mapPartitions{iter => {
       val set = HashSet[String]()
       val sources = br.value
       sources.foreach { x => set+=(x)}
       logger.info("set size={}", set.size)
       val map = new HashMap[String, String]()
       while (iter.hasNext) {
         val str = iter.next
         val data = ArticleInfoParser.parseArticleInfo(str, set)
         if (!data.isEmpty) {
           val arr = data.get
  
           val key = arr.apply(0)
           val value = arr.apply(1)
           map(key) = value
         }
       }
       map.toIterator
     } }.repartition(10).distinct
     
     articleInfoRdd
   }
 }