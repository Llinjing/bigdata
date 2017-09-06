package com.inveno.news.common

import com.alibaba.fastjson.JSON
import org.slf4j.LoggerFactory

/**
 * Created by gaofeilu on 2017/3/7.
 */
object ReformatParser {
  private val CLASS_NAME = ReformatParser.getClass.getSimpleName
  val logger = LoggerFactory.getLogger(CLASS_NAME)

  /*def isArticleValidActionName(str: String):Boolean = {
    val arr = Array("h5_share_click", "h5_page_request_call", "h5_click_download_app", "deep_link_deferred_h5")
    arr.contains(str)
  }

  def isUserValidActionName(str: String):Boolean = {
    val arr = Array("h5_page_request_call", "h5_click_download_app", "deep_link_deferred_h5")
    arr.contains(str)
  }*/
  def isValidActionName(str: String): Boolean = {
    val arr = Array("h5_share_click", "h5_page_request_call", "h5_click_download_app", "deep_link_deferred_h5")
    arr.contains(str)
  }

  def replaceComma(arr: Array[String]): Array[String] = {
    arr.map(x=>x.replace(",", " "))
  }

  def parseShareFunnelArticle(str: String): Array[String] ={
    var ret: Array[String] = Array()
    try{
      val json = JSON.parseObject(str)
      val action_name = json.getJSONObject("extend_event_extra").getString("action_name")
      if(isValidActionName(action_name)){
        val product_id = json.getString("product_id")
        val platform = json.getString("platform")
        val language = json.getString("app_lan")
        val action_value = json.getJSONObject("extend_event_extra").getJSONObject("action_value")
        val content_id = action_value.getString("content_id") match {
          case content_id if content_id != null => content_id
          case _ => "unknown"
        }
        val share_to = action_value.getString("share_to") match {
          case share_to if share_to != null => share_to
          case _ => "unknown"
        }
        val fb_uid = action_value.getString("fb_uid") match {
          case fb_uid if fb_uid != null => fb_uid
          case _ => "unknown"
        }
        val fb_gid = action_value.getString("fb_gid") match {
          case fb_gid if fb_gid != null => fb_gid
          case _ => "unknown"
        }
        ret  = replaceComma(Array(action_name, product_id, platform, content_id, language, share_to, fb_uid, fb_gid))
      }
    }catch {
      case ex:Exception=>logger.info("Reformat H5ShareFunnelUser fail" + ex.printStackTrace())
    }
    ret
  }

  def parseShareFunnelUser(str: String): Array[String] ={
    var ret: Array[String] = Array()
    try{
      val json = JSON.parseObject(str)
      val action_name = json.getJSONObject("extend_event_extra").getString("action_name")
      if(isValidActionName(action_name)){
        val product_id = json.getString("product_id")
        val platform = json.getString("platform")
        val language = json.getString("app_lan")
        val uid = json.getString("uid")
        val action_value = json.getJSONObject("extend_event_extra").getJSONObject("action_value")
        val share_to = action_value.getString("share_to") match {
          case share_to if share_to != null => share_to
          case _ => "unknown"
        }
        val fb_uid = action_value.getString("fb_uid") match {
          case fb_uid if fb_uid != null => fb_uid
          case _ => "unknown"
        }
        val fb_gid = action_value.getString("fb_gid") match {
          case fb_gid if fb_gid != null => fb_gid
          case _ => "unknown"
        }
        ret  = replaceComma(Array(action_name, product_id, platform,  language, share_to, fb_uid, fb_gid, uid))
      }
    }catch {
      case ex:Exception=>logger.info("Reformat H5ShareFunnelUser fail" + ex.printStackTrace())
    }
    ret
  }

}
