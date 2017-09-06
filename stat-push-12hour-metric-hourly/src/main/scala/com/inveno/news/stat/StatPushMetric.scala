package com.inveno.news.stat

import com.inveno.news.common.{CombineUtil, Constant, ReformatParser}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

/**
 * Created by admin on 2017/3/23.
 */
object StatPushMetric {

  val KEY_TAG = Constant.KEY_TAG

  def handlePush(pushInput: String, sc:SparkContext): RDD[(String, String)] ={
    val pushRdd: RDD[(String, String)] = sc.textFile(pushInput).distinct.map(line=>{
      val arr = ReformatParser.parsePush(line)
      (arr.apply(0),arr.apply(1))
    })
    pushRdd
  }
  def handleImp(impressionInput: String, pushInput: String, sc: SparkContext): RDD[(String, ((Long, Long), (Long, Long)))] = {
    val inputRdd = sc.textFile(impressionInput)
    val pushRdd = handlePush(pushInput, sc)

    val processRdd: RDD[(String, String)] = inputRdd.flatMap(line=>{
      val info = ReformatParser.parseImpression(line)
      if(info.length > 0) {
        List((info.head, info.tail.mkString(Constant.KEY_TAG)))
      }else{
        List(("", ""))
      }
    }).filter(x=>x._1.length > 0)

    val impResult: RDD[(String, ((Long, Long), (Long, Long)))] = processRdd.join(pushRdd).map(x=>{
      val content_id = x._1
      val key = x._2._1
      (s"$content_id$KEY_TAG$key", 1L)
    }).reduceByKey(_ + _).flatMap(x=>{
      var ret: List[(String, (List[String], Long))] = List()
      val info = x._1.split(KEY_TAG)
      val uid = info.apply(8)
      val value = x._2
      var list = new java.util.ArrayList[String]()
      var help_arr = new Array[String](32)
      CombineUtil.combine(info,help_arr, 0, info.length - 1,  list)
      for(i<- 0 until list.size()){
        val arr = list.get(i).split(KEY_TAG)
        ret = (arr.mkString(KEY_TAG),(List(uid), value))::ret
      }
      ret
    }).reduceByKey((a, b)=>(a._1 ::: b._1, a._2 + b._2)).map(x=>(x._1,((x._2._1.distinct.size.toLong, x._2._2),(0L, 0L))))

    impResult
  }

  def handleClick(clickInput:String, pushInput:String, sc:SparkContext): RDD[(String, ((Long, Long), (Long, Long)))] ={
    val inputRdd = sc.textFile(clickInput)
    val pushRdd = handlePush(pushInput, sc)

    val processRdd: RDD[(String, String)] = inputRdd.flatMap(line=>{
      val info = ReformatParser.parseClick(line)
      if(info.length > 0) {
        List((info.head, info.tail.mkString(Constant.KEY_TAG)))
      }else{
        List(("", ""))
      }
      }).filter(x=>x._1.length > 0)

    val clickResult: RDD[(String, ((Long, Long), (Long, Long)))] = processRdd.join(pushRdd).map(x=>{
      val content_id = x._1
      val key = x._2._1
      (s"$content_id$KEY_TAG$key", 1L)
    }).reduceByKey(_ + _).flatMap(x=>{
      var ret: List[(String, (List[String],Long))] = List()
      val info = x._1.split(KEY_TAG)
      val uid = info.apply(8)
      val value = x._2
      var list = new java.util.ArrayList[String]()
      var help_arr = new Array[String](32)
      CombineUtil.combine(info,help_arr, 0, info.length - 1,  list)
      for(i<- 0 until list.size()){
        val arr = list.get(i).split(KEY_TAG)
        ret = (arr.mkString(KEY_TAG), (List(uid), value))::ret
      }
      ret
    }).reduceByKey((a, b)=>(a._1 ::: b._1, a._2 + b._2)).map(x=>(x._1,((0L, 0L), (x._2._1.distinct.size.toLong, x._2._2))))

    clickResult
  }

  def help(): Unit ={
    println("need params: strImp strClick strPush strOutput")
  }
  def main(args: Array[String]) {
    if(args.length < 4){
      help()
      System.exit(1)
    }
    val Array(strImp, strClick, strPush, strOutput) = args
    val conf = new SparkConf()
    val sc = new SparkContext(conf)
    val impRdd = handleImp(strImp, strPush, sc)
    val clickRdd = handleClick(strClick, strPush, sc)

    val resultRdd = impRdd.union(clickRdd).reduceByKey((a, b)=>((a._1._1 + b._1._1, a._1._2 + b._1._2),(a._2._1 + b._2._1,a._2._2 + b._2._2)))
    val FILE_NUM = Constant.FILE_NUM
    resultRdd.coalesce(FILE_NUM).saveAsTextFile(strOutput)
  }
}
