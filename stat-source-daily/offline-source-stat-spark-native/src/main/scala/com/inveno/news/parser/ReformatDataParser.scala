package com.inveno.news.parser

import com.alibaba.fastjson.JSON
import org.slf4j.LoggerFactory

/**
 * @author Administrator
 */
object ReformatDataParser {
  
  private val CLASS_NAME = ReformatDataParser.getClass.getSimpleName

  private val logger = LoggerFactory.getLogger(CLASS_NAME)
  
  def parseRequest(str: String): Option[Array[String]] = {
     var ret: Option[Array[String]] = None
     
     try {
       val json = JSON.parseObject(str)
       val product_id = json.getString("product_id")
       val uid = json.getString("uid")
       val upack = json.getJSONObject("upack")
       val news_configid = upack.getString("news_configid")
       val ad_configid = upack.getString("ad_configid")
       val biz_configid = upack.getString("biz_configid")
       val article_request_extra = json.getJSONObject("article_request_extra")
       val content_id = article_request_extra.getString("content_id")
       val arr = Array(content_id, product_id+("##")+news_configid+("##")+ad_configid+("##")+biz_configid+("##")+uid)
       if (isNotNull(arr)) {
         ret = Option(arr)
       }
     } catch {
       case ex: Exception => logger.error("ReformatDataParser parseRequest failed to parse Json : {} ", str)
     }
     
     ret
   }
   
   def parseImpression(str: String): Option[Array[String]] = {
     var ret: Option[Array[String]] = None
     
     try {
       val json = JSON.parseObject(str)
       val product_id = json.getString("product_id")
       val uid = json.getString("uid")
       val upack = json.getJSONObject("upack")
       val news_configid = upack.getString("news_configid")
       val ad_configid = upack.getString("ad_configid")
       val biz_configid = upack.getString("biz_configid")
       val article_impression_extra = json.getJSONObject("article_impression_extra")
       val content_id = article_impression_extra.getString("content_id")
       val arr = Array(content_id, product_id+("##")+news_configid+("##")+ad_configid+("##")+biz_configid+("##")+uid)
       if (isNotNull(arr)) {
         ret = Option(arr)
       }
     } catch {
       case ex: Exception => logger.error("ReformatDataParser parseImpression failed to parse Json : {} ", str)
     }
     
     ret
   }
   
   def parseClick(str: String): Option[Array[String]] = {
     var ret: Option[Array[String]] = None
     
     try {
       val json = JSON.parseObject(str)
       val product_id = json.getString("product_id")
       val uid = json.getString("uid")
       val upack = json.getJSONObject("upack")
       val news_configid = upack.getString("news_configid")
       val ad_configid = upack.getString("ad_configid")
       val biz_configid = upack.getString("biz_configid")
       val article_click_extra = json.getJSONObject("article_click_extra")
       val content_id = article_click_extra.getString("content_id")
       val arr = Array(content_id, product_id+("##")+news_configid+("##")+ad_configid+("##")+biz_configid+("##")+uid)
  
       if (isNotNull(arr)) {
         ret = Option(arr)
       }
     } catch {
       case ex: Exception => logger.error("ReformatDataParser parseClick failed to parse Json : {} ", str)
     }
     
     ret
   }
  
  def isNotNull(arr: Array[String]): Boolean = {
    var result = true
    for(ele <- arr) {
      if (ele == null) {
        result = false
      }
    }
    result
  }
  
}