package com.inveno.news.other.parser

import com.alibaba.fastjson.JSON
import org.slf4j.LoggerFactory
import com.inveno.news.common.Constant

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
          val platform = json.getString("platform")
          val product_id = json.getString("product_id")
          if (product_id.equals("hotoday") || product_id.equals("mata")) {
            val promotion = json.getString("promotion")
            val article_impression_extra = json.getJSONObject("article_impression_extra")
            val content_type = article_impression_extra.getString("content_type")
            val strategy = article_impression_extra.getJSONObject("cpack").getString("strategy")
            val language = json.getString("app_lan")
            val app_ver = "all"
            val upack = json.getJSONObject("upack")
            val news_configid = "all"
            val biz_configid = "all"
            val ad_configid = upack.getString("ad_configid")
            val scenario_obj = json.getJSONObject("scenario")
            val scenario = scenario_obj.getString("position_desc")
            val scenario_channel = "all"
            val uid = json.getString("uid")
            val protocol = "https"
            val arr = Array(platform, product_id, app_ver, language, promotion, news_configid, protocol, content_type, strategy, scenario_channel, scenario, ad_configid, biz_configid, uid)
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
          val platform = json.getString("platform")
          val product_id = json.getString("product_id")
          if (product_id.equals("hotoday") || product_id.equals("mata")) {
            val promotion = json.getString("promotion")
            val article_impression_extra = json.getJSONObject("article_impression_extra")
            val content_type = article_impression_extra.getString("content_type")
            val strategy = article_impression_extra.getJSONObject("cpack").getString("strategy")
            val language = json.getString("app_lan")
            val app_ver = "all"
            val upack = json.getJSONObject("upack")
            val news_configid = "all"
            val biz_configid = "all"
            val ad_configid = upack.getString("ad_configid")
            val scenario_obj = json.getJSONObject("scenario")
            val scenario = scenario_obj.getString("position_desc")
            val scenario_channel = "all"
            val uid = json.getString("uid")
            val protocol = "https"
            val arr = Array(platform, product_id, app_ver, language, promotion, news_configid, protocol, content_type, strategy, scenario_channel, scenario, ad_configid, biz_configid, uid)
            if (isNotNull(arr)) {
              ret = Option(arr)
            }
          }  
    } catch {
      case ex: Exception => logger.warn("ReformatDataParser parseImpression failed to parse Json : {} ", str)
    }
    
    ret
  }
  
  def parseImpressionNopush(str: String): Option[Array[String]] = {
    var ret: Option[Array[String]] = None
    
    try {
        val json = JSON.parseObject(str)
        val scenario_obj = json.getJSONObject("scenario")
        var scenario = scenario_obj.getString("position_desc")
        if (!scenario.equals("push")) {
          scenario = "all"
          val scenario_channel = "all"
          val platform = json.getString("platform")
          val product_id = json.getString("product_id")
          if (product_id.equals("hotoday") || product_id.equals("mata")) {
            val promotion = json.getString("promotion")
            val article_impression_extra = json.getJSONObject("article_impression_extra")
            val content_type = article_impression_extra.getString("content_type")
            val strategy = article_impression_extra.getJSONObject("cpack").getString("strategy")
            val language = json.getString("app_lan")
            val app_ver = "all"
            val upack = json.getJSONObject("upack")
            val news_configid = "all"
            val biz_configid = "all"
            val ad_configid = upack.getString("ad_configid")
            val uid = json.getString("uid")
            val protocol = "https"
            val arr = Array(platform, product_id, app_ver, language, promotion, news_configid, protocol, content_type, strategy, scenario_channel, scenario, ad_configid, biz_configid, uid)
            if (isNotNull(arr)) {
              ret = Option(arr)
            }
          }
        }
    } catch {
      case ex: Exception => logger.warn("ReformatDataParser parseImpressionNopush failed to parse Json : {} ", str)
    }
    
    ret
  }
  
  def parseClick(str: String): Option[Array[String]] = {
    var ret: Option[Array[String]] = None
    
    try {
          val json = JSON.parseObject(str)
          val platform = json.getString("platform")
          val product_id = json.getString("product_id")
          if (product_id.equals("hotoday") || product_id.equals("mata")) {
            val promotion = json.getString("promotion")
            val article_click_extra = json.getJSONObject("article_click_extra")
            val content_type = article_click_extra.getString("content_type")
            val strategy = article_click_extra.getJSONObject("cpack").getString("strategy")
            val language = json.getString("app_lan")
            val app_ver = "all"
            val upack = json.getJSONObject("upack")
            val news_configid = "all"
            val biz_configid = "all"
            val ad_configid = upack.getString("ad_configid")
            val scenario_obj = json.getJSONObject("scenario")
            val scenario = scenario_obj.getString("position_desc")
            val scenario_channel = "all"
            val uid = json.getString("uid")
            val protocol = "https"
            val arr = Array(platform, product_id, app_ver, language, promotion, news_configid, protocol, content_type, strategy, scenario_channel, scenario, ad_configid, biz_configid, uid)
            if (isNotNull(arr)) {
              ret = Option(arr)
            }
          }
    } catch {
      case ex: Exception => logger.warn("ReformatDataParser parseClick failed to parse Json : {} ", str)
    }
    
    ret
  }
  
  def parseClickPush(str: String): Option[Array[String]] = {
    var ret: Option[Array[String]] = None
    
    try {
        val json = JSON.parseObject(str)
        val scenario_obj = json.getJSONObject("scenario")
        var scenario = scenario_obj.getString("position_desc")
        if (scenario.equals("push")) {
          scenario = "all"
          val scenario_channel = "all"
          val platform = json.getString("platform")
          val product_id = json.getString("product_id")
          if (product_id.equals("hotoday") || product_id.equals("mata")) {
            val promotion = json.getString("promotion")
            val article_click_extra = json.getJSONObject("article_click_extra")
            val content_type = article_click_extra.getString("content_type")
            val strategy = article_click_extra.getJSONObject("cpack").getString("strategy")
            val language = json.getString("app_lan")
            val app_ver = "all"
            val upack = json.getJSONObject("upack")
            val news_configid = "all"
            val biz_configid = "all"
            val ad_configid = upack.getString("ad_configid")
            val uid = json.getString("uid")
            val protocol = "https"
            val arr = Array(platform, product_id, app_ver, language, promotion, news_configid, protocol, content_type, strategy, scenario_channel, scenario, ad_configid, biz_configid, uid)
            if (isNotNull(arr)) {
              ret = Option(arr)
            }
          }  
        }
    } catch {
      case ex: Exception => logger.warn("ReformatDataParser parseClickPush failed to parse Json : {} ", str)
    }
    
    ret
  }
  
  def parseDwelltime(str: String): Option[Array[String]] = {
    var ret: Option[Array[String]] = None
    
    try {
          val json = JSON.parseObject(str)
          val platform = json.getString("platform")
          val product_id = json.getString("product_id")
          if (product_id.equals("hotoday") || product_id.equals("mata")) {
            val promotion = json.getString("promotion")
            val article_dwelltime_extra = json.getJSONObject("article_dwelltime_extra")
            val content_type = article_dwelltime_extra.getString("content_type")
            val strategy = article_dwelltime_extra.getJSONObject("cpack").getString("strategy")
            val language = json.getString("app_lan")
            val app_ver = "all"
            val upack = json.getJSONObject("upack")
            val news_configid = "all"
            val biz_configid = "all"
            val ad_configid = upack.getString("ad_configid")
            val scenario_obj = json.getJSONObject("scenario")
            val scenario = scenario_obj.getString("position_desc")
            val scenario_channel = "all"
            var dwelltime = article_dwelltime_extra.getString("dwelltime")
            if(content_type == "short_video" && dwelltime == "0") {
              dwelltime = article_dwelltime_extra.getString("play_time")
            }
            val uid = json.getString("uid")
            val protocol = "https"
            val arr = Array(platform, product_id, app_ver, language, promotion, news_configid, protocol, content_type, strategy, scenario_channel, scenario, ad_configid, biz_configid, uid, dwelltime)
            if (isNotNull(arr)) {
              ret = Option(arr)
            }
          }  
    } catch {
      case ex: Exception => logger.warn("ReformatDataParser parseDwelltime failed to parse Json : {} ", str)
    }
    
    ret
  }
  
  def parseListpageDwelltime(str: String): Option[Array[String]] = {
    var ret: Option[Array[String]] = None
    
    try {
          val json = JSON.parseObject(str)
          val platform = json.getString("platform")
          val product_id = json.getString("product_id")
          if (product_id.equals("hotoday") || product_id.equals("mata")) {
            val promotion = json.getString("promotion")
            val listpage_dwelltime_extra = json.getJSONObject("listpage_dwelltime_extra")
            val content_type = "unknown"
            val strategy = "unknown"
            val language = json.getString("app_lan")
            val app_ver = "all"
            val upack = json.getJSONObject("upack")
            val news_configid = "all"
            val biz_configid = "all"
            val ad_configid = upack.getString("ad_configid")
            val scenario_obj = json.getJSONObject("scenario")
            val scenario = scenario_obj.getString("position_desc")
            val scenario_channel = "all"
            val dwelltime = listpage_dwelltime_extra.getString("dwelltime")
            val uid = json.getString("uid")
            val protocol = "https"
            val arr = Array(platform, product_id, app_ver, language, promotion, news_configid, protocol, content_type, strategy, scenario_channel, scenario, ad_configid, biz_configid, uid, dwelltime)
            if (isNotNull(arr)) {
              ret = Option(arr)
            }
          }  
    } catch {
      case ex: Exception => logger.warn("ReformatDataParser parseListpageDwelltime failed to parse Json : {} ", str)
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