package com.inveno.news.base.parser

import com.alibaba.fastjson.JSON
import org.slf4j.LoggerFactory
import com.inveno.news.common.Constant
import scala.collection.Map

/**
 * @author Administrator
 */
object ReformatDataParser {
  
  private val CLASS_NAME = ReformatDataParser.getClass.getSimpleName

  private val logger = LoggerFactory.getLogger(CLASS_NAME)
  
  def parseRequest(str: String, map: Map[String, String]): Option[Array[String]] = {
    var ret: Option[Array[String]] = None
    
    try {
          val json = JSON.parseObject(str)
          val platform = json.getString("platform")
          val product_id = json.getString("product_id")
          val article_impression_extra = json.getJSONObject("article_impression_extra")
          val content_type = article_impression_extra.getString("content_type")
          val strategy = article_impression_extra.getJSONObject("cpack").getString("strategy")
          val language = json.getString("app_lan")
          val app_ver_tmp = json.getString("app_ver")
          val app_ver = app_ver_tmp.split(Constant.KEY_TAG_1).apply(0)
          val upack = json.getJSONObject("upack")
          val config_id = upack.getString("news_configid")
          val biz_configid = upack.getString("biz_configid")
          val scenario_obj = json.getJSONObject("scenario")
          val scenario = scenario_obj.getString("position_desc")
          val scenario_channel = scenario_obj.getString("channel_desc")
          val uid = json.getString("uid")
          val protocol = "https"
          if (map.contains(uid)) {
            val promotion = map.getOrElse(uid, "unknown")
            val arr = Array(platform, product_id, app_ver, language, promotion, config_id, protocol, content_type, strategy, scenario_channel, scenario, biz_configid, uid)
            if (isNotNull(arr)) {
              ret = Option(arr)
            }
          }
    } catch {
      case ex: Exception => logger.warn("ReformatDataParser parseRequest failed to parse Json : {} ", str)
    }
    
    ret
  }
  
  def parseImpression(str: String, map: Map[String, String]): Option[Array[String]] = {
    var ret: Option[Array[String]] = None
    
    try {
          val json = JSON.parseObject(str)
          val platform = json.getString("platform")
          val product_id = json.getString("product_id")
          val article_impression_extra = json.getJSONObject("article_impression_extra")
          val content_type = article_impression_extra.getString("content_type")
          val strategy = article_impression_extra.getJSONObject("cpack").getString("strategy")
          val language = json.getString("app_lan")
          val app_ver_tmp = json.getString("app_ver")
          val app_ver = app_ver_tmp.split(Constant.KEY_TAG_1).apply(0)
          val upack = json.getJSONObject("upack")
          val config_id = upack.getString("news_configid")
          val biz_configid = upack.getString("biz_configid")
          val scenario_obj = json.getJSONObject("scenario")
          val scenario = scenario_obj.getString("position_desc")
          val scenario_channel = scenario_obj.getString("channel_desc")
          val uid = json.getString("uid")
          val protocol = "https"
          if (map.contains(uid)) {
            val promotion = map.getOrElse(uid, "unknown")
            val arr = Array(platform, product_id, app_ver, language, promotion, config_id, protocol, content_type, strategy, scenario_channel, scenario, biz_configid, uid)
            if (isNotNull(arr)) {
              ret = Option(arr)
            }
          } 
    } catch {
      case ex: Exception => logger.warn("ReformatDataParser parseImpression failed to parse Json : {} ", str)
    }
    
    ret
  }
  
