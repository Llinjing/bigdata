package com.inveno.news.appver.parser

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
      if (product_id.equals("ali") || product_id.equals("meizu") || product_id.equals("coolpad") || product_id.equals("fuyiping-gionee")) {
        var app_ver = json.getString("app_ver")
        if (app_ver.length() > 25) {
          app_ver = app_ver.substring(0, 24)
        }
        val uid = json.getString("uid")
        val upack = json.getJSONObject("upack")
        val config_id = upack.getString("news_configid")
        val ad_configid = upack.getString("ad_configid")
        val biz_configid = upack.getString("biz_configid")
        val scenario_obj = json.getJSONObject("scenario")
        val scenario = scenario_obj.getString("desc")
        val article_impression_extra = json.getJSONObject("article_impression_extra")
        val content_type = article_impression_extra.getString("content_type")
        val strategy = article_impression_extra.getJSONObject("cpack").getString("strategy")
        val arr = Array(product_id, app_ver, config_id, content_type, strategy, ad_configid, biz_configid, scenario, uid)
        if (isNotNull(arr)) {
          ret = Option(arr)
        }
      }
    } catch {
      case ex: Exception => logger.warn("ReformatDataParser parseRequest failed to parse Json : {} ", str)
    }
    
    ret
  }
  
  def parseImpression(str: String): Option[Array[String]] = {
    var ret: Option[Array[String]] = None
    
    try {
      val json = JSON.parseObject(str)
      val product_id = json.getString("product_id")
      if (product_id.equals("ali") || product_id.equals("meizu") || product_id.equals("coolpad") || product_id.equals("fuyiping-gionee")) {
        var app_ver = json.getString("app_ver")
        if (app_ver.length() > 25) {
          app_ver = app_ver.substring(0, 24)
        }
        val uid = json.getString("uid")
        val upack = json.getJSONObject("upack")
        val config_id = upack.getString("news_configid")
        val ad_configid = upack.getString("ad_configid")
        val biz_configid = upack.getString("biz_configid")
        val scenario_obj = json.getJSONObject("scenario")
        val scenario = scenario_obj.getString("desc")
        val article_impression_extra = json.getJSONObject("article_impression_extra")
        val content_type = article_impression_extra.getString("content_type")
        val strategy = article_impression_extra.getJSONObject("cpack").getString("strategy")
        val arr = Array(product_id, app_ver, config_id, content_type, strategy, ad_configid, biz_configid, scenario, uid)
        if (isNotNull(arr)) {
          ret = Option(arr)
        }
      }
    } catch {
      case ex: Exception => logger.warn("ReformatDataParser parseImpression failed to parse Json : {} ", str)
    }
    
    ret
  }
  
  def parseClick(str: String): Option[Array[String]] = {
    var ret: Option[Array[String]] = None
    
    try {
      val json = JSON.parseObject(str)
      val product_id = json.getString("product_id")
      if (product_id.equals("ali") || product_id.equals("meizu") || product_id.equals("coolpad") || product_id.equals("fuyiping-gionee")) {
        var app_ver = json.getString("app_ver")
        if (app_ver.length() > 25) {
          app_ver = app_ver.substring(0, 24)
        }
        val uid = json.getString("uid")
        val upack = json.getJSONObject("upack")
        val config_id = upack.getString("news_configid")
        val ad_configid = upack.getString("ad_configid")
        val biz_configid = upack.getString("biz_configid")
        val scenario_obj = json.getJSONObject("scenario")
        val scenario = scenario_obj.getString("desc")
        val article_click_extra = json.getJSONObject("article_click_extra")
        val content_type = article_click_extra.getString("content_type")
        val strategy = article_click_extra.getJSONObject("cpack").getString("strategy")
        val arr = Array(product_id, app_ver, config_id, content_type, strategy, ad_configid, biz_configid, scenario, uid)
        if (isNotNull(arr)) {
          ret = Option(arr)
        }
      }
    } catch {
      case ex: Exception => logger.warn("ReformatDataParser parseClick failed to parse Json : {} ", str)
    }
    
    ret
  }
  
  def parseDwelltime(str: String): Option[Array[String]] = {
    var ret: Option[Array[String]] = None
    
    try {
      val json = JSON.parseObject(str)
      val product_id = json.getString("product_id")
      if (product_id.equals("ali") || product_id.equals("meizu") || product_id.equals("coolpad") || product_id.equals("fuyiping-gionee")) {
        var app_ver = json.getString("app_ver")
        if (app_ver.length() > 25) {
          app_ver = app_ver.substring(0, 24)
        }
        val uid = json.getString("uid")
        val upack = json.getJSONObject("upack")
        val config_id = upack.getString("news_configid")
        val ad_configid = upack.getString("ad_configid")
        val biz_configid = upack.getString("biz_configid")
        val scenario_obj = json.getJSONObject("scenario")
        val scenario = scenario_obj.getString("desc")
        val article_dwelltime_extra = json.getJSONObject("article_dwelltime_extra")
        val content_type = article_dwelltime_extra.getString("content_type")
        val strategy = article_dwelltime_extra.getJSONObject("cpack").getString("strategy")
        val dwelltime = article_dwelltime_extra.getString("dwelltime")
        val arr = Array(product_id, app_ver, config_id, content_type, strategy, ad_configid, biz_configid, scenario, uid, dwelltime)
        if (isNotNull(arr)) {
          ret = Option(arr)
        }
      }
    } catch {
      case ex: Exception => logger.warn("ReformatDataParser parseDwelltime failed to parse Json : {} ", str)
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