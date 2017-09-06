package com.inveno.news.tmp

import com.inveno.news.common.{Constant, UntilParser}
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by dory on 2016/12/28.
 */
object StatUnionHistory {
  def unionHistory(str_history: String, str_output: String, sc: SparkContext): Unit ={
    val rdd_history = sc.textFile(str_history).map(line=>{
      UntilParser.parseHistory(line)
    })
    val rdd_result = rdd_history.reduceByKey((a, b)=>(a._1 + b._1, a._2 + b._2, a._3 + b._3, a._4 + b._4))
    val FILE_NUM = Constant.ACCU_FILE_NUM
    rdd_result.repartition(FILE_NUM).saveAsTextFile(str_output)

  }

  def main(args: Array[String]) {
    if(args.length < 2){
      println("need 2 params: str_history str_output")
      System.exit(1)
    }
    val conf = new SparkConf()
    val sc = new SparkContext(conf)
    val Array(str_history, str_output) = args
    unionHistory(str_history, str_output, sc)

    sc.stop()
  }

}
