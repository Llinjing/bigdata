package com.inveno.news.tmp

import com.inveno.news.common.Constant
import com.inveno.news.stat.StatMetric
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by dory on 2016/12/26.
 */
object AccumulateHistory {


  def help(): Unit = {
    println("need 5 params:imp_input, click_input, list_dwelltime, detail_dwelltime, output")
  }

  def main(args: Array[String]) {
    if (args.length < 5) {
      help()
      System.exit(0)
    }
    val FILE_NUM = Constant.ACCU_FILE_NUM
    val conf = new SparkConf()
    val sc = new SparkContext(conf)
    val Array(str_imp, str_click, str_list_dwelltime, str_detail_dwelltime, str_output) = args
    val rdd_history = StatMetric.statFourMetric(str_imp, str_click, str_list_dwelltime, str_detail_dwelltime, sc)
    rdd_history.repartition(FILE_NUM).saveAsTextFile(str_output)

    sc.stop()
  }

}
