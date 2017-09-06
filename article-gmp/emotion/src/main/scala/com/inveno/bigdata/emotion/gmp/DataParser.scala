package com.inveno.bigdata.emotion.gmp

import com.alibaba.fastjson.JSON
import org.slf4j.LoggerFactory
import com.inveno.bigdata.common.TimeUtil
import com.inveno.bigdata.common.reformat.CombinationKey

object DataParser {
    
    val logger = LoggerFactory.getLogger(DataParser.getClass)
    
    val conf = ArticleEmotionGmpConfig.getInstance().getConfig;
    val date_format = conf.getString("date_format")
    val valid_hour = conf.getInt("valid_hour")
    val key_tag = conf.getString("key_tag", "###")
    
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
            
            if (!content_id.isEmpty && !product_id.isEmpty && !uid.isEmpty()) {
                if (!content_type.contains("advertisement")) {
                    product_id match {
                        case _ => {
                            val unique_key = CombinationKey.makeArticleCombinationKey(key_tag, content_id, product_id, uid)
                            data = (unique_key, true)
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
    
    // get data, and filter not-news data
    def parseEmotion(line: String) = {
        var data = ("", false)
        try {
            val json = JSON.parseObject(line)

            val product_id = json.getString("product_id")
            val uid = json.getString("uid")
            val content_id = json.getString("content_id")
            val emotion_extra = json.getJSONObject("ballot")
            val bored_num = emotion_extra.getInteger("Bored")
            val angry_num = emotion_extra.getInteger("Angry")
            val like_num = emotion_extra.getInteger("Like")
            val sad_num = emotion_extra.getInteger("Sad")
            val sum_num = bored_num+angry_num+like_num+sad_num
            
            if (sum_num == 1) {
                if (!content_id.isEmpty && !product_id.isEmpty && !uid.isEmpty()) {
                    val unique_key = CombinationKey.makeArticleCombinationKey(key_tag, content_id, product_id, bored_num, like_num, angry_num, sad_num, uid)
                    data = (unique_key, true)
                }
            }
        } catch {
            case ex: Exception => ex.printStackTrace()
            logger.error("parseEmotion fail, " + line)
        }
        data
    }
    
    // get data, and filter old data
    def parseHistoryEmotionGmp(line: String) = {
        //content_id, product_id, decay_bored, decay_like, decay_angry, decay_sad, decay_total_click, total_click, update_date
        var data = ("", "", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0L, "", false)
        try {
            val json = JSON.parseObject(line)

            val content_id = json.getString("content_id")
            val product_id = json.getString("product_id")
            val decay_bored = json.getFloat("decay_bored")
            val decay_like = json.getFloat("decay_like")
            val decay_angry = json.getFloat("decay_angry")
            val decay_sad = json.getFloat("decay_sad")
            val decay_total_click = json.getFloat("decay_total_click")
            val total_click = json.getLong("total_click")
            val update_date = json.getString("update_date")
            val validDate = TimeUtil.getValidDate(date_format, valid_hour)
            
            val total_click_threshold = conf.getInt("total_click_threshold")

            if (update_date>=validDate && !content_id.isEmpty && !product_id.isEmpty) {
                if (decay_bored<total_click_threshold && decay_like<total_click_threshold && decay_angry<total_click_threshold && decay_sad<total_click_threshold) { 
                    if (total_click < total_click_threshold) {
                        data = (content_id, product_id, decay_bored, decay_like, decay_angry, decay_sad, decay_total_click,  total_click, update_date, true)
                    }
                }
            }
        } catch {
            case ex: Exception => ex.printStackTrace()
            logger.error("parseHistory fail, " + line)
        }
        data
    }

}