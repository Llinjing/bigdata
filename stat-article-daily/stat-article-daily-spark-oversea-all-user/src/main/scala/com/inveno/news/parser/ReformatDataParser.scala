package com.inveno.news.parser

import com.alibaba.fastjson.JSON
import org.slf4j.LoggerFactory
import com.inveno.news.common.Constant

/**
 * @author Administrator
 */
object ReformatDataParser {
  
    private val CLASS_NAME = ReformatDataParser.getClass.getSimpleName

    private val logger = LoggerFactory.getLogger(CLASS_NAME)
    
    def parseArticle(str: String): Option[Array[String]] = {
      var ret: Option[Array[String]] = None
      
      try{
          val json = JSON.parseObject(str)
          val content_id = json.getString("content_id")
          val rate = json.getString("rate")
          val title = json.getString("title")
          val source = json.getString("source")
          val sourceType = json.getString("sourceType")
          val sourceFeeds = json.getString("sourceFeeds")
          val publisher = json.getString("publisher")
          val adult_score = "unknown"
          val channel = json.getString("channel")
          val discoveryTime = json.getString("discoveryTime")
          val important_level = json.getString("important_level")
          val link = json.getString("link")
          val flag = json.getString("flag")
          val arr = Array(content_id, rate, title, source, sourceType, sourceFeeds, publisher, adult_score, channel, discoveryTime, important_level, link, flag)
          if (isNotNull(arr)) {
           ret = Option(arr)
          } 
      }catch {
        case ex: Exception => logger.warn("ReformatDataParser parseArticle failed to parse Json : {} ", str)
      }
      
      ret
    }
    
    def parseRequest(str: String): Option[Array[String]] = {
      var ret: Option[Array[String]] = None
      
      try {
        val json = JSON.parseObject(str)
        val article_request_extra = json.getJSONObject("article_request_extra")
        val content_type = article_request_extra.getString("content_type")
        if (!content_type.startsWith("advertisement")) {
          val content_id = article_request_extra.getString("content_id")
          val platform = json.getString("platform")
          val product_id = json.getString("product_id")
          val upack = json.getJSONObject("upack")
          val news_configid = upack.getString("news_configid")
          val ad_configid = upack.getString("ad_configid")
          val biz_configid = upack.getString("biz_configid")
          val language = json.getString("app_lan")
          val app_ver_tmp = json.getString("app_ver")
          val app_ver = app_ver_tmp.split(Constant.KEY_TAG_1).apply(0)
          val cpack = article_request_extra.getJSONObject("cpack")
          val strategy = cpack.getString("strategy")
          val category = cpack.getString("category")
          val scenario = json.getJSONObject("scenario")
          val scenario_position = scenario.getString("position_desc")
          val scenario_channel = scenario.getString("channel_desc")
          val view_mode = "unknown"
          val arr = Array(platform, product_id, news_configid, scenario_position, scenario_channel, strategy, app_ver, language, ad_configid, biz_configid, view_mode, content_type, category, content_id)
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
        val article_impression_extra = json.getJSONObject("article_impression_extra")
        val content_type = article_impression_extra.getString("content_type")
        if (!content_type.startsWith("advertisement")) {
          val content_id = article_impression_extra.getString("content_id")
          val platform = json.getString("platform")
          val product_id = json.getString("product_id")
          val upack = json.getJSONObject("upack")
          val news_configid = upack.getString("news_configid")
          val ad_configid = upack.getString("ad_configid")
          val biz_configid = upack.getString("biz_configid")
          val language = json.getString("app_lan")
          val app_ver_tmp = json.getString("app_ver")
          val app_ver = app_ver_tmp.split(Constant.KEY_TAG_1).apply(0)
          val cpack = article_impression_extra.getJSONObject("cpack")
          val strategy = cpack.getString("strategy")
          val category = cpack.getString("category")
          val scenario = json.getJSONObject("scenario")
          val scenario_position = scenario.getString("position_desc")
          val scenario_channel = scenario.getString("channel_desc")
          val view_mode = article_impression_extra.getString("view_mode")
          val arr = Array(platform, product_id, news_configid, scenario_position, scenario_channel, strategy, app_ver, language, ad_configid, biz_configid, view_mode, content_type, category, content_id)
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
        val article_click_extra = json.getJSONObject("article_click_extra")
        val content_type = article_click_extra.getString("content_type")
        if (!content_type.startsWith("advertisement")) {
          val content_id = article_click_extra.getString("content_id")
          val platform = json.getString("platform")
          val product_id = json.getString("product_id")
          val upack = json.getJSONObject("upack")
          val news_configid = upack.getString("news_configid")
          val ad_configid = upack.getString("ad_configid")
          val biz_configid = upack.getString("biz_configid")
          val language = json.getString("app_lan")
          val app_ver_tmp = json.getString("app_ver")
          val app_ver = app_ver_tmp.split(Constant.KEY_TAG_1).apply(0)
          val cpack = article_click_extra.getJSONObject("cpack")
          val strategy = cpack.getString("strategy")
          val category = cpack.getString("category")
          val scenario = json.getJSONObject("scenario")
          val scenario_position = scenario.getString("position_desc")
          val scenario_channel = scenario.getString("channel_desc")
          val view_mode = article_click_extra.getString("view_mode")
          val arr = Array(platform, product_id, news_configid, scenario_position, scenario_channel, strategy, app_ver, language, ad_configid, biz_configid, view_mode, content_type, category, content_id)
          if (isNotNull(arr)) {
           ret = Option(arr)
          } 
        }        
      } catch {
        case ex: Exception => logger.warn("ReformatDataParser parseClick failed to parse Json : {} ", str)
      }
      
      ret
    }
    
    def parseDwelltime(str: String): Option[(Array[String], String)] = {
      var ret: Option[(Array[String], String)] = None
      
      try {
        val json = JSON.parseObject(str)
        val article_dwelltime_extra = json.getJSONObject("article_dwelltime_extra")
        val content_type = article_dwelltime_extra.getString("content_type")
        if (!content_type.startsWith("advertisement")) {
          val content_id = article_dwelltime_extra.getString("content_id")
          val platform = json.getString("platform")
          val product_id = json.getString("product_id")
          val upack = json.getJSONObject("upack")
          val news_configid = upack.getString("news_configid")
          val ad_configid = upack.getString("ad_configid")
          val biz_configid = upack.getString("biz_configid")
          val language = json.getString("app_lan")
          val app_ver_tmp = json.getString("app_ver")
          val app_ver = app_ver_tmp.split(Constant.KEY_TAG_1).apply(0)
          val cpack = article_dwelltime_extra.getJSONObject("cpack")
          val strategy = cpack.getString("strategy")
          val category = cpack.getString("category")
          val scenario = json.getJSONObject("scenario")
          val scenario_position = scenario.getString("position_desc")
          val scenario_channel = scenario.getString("channel_desc")
          val view_mode = article_dwelltime_extra.getString("view_mode")
          var dwelltime = article_dwelltime_extra.getString("dwelltime")
          if(content_type == "short_video" && dwelltime == "0") {
            dwelltime = article_dwelltime_extra.getString("play_time")
          }
          val arr = Array(platform, product_id, news_configid, scenario_position, scenario_channel, strategy, app_ver, language, ad_configid, biz_configid, view_mode, content_type, category, content_id)
          if (isNotNull(arr)) {
           ret = Option((arr, dwelltime))
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