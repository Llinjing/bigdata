package com.inveno.news.common

import com.alibaba.fastjson.JSON
import org.slf4j.LoggerFactory

/**
 * Created by admin on 2017/3/23.
 */
object ReformatParser {
  val CLASS_NAME = ReformatParser.getClass.getSimpleName
  val logger = LoggerFactory.getLogger(CLASS_NAME)

  def replaceComma(arr:Array[String]):Array[String]={
    arr.map(x=>x.replace(",", ""))
  }

  def parseImpression(str:String): Array[String] ={
    var ret: Array[String] = Array()
    try {
      val json = JSON.parseObject(str)
      val article_impression_extra = json.getJSONObject("article_impression_extra")
      val strategy = article_impression_extra.getJSONObject("cpack").getString("strategy")
      println(strategy)
      if(strategy == "push") {
        val product_id = json.getString("product_id")
        val content_id = article_impression_extra.getString("content_id")
        val language = json.getString("app_lan")
        val app_ver_tmp = json.getString("app_ver")
        val app_ver = app_ver_tmp.split(Constant.KEY_TAG_1).apply(0)
        val platform = json.getString("platform")
        val upack = json.getJSONObject("upack")
        val config_id = upack.getString("news_configid")
        val ad_configid = upack.getString("ad_configid")
        val biz_configid = upack.getString("biz_configid")
        val uid = json.getString("uid")
        ret = replaceComma(Array(content_id, product_id, app_ver, platform, language, config_id, biz_configid, ad_configid, uid))
      }
    } catch {
      case ex: Exception => logger.warn("ReformatDataParser parseImpression failed to parse Json : {} ", str)
    }
    ret
  }

  def parseClick(str:String): Array[String] ={
    var ret: Array[String] = Array()
    try {
      val json = JSON.parseObject(str)
      val article_click_extra = json.getJSONObject("article_click_extra")
      val strategy = article_click_extra.getJSONObject("cpack").getString("strategy")
      if(strategy == "push") {
        val product_id = json.getString("product_id")
        val content_id = article_click_extra.getString("content_id")
        val language = json.getString("app_lan")
        val app_ver_tmp = json.getString("app_ver")
        val app_ver = app_ver_tmp.split(Constant.KEY_TAG_1).apply(0)
        val platform = json.getString("platform")
        val upack = json.getJSONObject("upack")
        val config_id = upack.getString("news_configid")
        val ad_configid = upack.getString("ad_configid")
        val biz_configid = upack.getString("biz_configid")
        val uid = json.getString("uid")
        ret = replaceComma(Array(content_id, product_id, app_ver, platform, language, config_id, biz_configid, ad_configid, uid))
      }
    } catch {
      case ex: Exception => logger.warn("ReformatDataParser parseImpression failed to parse Json : {} ", str)
    }
    ret
  }

  def parsePush(str:String): Array[String] = {
    var ret:Array[String] = Array()
    try{
      val info = str.split(Constant.KEY_TAG)
      //println("info length" + info.length)
      val content_id = info.apply(0)
      val product_id = info.apply(1)
      ret = Array(content_id, product_id)
    } catch {
      case ex: Exception => logger.warn("ReformatDataParser parsePush failed to parse : {} ", str)
    }
    ret
  }


}
