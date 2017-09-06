package com.inveno.news.nearline.common

import com.inveno.news.nearline.filter.InfoProcess
import inveno.bigdata.common.Using

object FeedBackUtil extends Using with InfoProcess {

  val KEY_TAG = "###"

  def makeNativeArticleKeys(info: NativeJson): List[String] = {
    if (IsAdvertise(info.content_type)) {
      val ad_all = NativeJson(info.uid,
        info.content_id,
        info.product_id,
        info.config_id,
        info.ad_configid,
        info.biz_configid,
        info.scenario,
        info.strategy,
        info.app_ver,
        "advertisement_all",
        info.dwelltime,
        info.language,
        info.isValid)
      List(genArticleKey(info), genArticleKey(ad_all))
    } else List(genArticleKey(info))
  }

  def genArticleKey(info: NativeJson): String = {
    s"${info.content_id}$KEY_TAG${info.product_id}$KEY_TAG${info.config_id}$KEY_TAG${info.ad_configid}$KEY_TAG${info.biz_configid}$KEY_TAG${info.scenario}$KEY_TAG${info.strategy}$KEY_TAG${info.app_ver}$KEY_TAG${info.content_type}$KEY_TAG${info.language}"
  }

  def makeNativeUserKeys(info: NativeJson): List[String] = {
    if (IsAdvertise(info.content_type)) {
      val ad_all = NativeJson(info.uid,
        info.content_id,
        info.product_id,
        info.config_id,
        info.ad_configid,
        info.biz_configid,
        info.scenario,
        info.strategy,
        info.app_ver,
        "advertisement_all",
        info.dwelltime,
        info.language,
        info.isValid)
      List(genUserKey(info), genUserKey(ad_all))
    } else List(genUserKey(info))
  }

  def genUserKey(info: NativeJson): String = {
    s"${info.uid}$KEY_TAG${info.product_id}$KEY_TAG${info.config_id}$KEY_TAG${info.ad_configid}$KEY_TAG${info.biz_configid}$KEY_TAG${info.scenario}$KEY_TAG${info.strategy}$KEY_TAG${info.app_ver}$KEY_TAG${info.content_type}$KEY_TAG${info.language}"
  }

  def makeNativeUserResult(timestamp: String, keyfields: Array[String], value: (Long, Long, Long, Long)): String = {
    var res = ""
    usingJson {
      json =>
        val Array(uid, product_id, config_id, ad_config, biz_config, scenario, strategy, app_ver, content_type, language) = keyfields
        json.put("timestamp", timestamp)
        json.put("uid", uid)
        json.put("product_id", product_id)
        json.put("config_id", config_id)
        json.put("ad_configid", ad_config)
        json.put("biz_configid", biz_config)
        json.put("scenario", scenario)
        json.put("strategy", strategy)
        json.put("app_ver", app_ver)
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

  def makeNativeArticleResult(timestamp: String, keyfields: Array[String], value: (Long, Long, Long, Long)): String = {
    var res = ""
    usingJson {
      json =>
        val Array(content_id, product_id, config_id, ad_config, biz_config, scenario, strategy, app_ver, content_type, language) = keyfields
        json.put("timestamp", timestamp)
        json.put("content_id", content_id)
        json.put("product_id", product_id)
        json.put("config_id", config_id)
        json.put("ad_configid", ad_config)
        json.put("biz_configid", biz_config)
        json.put("scenario", scenario)
        json.put("app_ver", app_ver)
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