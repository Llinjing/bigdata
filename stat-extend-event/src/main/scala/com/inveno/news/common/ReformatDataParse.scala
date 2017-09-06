package com.inveno.news.common

import com.alibaba.fastjson.JSON
import com.inveno.news.log.LogSupport

/**
 * Created by dory on 2016/11/28.
 */
object ReformatDataParse extends LogSupport {
  def parseExtend(str: String): Option[Array[String]] = {
    var ret: Option[Array[String]] = None
    val json = JSON.parseObject(str)
    val product_id = json.getString("product_id")
    val news_configid = json.getJSONObject("upack").getString("news_configid")
    val app_ver = json.getString("app_ver")
    val language = json.getString("app_lan")
    val extend_extra = json.getJSONObject("extend_event_extra")
    val action_name = extend_extra.getString("action_name")
    val uid = json.getString("uid")
    val arr = Array(product_id, app_ver, news_configid, language, action_name, uid)
    try {
      if (isNull(arr)) {
        ret = Option(arr)
      }
    } catch {
      case ex: Exception => log.info("ReformatDataParser extend failed to parse Json !" + ex.printStackTrace())
    }
    ret
  }

  def isNull(arr: Array[String]): Boolean = {
    var result = true
    for (ele <- arr) {
      if (ele == null) {
        result = false
      }
    }
    result
  }

  /*def main(args: Array[String]) {
    val str="{\"aid\":\"916fa452a4eaac36\",\"api_ver\":\"2.0.0\",\"app_lan\":\"hindi\",\"app_ver\":\"2.3.1.0.0.3\",\"brand\":\"samsung\",\"event_id\":\"7\",\"event_time\":\"1481248794\",\"extend_event_extra\":{\"action_name\":\"request_success_timeexpend\",\"action_type\":\"2\",\"action_value\":{\"timeexpend\":14140}},\"gate_ip\":\"172.31.22.87\",\"imei\":\"357080054178745\",\"language\":\"en_gb\",\"log_time\":\"2016-12-09 01:59:51\",\"log_type\":\"report\",\"mcc\":\"IN\",\"mnc\":\"05\",\"model\":\"GT-I9082\",\"network\":\"2g\",\"osv\":\"4.2.2\",\"platform\":\"android\",\"product_id\":\"hotoday\",\"promotion\":\"gp\",\"protocol\":\"https\",\"report_time\":\"1481248843\",\"seq\":\"17\",\"sid\":\"581467146\",\"tk\":\"598b2b83edeee22327dfeca3fee1971f\",\"uid\":\"01011608280329354801000030903007\",\"upack\":{\"ad_configid\":\"18\",\"biz_configid\":\"23\",\"news_configid\":\"117\"}}"
    val arr = parseExtend(str).get
    println(arr.mkString("#"))
  }*/
}
