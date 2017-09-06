package com.inveno.news.nearline.common

import com.alibaba.fastjson.JSON
import com.github.panhongan.util.StringUtil

/**
 * Created by dory on 2017/2/23.
 */
object ReformatParser {
  def parse(line: String): Array[String] = {
    var data: Array[(String)] = Array()

    try {
      val json = JSON.parseObject(line)
      val product_id = json.getString("product_id")
      val platform = json.getString("platform")
      val extend_event_extra = json.getJSONObject("extend_event_extra")
      val action_name = extend_event_extra.getString("action_name")
      if (isValidAction(action_name)) {
        val content_id = extend_event_extra.getJSONObject("action_value").getString("content_id") match {
          case content_id if content_id != null => content_id
          case _ => "unknown"
        }
        val from = extend_event_extra.getJSONObject("action_value").getString("from") match {
          case from if from != null => from
          case _ => "unknown"
        }

        if (!StringUtil.isEmpty(product_id) &&
          !StringUtil.isEmpty(platform) &&
          !StringUtil.isEmpty(action_name) &&
          !StringUtil.isEmpty(content_id) &&
          !StringUtil.isEmpty(from)) {
          data = Array(product_id, platform, action_name, content_id, from)
        }
      }
    } catch {
      case ex: Exception => ex.printStackTrace()
    }
    data
  }

  def isValidAction(action_name: String): Boolean = {
    val valid_action = Array("share_facebook", "share_google", "share_whatsapp", "share_more", "share_copylink")
    valid_action.contains(action_name)
  }


}
