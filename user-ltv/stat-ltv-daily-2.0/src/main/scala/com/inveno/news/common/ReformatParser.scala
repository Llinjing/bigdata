package com.inveno.news.common

import com.alibaba.fastjson.JSON
import org.slf4j.LoggerFactory

/**
 * Created by dory on 2016/12/26.
 */
object ReformatParser {
  private val  CLASS_NAME = ReformatParser.getClass.getSimpleName
  val logger = LoggerFactory.getLogger(CLASS_NAME)
  def replaceComma(arr: Array[String]): Array[String] ={
     arr.map(x=>x.replace(",", ""))
  }

  def parseImp(str:String):Array[String]={
    var ret:Array[String] = Array()
    try{
      val json = JSON.parseObject(str)
      val product_id = json.getString("product_id")
      val promotion = json.getString("promotion")
      val uid = json.getString("uid")
      val content_type = json.getJSONObject("article_impression_extra").getString("content_type")
      val platform = json.getString("platform")
      val arr = Array(uid, product_id, promotion, content_type, platform)
      if(isNull(arr)) {
        ret = replaceComma(arr)
      }
    }catch {
      case ex: Exception => logger.info("ReformatDataParser extend failed to parse Json , the str is :" + str)
    }
    ret
  }

  def parseClick(str:String):Array[String]={
    var ret: Array[String] = Array()
    try {
      val json = JSON.parseObject(str)
      val product_id = json.getString("product_id")
      val promotion = json.getString("promotion")
      val content_type = json.getJSONObject("article_click_extra").getString("content_type")
      val uid = json.getString("uid")
      val platform = json.getString("platform")
      val arr = Array(uid, product_id,  promotion, content_type, platform)

      if (isNull(arr)) {
        ret = replaceComma(arr)
      }
    } catch {
      case ex: Exception => logger.info("ReformatDataParser extend failed to parse Json , the str is :"  + str)
    }
    ret
  }

  def parseListDwelltime(str:String):Array[String] = {
    var ret: Array[String] = Array()
    try{
      val json = JSON.parseObject(str)
      val product_id = json.getString("product_id")
      val promotion = json.getString("promotion")
      val content_type = "news_all"
      val list_dwelltime = getDwelltime(json.getJSONObject("listpage_dwelltime_extra").getString("dwelltime"))
      val uid = json.getString("uid")
      val platform = json.getString("platform")
      val arr = Array(list_dwelltime, uid, product_id, promotion, content_type, platform)
      if(isNull(arr)){
        ret = replaceComma(arr)
      }

    }catch {
      case ex: Exception => logger.info("ReformatDataParser extend failed to parse Json , the str is :"  + str)
    }

    ret
  }

  def parseDetailDwelltime(str:String):Array[String] = {
    var ret: Array[String] = Array()
    try{
      val json = JSON.parseObject(str)
      val product_id = json.getString("product_id")
      val promotion = json.getString("promotion")
      val content_type = json.getJSONObject("article_dwelltime_extra").getString("content_type")
      val detail_dwelltime = getDwelltime(json.getJSONObject("article_dwelltime_extra").getString("dwelltime"))
      val uid = json.getString("uid")
      val platform = json.getString("platform")
      val arr = Array(detail_dwelltime, uid, product_id,  promotion, content_type, platform)
      if(isNull(arr)){
        ret = replaceComma(arr)
      }

    }catch {
      case ex: Exception => logger.info("ReformatDataParser extend failed to parse Json , the str is :"  + str)
    }
    ret
  }

  def isNull(arr: Array[String]): Boolean = {
    !arr.map(x => x == null).contains(true)
  }

  def getDwelltime(dwelltime:String):String = {
    dwelltime match {
      case _ if dwelltime.length == 0 =>"0"
      case _ if dwelltime.toInt <= 0 => "0"
      case _ if dwelltime.toInt > 300 => "300"
      case _ => dwelltime
    }
  }
}
