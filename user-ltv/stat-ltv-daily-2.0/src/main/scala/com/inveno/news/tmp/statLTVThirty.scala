package com.inveno.news.tmp

import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by dory on 2016/12/27.
 */
object StatLTVThirty {
  def main(args: Array[String]) {
    if(args.length < 3){
      println("need 6 params")
      System.exit(1)
    }
    val conf = new SparkConf()
    val sc = new SparkContext(conf)
    val Array(str_before, before_thirtyday_newuser, str_output) = args
    val rdd_thirty = StatLTV.statThirtyLTV(str_before, before_thirtyday_newuser, sc)
    rdd_thirty.repartition(10).saveAsTextFile(str_output)
  }
}
