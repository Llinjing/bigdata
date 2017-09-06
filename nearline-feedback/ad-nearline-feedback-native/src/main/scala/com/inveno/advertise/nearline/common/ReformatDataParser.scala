package com.inveno.advertise.nearline.common

import com.alibaba.fastjson.{JSON, JSONObject}
import com.inveno.advertise.nearline.filter.InfoProcess
import com.inveno.advertise.nearline.log.LogSupport

object ReformatDataParser extends LogSupport with InfoProcess {

  private def parseJson(line: String)(op: JSONObject => Unit) = {
    try {
      val json = JSON.parseObject(line)
      op(json)
    } catch {
      case ex: Exception => log.info(s"ReformatDataParser failed to parse Json ! Json info => $line" + ex.printStackTrace())
    }
  }

  def parseAdvertiseRequest(line: String): Array[String] = {
    var data = Array[String]()
    parseJson(line) {
      json =>
        val requestDelivery = json.getJSONObject("ad_request_extra").getJSONObject("delivery")

        val ad_id = requestDelivery.getString("delivery_id") match {
          case "" => "unknown"
          case id if id != null => id
          case _ => "unknown"
        }
        val ad_source = requestDelivery.getJSONObject("ad").getString("ad_source") match {
          case "" => "unknown"
          case source if source != null => source
          case _ => "unknown"
        }
        val ad_type = requestDelivery.getJSONObject("ad").getString("type") match {
          case "" => "unknown"
          case ty if ty != null => ty
          case _ => "unknown"
        }
        val advertiser_name = requestDelivery.getJSONObject("ad").getString("advertiser_name") match {
          case "" => "unknown"
          case name if name != null => name
          case _ => "unknown"
        }
        val ad_product_name = requestDelivery.getJSONObject("ad").getString("ad_product_name") match {
          case "" => "unknown"
          case ad_product if ad_product != null => ad_product
          case _ => "unknown"
        }
        val industry = requestDelivery.getJSONObject("ad").getString("industry") match {
          case "" => "unknown"
          case ind if ind != null => ind
          case _ => "unknown"
        }

        val position_id = requestDelivery.getJSONObject("marketing").getString("position_id") match {
          case "" => "unknown"
          case pos if pos != null => pos
          case _ => "unknown"
        }
        val position_type = requestDelivery.getJSONObject("marketing").getString("position_type") match {
          case "" => "unknown"
          case pos if pos != null => pos
          case _ => "unknown"
        }
        val target_size = requestDelivery.getJSONObject("marketing").getString("target_size") match {
          case "" => "unknown"
          case size if size != null => size
          case _ => "unknown"
        }
        val daily_count_limit = requestDelivery.getJSONObject("marketing").getString("daily_count_limit") match {
          case "" => "0"
          case limit if limit != null => limit
          case _ => "0"
        }
        val pay_model = requestDelivery.getJSONObject("marketing").getString("pay_model") match {
          case "" => "unknown"
          case model if model != null => model
          case _ => "unknown"
        }
        val unit_price = requestDelivery.getJSONObject("marketing").getString("unit_price") match {
          case "" => "0.0"
          case price if price != null => price
          case _ => "0.0"
        }

        val channel = requestDelivery.getJSONObject("ad_scenario").getString("channel_id") match {
          case "" => "unknown"
          case id if id != null => id
          case _ => "unknown"
        }
        val scenario = requestDelivery.getJSONObject("ad_scenario").getString("channel_desc") match {
          case "" => "unknown"
          case desc if desc != null => desc
          case _ => "unknown"
        }

        val sort_weight = requestDelivery.getJSONObject("operation_strategy") match {
          case oper if oper != null => oper.getString("sort_weight")
          case _ => "-1"
        }

        val product_id = json.getString("product_id") match {
          case "" => "unknown"
          case product if product != null => product
          case _ => "unknown"
        }

        val app_ver = json.getString("app_ver") match {
          case "" => "unknown"
          case ver if ver != null => ver
          case _ => "unknown"
        }

        val uid = json.getString("uid") match {
          case "" => "unknown"
          case ver if ver != null => ver
          case _ => "unknown"
        }

        val log_type = json.getString("log_type") match {
          case "" => "unknown"
          case lt if lt != null => lt
          case _ => "unknown"
        }

        val ad_configid = json.getJSONObject("upack").getString("ad_configid") match {
          case "" => "unknown"
          case id if id != null => id
          case _ => "unknown"
        }
        val biz_configid = json.getJSONObject("upack").getString("biz_configid") match {
          case "" => "unknown"
          case id if id != null => id
          case _ => "unknown"
        }
        val news_configid = json.getJSONObject("upack").getString("news_configid") match {
          case "" => "unknown"
          case id if id != null => id
          case _ => "unknown"
        }

        val result_code = if (checkLogType(log_type)) json.getString("result_code") else "none"

        if (!checkLogType(log_type) || result_code == "request_success") {
          data = Array(ad_id, uid, ad_type, ad_source, advertiser_name, ad_product_name, industry, product_id, app_ver, position_id, position_type,
            target_size, daily_count_limit, news_configid, biz_configid, ad_configid, sort_weight,
            pay_model, channel, scenario, unit_price, log_type)
        }
    }
    data
  }

