package com.inveno.news.nearline.common

import com.inveno.news.nearline.filter.InfoProcess
import inveno.bigdata.common.Using


object FeedBackUtil extends Using with InfoProcess {

  val KEY_TAG = "###"

  def makeOverseasArticleKey(info: OverseasJson): String = {
    s"${info.content_id}$KEY_TAG${info.platform}$KEY_TAG${info.product_id}$KEY_TAG${info.config_id}$KEY_TAG${info.ad_configid}$KEY_TAG${info.ad_source}$KEY_TAG${info.biz_configid}$KEY_TAG${info.scenario}$KEY_TAG${info.scenarioChannel}$KEY_TAG${info.app_ver}$KEY_TAG${info.strategy}$KEY_TAG${info.content_type}$KEY_TAG${info.app_lan}"
  }

  def makeOverseasUserKey(info: OverseasJson): String = {
    s"${info.uid}$KEY_TAG${info.platform}$KEY_TAG${info.product_id}$KEY_TAG${info.config_id}$KEY_TAG${info.ad_configid}$KEY_TAG${info.biz_configid}$KEY_TAG${info.scenario}$KEY_TAG${info.scenarioChannel}$KEY_TAG${info.app_ver}$KEY_TAG${info.strategy}$KEY_TAG${info.content_type}$KEY_TAG${info.app_lan}"
  }

  def makeOverseasUserResult(timestamp: String, fields: Array[String], value: (Long, Long, Long, Long)): String = {
    var res: String = ""
    usingJson {
      json =>
        val Array(uid, platform, product_id, config_id, ad_configid, biz_configid, scenario, scenarioChannel, appVersion, strategy, content_type, language) = fields
        json.put("timestamp", timestamp)
        json.put("uid", uid)
        json.put("platform", platform)
        json.put("product_id", product_id)
        json.put("config_id", config_id)
        json.put("ad_configid", ad_configid)
        json.put("biz_configid", biz_configid)
        json.put("scenario", scenario)
        json.put("scenario_channel", scenarioChannel)
        json.put("app_ver", appVersion)
        json.put("strategy", strategy)
        json.put("content_type", content_type)
        json.put("language", language)
        json.put("request", value._1)
        json.put("request_valid", getFlag(value._1))
        json.put("impression", value._2)
        json.put("impression_valid", getFlag(value._2))
        json.put("click", value._3)
        json.put("click_valid", getFlag(value._3))
        json.put("dwelltime", value._4)
        res = json.toString
    }
    res
  }

  def makeOverseasArticleResult(timestamp: String, fields: Array[String], value: (Long, Long, Long, Long)): String = {
    var res = ""
    usingJson {
      json =>
        val Array(content_id, platform, product_id, config_id, ad_configid, ad_source, biz_configid, scenario, scenarioChannel, appVersion, strategy, content_type, language) = fields
        json.put("timestamp", timestamp)
        json.put("content_id", content_id)
        json.put("platform", platform)
        json.put("product_id", product_id)
        json.put("config_id", config_id)
        json.put("ad_configid", ad_configid)
        json.put("ad_source", ad_source)
        json.put("biz_configid", biz_configid)
        json.put("scenario", scenario)
        json.put("scenario_channel", scenarioChannel)
        json.put("app_ver", appVersion)
        json.put("strategy", strategy)
        json.put("content_type", content_type)
        json.put("language", language)
        json.put("request", value._1)
        json.put("impression", value._2)
        json.put("click", value._3)
        json.put("dwelltime", value._4)
        res = json.toString
    }
    res
  }

}