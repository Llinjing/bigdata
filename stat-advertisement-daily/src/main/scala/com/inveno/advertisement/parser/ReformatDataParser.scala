package com.inveno.advertisement.parser

import com.alibaba.fastjson.JSON
import org.slf4j.LoggerFactory
import com.inveno.advertisement.common.Constant

/**
 * @author Administrator
 */
object ReformatDataParser {
  
    private val CLASS_NAME = ReformatDataParser.getClass.getSimpleName

    private val logger = LoggerFactory.getLogger(CLASS_NAME)
    
    def parserOriginalAdDemand(str: String): Option[Array[String]] = {
      var ret: Option[Array[String]] = None
      
      try{
        val json = JSON.parseObject(str)
        val extend_event_extra = json.getJSONObject("extend_event_extra")
        val action_name = extend_event_extra.getString("action_name")
        if ("original_ad_demand".equals(action_name)) {
          val product_id = json.getString("product_id")
          val upack = json.getJSONObject("upack")
          val news_configid = upack.getString("news_configid")
          val ad_configid = upack.getString("ad_configid")
          val biz_configid = upack.getString("biz_configid")
          val language = json.getString("app_lan")
          val app_ver_tmp = json.getString("app_ver")
          val app_ver = app_ver_tmp.split(Constant.KEY_TAG_1).apply(0)
          val ad_source = "unknown"
          val platform = json.getString("platform")
          val action_value = extend_event_extra.getJSONObject("action_value")
          val adspace_id = action_value.getString("adspace_id")
          val promotion = json.getString("promotion")
          val t_adspace_id = "unknown"
          val arr = Array(product_id, news_configid, ad_configid, biz_configid, language, app_ver, ad_source, platform, adspace_id, promotion, t_adspace_id)
          if (isNotNull(arr)) {
            ret = Option(arr)
          }
        }        
      } catch {
        case ex: Exception => logger.warn("ReformatDataParser parserOriginalAdDemand failed to parse Json : {} ", str)
      }
      
      ret
    }
    
    def parseAdDemandFillSuccess(str: String): Option[Array[String]] = {
      var ret: Option[Array[String]] = None
      
      try{
        val json = JSON.parseObject(str)
        val extend_event_extra = json.getJSONObject("extend_event_extra")
        val action_name = extend_event_extra.getString("action_name")
        if ("ad_demand_fill_success".equals(action_name)) {
          val product_id = json.getString("product_id")
          val upack = json.getJSONObject("upack")
          val news_configid = upack.getString("news_configid")
          val ad_configid = upack.getString("ad_configid")
          val biz_configid = upack.getString("biz_configid")
          val language = json.getString("app_lan")
          val app_ver_tmp = json.getString("app_ver")
          val app_ver = app_ver_tmp.split(Constant.KEY_TAG_1).apply(0)
          val ad_source = "unknown"
          val platform = json.getString("platform")
          val action_value = extend_event_extra.getJSONObject("action_value")
          val adspace_id = action_value.getString("adspace_id")
          val promotion = json.getString("promotion")
          val t_adspace_id = "unknown"
          val arr = Array(product_id, news_configid, ad_configid, biz_configid, language, app_ver, ad_source, platform, adspace_id, promotion, t_adspace_id)
          if (isNotNull(arr)) {
            ret = Option(arr)
          }
        }
      } catch {
        case ex: Exception => logger.warn("ReformatDataParser parseAdDemandFillSuccess failed to parse Json : {} ", str)
      }
      
      ret
    }
    
    def parseAdRequestResponse(str: String): Option[(Array[String], (String, String))] = {
      var ret: Option[(Array[String], (String, String))] = None
      
      try{
        val json = JSON.parseObject(str)
        val extend_event_extra = json.getJSONObject("extend_event_extra")
        val action_name = extend_event_extra.getString("action_name")
        if ("ad_request_response".equals(action_name)) {
          val product_id = json.getString("product_id")
          val upack = json.getJSONObject("upack")
          val news_configid = upack.getString("news_configid")
          val ad_configid = upack.getString("ad_configid")
          val biz_configid = upack.getString("biz_configid")
          val language = json.getString("app_lan")
          val app_ver_tmp = json.getString("app_ver")
          val app_ver = app_ver_tmp.split(Constant.KEY_TAG_1).apply(0)
          val platform = json.getString("platform")
          val action_value = extend_event_extra.getJSONObject("action_value")
          val ad_source = action_value.getString("ad_source")
          val adspace_id = action_value.getString("adspace_id")
          val t_adspace_id = action_value.getString("t_adspace_id")
          val request_count = action_value.getString("request_count")
          val response_count = action_value.getString("response_count")
          val promotion = json.getString("promotion")
          val arr = Array(product_id, news_configid, ad_configid, biz_configid, language, app_ver, ad_source, platform, adspace_id, promotion, t_adspace_id)
          val obj = (request_count, response_count)
          if (isNotNull(arr)) {
            ret = Option((arr, obj))
          }
        }
      } catch {
        case ex: Exception => logger.warn("ReformatDataParser parseAdRequestResponse failed to parse Json : {} ", str)
      }
      
      ret
    }
    
