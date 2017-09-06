package com.inveno.news.stat

import com.inveno.news.common.{Constant, ReformatParser}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkContext, SparkConf}

/**
 * Created by gaofeilu on 2017/3/7.
 */
object StatShareFunnelArticle {

  def handler(str_input: String, str_output: String, sc: SparkContext): Unit ={
    val rdd_input = sc.textFile(str_input)
    val rdd_process = rdd_input.flatMap(line=>{
      val info = ReformatParser.parseShareFunnelArticle(line)
      val key = info match {
        case info if info.length > 0 => info.mkString(Constant.KEY_TAG)
        case _ => ""
      }
      List((key, 1L))
    }).reduceByKey(_ + _)

    val rdd_result = rdd_process.filter(x=>x._1.length > 0).map(x=>{
      val info = x._1.split(Constant.KEY_TAG)
      val action_name = info.head
      val key = info.tail.mkString(Constant.KEY_TAG)

      val values = {
        if(action_name == "h5_share_click"){
          (x._2, 0L, 0L, 0L)
        }else if(action_name == "h5_page_request_call"){
          (0L, x._2, 0L, 0L)
        }else if (action_name == "h5_click_download_app"){
          (0L, 0L, x._2, 0L)
        }else if(action_name == "deep_link_deferred_h5"){
          (0L, 0L, 0L, x._2)
        }else{
          (0L, 0L, 0L, 0L)
        }
      }
      (key, values)
    }).reduceByKey((a, b)=>(a._1 + b._1, a._2 + b._2, a._3 + b._3, a._4 + b._4))

    rdd_result.coalesce(Constant.FILE_NUM).saveAsTextFile(str_output)
  }
  def help: Unit ={
    println("need params: input_file output_path")
  }
  def main(args: Array[String]) {
    if(args.length < 2){
      help
      System.exit(1)
    }
    val Array(input_file, output_path) = args
    val conf = new SparkConf()
    val sc = new SparkContext(conf)
    handler(input_file, output_path, sc)

    sc.stop()
  }

}
