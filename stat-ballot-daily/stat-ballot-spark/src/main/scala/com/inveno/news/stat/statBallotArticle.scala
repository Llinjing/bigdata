package com.inveno.news.stat

import com.inveno.news.common.{Constant, FourEmotionUtil, ReformatParse}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by dory on 2016/12/23.
 */
object StatBallotArticle {
  val KEY_TAG = Constant.KEY_TAG
  def statEmotionArticle(str_ballot:String, str_new:String, str_fresh: String, str_output:String, sc:SparkContext) {
    val rdd_input = sc.textFile(str_ballot)
    val rdd_new = StatProfile.statNewUser(str_new, sc).distinct.map(x=>(x, 1L))
    val rdd_fresh_tmp = sc.textFile(str_fresh).map(x=>(x, 1L))
    //fresh_user 章秋的fresh user + new user 去重
    val rdd_fresh = rdd_new.union(rdd_fresh_tmp).distinct

    val rdd_process = rdd_input.map(ReformatParse.parseEmotionArticle).filter(_.length > 0).map(info=>{
      val Array(uid, product_id, language, content_id, bore_num, like_num, angry_num, sad_num) = info
      val key = FourEmotionUtil.makeArticleCombineKey(uid, product_id, language, content_id)
      (key, (bore_num.toLong, like_num.toLong, angry_num.toLong, sad_num.toLong))
    }).reduceByKey((a, b)=>(a._1 + b._1, a._2 + b._2, a._3 + b._3, a._4 + b._4))

    val rdd_type = rdd_process.map(x=>{
      val (key, values) = x
      val info = key.split(KEY_TAG)
      val (uid, dimention) = (info.head, info.tail.mkString(KEY_TAG))
      (uid, (dimention,values))
    })

    val ALL = Constant.ALL
    val rdd_all_result = rdd_type.map(x=>x._2
    ).reduceByKey((a, b)=>(a._1 + b._1, a._2 + b._2, a._3 + b._3, a._4 + b._4)).map(x=>{
      val (key, values) = x
      (s"$key$KEY_TAG$ALL", values)
    })

    val FRESH = Constant.FRESH_TYPE
    val rdd_fresh_result = rdd_type.join(rdd_fresh).map(x=>x._2._1
    ).reduceByKey((a, b)=>(a._1 + b._1, a._2 + b._2, a._3 + b._3, a._4 + b._4)).map(x=>{
      val (key, values) = x
      (s"$key$KEY_TAG$FRESH", values)
    })

    val NEW = Constant.NEW_TYPE
    val rdd_new_result = rdd_type.join(rdd_new).map(x=>x._2._1
    ).reduceByKey((a, b)=>(a._1 + b._1, a._2 + b._2, a._3 + b._3, a._4 + b._4)).map(x=>{
      val (key, values) = x
      (s"$key$KEY_TAG$NEW", values)
    })
    val FILE_NUM = Constant.FILE_NUM
    val rdd_result = rdd_all_result.union(rdd_fresh_result).union(rdd_new_result)
    rdd_result.coalesce(FILE_NUM).saveAsTextFile(str_output)

  }

  def help() {
    println("need 4 params: ballot_file new_file fresh_file output_path")
  }

  def main(args: Array[String]) {
    if(args.length < 4){
      help()
      System.exit(1)
    }
    val conf = new SparkConf()
    val sc = new SparkContext(conf)
    val Array(str_ballot, str_new, str_fresh, str_output) = args
    statEmotionArticle(str_ballot, str_new, str_fresh, str_output, sc)

    sc.stop()
  }
}
