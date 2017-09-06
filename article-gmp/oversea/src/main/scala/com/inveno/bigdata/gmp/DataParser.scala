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
    def parseRequest(line: String) = {
        var data = ("", false)
        try {
            val json = JSON.parseObject(line)
            
            val product_id = json.getString("product_id")
            val uid = json.getString("uid")
            val article_impression_extra = json.getJSONObject("article_impression_extra")
            val content_id = article_impression_extra.getString("content_id")            
            val content_type = article_impression_extra.getString("content_type")
            val scenario = json.getJSONObject("scenario").getString("desc")
            
            if (!content_id.isEmpty && !product_id.isEmpty && !uid.isEmpty()) {
                if (!content_type.contains("advertisement")) {
                    product_id match {
                        case _ => {
                            if(scenario.contentEquals("long_listpage") || scenario.contentEquals("waterfall")) {
                                val unique_key = CombinationKey.makeArticleCombinationKey(key_tag, content_id, product_id, uid)
                                data = (unique_key, true)
                            } 
                        }
                    }
                }
            }

        } catch {
            case ex: Exception => ex.printStackTrace()
        }
        data
    }
    
    // get data, and filter not-news data      
    def parseImpression(line: String) = {
        var data = ("", false)
        try {
            val json = JSON.parseObject(line)
            
            val product_id = json.getString("product_id")
            val uid = json.getString("uid")
            val article_impression_extra = json.getJSONObject("article_impression_extra")
            val content_id = article_impression_extra.getString("content_id")            
            val content_type = article_impression_extra.getString("content_type")
            val scenario = json.getJSONObject("scenario").getString("desc")
            
            if (!content_id.isEmpty && !product_id.isEmpty && !uid.isEmpty()) {
                if (!content_type.contains("advertisement")) {
                    product_id match {
                        case _ => {
                            if(scenario.contentEquals("long_listpage") || scenario.contentEquals("waterfall")) {
                                val unique_key = CombinationKey.makeArticleCombinationKey(key_tag, content_id, product_id, uid)
                                data = (unique_key, true)
                            } 
                        }
                    }
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

            val product_id = json.getString("product_id")
            val uid = json.getString("uid")
            val article_click_extra = json.getJSONObject("article_click_extra")
            val content_id = article_click_extra.getString("content_id")           
            val content_type = article_click_extra.getString("content_type")
            val scenario = json.getJSONObject("scenario").getString("desc")
            
            if (!content_id.isEmpty && !product_id.isEmpty && !uid.isEmpty()) {
                if (!content_type.contains("advertisement")) {
                    product_id match {
                        case _ => {
                            if(scenario.contentEquals("long_listpage") || scenario.contentEquals("waterfall")) {
                                val unique_key = CombinationKey.makeArticleCombinationKey(key_tag, content_id, product_id, uid)
                                data = (unique_key, true)
                            }
                        }
                    }
                }
            }
        } catch {
            case ex: Exception => ex.printStackTrace()
            logger.error("parseClick fail, " + line)
        }
        data
    }
    
    // get data, and filter old data
    def parseHistoryGmp(line: String) = {
        //content_id, product_id, decay_impression, decay_click, total_impression, update_date
        var data = ("", false)
        try {
            val json = JSON.parseObject(line)

            val content_id = json.getString("content_id")
            val product_id = json.getString("product_id")
            val decay_impression = json.getFloat("decay_impression")
            val decay_click = json.getFloat("decay_click")
            val total_impression = json.getLong("total_impression")
            val update_date = json.getString("update_date")
            val validDate = TimeUtil.getValidDate(date_format, valid_hour)
            
            if (update_date>=validDate && !content_id.isEmpty && !product_id.isEmpty) {
                data = (CombinationKey.makeArticleCombinationKey(key_tag, content_id, product_id, decay_impression, decay_click, total_impression, update_date), true)
            }
        } catch {
            case ex: Exception => ex.printStackTrace()
            logger.error("parseHistory fail, " + line)
        }
        data
    }
    
    // get data, and filter old data
    def parseHistoryGmpV2(line: String) = {
        //content_id, product_id, decay_click, decay_impression, total_impression, update_date
        var data = ("", "", 0.0f, 0.0f, 0.0f, 0L, 0L, "", false)
        try {
            val json = JSON.parseObject(line)

            val content_id = json.getString("content_id")
            val product_id = json.getString("product_id")
            val decay_request = json.getString("decay_reqeust").toFloat
            val decay_impression = json.getFloat("decay_impression")
            val decay_click = json.getFloat("decay_click")
            val total_request = json.getLong("total_reqeust")
            val total_impression = json.getLong("total_impression")
            val update_date = json.getString("update_date")
            val validDate = TimeUtil.getValidDate()
            
            if (update_date>=validDate && !content_id.isEmpty && !product_id.isEmpty) {
                data = (content_id, product_id, decay_request, decay_impression, decay_click, total_request, total_impression, update_date, true)
            }
        } catch {
            case ex: Exception => ex.printStackTrace()
            logger.error("parseHistory fail, " + line)
        }
        data
    }

}