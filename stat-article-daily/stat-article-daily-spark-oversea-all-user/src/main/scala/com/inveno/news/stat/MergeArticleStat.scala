package com.inveno.news.stat

import org.slf4j.LoggerFactory
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions

/**
 * @author Administrator
 */
object MergeArticleStat {
  
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf()
        val sc = new SparkContext(conf)
        val articleInfoRdd = StatArticleInfo.info(args.apply(0), args.apply(1), sc)
        val requestRdd = StatArticleRequest.request(args.apply(2), sc)
        val impressionRdd = StatArticleImpression.impression(args.apply(3), sc)
        val clickRdd = StatArticleClick.click(args.apply(4), sc)
        val dwelltimeRdd = StatArticleDwelltime.dwelltime(args.apply(5), sc)
        val unionRdd = requestRdd.union(impressionRdd).union(clickRdd).union(dwelltimeRdd).reduceByKey((a, b) => (a._1 + b._1, a._2 + b._2, a._3 + b._3, a._4 + b._4))
        val ressultRdd = unionRdd.map(x => (x._1, x._2._1, x._2._2, x._2._3, x._2._4))
        ressultRdd.saveAsTextFile(args.apply(6))
    }
  
}