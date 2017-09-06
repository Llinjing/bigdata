package com.inveno.extend.common

import inveno.bigdata.common.Using


/**
 * Created by dory on 2017/2/10.
 */
object ReformatDataParser extends Using{

  def parseClick(line: String): ClickJson = {
    var res = ClickJson("", "", false)
    parseJson(line) {
      json =>
        val product_id = json.getString("product_id")
        val app_lan = json.getString("app_lan")
        val article_click_extra = json.getJSONObject("article_click_extra")
        val content_type = article_click_extra.getString("content_type")

        val isVaild = !product_id.isEmpty && !content_type.isEmpty && !app_lan.isEmpty

        if (isVaild && content_type == "short_video") {
          res = ClickJson(product_id, app_lan, true)
        }
    }
    res
  }

  def parseExtend(line: String) = {
    var res = ExtendJson("", "", "", "", false)
    parseJson(line) {
      json =>
        val product_id = json.getString("product_id")
        val app_lan = json.getString("app_lan")
        val event_id = json.getString("event_id")
        val uid = json.getString("uid")
        val extend_event_extra = json.getJSONObject("extend_event_extra")
        val action_name = extend_event_extra.getString("action_name")
        val error_code = extend_event_extra.getJSONObject("action_value").getString("error_code")
        val content_id = extend_event_extra.getJSONObject("action_value").getString("content_id")
        //"action_value":{"content_id":"1028021031","error_code":"INTERNAL_ERROR"}}

        val isVaild = !product_id.isEmpty && !app_lan.isEmpty && !uid.isEmpty &&
          !event_id.isEmpty && !action_name.isEmpty && !error_code.isEmpty && !content_id.isEmpty

        //action name= video_play_error     error_code: "UNAUTHORIZED_OVERLAY" , "SUCCESS"
        if (isVaild && event_id == "7" && action_name == "video_play_error" && error_code != "SUCCESS" && error_code != "UNAUTHORIZED_OVERLAY") {
          res = ExtendJson(uid, content_id, product_id, app_lan, true)
        }
    }
    res
  }

}

case class ExtendJson(uid: String,
                      content_id: String,
                      product_id: String,
                      app_lan: String,
                      isValid: Boolean)

case class ClickJson(product_id: String,
                     app_lan: String,
                     isValid: Boolean)
