package com.inveno.advertisement.stat

import org.slf4j.LoggerFactory
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions

/**
 * @author Administrator
 */
object MergeAdvertisementStat {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    val sc = new SparkContext(conf)
    val originalAdDemandRdd = StatOriginalAdDemand.originalAdDemand(args.apply(0), sc)
    val adRequestResponseRdd = StatAdRequestResponse.adRequestResponse(args.apply(0), sc)
    val adDemandFillSuccessRdd = StatAdDemandFillSuccess.adDemandFillSuccess(args.apply(0), sc)
    val impressionRdd = StatAdvertisementImpression.impression(args.apply(1), sc)
    val clickRdd = StatAdvertisementClick.click(args.apply(2), sc)
    val adRequestRdd = StatAdRequest.adRequest(args.apply(0), sc)
    val adRequestFillRdd = StatAdRequestFill.adRequestFill(args.apply(0), sc)
    val adSpaceImpressionRdd = StatAdSpaceImpression.adSpaceImpression(args.apply(0), sc)
    val adDemandFillTimeoutRdd = StatAdDemandFillTimeout.adDemandFillTimeout(args.apply(0), sc)
    val adDemandNoFillRdd = StatAdDemandNoFill.adDemandNoFill(args.apply(0), sc)

    val unionRdd = originalAdDemandRdd.union(adRequestResponseRdd).union(adDemandFillSuccessRdd).union(impressionRdd).union(clickRdd).union(adRequestRdd).union(adRequestFillRdd).union(adSpaceImpressionRdd).union(adDemandFillTimeoutRdd).union(adDemandNoFillRdd).reduceByKey((a, b) => (a._1 + b._1, a._2 + b._2, a._3 + b._3, a._4 + b._4, a._5 + b._5, a._6 + b._6, a._7 + b._7, a._8 + b._8, a._9 + b._9, a._10 + b._10, a._11 + b._11))
    val ressultRdd = unionRdd.map(x => (x._1, x._2._1, x._2._2, x._2._3, x._2._4, x._2._5, x._2._6, x._2._7, x._2._8, x._2._9, x._2._10, x._2._11))
    ressultRdd.saveAsTextFile(args.apply(3))
  }

}