package com.inveno.news.stat

import com.inveno.news.common.{Constant, ReformatParser, CombineUtil}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

import scala.collection.mutable.{HashMap, LinkedList}

/**
 * Created by admin on 2017/3/23.
 */
object StatImpressionUser {
  def handle(impressionInput: String, pushInput: String, strOutput: String, sc: SparkContext) = {
    val inputRdd = sc.textFile(impressionInput)
    val pushRdd: RDD[(String, String)] = sc.textFile(pushInput).distinct.map(line=>{
      val arr = ReformatParser.parsePush(line)
      (arr.apply(0),arr.apply(1))
    })

    val processRdd: RDD[(String, String)] = inputRdd.flatMap(line=>{
      val info = ReformatParser.parseImpression(line)
      List((info.head, info.tail.mkString(Constant.KEY_TAG)))
    })
    val KEY_TAG = Constant.KEY_TAG

    val tmpResult: RDD[(String, (Long, Long))] = processRdd.join(pushRdd).map(x=>{
      val key = x._1
      val value = x._2._1
      (s"$key$KEY_TAG$value", 1L)
    }).reduceByKey(_ + _).flatMap(x=>{
      var ret: List[(String, Long)] = List()
      val info = x._1.split(KEY_TAG)
      val value = x._2
      var list = new java.util.ArrayList[String]()
      var help_arr = new Array[String](32)
      CombineUtil.combine(info,help_arr, 0, info.length - 1,  list)
      for(i<- 0 until list.size()){
        val arr = list.get(i).split(KEY_TAG)
        ret = (arr.mkString(KEY_TAG), value)::ret
      }
      ret
    }).map(x=>(x._1, (1L, x._2))).reduceByKey((a,b)=>(a._1 + b._1, a._2 + b._2))

   /* val test = processRdd.join(pushRdd).map(x=>{
      val key = x._1
      val value = x._2._1
      (s"$key$KEY_TAG$value", 1L)
    }).reduceByKey(_ + _).flatMap(x=>{
      var ret: List[(String, Long)] = List()
      val info = x._1.split(KEY_TAG)
      val value = x._2
      var list = new java.util.ArrayList[String]()
      var help_arr = new Array[String](32)
      CombineUtil.combine(info, help_arr, 0, info.length - 1,  list, value)

      for(i<- 0 until list.size()){
        val arr = list.get(i).split(KEY_TAG)
        ret = (arr.tail.mkString(KEY_TAG), arr.head.toLong)::ret
      }
      ret
    })*/



    tmpResult.coalesce(10).saveAsTextFile(strOutput)
  }

  def main(args: Array[String]) {
    if(args.length < 3){
      System.exit(1)
    }
    val Array(strImp, strPush, strOutput) = args
    val conf = new SparkConf()
    val sc = new SparkContext(conf)
    handle(strImp, strPush, strOutput,sc)
  }

}
