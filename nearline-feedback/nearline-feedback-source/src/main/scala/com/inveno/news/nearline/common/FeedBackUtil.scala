package com.inveno.news.nearline.common

import inveno.bigdata.common.Using


object FeedBackUtil extends Using {

  val KEY_TAG = "###"

  def makeArticleCombinationKey(info: ReformatJson): String = {
    s"${info.content_id}$KEY_TAG${info.product_id}$KEY_TAG${info.source}"
  }

  def makeArticleFeedBackResult(timestamp: String, keyfields: Array[String], value: (Long, Long, Long, Long)) = {
    var res = ""
    usingJson {
      json =>
        val Array(content_id, product_id, source) = keyfields
        json.put("timestamp", timestamp)
        json.put("content_id", content_id)
        json.put("product_id", product_id)
        json.put("source", source)
        json.put("request", value._1)
        json.put("impression", value._2)
        json.put("click", value._3)
        json.put("dwelltime", value._4)
        res = json.toString
    }
    res
  }
}