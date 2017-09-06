package com.inveno.news.stat

import com.inveno.news.common.{CombineUtil, ReformatDataParse}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.{HashMap, LinkedList}

/**
 * Created by dory on 2016/12/9.
 */
object statExtendUserCnt_Test1 {
  def extend(str_input:String, str_output:String, sc:SparkContext): Unit ={
    val rdd1 = sc.textFile(str_input)
    val rdd2 = rdd1.mapPartitions(processData)
    val rdd3 = rdd2.map(x=>(x._1, x._2._1)).reduceByKey(_ + _, 50).map(x=>(x._1,(x._2, 0L)))
    val rdd4 = rdd2.mapPartitions(statUser).reduceByKey((a,b)=>a.append(b).distinct).map(x=>(x._1, (0L, x._2.length.toLong)))

    val rdd5 = rdd3.union(rdd4).reduceByKey((a,b)=>(a._1 + b._1, a._2 + b._2))
    rdd5.saveAsTextFile(str_output)

  }


  def statUser(iter:Iterator[(String,(Long, LinkedList[String]))]): Iterator[(String,LinkedList[String])] = {
    val map = new HashMap[String, LinkedList[String]]()
    while(iter.hasNext){
      val line = iter.next()
      val key = line._1
      val data = line._2._2
      data.foreach(uids=>{
        map(key) = LinkedList(uids).append(map.get(key).getOrElse(LinkedList()))
      })
    }

    var list = List[(String, LinkedList[String])]();
    map.foreach(ele=>{
      list ::= (ele._1, ele._2.distinct)
    })

    list.toIterator

  }
  def processData(iter: Iterator[String]): Iterator[(String, (Long, LinkedList[String]))] ={
    val map = new HashMap[String, (Long, LinkedList[String])]()

    val list = new java.util.ArrayList[String]()
    val help_arr = new Array[String](32);

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
