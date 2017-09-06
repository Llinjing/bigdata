package com.inveno.news.tmp

import com.inveno.news.common.Constant
import org.apache.spark.{SparkConf, SparkContext}
/**
 * Created by dory on 2016/12/27.
 */
object StatLTVTail {
  val KEY_TAG = Constant.KEY_TAG
  def main(args: Array[String]) {
    if(args.length < 6){
      println("need 6 params")
      System.exit(1)
    }
    val conf = new SparkConf()
    val sc = new SparkContext(conf)
    val Array(before_thirtyday_newuser, ystd_imp, ystd_click, ystd_list_dwelltime, ystd_detail_dwelltime, str_output) = args
    val rdd_tail = StatLTV.statTailLTV(before_thirtyday_newuser, ystd_imp, ystd_click, ystd_list_dwelltime, ystd_detail_dwelltime,sc)
    rdd_tail.repartition(10).saveAsTextFile(str_output)
  }
}
