package com.inveno.news.stat

import com.inveno.news.common.{Constant, UntilParser, ReformatParser}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

/**
 * Created by dory on 2016/12/26.
 */
object UpdateAccumulation {

  def updateHistory(str_before: String, str_imp: String, str_click: String, str_list_dwelltime: String, str_detail_dwelltime: String, str_output: String, sc: SparkContext): Unit ={
    val rdd_before = sc.textFile(str_before).map(line=>{
      UntilParser.parseHistory(line)
    })

    val rdd_yesterday = StatMetric.statFourMetric(str_imp, str_click, str_list_dwelltime, str_detail_dwelltime, sc)
    val rdd_result = rdd_before.union(rdd_yesterday).reduceByKey((a,b)=>(a._1 + b._1, a._2 + b._2, a._3 + b._3, a._4 + b._4))
    val FILE_NUM = Constant.ACCU_FILE_NUM
    rdd_result.repartition(FILE_NUM).saveAsTextFile(str_output)
  }
  def help(): Unit ={
    println("need 6 params: str_history str_imp str_click str_list_dwelltime str_detail_dwelltime str_output")
  }
  def main(args: Array[String]) {
    if(args.length < 6){
      help()
      System.exit(1)
    }
    val conf = new SparkConf()
    val sc = new SparkContext(conf)
    val Array(str_before, str_imp, str_click, str_list_dwelltime, str_detail_dwelltime, str_output) = args
    updateHistory(str_before, str_imp, str_click, str_list_dwelltime, str_detail_dwelltime, str_output, sc)

    sc.stop()
  }

}
