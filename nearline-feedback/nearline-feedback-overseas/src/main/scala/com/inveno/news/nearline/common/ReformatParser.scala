package com.inveno.news.nearline.common

import com.inveno.news.nearline.filter.InfoProcess
import inveno.bigdata.common.Using

object ReformatParser extends Using with InfoProcess {

  def parseOverSeasRequest(line: String): OverseasJson = {
    var info = OverseasJson("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", false)
    parseJson(line) {
      json =>
        val uid = json.getString("uid")
        val platform = json.getString("platform")
        val product_id = json.getString("product_id")
        val article_impression_extra = json.getJSONObject("article_request_extra")
        val content_id = article_impression_extra.getString("content_id")
        val config_id = json.getJSONObject("upack").getString("news_configid") match {
          case "" => "unknown"
          case config_id if config_id != null => config_id
          case _ => "unknown"
        }
        val ad_configid = json.getJSONObject("upack").getString("ad_configid") match {
          case "" => "unknown"
          case ad_configid if ad_configid != null => ad_configid
          case _ => "unknown"
        }
        val biz_configid = json.getJSONObject("upack").getString("biz_configid") match {
          case "" => "unknown"
          case biz_configid if biz_configid != null => biz_configid
          case _ => "unknown"
        }
        val strategy = article_impression_extra.getJSONObject("cpack").getString("strategy") match{
          case "" => "unknown"
          case strgy if strgy != null => strgy
          case _ => "unknown"
        }
        val app_ver = json.getString("app_ver") match {
          case "" => "unknown"
          case app_ver if app_ver != null => app_ver
          case _ => "unknown"
        }

        val app_lan = json.getString("app_lan") match {
          case "" => "unknown"
          case lan if lan != null => lan
          case _ => "unknown"
        }

        val content_type = article_impression_extra.getString("content_type") match {
          case "" => "unknown"
          case content_type if content_type != null => content_type
          case _ => "unknown"
        }

        val adFlag = IsAdvertise(content_type)

        val ad_source = if (adFlag) json.getString("ad_source") match {
          case "" => "unknown"
          case source if source != null => source
          case _ => "unknown"
        } else "null"

        val scenario = if (!adFlag) json.getJSONObject("scenario").getString("position_desc") match {
          case "" => "unknown"
          case sce if sce != null => sce
          case _ => "unknown"
        } else "unknown"

        val scenarioChannel = if (!adFlag) json.getJSONObject("scenario").getString("channel_desc") match {
          case "" => "unknown"
          case channel if channel != null => channel
          case _ => "unknown"
        } else "unknown"

        val isFilter = AdvertiseFilter(json.getString("log_type"), app_ver)

        val isVaild = uid.nonEmpty && content_id.nonEmpty && platform.nonEmpty && product_id.nonEmpty &&
          config_id.nonEmpty && scenario.nonEmpty && scenarioChannel.nonEmpty &&
          app_ver.nonEmpty && content_type.nonEmpty && app_lan.nonEmpty &&
          strategy.nonEmpty && !isFilter

        if (isVaild) {
          info = OverseasJson(uid, content_id, platform, product_id, config_id, ad_configid, ad_source, biz_configid, scenario, scenarioChannel, strategy, app_ver, content_type, "", app_lan, true)
        }
    }
    info
  }

