package com.inveno.news.stat

import com.inveno.news.common.{Constant, ReformatParser, CombineUtil}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkContext, SparkConf}

import scala.collection.mutable.{HashMap, LinkedList}

/**
 * Created by gaofeilu on 2017/3/7.
 */
object StatShareFunnelUser {

  def handler(str_input: String, str_output: String, sc: SparkContext): Unit ={

    val rdd_input = sc.textFile(str_input)
    val rdd_process = rdd_input.mapPartitions(processData)
    val rdd_user = rdd_process.mapPartitions(statUser)
      .reduceByKey((a, b)=>a.append(b).distinct)
      .mapPartitions(statCnt)
      .reduceByKey(_ + _)

    val rdd_result = rdd_user.map(x=>{
      val info = x._1.split(Constant.KEY_TAG)
      val action_name = info.head
      val key = info.tail.mkString(Constant.KEY_TAG)
      val values = {
        if(action_name == "h5_share_click"){
          (x._2, 0L, 0L ,0L)
        }else if(action_name == "h5_page_request_call"){
          (0L, x._2, 0L, 0L)
        }else if(action_name == "h5_click_download_app"){
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

  def statUser(iter: Iterator[(String, LinkedList[String])]): Iterator[(String,LinkedList[String])] = {
    val map = new HashMap[String, LinkedList[String]]()

    while(iter.hasNext){
      val line = iter.next()
      val key = line._1
      val data = line._2
      data.foreach(elem=>{
        map(elem) = LinkedList(key).append(map.get(elem).getOrElse(LinkedList()))
      })
    }

    var list = List[(String, LinkedList[String])]();
    map.foreach(ele=>{
      list ::= (ele._1, ele._2.distinct)
    })

    list.toIterator
  }

  def statCnt(iter:Iterator[(String,LinkedList[String])]): Iterator[(String, Long)] ={
    val map = new HashMap[String, Long]()
    while(iter.hasNext){
      val line = iter.next()
      val data = line._2
      data.foreach(elem=>{
        map(elem) = map.getOrElse(elem, 0L) + 1L
      })
    }
    map.toIterator

  }
  def processData(iter: Iterator[String]): Iterator[(String, LinkedList[String])] ={
    val map = new HashMap[String, LinkedList[String]]()
    var list = new java.util.ArrayList[String]()
    var help_arr = new Array[String](32)

    while (iter.hasNext) {
      val str = iter.next
      val data = ReformatParser.parseShareFunnelUser(str)
      if (!data.isEmpty) {
        val uid = data.apply(7)
        list.clear()
        CombineUtil.combine(data, help_arr, 0, data.length - 1, list)

        for (i <- 0 until list.size) {
          val key = list.get(i)
          map(key) = merge(LinkedList(uid), map.get(key).getOrElse(LinkedList[String]()))
        }
      }
    }
    var ret_list = List[(String,  LinkedList[String])]()
    map.foreach(ele => {
      ret_list ::= (ele._1, ele._2.distinct)
    })

    ret_list.toIterator
  }

  def merge(t1: (LinkedList[String]), t2: (LinkedList[String])): (LinkedList[String]) = {
    (t1.append(t2))
  }

  def help(): Unit ={
    println("need param: input_file output_path")
  }

  def main(args: Array[String]) {
    if(args.length < 2){
      help()
      System.exit(1)
    }
    val Array(input_file, output_path) = args
    val conf = new SparkConf()
    val sc = new SparkContext(conf)
    handler(input_file, output_path, sc)
    sc.stop()
  }

}
