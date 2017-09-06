package com.inveno.advertise.nearline.common

import inveno.bigdata.common.Using
import com.inveno.advertise.nearline.filter.InfoProcess


object FeedBackUtil extends Using with InfoProcess {

  val KEY_TAG = "###"

  def generateAdvertiseKey(info: Array[String]): String = {
    s"""${info(AdvJson.ad_id)}$KEY_TAG${info(AdvJson.ad_type)}$KEY_TAG${info(AdvJson.ad_source)}$KEY_TAG${info(AdvJson.advertiser_name)}$KEY_TAG${info(AdvJson.ad_product_name)}$KEY_TAG${info(AdvJson.industry)}$KEY_TAG${info(AdvJson.app_ver)}$KEY_TAG${info(AdvJson.product_id)}$KEY_TAG${info(AdvJson.position_id)}$KEY_TAG${info(AdvJson.position_type)}$KEY_TAG${info(AdvJson.target_size)}$KEY_TAG${info(AdvJson.daily_count_limit)}$KEY_TAG${info(AdvJson.news_configid)}$KEY_TAG${info(AdvJson.biz_configid)}$KEY_TAG${info(AdvJson.ad_configid)}$KEY_TAG${info(AdvJson.sort_weight)}$KEY_TAG${info(AdvJson.pay_model)}$KEY_TAG${info(AdvJson.channel)}$KEY_TAG${info(AdvJson.scenario)}$KEY_TAG${info(AdvJson.unit_price)}$KEY_TAG${info(AdvJson.log_type)}"""
  }

  def generateUserKey(info: Array[String]): String = {
    s"""${info(AdvJson.uid)}$KEY_TAG${info(AdvJson.product_id)}$KEY_TAG${info(AdvJson.ad_source)}$KEY_TAG${info(AdvJson.log_type)}$KEY_TAG${info(AdvJson.news_configid)}$KEY_TAG${info(AdvJson.biz_configid)}$KEY_TAG${info(AdvJson.ad_configid)}"""
  }

  def makeAdvertiseFeedBackResult(timestamp: String, fields: Array[String], value: (Long, Long, Long)): String = {
    var res = ""
    usingJson {
      json =>
        val Array(ad_id, ad_type, ad_source, advertiser_name, ad_product_name, industry, app_ver, product_id, position_id, position_type, target_size, daily_count_limit, news_configid, biz_configid, ad_configid, sort_weight, pay_model, channel, scenario, unit_price, log_type) = fields
        json.put("timestamp", timestamp)
        json.put("ad_id", ad_id)
        json.put("ad_type", ad_type)
        json.put("ad_source", ad_source)
        json.put("advertiser_name", advertiser_name)
        json.put("ad_product_name", ad_product_name)
        json.put("industry", industry)
        json.put("app_ver", app_ver)
        json.put("product_id", product_id)
        json.put("position_id", position_id) //"position_id": "156",
        json.put("position_type", position_type)
        json.put("target_size", target_size)
        json.put("daily_count_limit", daily_count_limit)
        json.put("news_configid", news_configid)
        json.put("biz_configid", biz_configid)
        json.put("ad_configid", ad_configid)
        json.put("sort_weight", sort_weight)
        json.put("pay_model", pay_model)
        json.put("channel", channel)
        json.put("scenario", scenario)
        json.put("unit_price", unit_price)
        json.put("log_type", log_type)
        json.put("complete_rate", getCompleteRate(pay_model, daily_count_limit, log_type, value._1, value._2, value._3))
        json.put("request", value._1)
        json.put("impression", value._2)
        json.put("click", value._3)
        json.put("budget_cost", getBudgetCost(pay_model, unit_price, log_type, value._1, value._2, value._3))
        res = json.toString
    }
    res
  }

  def makeUserFeedBackResult(timestamp: String, fields: Array[String], value: (Long, Long, Long)): String = {
    var res = ""
    usingJson {
      json =>
        val Array(uid, product_id, ad_source, log_type, news_configid, ad_configid, biz_configid) = fields
        json.put("timestamp", timestamp)
        json.put("uid", uid)
        json.put("product_id", product_id)
        json.put("news_configid", news_configid)
        json.put("ad_configid", ad_configid)
        json.put("biz_configid", biz_configid)
        json.put("ad_source", ad_source)
        json.put("log_type", log_type)
        json.put("request", value._1)
        json.put("request_valid", getFlag(value._1))
        json.put("impression", value._2)
        json.put("impression_valid", getFlag(value._2))
        json.put("click", value._3)
        json.put("click_valid", getFlag(value._3))
        res = json.toString
    }
    res
  }

}