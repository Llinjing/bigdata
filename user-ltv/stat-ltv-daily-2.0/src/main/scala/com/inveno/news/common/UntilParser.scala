package com.inveno.news.common

/**
 * Created by dory on 2016/12/26.
 */
object UntilParser {
  val HISTORY_TAG = Constant.HISTORY_TAG
  val NEWUSER_TAG = Constant.NEWUSER_TAG

  def parseHistory(str_history: String): (String, (Long, Long, Long, Long)) ={
    val info: Array[String] = str_history.split(HISTORY_TAG)
    val key = info(0).replace("(", "")
    val imp = info(1).replace("(", "").toLong
    val click = info(2).toLong
    val list_dwelltime = info(3).toLong
    val detail_dwelltime = info(4).replace("))", "").toLong
    //println("dwelltime is " + detail_dwelltime)

    (key, (imp, click, list_dwelltime, detail_dwelltime))

  }
  def parseNewUser(str_newuser: String) ={
    val ret: Array[String] = Array()
    val info = str_newuser.split(NEWUSER_TAG)
    println(info.length)
    val uid = info(8)
    val product_id = info(0)
    val promotion = info(2)
    val arr = Array(uid, product_id, promotion)
    arr
  }

  def getContentType(content_type:String):String = {
    content_type match {
      case _ if content_type.contains("advertisement") => "ad"
      case _ => "news_all"
    }
  }

  /*def main(args: Array[String]) {
    //val str_test = "(\"11111##coolpad##gp##news\",(3,2,2,1))"
    val str_test = "(01011610141449034801000120828604##hotoday##gp##advertisement_third_party,(3,0,0,0))"
    val ret = parseHistory(str_test)
    println(ret)
    /*val str_new = "mata\t360os2_m\t01011612260531275101000019226302"
    println(parseNewUser(str_new).mkString("##"))*/

  }*/

}
