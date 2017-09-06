package com.inveno.bigdata.gmp

import com.alibaba.fastjson.JSON
import org.slf4j.LoggerFactory
import com.inveno.bigdata.common.reformat.CombinationKey
import com.inveno.bigdata.common.TimeUtil

object DataParserBak {
    
    val logger = LoggerFactory.getLogger(DataParserBak.getClass)
    
    private val config = ArticleGmpConfig.getInstance();
    val conf = config.getConfig
    val date_format = conf.getString("date_format")
    val valid_hour = conf.getInt("valid_hour")
    val key_tag = conf.getString("key_tag", "###")
    val product_key_tag = conf.getString("product_key_tag", "___")
        
    // get data, and filter not-news data      
    def parseRequest(line: String) = {
        var data = ("", false)
        try {
            val json = JSON.parseObject(line)
            
            val product_id = json.getString("product_id")
            val uid = json.getString("uid")
            val article_request_extra = json.getJSONObject("article_impression_extra")
            val content_id = article_request_extra.getString("content_id")            
            val content_type = article_request_extra.getString("content_type")
            val scenario = json.getJSONObject("scenario").getString("desc")
            val log_type = json.getString("log_type")
                      
            if (!log_type.contentEquals("request_n") && (scenario.contentEquals("long_listpage") || scenario.contentEquals("waterfall"))){
                if (content_type.contentEquals("news") && !content_id.isEmpty && !product_id.isEmpty && !uid.isEmpty()) {
                    product_id match {
                        case tcl if conf.getString("product_id_tcl").contentEquals(tcl) => {
                            val unique_key_tcl = CombinationKey.makeArticleCombinationKey(key_tag, content_id, product_id, uid)
                            val unique_key_union_tcl_dw_xlj = CombinationKey.makeArticleCombinationKey(key_tag, content_id, conf.getString("product_id_union_tcl_dw_xlj"), uid)
                            val unique_key = CombinationKey.makeProduceCombinationKey(product_key_tag, unique_key_tcl, unique_key_union_tcl_dw_xlj)
                            data = (unique_key, true)
                        } 
                        case duowei if conf.getString("product_id_duowei").contentEquals(duowei) => {
                            val unique_key_duowei = CombinationKey.makeArticleCombinationKey(key_tag, content_id, product_id, uid)
                            val unique_key_union_tcl_dw_xlj = CombinationKey.makeArticleCombinationKey(key_tag, content_id, conf.getString("product_id_union_tcl_dw_xlj"), uid)
                            val unique_key = CombinationKey.makeProduceCombinationKey(product_key_tag, unique_key_duowei, unique_key_union_tcl_dw_xlj)
                            data = (unique_key, true)
                        }
                        case xiaolajiao if conf.getString("product_id_xiaolajiao").contentEquals(xiaolajiao) => {
                            val unique_key_xiaolajiao = CombinationKey.makeArticleCombinationKey(key_tag, content_id, product_id, uid)
                            val unique_key_union_tcl_dw_xlj = CombinationKey.makeArticleCombinationKey(key_tag, content_id, conf.getString("product_id_union_tcl_dw_xlj"), uid)
                            val unique_key = CombinationKey.makeProduceCombinationKey(product_key_tag, unique_key_xiaolajiao, unique_key_union_tcl_dw_xlj)
                            data = (unique_key, true)
                        }
                        case _ => {
                            val unique_key = CombinationKey.makeArticleCombinationKey(key_tag, content_id, product_id, uid)
                            data = (unique_key, true)
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
                if (content_type.contentEquals("news")) {
                    product_id match {
                        case ali if conf.getString("product_id_ali").contentEquals(ali) => {
                            if(scenario.contentEquals("long_listpage") || scenario.contentEquals("relevant_recommendation") || scenario.contentEquals("waterfall")) {
                                val unique_key = CombinationKey.makeArticleCombinationKey(key_tag, content_id, product_id, uid)
                                data = (unique_key, true)
                            } 
                        }
                        case meizu if conf.getString("product_id_meizu").contentEquals(meizu) => {
                            if(scenario.contentEquals("short_listpage") || scenario.contentEquals("long_listpage") || scenario.contentEquals("waterfall")) {
                                val unique_key = CombinationKey.makeArticleCombinationKey(key_tag, content_id, product_id, uid)
                                data = (unique_key, true)
                            } 
                        }
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
            
                if (content_type.contentEquals("news")) {
                	if (!content_id.isEmpty && !product_id.isEmpty && !uid.isEmpty()) {
                    product_id match {
                        case ali if conf.getString("product_id_ali").contentEquals(ali) => {
                            if(scenario.contentEquals("long_listpage") || scenario.contentEquals("relevant_recommendation") || scenario.contentEquals("waterfall")) {
                                val unique_key = CombinationKey.makeArticleCombinationKey(key_tag, content_id, product_id, uid)
                                data = (unique_key, true)
                            } 
                        }
                        case meizu if conf.getString("product_id_meizu").contentEquals(meizu) => {
                            if(scenario.contentEquals("short_listpage") || scenario.contentEquals("long_listpage") || scenario.contentEquals("waterfall")) {
                                val unique_key = CombinationKey.makeArticleCombinationKey(key_tag, content_id, product_id, uid)
                                data = (unique_key, true)
                            } 
                        }
                        case tcl if conf.getString("product_id_tcl").contentEquals(tcl) => {
                            if(scenario.contentEquals("long_listpage") || scenario.contentEquals("waterfall")) {
                                val unique_key_tcl = CombinationKey.makeArticleCombinationKey(key_tag, content_id, product_id, uid)
                                val unique_key_union_tcl_dw_xlj = CombinationKey.makeArticleCombinationKey(key_tag, content_id, conf.getString("product_id_union_tcl_dw_xlj"), uid)
                                val unique_key = CombinationKey.makeProduceCombinationKey(product_key_tag, unique_key_tcl, unique_key_union_tcl_dw_xlj)
                                data = (unique_key, true)
                            }
                        } 
                        case duowei if conf.getString("product_id_duowei").contentEquals(duowei) => {
                            if(scenario.contentEquals("long_listpage") || scenario.contentEquals("waterfall")) {
                                val unique_key_duowei = CombinationKey.makeArticleCombinationKey(key_tag, content_id, product_id, uid)
                                val unique_key_union_tcl_dw_xlj = CombinationKey.makeArticleCombinationKey(key_tag, content_id, conf.getString("product_id_union_tcl_dw_xlj"), uid)
                                val unique_key = CombinationKey.makeProduceCombinationKey(product_key_tag, unique_key_duowei, unique_key_union_tcl_dw_xlj)
                                data = (unique_key, true)
                            }
                        }
                        case xiaolajiao if conf.getString("product_id_xiaolajiao").contentEquals(xiaolajiao) => {
                            if(scenario.contentEquals("long_listpage") || scenario.contentEquals("waterfall")) {
                                val unique_key_xiaolajiao = CombinationKey.makeArticleCombinationKey(key_tag, content_id, product_id, uid)
                                val unique_key_union_tcl_dw_xlj = CombinationKey.makeArticleCombinationKey(key_tag, content_id, conf.getString("product_id_union_tcl_dw_xlj"), uid)
                                val unique_key = CombinationKey.makeProduceCombinationKey(product_key_tag, unique_key_xiaolajiao, unique_key_union_tcl_dw_xlj)
                                data = (unique_key, true)
                            }
                        }
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
        var data = ("", "", 0.0f, 0.0f, 0L, "", false)
        try {
            val json = JSON.parseObject(line)

            val content_id = json.getString("content_id")
            val product_id = json.getString("product_id")
            val decay_click = json.getFloat("decay_click")
            val decay_impression = json.getFloat("decay_impression")
            val total_impression = json.getLong("total_impression")
            val update_date = json.getString("update_date")
            val validDate = TimeUtil.getValidDate(date_format, valid_hour)
            
            if (update_date>=validDate && !content_id.isEmpty && !product_id.isEmpty) {
                data = (content_id, product_id, decay_impression, decay_click, total_impression, update_date, true)
            }
        } catch {
            case ex: Exception => ex.printStackTrace()
            logger.error("parseHistory fail, " + line)
        }
        data
    }

}