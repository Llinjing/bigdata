package com.inveno.news.stat

import com.inveno.news.common.{Constant, FourEmotionUtil, ReformatParse}
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by dory on 2016/12/23.
 */
object StatBallotUser {
  val KEY_TAG = Constant.KEY_TAG
  def statEmotionUser(str_input:String, str_new:String, str_fresh:String,str_output:String, sc:SparkContext) {
    val rdd_input = sc.textFile(str_input)
    val rdd_new = StatProfile.statNewUser(str_new, sc).distinct.filter(_.length > 0).map(x=>(x, 1L))
    val rdd_fresh_tmp = sc.textFile(str_fresh).distinct.filter(_.length > 0).map(x=>(x, 1L))
    val rdd_fresh = rdd_fresh_tmp.union(rdd_new).distinct

    val rdd_process = rdd_input.map(ReformatParse.parseFourEmotion).filter(_.length > 0).flatMap(info=>{
      val Array(product_id, language,emotion, uid) = info
      FourEmotionUtil.makeCombineKey(uid, product_id, language, emotion)
    }).map(x=>(x, 1L)).reduceByKey((a, b)=>(a + b))

    val rdd_type = rdd_process.map(elem=>{
      val (key, values) = elem
      val info = key.split(KEY_TAG)
      val uid = info.head
      val dimention = info.tail.mkString(KEY_TAG)
      (uid, (dimention, (1L,values)))
    })

    val ALL = Constant.ALL
    val rdd_all_result = rdd_process.map(elem=> {
      val (key, values) = elem
      val info = key.split(KEY_TAG)
      val dimention = info.tail.mkString(KEY_TAG)
      (dimention, (1L, values))
    }).reduceByKey((a, b)=>(a._1 + b._1, a._2 + b._2)).map(x=>{
      val (key, value) = x
      (s"$key$KEY_TAG$ALL", value)
    })

    val NEW = Constant.NEW_TYPE
    val rdd_new_result = rdd_type.join(rdd_new).map(x=>x._2._1
    ).reduceByKey((a, b)=>(a._1 + b._1, a._2 + b._2)).map(x=>{
      val (key, value) = x
      (s"$key$KEY_TAG$NEW", value)
    })

    val FRESH = Constant.FRESH_TYPE
    val rdd_fresh_result = rdd_type.join(rdd_fresh).map(x=>x._2._1
    ).reduceByKey((a, b)=>(a._1 + b._1, a._2 + b._2)).map(x=>{
      val (key, value) = x
      (s"$key$KEY_TAG$FRESH", value)
    })
    val FILE_NUM = Constant.FILE_NUM
    val rdd_result = rdd_all_result.union(rdd_new_result).union(rdd_fresh_result)
    rdd_result.coalesce(FILE_NUM).saveAsTextFile(str_output)

  }

  def help() {
    println("need 4 params: str_ballot str_new str_fresh str_output")
  }

  def main(args: Array[String]) {
    if(args.length < 4){
      help()
      System.exit(0)
    }
    val Array(str_input, str_new, str_fresh, str_output) = args
    val conf = new SparkConf()
    val sc = new SparkContext(conf)
    statEmotionUser(str_input, str_new, str_fresh, str_output, sc)

    sc.stop()
  }
}
