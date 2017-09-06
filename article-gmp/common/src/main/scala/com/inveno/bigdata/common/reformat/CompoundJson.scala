package com.inveno.bigdata.common.reformat

import org.slf4j.LoggerFactory

import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.JSONArray

object CompoundJson {

    val log = LoggerFactory.getLogger(CompoundJson.getClass.getSimpleName)
     
    def usingJson(op: JSONObject => String) = {
        val json = new JSONObject
        try {
            op(json)
        } catch {
            case e: Exception => log.error("usingJson, failed to get json results !", e.printStackTrace())
            ""
        }
    }

    def usingJsonArray(op: JSONArray => String) = {
        val json_array = new JSONArray
        try {
            op(json_array)
        } catch {
            case e: Exception => log.error("usingJsonArra, failed to get json results !", e.printStackTrace())
            ""
        }
    }
        
    def getEmotionGmpHistoryJson(key_tag: String, key: String, decay_bored: Float, decay_like: Float, decay_angry: Float, decay_sad: Float, decay_total_click: Float, total_click: Long, update_date: String) = {
        usingJson {
            json =>
                val Array(article_id, product_id) = key.split(key_tag)
                json.put("content_id", article_id)
                json.put("product_id", product_id)
                json.put("decay_bored", decay_bored)
                json.put("decay_like", decay_like)
                json.put("decay_angry", decay_angry)
                json.put("decay_sad", decay_sad)
                json.put("decay_total_click", decay_total_click)
                json.put("total_click", total_click)
                json.put("update_date", update_date)
                json.toString
        }
    }
    
    def getEmotionGmpRedisJson(gmp: Set[(String, Float, Float, Float, Float)]) = {
        usingJsonArray {
            json_array =>
                gmp.foreach(ele => {
                    val json_product_id = new JSONObject()
                    val json_detail = new JSONObject()
                    json_detail.put("bored_gmp", ele._2)
                    json_detail.put("like_gmp", ele._3)
                    json_detail.put("angry_gmp", ele._4)
                    json_detail.put("sad_gmp", ele._5)
                    json_product_id.put(ele._1, json_detail)
                    json_array.add(json_product_id)
                })
                json_array.toString
        }
    }
        
    //product_id, decay_meme, decay_impression, meme_gmp
    def getArticleMemeGmpRedisJson(gmp: Set[(String, Float, Long, Float)]) = {
        usingJsonArray {
            json_array =>
                gmp.foreach(ele => {
                    val json_product_id = new JSONObject()
                    val json_detail = new JSONObject()
                    json_detail.put("meme", ele._2)
                    json_detail.put("impression", ele._3)
                    json_detail.put("meme_gmp", ele._4)
                    json_product_id.put(ele._1, json_detail)
                    json_array.add(json_product_id)
                })
                json_array.toString
        }
    }
    
    //current data - (articleId###productId, (decay_meme, decay_impression, total_impression, update_date))
    def getArticleMemeGmpHistoryJson(key_tag: String, key: String, decay_meme: Float, decay_impression: Float, total_impression: Long, update_date: String) = {
        usingJson {
            json =>
                val Array(article_id, product_id) = key.split(key_tag)
                json.put("content_id", article_id)
                json.put("product_id", product_id)
                json.put("decay_meme", decay_meme)
                json.put("decay_impression", decay_impression)
                json.put("total_impression", total_impression)
                json.put("update_date", update_date)
                json.toString
        }
    }
        
    def getArticleGmpHistoryJson(key_tag: String, key: String, decay_impression: Float, decay_click: Float, total_impression: Long, update_date: String) = {
        usingJson {
            json =>
                val Array(article_id, product_id) = key.split(key_tag)
                json.put("content_id", article_id)
                json.put("product_id", product_id)
                json.put("decay_click", decay_click)
                json.put("decay_impression", decay_impression)
                json.put("total_impression", total_impression)
                json.put("update_date", update_date)
                json.toString
        }
    }
    
        def getAdvertisementGmpHistoryJson(key_tag: String, key: String, decay_impression: Float, decay_click: Float, total_impression: Long, update_date: String) = {
        usingJson {
            json =>
                val Array(product_id, adspace_id, ad_md5) = key.split(key_tag)
                json.put("product_id", product_id)
                json.put("adspace_id", adspace_id)
                json.put("ad_md5", ad_md5)
                json.put("decay_click", decay_click)
                json.put("decay_impression", decay_impression)
                json.put("total_impression", total_impression)
                json.put("update_date", update_date)
                json.toString
        }
    }
    
    //product_id, adspace_id, decay_click, decay_impression, gmp
    def getAdvertisementGmpRedisJson(gmp: Set[(String, String, Float, Long, Float)]) = {
        usingJsonArray {
            json_array =>
                gmp.foreach(ele => {
                    val json_detail = new JSONObject()
                    json_detail.put("product_id", ele._1)
                    json_detail.put("adspace_id", ele._2)
                    json_detail.put("click", ele._3)
                    json_detail.put("impression", ele._4)
                    json_detail.put("gmp", ele._5)
                    json_array.add(json_detail)
                })
                json_array.toString
        }
    }
    
    //product_id, decay_click, decay_impression, gmp
    def getArticleGmpRedisJson(gmp: Set[(String, Float, Long, Float)]) = {
        usingJsonArray {
            json_array =>
                gmp.foreach(ele => {
                    val json_product_id = new JSONObject()
                    val json_detail = new JSONObject()
                    json_detail.put("click", ele._2)
                    json_detail.put("impression", ele._3)
                    json_detail.put("ctr", ele._4)
                    json_product_id.put(ele._1, json_detail)
                    json_array.add(json_product_id)
                })
                json_array.toString
        }
    }
    
    //(articleId###productId, (decay_request, decay_impression, decay_click, total_request, total_impression, update_date))
    def getArticleGmpHistoryJsonV2(key_tag: String, key: String, decay_request: Float, decay_impression: Float, decay_click: Float, total_request: Long, total_impression: Long, update_date: String) = {
        usingJson {
            json =>
                val Array(article_id, product_id) = key.split(key_tag)
                json.put("content_id", article_id)
                json.put("product_id", product_id)
                json.put("decay_request", decay_request)
                json.put("decay_impression", decay_impression)
                json.put("decay_click", decay_click)                
                json.put("total_request", total_request)
                json.put("total_impression", total_impression)
                json.put("update_date", update_date)
                json.toString
        }
    }

    //product_id, total_request, total_impression, request_gmp, impression_gmp
    def getArticleGmpRedisJsonV2(gmp: Set[(String, Long, Long, Float, Float)]) = {
        usingJsonArray {
            json_array =>
                gmp.foreach(ele => {
                    val json_product_id = new JSONObject()
                    val json_detail = new JSONObject()                    
                    if(ele._4 > -1f){
                        json_detail.put("req", ele._2)
                        json_detail.put("req_gmp", ele._4)  
                    }
                    if(ele._5 > -1f){
                        json_detail.put("imp", ele._3)
                        json_detail.put("imp_gmp", ele._4)  
                    }                    
                    json_product_id.put(ele._1, json_detail)
                    json_array.add(json_product_id)
                })
                json_array.toString
        }
    }
}