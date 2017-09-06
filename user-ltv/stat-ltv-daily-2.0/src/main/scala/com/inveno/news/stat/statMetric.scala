package com.inveno.news.stat

import com.inveno.news.common.{Constant, ReformatParser}
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

/**
 * Created by dory on 2016/12/26.
 */
object StatMetric {
  val KEY_TAG = Constant.KEY_TAG
  def statFourMetric(str_imp:String, str_click:String, str_list_dwelltime:String, str_detail_dwelltime:String, sc: SparkContext): RDD[(String, (Long, Long, Long, Long))] = {

    val rdd_imp: RDD[String] = sc.textFile(str_imp)

    val rdd_imp_result = rdd_imp.map(ReformatParser.parseImp).filter(_.length > 0).map(info=>{
      val key = info.mkString(KEY_TAG)
      (key, 1L)
    }).reduceByKey(_ + _).map(x=>(x._1,(x._2, 0L, 0L, 0L)))


    val rdd_click = sc.textFile(str_click)

    val rdd_click_result = rdd_click.map(ReformatParser.parseClick).filter(_.length > 0).map(info=>{
      val key = info.mkString(KEY_TAG)
      (key, 1L)
    }).reduceByKey(_ + _).map(x =>(x._1, (0L, x._2, 0L, 0L)))

    val rdd_list_dwelltime = sc.textFile(str_list_dwelltime)

    val rdd_list_dwelltime_result = rdd_list_dwelltime.map(ReformatParser.parseListDwelltime).filter(_.length > 0).map(info=>{
      val (dwelltime, key) = (info.head.toLong, info.tail.mkString(KEY_TAG))
      (key, dwelltime)
    }).reduceByKey(_ + _).map(x=>(x._1, (0L, 0L, x._2, 0L)))

    val rdd_detail_dwelltime = sc.textFile(str_detail_dwelltime)

    val rdd_detail_dwelltime_result = rdd_detail_dwelltime.map(ReformatParser.parseDetailDwelltime).filter(_.length > 0).map(info=>{
      val (dwelltime, key) = (info.head.toLong, info.tail.mkString(KEY_TAG))
      (key, dwelltime)
    }).reduceByKey(_ + _).map(x=>(x._1, (0L, 0L, 0L, x._2)))

    val rdd_result = rdd_imp_result.union(rdd_click_result).union(rdd_list_dwelltime_result).union(rdd_detail_dwelltime_result).reduceByKey((a, b) =>
      (a._1 + b._1, a._2 + b._2, a._3 + b._3, a._4 + b._4))

    rdd_result

  }

}
