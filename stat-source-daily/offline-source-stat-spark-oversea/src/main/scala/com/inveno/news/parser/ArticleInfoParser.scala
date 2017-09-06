package com.inveno.news.parser

import com.alibaba.fastjson.JSON
import org.slf4j.LoggerFactory
import scala.collection.mutable.HashSet

/**
 * @author Administrator
 */
object ArticleInfoParser {
  
  private val CLASS_NAME = ArticleInfoParser.getClass.getSimpleName

  private val logger = LoggerFactory.getLogger(CLASS_NAME)
  
  def parseArticleInfo(str: String, set: HashSet[String]): Option[Array[String]] = {
    var ret: Option[Array[String]] = None
    
    try {
      val json = JSON.parseObject(str)
      
      val content_id = json.getString("content_id")
      var source = json.getString("source")
      if ("Excelsior".equals(source)) {
        source = "Excélsior" 
      }
      if (set.contains(source)) {
        val arr = Array(content_id, source)
        if (isNotNull(arr)) {
          ret = Option(arr)
        }
      }
    } catch {
      case ex: Exception => logger.error("ArticleInfoParser parseArticleInfo failed to parse Json : {} ", str)
    }
    
    ret
  }
  
  def isNotNull(arr: Array[String]): Boolean = {
    var result = true
    for(ele <- arr) {
      if (ele == null) {
        result = false
      }
    }
    result
  }
  
}