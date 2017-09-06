package com.inveno.news.base.parser

import com.alibaba.fastjson.JSON
import org.slf4j.LoggerFactory
import scala.collection.Map

/**
 * @author Administrator
 */
object ProfileParser {
  
    private val CLASS_NAME = ProfileParser.getClass.getSimpleName

    private val logger = LoggerFactory.getLogger(CLASS_NAME)
  
    def parseProfile(str: String, map: Map[String, String]): Option[(String, String)] = {
      var ret: Option[(String, String)] = None
      
      try {
        val json = JSON.parseObject(str)
        val uid = json.getString("uid")
        val aid = json.getString("aid")
        var promotion = json.getString("promotion")
        if (map.contains(aid)) {
          promotion = map.getOrElse(aid, promotion)
        }
        if (uid != null) {
          ret = Option((uid, promotion))
        }
      } catch {
        case ex: Exception => logger.warn("ProfileParser parseProfile failed to parse Json : {} ", str)
      }

      ret
    }
    
}