    def parseImpression(str: String): Option[Array[String]] = {
      var ret: Option[Array[String]] = None
      
      try {
        val json = JSON.parseObject(str)
        val article_impression_extra = json.getJSONObject("article_impression_extra")
        val content_type = article_impression_extra.getString("content_type")
        if (content_type.startsWith("advertisement")) {
          val product_id = json.getString("product_id")
          val upack = json.getJSONObject("upack")
          val news_configid = upack.getString("news_configid")
          val ad_configid = upack.getString("ad_configid")
          val biz_configid = upack.getString("biz_configid")
          val language = json.getString("app_lan")
          val app_ver_tmp = json.getString("app_ver")
          val app_ver = app_ver_tmp.split(Constant.KEY_TAG_1).apply(0)
          val cpack = article_impression_extra.getJSONObject("cpack")
          val ad_source = cpack.getString("ad_source")
          val platform = json.getString("platform")
          val promotion = json.getString("promotion")
          var adspace_id = "unknown"
          var t_adspace_id = "unknown"
          val extra_msg = article_impression_extra.getJSONObject("extra_msg")
          if (extra_msg!=null) {
            adspace_id = extra_msg.getString("adspace_id")
            t_adspace_id = extra_msg.getString("t_adspace_id")
          }
          val arr = Array(product_id, news_configid, ad_configid, biz_configid, language, app_ver, ad_source, platform, adspace_id, promotion, t_adspace_id)
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
        if (content_type.startsWith("advertisement")) {
          val product_id = json.getString("product_id")
          val upack = json.getJSONObject("upack")
          val news_configid = upack.getString("news_configid")
          val ad_configid = upack.getString("ad_configid")
          val biz_configid = upack.getString("biz_configid")
          val language = json.getString("app_lan")
          val app_ver_tmp = json.getString("app_ver")
          val app_ver = app_ver_tmp.split(Constant.KEY_TAG_1).apply(0)
          val cpack = article_click_extra.getJSONObject("cpack")
          val ad_source = cpack.getString("ad_source")
          val platform = json.getString("platform")
          val promotion = json.getString("promotion")
          var adspace_id = "unknown"
          var t_adspace_id = "unknown"
          val extra_msg = article_click_extra.getJSONObject("extra_msg")
          if (extra_msg!=null) {
            adspace_id = extra_msg.getString("adspace_id")
            t_adspace_id = extra_msg.getString("t_adspace_id")
          }
          val arr = Array(product_id, news_configid, ad_configid, biz_configid, language, app_ver, ad_source, platform, adspace_id, promotion, t_adspace_id)
          if (isNotNull(arr)) {
            ret = Option(arr)
          }
        }
      } catch {
        case ex: Exception => logger.warn("ReformatDataParser parseClick failed to parse Json : {} ", str)
      }
      
      ret
    }
    
    def parseAdRequest(str: String): Option[Array[String]] = {
      var ret: Option[Array[String]] = None
      
      try{
        val json = JSON.parseObject(str)
        val extend_event_extra = json.getJSONObject("extend_event_extra")
        val action_name = extend_event_extra.getString("action_name")
        if ("ad_request".equals(action_name)) {
          val product_id = json.getString("product_id")
          val upack = json.getJSONObject("upack")
          val news_configid = upack.getString("news_configid")
          val ad_configid = upack.getString("ad_configid")
          val biz_configid = upack.getString("biz_configid")
          val language = json.getString("app_lan")
          val app_ver_tmp = json.getString("app_ver")
          val app_ver = app_ver_tmp.split(Constant.KEY_TAG_1).apply(0)
          val ad_source = "unknown"
          val platform = json.getString("platform")
          val action_value = extend_event_extra.getJSONObject("action_value")
          val adspace_id = action_value.getString("adspace_id")
          val promotion = json.getString("promotion")
          val t_adspace_id = "unknown"
          val arr = Array(product_id, news_configid, ad_configid, biz_configid, language, app_ver, ad_source, platform, adspace_id, promotion, t_adspace_id)
          if (isNotNull(arr)) {
            ret = Option(arr)
          }
        }
      } catch {
        case ex: Exception => logger.warn("ReformatDataParser parseAdRequest failed to parse Json : {} ", str)
      }
      
      ret
    }
    
    def parseAdRequestFill(str: String): Option[Array[String]] = {
      var ret: Option[Array[String]] = None
      
      try{
        val json = JSON.parseObject(str)
        val extend_event_extra = json.getJSONObject("extend_event_extra")
        val action_name = extend_event_extra.getString("action_name")
        if ("ad_request_fill".equals(action_name)) {
          val product_id = json.getString("product_id")
          val upack = json.getJSONObject("upack")
          val news_configid = upack.getString("news_configid")
          val ad_configid = upack.getString("ad_configid")
          val biz_configid = upack.getString("biz_configid")
          val language = json.getString("app_lan")
          val app_ver_tmp = json.getString("app_ver")
          val app_ver = app_ver_tmp.split(Constant.KEY_TAG_1).apply(0)
          val ad_source = "unknown"
          val platform = json.getString("platform")
          val action_value = extend_event_extra.getJSONObject("action_value")
          val adspace_id = action_value.getString("adspace_id")
          val promotion = json.getString("promotion")
          val t_adspace_id = "unknown"
          val arr = Array(product_id, news_configid, ad_configid, biz_configid, language, app_ver, ad_source, platform, adspace_id, promotion, t_adspace_id)
          if (isNotNull(arr)) {
            ret = Option(arr)
          }
        }
      } catch {
        case ex: Exception => logger.warn("ReformatDataParser parseAdRequestFill failed to parse Json : {} ", str)
      }
      
      ret
    }
    
    def parseAdSpaceImpression(str: String): Option[Array[String]] = {
      var ret: Option[Array[String]] = None
      
      try{
        val json = JSON.parseObject(str)
        val extend_event_extra = json.getJSONObject("extend_event_extra")
        val action_name = extend_event_extra.getString("action_name")
        if ("adspace_impression".equals(action_name)) {
          val product_id = json.getString("product_id")
          val upack = json.getJSONObject("upack")
          val news_configid = upack.getString("news_configid")
          val ad_configid = upack.getString("ad_configid")
          val biz_configid = upack.getString("biz_configid")
          val language = json.getString("app_lan")
          val app_ver_tmp = json.getString("app_ver")
          val app_ver = app_ver_tmp.split(Constant.KEY_TAG_1).apply(0)
          val ad_source = "unknown"
          val platform = json.getString("platform")
          val action_value = extend_event_extra.getJSONObject("action_value")
          val adspace_id = action_value.getString("adspace_id")
          val promotion = json.getString("promotion")
          val t_adspace_id = "unknown"
          val arr = Array(product_id, news_configid, ad_configid, biz_configid, language, app_ver, ad_source, platform, adspace_id, promotion, t_adspace_id)
          if (isNotNull(arr)) {
            ret = Option(arr)
          }
        }
      } catch {
        case ex: Exception => logger.warn("ReformatDataParser parseAdSpaceImpression failed to parse Json : {} ", str)
      }
      
      ret
    }
    
    def parseAdDemandFillTimeout(str: String): Option[Array[String]] = {
      var ret: Option[Array[String]] = None
      
      try{
        val json = JSON.parseObject(str)
        val extend_event_extra = json.getJSONObject("extend_event_extra")
        val action_name = extend_event_extra.getString("action_name")
        if ("ad_demand_fill_timeout".equals(action_name)) {
          val product_id = json.getString("product_id")
          val upack = json.getJSONObject("upack")
          val news_configid = upack.getString("news_configid")
          val ad_configid = upack.getString("ad_configid")
          val biz_configid = upack.getString("biz_configid")
          val language = json.getString("app_lan")
          val app_ver_tmp = json.getString("app_ver")
          val app_ver = app_ver_tmp.split(Constant.KEY_TAG_1).apply(0)
          val ad_source = "unknown"
          val platform = json.getString("platform")
          val action_value = extend_event_extra.getJSONObject("action_value")
          val adspace_id = action_value.getString("adspace_id")
          val promotion = json.getString("promotion")
          val t_adspace_id = "unknown"
          val arr = Array(product_id, news_configid, ad_configid, biz_configid, language, app_ver, ad_source, platform, adspace_id, promotion, t_adspace_id)
          if (isNotNull(arr)) {
            ret = Option(arr)
          }
        }
      } catch {
        case ex: Exception => logger.warn("ReformatDataParser parseAdDemandFillTimeout failed to parse Json : {} ", str)
      }
      
      ret
    }
    
    def parseAdDemandNoFill(str: String): Option[Array[String]] = {
      var ret: Option[Array[String]] = None
      
      try{
        val json = JSON.parseObject(str)
        val extend_event_extra = json.getJSONObject("extend_event_extra")
        val action_name = extend_event_extra.getString("action_name")
        if ("ad_demand_nofill".equals(action_name)) {
          val product_id = json.getString("product_id")
          val upack = json.getJSONObject("upack")
          val news_configid = upack.getString("news_configid")
          val ad_configid = upack.getString("ad_configid")
          val biz_configid = upack.getString("biz_configid")
          val language = json.getString("app_lan")
          val app_ver_tmp = json.getString("app_ver")
          val app_ver = app_ver_tmp.split(Constant.KEY_TAG_1).apply(0)
          val ad_source = "unknown"
          val platform = json.getString("platform")
          val action_value = extend_event_extra.getJSONObject("action_value")
          val adspace_id = action_value.getString("adspace_id")
          val promotion = json.getString("promotion")
          val t_adspace_id = "unknown"
          val arr = Array(product_id, news_configid, ad_configid, biz_configid, language, app_ver, ad_source, platform, adspace_id, promotion, t_adspace_id)
          if (isNotNull(arr)) {
            ret = Option(arr)
          }
        }
      } catch {
        case ex: Exception => logger.warn("ReformatDataParser parseAdDemandNoFill failed to parse Json : {} ", str)
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