  def parseAdvertiseImpression(line: String): Array[String] = {
    var data = Array[String]()
    parseJson(line) {
      json =>

        val impressionDelivery = json.getJSONObject("ad_impression_extra").getJSONObject("delivery")

        val ad_id = impressionDelivery.getString("delivery_id") match {
          case "" => "unknown"
          case id if id != null => id
          case _ => "unknown"
        }
        val ad_source = impressionDelivery.getJSONObject("ad").getString("ad_source") match {
          case "" => "unknown"
          case source if source != null => source
          case _ => "unknown"
        }
        val ad_type = impressionDelivery.getJSONObject("ad").getString("type") match {
          case "" => "unknown"
          case ty if ty != null => ty
          case _ => "unknown"
        }
        val advertiser_name = impressionDelivery.getJSONObject("ad").getString("advertiser_name") match {
          case "" => "unknown"
          case name if name != null => name
          case _ => "unknown"
        }
        val ad_product_name = impressionDelivery.getJSONObject("ad").getString("ad_product_name") match {
          case "" => "unknown"
          case ad_product if ad_product != null => ad_product
          case _ => "unknown"
        }
        val industry = impressionDelivery.getJSONObject("ad").getString("industry") match {
          case "" => "unknown"
          case ids if ids != null => ids
          case _ => "unknown"
        }

        val position_id = impressionDelivery.getJSONObject("marketing").getString("position_id") match {
          case "" => "unknown"
          case pos if pos != null => pos
          case _ => "unknown"
        }
        val position_type = impressionDelivery.getJSONObject("marketing").getString("position_type") match {
          case "" => "unknown"
          case pos if pos != null => pos
          case _ => "unknown"
        }
        val target_size = impressionDelivery.getJSONObject("marketing").getString("target_size") match {
          case "" => "unknown"
          case size if size != null => size
          case _ => "unknown"
        }
        val daily_count_limit = impressionDelivery.getJSONObject("marketing").getString("daily_count_limit") match {
          case "" => "0"
          case limit if limit != null => limit
          case _ => "0"
        }
        val pay_model = impressionDelivery.getJSONObject("marketing").getString("pay_model") match {
          case "" => "unknown"
          case model if model != null => model
          case _ => "unknown"
        }
        val unit_price = impressionDelivery.getJSONObject("marketing").getString("unit_price") match {
          case "" => "0.0"
          case price if price != null => price
          case _ => "0.0"
        }

        val channel = impressionDelivery.getJSONObject("ad_scenario").getString("channel_id") match {
          case "" => "unknown"
          case id if id != null => id
          case _ => "unknown"
        }
        val scenario = impressionDelivery.getJSONObject("ad_scenario").getString("channel_desc") match {
          case "" => "unknown"
          case desc if desc != null => desc
          case _ => "unknown"
        }

        val sort_weight = impressionDelivery.getJSONObject("operation_strategy") match {
          case oper if oper != null => oper.getString("sort_weight")
          case _ => "-1"
        }

        val product_id = json.getString("product_id") match {
          case "" => "unknown"
          case product if product != null => product
          case _ => "unknown"
        }

        val app_ver = json.getString("app_ver") match {
          case "" => "unknown"
          case ver if ver != null => ver
          case _ => "unknown"
        }

        val uid = json.getString("uid") match {
          case "" => "unknown"
          case ver if ver != null => ver
          case _ => "unknown"
        }

        val log_type = json.getString("log_type") match {
          case "" => "unknown"
          case lt if lt != null => lt
          case _ => "unknown"
        }

        val ad_configid = json.getJSONObject("upack").getString("ad_configid") match {
          case "" => "unknown"
          case id if id != null => id
          case _ => "unknown"
        }
        val biz_configid = json.getJSONObject("upack").getString("biz_configid") match {
          case "" => "unknown"
          case id if id != null => id
          case _ => "unknown"
        }
        val news_configid = json.getJSONObject("upack").getString("news_configid") match {
          case "" => "unknown"
          case id if id != null => id
          case _ => "unknown"
        }

        data = Array(ad_id, uid, ad_type, ad_source, advertiser_name, ad_product_name, industry, product_id, app_ver, position_id, position_type,
          target_size, daily_count_limit, news_configid, biz_configid, ad_configid, sort_weight,
          pay_model, channel, scenario, unit_price, log_type)

    }
    data
  }

