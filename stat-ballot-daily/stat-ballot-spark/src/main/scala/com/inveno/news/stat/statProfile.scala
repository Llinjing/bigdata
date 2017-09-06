package com.inveno.news.stat

import com.inveno.news.common.ReformatParse
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

/**
 * Created by dory on 2016/12/23.
 */
object StatProfile {
  def statNewUser(str_profile: String, sc: SparkContext): RDD[String] ={
    val rdd_new = sc.textFile(str_profile)
    val rdd_new_ret = rdd_new.map(ReformatParse.parseProfile).filter(_.length > 0)
    rdd_new_ret
  }
}
