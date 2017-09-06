package com.inveno.news.nearline.common

import inveno.bigdata.common.Using

object ReformatDataParser extends Using {

  def parseRequest(line: String): ReformatJson = {
    var res = ReformatJson("", "", "", "", "", false)
    parseJson(line) {
      json =>
        val uid = json.getString("uid")
        val product_id = json.getString("product_id")
        val article_impression_extra = json.getJSONObject("article_request_extra")
        val content_id = article_impression_extra.getString("content_id")
        val source = article_impression_extra.getJSONObject("cpack").getString("source") match {
          case src if src != null => src
          case _ => "unknown"
        }

        val isVaild = !uid.isEmpty && !content_id.isEmpty && !product_id.isEmpty && !source.isEmpty

        if (isVaild) {
          res = ReformatJson(uid, content_id, product_id, source, "", true)
        }
    }
    res
  }

  def parseImpression(line: String): ReformatJson = {
    var res = ReformatJson("", "", "", "", "", false)
    parseJson(line) {
      json =>
        val uid = json.getString("uid")
        val product_id = json.getString("product_id")
        val article_impression_extra = json.getJSONObject("article_impression_extra")
        val content_id = article_impression_extra.getString("content_id")
        val source = article_impression_extra.getJSONObject("cpack").getString("source") match {
          case src if src != null => src
          case _ => "unknown"
        }

        val isVaild = !uid.isEmpty && !content_id.isEmpty && !product_id.isEmpty && !source.isEmpty

        if (isVaild) {
          res = ReformatJson(uid, content_id, product_id, source, "", true)
        }
    }
    res
  }

  def parseClick(line: String) = {
    var res = ReformatJson("", "", "", "", "", false)
    parseJson(line) {
      json =>
        val uid = json.getString("uid")
        val product_id = json.getString("product_id")
        val article_click_extra = json.getJSONObject("article_click_extra")
        val content_id = article_click_extra.getString("content_id")
        val source = article_click_extra.getJSONObject("cpack").getString("source") match {
          case src if src != null => src
          case _ => "unknown"
        }

        val isVaild = !uid.isEmpty && !content_id.isEmpty && !product_id.isEmpty && !source.isEmpty

        if (isVaild) {
          res = ReformatJson(uid, content_id, product_id, source, "", true)
        }
    }
    res
  }

  def parseDwelltime(line: String) = {
    var res = ReformatJson("", "", "", "", "", false)
    parseJson(line) {
      json =>
        val uid = json.getString("uid")
        val product_id = json.getString("product_id")
        val article_dwelltime_extra = json.getJSONObject("article_dwelltime_extra")
        val content_id = article_dwelltime_extra.getString("content_id")
        val source = article_dwelltime_extra.getJSONObject("cpack").getString("source") match {
          case src if src != null => src
          case _ => "unknown"
        }

        val dwelltime = article_dwelltime_extra.getString("dwelltime")

        val isVaild = !uid.isEmpty && !content_id.isEmpty && !product_id.isEmpty && !source.isEmpty

        if (isVaild) {
          res = ReformatJson(uid, content_id, product_id, source, dwelltime, true)
        }
    }
    res
  }

}


case class ReformatJson(uid: String,
                        content_id: String,
                        product_id: String,
                        source: String,
                        dwelltime: String,
                        isValid: Boolean)

