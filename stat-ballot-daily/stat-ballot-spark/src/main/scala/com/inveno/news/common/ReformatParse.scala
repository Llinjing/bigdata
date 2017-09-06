package com.inveno.news.common


import com.alibaba.fastjson.JSON
import org.slf4j.LoggerFactory


/**
 * Created by dory on 2016/12/15.
 */
object ReformatParse {
  private val CLASS_NAME = ReformatParse.getClass.getSimpleName
  val logger = LoggerFactory.getLogger(CLASS_NAME)

  def parseFourEmotion(str: String): Array[String] ={
    var ret: Array[String] = Array()
    try{
      val json = JSON.parseObject(str)
      val product_id = json.getString("product_id")
      val language = json.getString("language")
      val uid = json.getString("uid")

      val emotion_type = {
        val ballot = json.getJSONObject("ballot")
        val emo_type = if(ballot.getString("Bored").toInt > 0){
          "bore"
        }else if(ballot.getString("Like").toInt > 0){
          "like"
        }else if(ballot.getString("Angry").toInt > 0 ){
          "angry"
        }else if(ballot.getString("Sad").toInt > 0){
          "sad"
        }
        emo_type.toString
      }
      val arr = Array(product_id, language,  emotion_type, uid)

      if(isNull(arr)){
        ret = arr
      }
    }catch {
      case ex:Exception => logger.info("ReformatDataParser ballot failed to parse Json !" + ex.printStackTrace())
    }
    ret
  }

  def parseEmotionArticle(str: String): Array[String] ={
    var ret: Array[String] = Array()
    try{
      val json = JSON.parseObject(str)
      val product_id = json.getString("product_id")
      val language = json.getString("language")
      val content_id = json.getString("content_id")
      val ballot = json.getJSONObject("ballot")
      val bore_num = ballot.getString("Bored")
      val like_num = ballot.getString("Like")
      val angry_num = ballot.getString("Angry")
      val sad_num = ballot.getString("Sad")
      val uid = json.getString("uid")
      val arr = Array(uid, product_id, language, content_id, bore_num, like_num, angry_num, sad_num)
      if(isNull(arr)){
        ret = arr
      }
    }catch {
      case ex: Exception => logger.info("ReformatDataParse ballot failed to parse json, the str " + str )
    }
    ret
  }

  def isNull(arr:Array[String]): Boolean ={
    !arr.map(x=>x==null).contains(true)
  }

  def parseProfile(str: String): String ={
    var str_uid = ""
    try{
      val json = JSON.parseObject(str)
      val uid = json.getString("uid")
      str_uid = uid
    }catch {
      case ex: Exception => logger.info("ReformatDataParse Profile, the str " + str)
    }
    str_uid
  }

}