  def parseAdvertiseClick(line: String): Array[String] = {
    var data = Array[String]()
    parseJson(line) {
      json =>

        val clickDelivery = json.getJSONObject("ad_click_extra").getJSONObject("delivery")

        val ad_id = clickDelivery.getString("delivery_id") match {
          case "" => "unknown"
          case id if id != null => id
          case _ => "unknown"
        }
        val ad_source = clickDelivery.getJSONObject("ad").getString("ad_source") match {
          case "" => "unknown"
          case source if source != null => source
          case _ => "unknown"
        }
        val ad_type = clickDelivery.getJSONObject("ad").getString("type") match {
          case "" => "unknown"
          case ty if ty != null => ty
          case _ => "unknown"
        }
        val advertiser_name = clickDelivery.getJSONObject("ad").getString("advertiser_name") match {
          case "" => "unknown"
          case name if name != null => name
          case _ => "unknown"
        }
        val ad_product_name = clickDelivery.getJSONObject("ad").getString("ad_product_name") match {
          case "" => "unknown"
          case ad_product if ad_product != null => ad_product
          case _ => "unknown"
        }
        val industry = clickDelivery.getJSONObject("ad").getString("industry") match {
          case "" => "unknown"
          case ind if ind != null => ind
          case _ => "unknown"
        }

        val position_id = clickDelivery.getJSONObject("marketing").getString("position_id") match {
          case "" => "unknown"
          case pos if pos != null => pos
          case _ => "unknown"
        }
        val position_type = clickDelivery.getJSONObject("marketing").getString("position_type") match {
          case "" => "unknown"
          case pos if pos != null => pos
          case _ => "unknown"
        }
        val target_size = clickDelivery.getJSONObject("marketing").getString("target_size") match {
          case "" => "unknown"
          case size if size != null => size
          case _ => "unknown"
        }
        val daily_count_limit = clickDelivery.getJSONObject("marketing").getString("daily_count_limit") match {
          case "" => "0"
          case limit if limit != null => limit
          case _ => "0"
        }
        val pay_model = clickDelivery.getJSONObject("marketing").getString("pay_model") match {
          case "" => "unknown"
          case model if model != null => model
          case _ => "unknown"
        }
        val unit_price = clickDelivery.getJSONObject("marketing").getString("unit_price") match {
          case "" => "0.0"
          case price if price != null => price
          case _ => "0.0"
        }

        val channel = clickDelivery.getJSONObject("ad_scenario").getString("channel_id") match {
          case "" => "unknown"
          case id if id != null => id
          case _ => "unknown"
        }
        val scenario = clickDelivery.getJSONObject("ad_scenario").getString("channel_desc") match {
          case "" => "unknown"
          case desc if desc != null => desc
          case _ => "unknown"
        }

        val sort_weight = clickDelivery.getJSONObject("operation_strategy") match {
          case op if op != null => op.getString("sort_weight")
          case _ => "-1"
        }

        val product_id = json.getString("product_id") match {
          case "" => "unknown"
          case product if product != null => product
          case _ => "unknown"
        }

        val app_ver = json.getString("app_ver") match {
          case "" => "unknown"
          case ver if ver != null => ver
          case _ => "unknown"
        }

        val uid = json.getString("uid") match {
          case "" => "unknown"
          case ver if ver != null => ver
          case _ => "unknown"
        }

        val log_type = json.getString("log_type") match {
          case "" => "unknown"
          case lt if lt != null => lt
          case _ => "unknown"
        }

        val ad_configid = json.getJSONObject("upack").getString("ad_configid") match {
          case "" => "unknown"
          case id if id != null => id
          case _ => "unknown"
        }
        val biz_configid = json.getJSONObject("upack").getString("biz_configid") match {
          case "" => "unknown"
          case id if id != null => id
          case _ => "unknown"
        }
        val news_configid = json.getJSONObject("upack").getString("news_configid") match {
          case "" => "unknown"
          case id if id != null => id
          case _ => "unknown"
        }

        data = Array(ad_id, uid, ad_type, ad_source, advertiser_name, ad_product_name, industry, product_id, app_ver, position_id, position_type,
          target_size, daily_count_limit, news_configid, biz_configid, ad_configid, sort_weight,
          pay_model, channel, scenario, unit_price, log_type)
    }
    data
  }

}

object AdvJson extends Enumeration {
  type AdvJson = Value
  val ad_id, uid, ad_type, ad_source, advertiser_name, ad_product_name, industry, product_id, app_ver, position_id, position_type,
  target_size, daily_count_limit, news_configid, biz_configid, ad_configid, sort_weight,
  pay_model, channel, scenario, unit_price, log_type = Value.id
}




