package com.inveno.bigdata.common

import com.alibaba.fastjson.JSON
import org.slf4j.LoggerFactory

object DataParser {
  
    private val CLASS_NAME = DataParser.getClass.getSimpleName
  
    private val logger = LoggerFactory.getLogger(CLASS_NAME)
    
    def parseH5Share(str: String): String = {
        var ret: String = ""
        
        try {
            val json = JSON.parseObject(str)
            val product_id = json.getString("product_id")
            val app_lan = json.getString("app_lan")
            val content_id = json.getString("content_id")
            val uid = json.getString("uid")
            val scenario = json.getString("scenario")
            val theme = json.getString("theme")
            
            val arr = Array(product_id, app_lan, content_id, uid, scenario, theme)
            ret = compound(arr)
        } catch {
            case ex: Exception => logger.warn("DataParser parseH5Share failed to parse Json : {} ", str)
        }
        
        ret
    }  
    
    def compound(arr: Array[String]): String = {
        val buf = new StringBuilder;
        val size = arr.length
        var i = 0
        for(ele <- arr) {
            if (ele==null || ele=="") {
                buf ++= "unknown"
            } else{
                buf ++= ele
            }
            i += 1
            if (i < size) {
                buf ++= Constant.KEY_TAG  
            }
        }
        buf.toString()
    }
  
}