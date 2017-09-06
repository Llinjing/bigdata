package com.inveno.news.stat

import com.inveno.news.common.{CombineUtil, ReformatDataParse}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.{HashMap, LinkedList}

/**
 * Created by dory on 2016/12/9.
 */
object statExtendUserCnt {
  def extend(str_input:String, str_output:String, sc:SparkContext): Unit ={
    val rdd_input = sc.textFile(str_input)
    val rdd_process = rdd_input.mapPartitions(processData)
    val rdd_cnt = rdd_process.map(x=>(x._1, x._2._1)).reduceByKey(_ + _, 50).map(x=>(x._1,(x._2, 0L)))
    val rdd_user = rdd_process.mapPartitions(statUser)
      .reduceByKey((a, b)=>a.append(b).distinct)
      .mapPartitions(statCnt)
      .reduceByKey(_ + _, 100)
      .map(x=>(x._1, (0L, x._2)))
    val rdd_result = rdd_cnt.union(rdd_user).reduceByKey((a,b)=>(a._1 + b._1, a._2 + b._2)).map(x=>(x._1, x._2._1,x._2._2))
    rdd_result.saveAsTextFile(str_output)

  }


  def statUser(iter: Iterator[(String,(Long, LinkedList[String]))]): Iterator[(String,LinkedList[String])] = {
    val map = new HashMap[String, LinkedList[String]]()

    while(iter.hasNext){
      val line = iter.next()
      val key = line._1
      val data = line._2._2
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
      //val key = line._1
      val data = line._2
      data.foreach(elem=>{
        map(elem) = map.getOrElse(elem, 0L) + 1L
      })
    }
    map.toIterator

  }
  def processData(iter: Iterator[String]): Iterator[(String, (Long, LinkedList[String]))] ={
    val map = new HashMap[String, (Long, LinkedList[String])]()

    var list = new java.util.ArrayList[String]()
    var help_arr = new Array[String](32);

    while (iter.hasNext) {
      val str = iter.next
      val data = ReformatDataParse.parseExtend(str)
      if (!data.isEmpty) {
        val arr = data.get
        val uid = arr.apply(5)

        list.clear()
        CombineUtil.combine(arr, help_arr, 0, arr.length - 1, list)

        for (i <- 0 until list.size) {
          val key = list.get(i)
          map(key) = merge(( 1L, LinkedList(uid)), map.get(key).getOrElse((0L, LinkedList[String]())) )
        }
      }
    }

    var ret_list = List[(String, (Long, LinkedList[String]))]()
    map.foreach(ele => {
      ret_list ::= (ele._1, (ele._2._1, ele._2._2.distinct))
    })

    ret_list.toIterator
  }

  def merge(t1: (Long, LinkedList[String]), t2: (Long, LinkedList[String])): (Long, LinkedList[String]) = {
    (t1._1 + t2._1, t1._2.append(t2._2))
  }

  def help(): Unit ={
    println("need 2 params: input_file, output_file")
  }
  def main(args: Array[String]) {
    if(args.length < 2)
    {
      help()
      System.exit(1)
    }
    val conf = new SparkConf()
    val sc = new SparkContext(conf)
    val Array(str_input, str_output) = args
    extend(str_input, str_output, sc)

  }
}
