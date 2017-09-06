package com.inveno.bigdata.common.reformat

import org.slf4j.LoggerFactory

import com.alibaba.fastjson.JSONObject
import com.inveno.bigdata.common.CommonConfig
import com.alibaba.fastjson.JSONArray

object CombinationKey {

    def makeArticleCombinationKey(key_tag: String, a: String, b: String) = {
        s"$a$key_tag$b"
    }

    def makeArticleCombinationKey(key_tag: String, a: String, b: String, c: String) = {
        s"$a$key_tag$b$key_tag$c"
    }
        
    def makeArticleCombinationKey(key_tag: String, a: String, b: String, c: String, d: String) = {
        s"$a$key_tag$b$key_tag$c$key_tag$d"
    }

    def makeArticleCombinationKey(key_tag: String, a: String, b: String, c: Float, d: Float, e: Long, f: String) = {
        s"$a$key_tag$b$key_tag$c$key_tag$d$key_tag$e$key_tag$f"
    }
        
    def makeArticleCombinationKey(key_tag: String, a: String, b: String, c: String, d: Float, e: Float, f: Long, g: String) = {
        s"$a$key_tag$b$key_tag$c$key_tag$d$key_tag$e$key_tag$f$key_tag$g"
    }
        
    def makeArticleCombinationKey(key_tag: String, a: String, b: String, c: Integer, d: Integer, e: Integer, f: Integer, g: String) = {
        s"$a$key_tag$b$key_tag$c$key_tag$d$key_tag$e$key_tag$f$key_tag$g"
    }
    
    def makeProduceCombinationKey(product_key_tag: String, a: String, b: String) = { 
        s"$a$product_key_tag$b"
    }
}