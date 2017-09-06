package com.inveno.extend.common

import inveno.bigdata.common.TimeUtil
import inveno.bigdata.common.Using

/**
 * Created by dory on 2017/2/10.
 */
object FeedBackUtil extends Using with InfoProcess {

  def shortVideoResult(result: (String, (Long, Long))): String = {
    var res = ""
    usingJson {
      json =>
        val (key, values) = result
        val fields = key.split("#")
        json.put("@timestamp", TimeUtil.getTimeIndex)
        json.put("product_id", fields.head)
        json.put("app_lan", fields.last)
        json.put("video_play_error", values._1)
        json.put("video_play", values._2)
        json.put("video_play_success", values._2 - values._1)
        json.put("video_play_error_rate", getRate(values._1, values._2).toFloat)
        json.put("video_play_success_rate", getRate(values._2 - values._1, values._2).toFloat)
        res = json.toString
    }
    res
  }

}