  def parseOverSeasImpression(line: String): OverseasJson = {
    var info = OverseasJson("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", false)
    parseJson(line) {
      json =>
        val uid = json.getString("uid")
        val platform = json.getString("platform")
        val product_id = json.getString("product_id")
        val article_impression_extra = json.getJSONObject("article_impression_extra")
        val content_id = article_impression_extra.getString("content_id")
        val config_id = json.getJSONObject("upack").getString("news_configid") match {
          case "" => "unknown"
          case config_id if config_id != null => config_id
          case _ => "unknown"
        }
        val ad_configid = json.getJSONObject("upack").getString("ad_configid") match {
          case "" => "unknown"
          case ad_configid if ad_configid != null => ad_configid
          case _ => "unknown"
        }
        val biz_configid = json.getJSONObject("upack").getString("biz_configid")match {
          case "" => "unknown"
          case biz_configid if biz_configid != null => biz_configid
          case _ => "unknown"
        }
        val strategy = article_impression_extra.getJSONObject("cpack").getString("strategy") match{
          case "" => "unknown"
          case strgy if strgy != null => strgy
          case _ => "unknown"
        }
        val app_ver = json.getString("app_ver") match {
          case "" => "unknown"
          case app_ver if app_ver != null => app_ver
          case _ => "unknown"
        }

        val app_lan = json.getString("app_lan") match {
          case "" => "unknown"
          case lan if lan != null => lan
          case _ => "unknown"
        }

        val content_type = article_impression_extra.getString("content_type") match {
          case "" => "unknown"
          case content_type if content_type != null => content_type
          case _ => "unknown"
        }

        val adFlag = IsAdvertise(content_type)

        val ad_source = if (adFlag) json.getString("ad_source") match {
          case "" => "unknown"
          case source if source != null => source
          case _ => "unknown"
        } else "null"

        val scenario = if (!adFlag) json.getJSONObject("scenario").getString("position_desc") match {
          case "" => "unknown"
          case sce if sce != null => sce
          case _ => "unknown"
        } else "unknown"

        val scenarioChannel = if (!adFlag) json.getJSONObject("scenario").getString("channel_desc") match {
          case "" => "unknown"
          case channel if channel != null => channel
          case _ => "unknown"
        } else "unknown"

        val isFilter = AdvertiseFilter(json.getString("log_type"), app_ver)

        val isVaild = uid.nonEmpty && content_id.nonEmpty && platform.nonEmpty && product_id.nonEmpty &&
          config_id.nonEmpty && scenario.nonEmpty && scenarioChannel.nonEmpty &&
          app_ver.nonEmpty && content_type.nonEmpty && app_lan.nonEmpty &&
          strategy.nonEmpty && !isFilter

        if (isVaild) {
          info = OverseasJson(uid, content_id, platform, product_id, config_id, ad_configid, ad_source, biz_configid, scenario, scenarioChannel, strategy, app_ver, content_type, "", app_lan, true)
        }
    }
    info
  }

  def parseOverSeasClick(line: String): OverseasJson = {
    var info = OverseasJson("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", false)

    parseJson(line) {
      json =>
        val uid = json.getString("uid")
        val platform = json.getString("platform")
        val product_id = json.getString("product_id")
        val article_click_extra = json.getJSONObject("article_click_extra")
        val content_id = article_click_extra.getString("content_id")
        val config_id = json.getJSONObject("upack").getString("news_configid") match {
          case "" => "unknown"
          case config_id if config_id != null => config_id
          case _ => "unknown"
        }
        val ad_configid = json.getJSONObject("upack").getString("ad_configid") match {
          case "" => "unknown"
          case ad_configid if ad_configid != null => ad_configid
          case _ => "unknown"
        }
        val biz_configid = json.getJSONObject("upack").getString("biz_configid")match {
          case "" => "unknown"
          case biz_configid if biz_configid != null => biz_configid
          case _ => "unknown"
        }
        val strategy = article_click_extra.getJSONObject("cpack").getString("strategy") match{
          case "" => "unknown"
          case strgy if strgy != null => strgy
          case _ => "unknown"
        }
        val app_ver = json.getString("app_ver") match {
          case "" => "unknown"
          case app_ver if app_ver != null => app_ver
          case _ => "unknown"
        }

        val app_lan = json.getString("app_lan") match {
          case "" => "unknown"
          case lan if lan != null => lan
          case _ => "unknown"
        }

        val content_type = article_click_extra.getString("content_type") match {
          case "" => "unknown"
          case content_type if content_type != null => content_type
          case _ => "unknown"
        }

        val adFlag = IsAdvertise(content_type)

        val ad_source = if (adFlag) json.getString("ad_source") match {
          case "" => "unknown"
          case source if source != null => source
          case _ => "unknown"
        } else "null"

        val scenario = if (!adFlag) json.getJSONObject("scenario").getString("position_desc") match {
          case "" => "unknown"
          case sce if sce != null => sce
          case _ => "unknown"
        } else "unknown"

        val scenarioChannel = if (!adFlag) json.getJSONObject("scenario").getString("channel_desc") match {
          case "" => "unknown"
          case channel if channel != null => channel
          case _ => "unknown"
        } else "unknown"

        val isFilter = AdvertiseFilter(json.getString("log_type"), app_ver)

        val isVaild = uid.nonEmpty && content_id.nonEmpty && platform.nonEmpty && product_id.nonEmpty &&
          config_id.nonEmpty && scenario.nonEmpty && scenarioChannel.nonEmpty &&
          app_ver.nonEmpty && content_type.nonEmpty && app_lan.nonEmpty &&
          strategy.nonEmpty && !isFilter
        if (isVaild) {
          info = OverseasJson(uid, content_id, platform, product_id, config_id, ad_configid, ad_source, biz_configid, scenario, scenarioChannel, strategy, app_ver, content_type, "", app_lan, true)
        }
    }
    info
  }

