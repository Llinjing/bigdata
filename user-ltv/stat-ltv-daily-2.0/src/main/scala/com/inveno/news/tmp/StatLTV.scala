package com.inveno.news.tmp

import com.inveno.news.common.{Constant, UntilParser}
import com.inveno.news.stat.StatMetric
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by dory on 2016/12/26.
 */
object StatLTV {
  val KEY_TAG = Constant.KEY_TAG
  def statNewUser(str_newuser: String, sc: SparkContext) ={
    val rdd_new_tmp = sc.textFile(str_newuser).map(UntilParser.parseNewUser).filter(_.length > 0).map(info=>
      info.mkString(KEY_TAG)).distinct

    val rdd_new = rdd_new_tmp.map(line=>{
      val info = line.split(KEY_TAG)
      val (uid, dimention) = (info.head, info.tail.mkString(KEY_TAG))
      (uid, dimention)
    })
    rdd_new
  }


  def statThirtyLTV(str_accumulation: String, str_newuser: String, sc: SparkContext) ={
    val rdd_new = statNewUser(str_newuser, sc)
    val rdd_accu = sc.textFile(str_accumulation).map(line=>{
      UntilParser.parseHistory(line)
    }).map(x=>{
      val info = x._1.split(KEY_TAG)
      val (uid, dimention) = info.length match {
        case len if len > 0 => (info.head, info.tail.mkString(KEY_TAG))
        case _ => ("", "")
      }
      (uid, (dimention, x._2))
    })


    val rdd_thirty_ltv = rdd_accu.join(rdd_new).map(x=>{
      val (dimention1, values) = x._2._1
      val dimention2 = x._2._2
      val info = dimention1.split(KEY_TAG)
      val (content_type, platform) = info.length match {
        case len if len == 4 => (UntilParser.getContentType(info(2)), info(3))
        case _ => ("", "")
      }
      (s"$dimention2$KEY_TAG$content_type$KEY_TAG$platform", values)
    }).reduceByKey((a, b)=>(a._1 + b._1, a._2 + b._2, a._3 + b._3, a._4 + b._4))

    rdd_thirty_ltv
  }

  def statTailLTV(str_newuser: String, str_imp: String, str_click: String, str_list_dwelltime: String, str_detail_dwelltime: String, sc: SparkContext) ={

    val rdd_yesterday_tmp = StatMetric.statFourMetric(str_imp, str_click, str_list_dwelltime, str_detail_dwelltime, sc)
    val rdd_yesterday = rdd_yesterday_tmp.map(x=>{
      val info = x._1.split(KEY_TAG)
      val (uid, dimention) = info.length match {
        case len if len > 0 => (info.head, info.tail.mkString(KEY_TAG))
        case _ => ("", "")
      }
      (uid, (dimention, x._2))
    })
    val rdd_new = statNewUser(str_newuser, sc)

    val TIMES = Constant.TAIL_TIMES
    val rdd_tail_ltv = rdd_yesterday.join(rdd_new).map(x=>{
      val (dimention1, values) = x._2._1
      val dimention2 = x._2._2
      val info = dimention1.split(KEY_TAG)
      val (content_type, platform) = info.length match {
        case len if len == 4 => (UntilParser.getContentType(info(2)), info(3))
        case _ => ("","")
      }
      (s"$dimention2$KEY_TAG$content_type$KEY_TAG$platform", values)
    }).reduceByKey((a, b)=>(a._1 + b._1, a._2 + b._2, a._3 + b._3, a._4 + b._4)).map(x=>{
      val new_values = (TIMES * x._2._1, TIMES * x._2._2, TIMES * x._2._3, TIMES * x._2._4)
      (x._1, new_values)
    })

    rdd_tail_ltv
  }

  def statMergeLTV(str_before: String, before_thirtyday_newuser: String, ystd_imp: String, ystd_click: String, ystd_list_dwelltime: String, ystd_detail_dwelltime: String, str_output: String, sc: SparkContext) ={
    val FILE_NUM = Constant.LTV_FILE_NUM
    val rdd_thirty_ltv = statThirtyLTV(str_before, before_thirtyday_newuser, sc)
    val rdd_tail_ltv = statTailLTV(before_thirtyday_newuser, ystd_imp, ystd_click, ystd_list_dwelltime, ystd_detail_dwelltime, sc)
    val rdd_ltv_result = rdd_thirty_ltv.union(rdd_tail_ltv).reduceByKey((a, b)=>(a._1 + b._1, a._2 + b._2, a._3 + b._3, a._4 + b._4))
    rdd_ltv_result.repartition(FILE_NUM).saveAsTextFile(str_output)
  }

  def help(): Unit ={
    println("need 7 params: str_before before_thirtyday_newuser ystd_imp ystd_click ystd_list_dwelltime ystd_detail_dwelltime str_output")
  }

  def main(args: Array[String]) {
    if(args.length < 7){
      help()
      System.exit(1)
    }
    val conf = new SparkConf()
    val sc = new SparkContext(conf)
    val Array(str_before, before_thirtyday_newuser, ystd_imp, ystd_click, ystd_list_dwelltime, ystd_detail_dwelltime, str_output) = args
    statMergeLTV(str_before, before_thirtyday_newuser, ystd_imp, ystd_click, ystd_list_dwelltime, ystd_detail_dwelltime, str_output, sc)

    sc.stop()
  }

}
