package com.inveno.advertise.nearline.common

import java.text.SimpleDateFormat


object TimeUtil {

  val HOUR_SECONDE = 60 * 60

  def getCurrTimeIndex(): String = {
    val dateFormat = new SimpleDateFormat("yyyyMMddHHmm")
    val currDateTime = System.currentTimeMillis()
    val timeMin = dateFormat.format(currDateTime).toLong
    (timeMin / 10 * 10).toString
  }

  //时间向前推或向后推 amountHour 后的时间段
  def getPreTimeIndex(currTimeIndex: String, timeHour: Int): String = {
    val dateFormat = new SimpleDateFormat("yyyyMMddHHmm")
    val date = dateFormat.parse(currTimeIndex)
    val outDateStamp = (date.getTime / 1000) - timeHour * HOUR_SECONDE
    dateFormat.format(outDateStamp * 1000)
  }

  def getTimeIndexFormat(currTimeIndex: String, format: String = "yyyy-MM-ddT HH:mm:00Z"): String = {
    val dateFormat = new SimpleDateFormat(format)
    val date = dateFormat.parse(currTimeIndex)
    date.toString
  }

}