  def parseOverSeasDwelltime(line: String): OverseasJson = {
    var info = OverseasJson("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", false)
    parseJson(line) {
      json =>
        val uid = json.getString("uid")
        val platform = json.getString("platform")
        val product_id = json.getString("product_id")
        val article_dwelltime_extra = json.getJSONObject("article_dwelltime_extra")
        val content_id = article_dwelltime_extra.getString("content_id")
        val config_id = json.getJSONObject("upack").getString("news_configid") match {
          case "" => "unknown"
          case config_id if config_id != null => config_id
          case _ => "unknown"
        }
        val ad_configid = json.getJSONObject("upack").getString("ad_configid") match {
          case "" => "unknown"
          case ad_configid if ad_configid != null => ad_configid
          case _ => "unknown"
        }
        val biz_configid = json.getJSONObject("upack").getString("biz_configid")match {
          case "" => "unknown"
          case biz_configid if biz_configid != null => biz_configid
          case _ => "unknown"
        }
        val strategy = article_dwelltime_extra.getJSONObject("cpack").getString("strategy") match{
          case "" => "unknown"
          case strgy if strgy != null => strgy
          case _ => "unknown"
        }
        val app_ver = json.getString("app_ver") match {
          case "" => "unknown"
          case app_ver if app_ver != null => app_ver
          case _ => "unknown"
        }

        val content_type = article_dwelltime_extra.getString("content_type") match {
          case "" => "unknown"
          case content_type if content_type != null => content_type
          case _ => "unknown"
        }

        val dwelltime = if (content_type == "short_video") article_dwelltime_extra.getString("dwelltime") match {
          case dt if dt.toInt > 0 => dt
          case _ => article_dwelltime_extra.getString("play_time")
        } else {
          article_dwelltime_extra.getString("dwelltime")
        }

        val app_lan = json.getString("app_lan") match {
          case "" => "unknown"
          case lan if lan != null => lan
          case _ => "unknown"
        }

        val adFlag = IsAdvertise(content_type)

        val ad_source = if (adFlag) json.getString("ad_source") match {
          case "" => "unknown"
          case source if source != null => source
          case _ => "unknown"
        } else "null"

        val scenario = if (!adFlag) json.getJSONObject("scenario").getString("position_desc") match {
          case "" => "unknown"
          case sce if sce != null => sce
          case _ => "unknown"
        } else "unknown"

        val scenarioChannel = if (!adFlag) json.getJSONObject("scenario").getString("channel_desc") match {
          case "" => "unknown"
          case channel if channel != null => channel
          case _ => "unknown"
        } else "unknown"

        val isFilter = AdvertiseFilter(json.getString("log_type"), app_ver)

        val isVaild = uid.nonEmpty && content_id.nonEmpty && platform.nonEmpty && product_id.nonEmpty &&
          config_id.nonEmpty && scenario.nonEmpty && scenarioChannel.nonEmpty &&
          app_ver.nonEmpty && content_type.nonEmpty && app_lan.nonEmpty &&
          strategy.nonEmpty && !isFilter

        if (isVaild) {
          info = OverseasJson(uid, content_id, platform, product_id, config_id, ad_configid, ad_source, biz_configid, scenario, scenarioChannel, strategy, app_ver, content_type, dwelltime, app_lan, true)
        }
    }
    info
  }
}

case class OverseasJson(uid: String,
                        content_id: String,
                        platform: String,
                        product_id: String,
                        config_id: String,
                        ad_configid: String,
                        ad_source: String,
                        biz_configid: String,
                        scenario: String,
                        scenarioChannel: String,
                        strategy: String,
                        app_ver: String,
                        content_type: String,
                        dwelltime: String,
                        app_lan: String,
                        isValid: Boolean)



