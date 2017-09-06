package com.inveno.news.tmp

import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by dory on 2016/12/27.
 */
object StatNewUser {
  def main(args: Array[String]) {
    if(args.length < 2){
      println("need 2 params: str_newuser str_output")
      System.exit(1)
    }
    val conf = new SparkConf()
    val sc = new SparkContext(conf)
    val Array(str_newuser, str_output) = args
    val rdd_new = StatLTV.statNewUser(str_newuser, sc)
    rdd_new.saveAsTextFile(str_output)
  }

}