  def parseImpressionNopush(str: String, map: Map[String, String]): Option[Array[String]] = {
    var ret: Option[Array[String]] = None
    
    try {
        val json = JSON.parseObject(str)
        val scenario_obj = json.getJSONObject("scenario")
        var scenario = scenario_obj.getString("position_desc")
        if (!scenario.equals("push")) {
          scenario = "all"
          val scenario_channel = scenario_obj.getString("channel_desc")
          val platform = json.getString("platform")
          val product_id = json.getString("product_id")
          val article_impression_extra = json.getJSONObject("article_impression_extra")
          val content_type = article_impression_extra.getString("content_type")
          val strategy = article_impression_extra.getJSONObject("cpack").getString("strategy")
          val language = json.getString("app_lan")
          val app_ver_tmp = json.getString("app_ver")
          val app_ver = app_ver_tmp.split(Constant.KEY_TAG_1).apply(0)
          val upack = json.getJSONObject("upack")
          val config_id = upack.getString("news_configid")
          val biz_configid = upack.getString("biz_configid")
          val uid = json.getString("uid")
          val protocol = "https"
          if (map.contains(uid)) {
            val promotion = map.getOrElse(uid, "unknown")  
            val arr = Array(platform, product_id, app_ver, language, promotion, config_id, protocol, content_type, strategy, scenario_channel, scenario, biz_configid, uid)
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
  
  def parseClick(str: String, map: Map[String, String]): Option[Array[String]] = {
    var ret: Option[Array[String]] = None
    
    try {
          val json = JSON.parseObject(str)
          val platform = json.getString("platform")
          val product_id = json.getString("product_id")
          val article_click_extra = json.getJSONObject("article_click_extra")
          val content_type = article_click_extra.getString("content_type")
          val strategy = article_click_extra.getJSONObject("cpack").getString("strategy")
          val language = json.getString("app_lan")
          val app_ver_tmp = json.getString("app_ver")
          val app_ver = app_ver_tmp.split(Constant.KEY_TAG_1).apply(0)
          val upack = json.getJSONObject("upack")
          val config_id = upack.getString("news_configid")
          val biz_configid = upack.getString("biz_configid")
          val scenario_obj = json.getJSONObject("scenario")
          val scenario = scenario_obj.getString("position_desc")
          val scenario_channel = scenario_obj.getString("channel_desc")
          val uid = json.getString("uid")
          val protocol = "https"
          if (map.contains(uid)) {
            val promotion = map.getOrElse(uid, "unknown") 
            val arr = Array(platform, product_id, app_ver, language, promotion, config_id, protocol, content_type, strategy, scenario_channel, scenario, biz_configid, uid)
            if (isNotNull(arr)) {
              ret = Option(arr)
            }
          }
    } catch {
      case ex: Exception => logger.warn("ReformatDataParser parseClick failed to parse Json : {} ", str)
    }
    
    ret
  }
  
  def parseClickPush(str: String, map: Map[String, String]): Option[Array[String]] = {
    var ret: Option[Array[String]] = None
    
    try {
        val json = JSON.parseObject(str)
        val scenario_obj = json.getJSONObject("scenario")
        var scenario = scenario_obj.getString("position_desc")
        if (scenario.equals("push")) {
          scenario = "all"
          val scenario_channel = scenario_obj.getString("channel_desc")
          val platform = json.getString("platform")
          val product_id = json.getString("product_id")
          val article_click_extra = json.getJSONObject("article_click_extra")
          val content_type = article_click_extra.getString("content_type")
          val strategy = article_click_extra.getJSONObject("cpack").getString("strategy")
          val language = json.getString("app_lan")
          val app_ver_tmp = json.getString("app_ver")
          val app_ver = app_ver_tmp.split(Constant.KEY_TAG_1).apply(0)
          val upack = json.getJSONObject("upack")
          val config_id = upack.getString("news_configid")
          val biz_configid = upack.getString("biz_configid")
          val uid = json.getString("uid")
          val protocol = "https"
          if (map.contains(uid)) {
            val promotion = map.getOrElse(uid, "unknown") 
            val arr = Array(platform, product_id, app_ver, language, promotion, config_id, protocol, content_type, strategy, scenario_channel, scenario, biz_configid, uid)
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
  
  def parseDwelltime(str: String, map: Map[String, String]): Option[Array[String]] = {
    var ret: Option[Array[String]] = None
    
    try {
          val json = JSON.parseObject(str)
          val platform = json.getString("platform")
          val product_id = json.getString("product_id")
          val article_dwelltime_extra = json.getJSONObject("article_dwelltime_extra")
          val content_type = article_dwelltime_extra.getString("content_type")
          val strategy = article_dwelltime_extra.getJSONObject("cpack").getString("strategy")
          val language = json.getString("app_lan")
          val app_ver_tmp = json.getString("app_ver")
          val app_ver = app_ver_tmp.split(Constant.KEY_TAG_1).apply(0)
          val upack = json.getJSONObject("upack")
          val config_id = upack.getString("news_configid")
          val biz_configid = upack.getString("biz_configid")
          val scenario_obj = json.getJSONObject("scenario")
          val scenario = scenario_obj.getString("position_desc")
          val scenario_channel = scenario_obj.getString("channel_desc")
          var dwelltime = article_dwelltime_extra.getString("dwelltime")
          if(content_type == "short_video" && dwelltime == "0") {
            dwelltime = article_dwelltime_extra.getString("play_time")
          }
          val uid = json.getString("uid")
          val protocol = "https"
          if (map.contains(uid)) {
            val promotion = map.getOrElse(uid, "unknown") 
            val arr = Array(platform, product_id, app_ver, language, promotion, config_id, protocol, content_type, strategy, scenario_channel, scenario, biz_configid, uid, dwelltime)
            if (isNotNull(arr)) {
              ret = Option(arr)
            } 
          }
    } catch {
      case ex: Exception => logger.warn("ReformatDataParser parseDwelltime failed to parse Json : {} ", str)
    }
    
    ret
  }
  
  def parseListpageDwelltime(str: String, map: Map[String, String]): Option[Array[String]] = {
    var ret: Option[Array[String]] = None
    
    try {
          val json = JSON.parseObject(str)
          val platform = json.getString("platform")
          val product_id = json.getString("product_id")
          val listpage_dwelltime_extra = json.getJSONObject("listpage_dwelltime_extra")
          val content_type = "unknown"
          val strategy = "unknown"
          val language = json.getString("app_lan")
          val app_ver_tmp = json.getString("app_ver")
          val app_ver = app_ver_tmp.split(Constant.KEY_TAG_1).apply(0)
          val upack = json.getJSONObject("upack")
          val config_id = upack.getString("news_configid")
          val biz_configid = upack.getString("biz_configid")
          val scenario_obj = json.getJSONObject("scenario")
          val scenario = scenario_obj.getString("position_desc")
          val scenario_channel = scenario_obj.getString("channel_desc")
          val dwelltime = listpage_dwelltime_extra.getString("dwelltime")
          val uid = json.getString("uid")
          val protocol = "https"
          if (map.contains(uid)) {
            val promotion = map.getOrElse(uid, "unknown") 
            val arr = Array(platform, product_id, app_ver, language, promotion, config_id, protocol, content_type, strategy, scenario_channel, scenario, biz_configid, uid, dwelltime)
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