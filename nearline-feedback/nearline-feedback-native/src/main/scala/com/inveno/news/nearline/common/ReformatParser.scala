package com.inveno.news.nearline.common

import com.inveno.news.nearline.filter.InfoProcess
import inveno.bigdata.common.Using

object ReformatParser extends Using with InfoProcess {

  def parseRequest(line: String): NativeJson = {
    var info = NativeJson("", "", "", "", "", "", "", "", "", "", "", "", false)
    parseJson(line) {
      json =>
        val uid = json.getString("uid")
        val product_id = json.getString("product_id")
        val language = json.getString("app_lan") match {
          case "" => "unknown"
          case lan if lan != null => lan
          case _ => "unknown"
        }
        val article_impression_extra = json.getJSONObject("article_request_extra")
        val content_id = article_impression_extra.getString("content_id")
        val content_type = article_impression_extra.getString("content_type") match {
          case "" => "unknown"
          case content_type if content_type != null => content_type
          case _ => "unknown"
        }
        val config_id = json.getJSONObject("upack").getString("news_configid") match {
          case "" => "unknown"
          case news_configid if news_configid != null => news_configid
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
        val scenario = json.getJSONObject("scenario").getString("desc") match {
          case "" => "unknown"
          case sce if sce != null => sce
          case _ => "unknown"
        }
        val strategy = article_impression_extra.getJSONObject("cpack").getString("strategy") match {
          case "" => "unknown"
          case strgy if strgy != null => strgy
          case _ => "unknown"
        }
        val app_ver = json.getString("app_ver") match {
          case "" => "unknown"
          case app_ver if app_ver != null => app_ver
          case _ => "unknown"
        }

        val isVaild = !uid.isEmpty && !content_id.isEmpty && !product_id.isEmpty &&
          !config_id.isEmpty && !ad_configid.isEmpty && !biz_configid.isEmpty &&
          !scenario.isEmpty && !content_type.isEmpty && !language.isEmpty &&
          !strategy.isEmpty && !app_ver.isEmpty

        if (isVaild) {
          info = NativeJson(uid, content_id, product_id, config_id, ad_configid, biz_configid, scenario, strategy, app_ver, content_type, "", language, true)
        }

    }
    info
  }

  def parseImpression(line: String): NativeJson = {
    var info = NativeJson("", "", "", "", "", "", "", "", "", "", "", "", false)
    parseJson(line) {
      json =>
        val uid = json.getString("uid")
        val product_id = json.getString("product_id")
        val language = json.getString("app_lan") match {
          case "" => "unknown"
          case lan if lan != null => lan
          case _ => "unknown"
        }
        val article_impression_extra = json.getJSONObject("article_impression_extra")
        val content_id = article_impression_extra.getString("content_id")
        val content_type = article_impression_extra.getString("content_type") match {
          case "" => "unknown"
          case content_type if content_type != null => content_type
          case _ => "unknown"
        }
        val config_id = json.getJSONObject("upack").getString("news_configid") match {
          case "" => "unknown"
          case news_configid if news_configid != null => news_configid
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
        val scenario = json.getJSONObject("scenario").getString("desc") match {
          case "" => "unknown"
          case sce if sce != null => sce
          case _ => "unknown"
        }
        val strategy = article_impression_extra.getJSONObject("cpack").getString("strategy") match {
          case "" => "unknown"
          case strgy if strgy != null => strgy
          case _ => "unknown"
        }
        val app_ver = json.getString("app_ver") match {
          case "" => "unknown"
          case app_ver if app_ver != null => app_ver
          case _ => "unknown"
        }

        val isVaild = !uid.isEmpty && !content_id.isEmpty && !product_id.isEmpty &&
          !config_id.isEmpty && !ad_configid.isEmpty && !biz_configid.isEmpty &&
          !scenario.isEmpty && !content_type.isEmpty && !language.isEmpty &&
          !strategy.isEmpty && !app_ver.isEmpty

        if (isVaild) {
          info = NativeJson(uid, content_id, product_id, config_id, ad_configid, biz_configid, scenario, strategy, app_ver, content_type, "", language, true)
        }

    }
    info
  }

  def parseClick(line: String) = {
    var info = NativeJson("", "", "", "", "", "", "", "", "", "", "", "", false)
    parseJson(line) {
      json =>
        val uid = json.getString("uid")
        val product_id = json.getString("product_id")
        val language = json.getString("app_lan") match {
          case "" => "unknown"
          case lan if lan != null => lan
          case _ => "unknown"
        }
        val article_click_extra = json.getJSONObject("article_click_extra")
        val content_id = article_click_extra.getString("content_id")
        val content_type = article_click_extra.getString("content_type") match {
          case "" => "unknown"
          case content_type if content_type != null => content_type
          case _ => "unknown"
        }
        val config_id = json.getJSONObject("upack").getString("news_configid") match {
          case "" => "unknown"
          case news_configid if news_configid != null => news_configid
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
        val scenario = json.getJSONObject("scenario").getString("desc") match {
          case "" => "unknown"
          case sce if sce != null => sce
          case _ => "unknown"
        }
        val strategy = article_click_extra.getJSONObject("cpack").getString("strategy") match {
          case "" => "unknown"
          case strgy if strgy != null => strgy
          case _ => "unknown"
        }
        val app_ver = json.getString("app_ver") match {
          case "" => "unknown"
          case app_ver if app_ver != null => app_ver
          case _ => "unknown"
        }

        val isVaild = !uid.isEmpty && !content_id.isEmpty && !product_id.isEmpty &&
          !config_id.isEmpty && !ad_configid.isEmpty && !biz_configid.isEmpty &&
          !scenario.isEmpty && !content_type.isEmpty && !language.isEmpty &&
          !strategy.isEmpty && !app_ver.isEmpty

        if (isVaild) {
          info = NativeJson(uid, content_id, product_id, config_id, ad_configid, biz_configid, scenario, strategy, app_ver, content_type, "", language, true)
        }
    }
    info
  }

  def parseDwelltime(line: String) = {
    var info = NativeJson("", "", "", "", "", "", "", "", "", "", "", "", false)
    parseJson(line) {
      json =>
        val uid = json.getString("uid")
        val product_id = json.getString("product_id")
        val language = json.getString("app_lan") match {
          case "" => "unknown"
          case lan if lan != null => lan
          case _ => "unknown"
        }
        val article_dwelltime_extra = json.getJSONObject("article_dwelltime_extra")
        val content_id = article_dwelltime_extra.getString("content_id")
        val dwelltime = article_dwelltime_extra.getString("dwelltime")
        val content_type = article_dwelltime_extra.getString("content_type") match {
          case "" => "unknown"
          case content_type if content_type != null => content_type
          case _ => "unknown"
        }
        val config_id = json.getJSONObject("upack").getString("news_configid") match {
          case "" => "unknown"
          case news_configid if news_configid != null => news_configid
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
        val scenario = json.getJSONObject("scenario").getString("desc") match {
          case "" => "unknown"
          case sce if sce != null => sce
          case _ => "unknown"
        }
        val strategy = article_dwelltime_extra.getJSONObject("cpack").getString("strategy") match {
          case "" => "unknown"
          case strgy if strgy != null => strgy
          case _ => "unknown"
        }
        val app_ver = json.getString("app_ver") match {
          case "" => "unknown"
          case app_ver if app_ver != null => app_ver
          case _ => "unknown"
        }

        val isVaild = !uid.isEmpty && !content_id.isEmpty && !product_id.isEmpty &&
          !config_id.isEmpty && !ad_configid.isEmpty && !biz_configid.isEmpty &&
          !scenario.isEmpty && !content_type.isEmpty && !language.isEmpty &&
          !strategy.isEmpty && !app_ver.isEmpty
        if (isVaild) {
          info = NativeJson(uid, content_id, product_id, config_id, ad_configid, biz_configid, scenario, strategy, app_ver, content_type, dwelltime, language, true)
        }
    }
    info
  }

}


case class NativeJson(uid: String,
                      content_id: String,
                      product_id: String,
                      config_id: String,
                      ad_configid: String,
                      biz_configid: String,
                      scenario: String,
                      strategy: String,
                      app_ver: String,
                      content_type: String,
                      dwelltime: String,
                      language: String,
                      isValid: Boolean)




