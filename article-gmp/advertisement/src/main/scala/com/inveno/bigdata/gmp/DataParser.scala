package com.inveno.bigdata.gmp

import com.alibaba.fastjson.JSON

import org.slf4j.LoggerFactory

import com.inveno.bigdata.common.TimeUtil
import com.inveno.bigdata.common.reformat.CombinationKey

object DataParser {

  val logger = LoggerFactory.getLogger(DataParser.getClass)

  val conf = ArticleGmpConfig.getInstance().getConfig;
  val date_format = conf.getString("date_format")
  val valid_hour = conf.getInt("valid_hour")
  val key_tag = conf.getString("key_tag", "###")

  // get data, and filter not-news data      
  def parseImpression(line: String) = {
    var data = ("", false)
    try {
      val json = JSON.parseObject(line)          
      val article_impression_extra = json.getJSONObject("article_impression_extra")

      val content_type = article_impression_extra.getString("content_type")
      if (content_type.contains("advertisement")) {
        val product_id = json.getString("product_id")
        val ad_md5 = article_impression_extra.getJSONObject("extra_msg").getString("ad_md5")
        val adspace_id = article_impression_extra.getJSONObject("extra_msg").getString("adspace_id")
        
        if (!product_id.isEmpty && !adspace_id.isEmpty && !ad_md5.isEmpty) {
          val unique_key = CombinationKey.makeArticleCombinationKey(key_tag, product_id, adspace_id, ad_md5)
          data = (unique_key, true)
        }
      }
    } catch {
      case ex: Exception => ex.printStackTrace()
    }
    data
  }

  // get data, and filter not-news data
  def parseClick(line: String) = {
    var data = ("", false)
    try {
      val json = JSON.parseObject(line)
      
      val article_click_extra = json.getJSONObject("article_click_extra")

      val content_type = article_click_extra.getString("content_type")
      if (content_type.contains("advertisement")) {
        val product_id = json.getString("product_id")
        val ad_md5 = article_click_extra.getJSONObject("extra_msg").getString("ad_md5")
        val adspace_id = article_click_extra.getJSONObject("extra_msg").getString("adspace_id")

        if (!product_id.isEmpty && !adspace_id.isEmpty && !ad_md5.isEmpty) {
          val unique_key = CombinationKey.makeArticleCombinationKey(key_tag, product_id, adspace_id, ad_md5)
          data = (unique_key, true)
        }
      }
      
    } catch {
      case ex: Exception =>
        ex.printStackTrace()
        logger.error("parseClick fail, " + line)
    }
    data
  }

  // get data, and filter old data
  def parseHistoryGmp(line: String) = {
    //ad_md5, product_id, decay_impression, decay_click, total_impression, update_date
    var data = ("", false)
    try {
      val json = JSON.parseObject(line)

      val ad_md5 = json.getString("ad_md5")
      val product_id = json.getString("product_id")
      val adspace_id = json.getString("adspace_id")
      val decay_impression = json.getFloat("decay_impression")
      val decay_click = json.getFloat("decay_click")
      val total_impression = json.getLong("total_impression")
      val update_date = json.getString("update_date")
      val validDate = TimeUtil.getValidDate(date_format, valid_hour)

      if (update_date >= validDate && !ad_md5.isEmpty && !product_id.isEmpty) {
        data = (CombinationKey.makeArticleCombinationKey(key_tag, product_id, adspace_id, ad_md5, decay_impression, decay_click, total_impression, update_date), true)
      }
    } catch {
      case ex: Exception =>
        ex.printStackTrace()
        logger.error("parseHistory fail, " + line)
    }
    data
  }

}