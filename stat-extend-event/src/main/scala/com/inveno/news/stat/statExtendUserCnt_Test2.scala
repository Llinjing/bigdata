package com.inveno.news.stat

import com.inveno.news.common.{CombineUtil, ReformatDataParse}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

import scala.collection.immutable.Seq

/**
 * Created by dory on 2016/12/9.
 */
object statExtendUserCnt_Test2 {
  def extend(str_input:String, str_output:String, sc:SparkContext): Unit ={
    val rdd1: RDD[String] = sc.textFile(str_input)
    val rdd2 = rdd1.flatMap(line => {

      val list = new java.util.ArrayList[String]()
      val help_arr = new Array[String](32);

      val data = ReformatDataParse.parseExtend(line)

      val result: Seq[String] = if (!data.isEmpty) {
        val arr = data.get
        val uid = arr.apply(5)

        list.clear()
        CombineUtil.combine(arr, help_arr, 0, arr.length - 1, list)

        for (i <- 0 until list.size) yield {
          val key: String = list.get(i)
          s"$uid##$key"
        }
      }else Nil

      result.toList

    }).map(line => (line,1L)).reduceByKey((a,b) => a + b )


    val rdd3 = rdd2.map(element => {
      val (key,values) = element
      val info = key.split("##").toList
      val dimention = info.tail.mkString("","##","")
      (dimention, values)
    }).reduceByKey((a,b) => a + b).map(x=>(x._1,(x._2, 0L)))

    val rdd4 = rdd2.map(elem =>{
      val (key, values) = elem
      val info = key.split("##").toList
      val dimention = info.tail.mkString("","##","")
      (dimention, 1L)
    }).reduceByKey((a, b) => a + b).map(x=>(x._1, (0L, x._2)))

    val rdd5 = rdd3.union(rdd4).reduceByKey((a,b)=>(a._1 + b._1, a._2 + b._2))
    rdd5.saveAsTextFile(str_output)

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
