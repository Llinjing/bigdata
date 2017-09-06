package inveno.bigdata.common

import java.text.SimpleDateFormat
import java.util.{Calendar, TimeZone}



object TimeUtil {

  val HOUR_SECONDE = 60 * 60
  val TIMEZONE_SHANGHAI = TimeZone.getTimeZone("Asia/Shanghai")
  def getCurrTimeIndex(): String = {
    val dateFormat = new SimpleDateFormat("yyyyMMddHHmm")
    dateFormat.setTimeZone(TIMEZONE_SHANGHAI)
    val currDateTime = System.currentTimeMillis()
    val timeMin = dateFormat.format(currDateTime).toLong
    (timeMin / 10 * 10).toString
  }

  def getCurrCharTimeIndex(): String = {
    val dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm")
    dateFormat.setTimeZone(TIMEZONE_SHANGHAI)
    val currDateTime = System.currentTimeMillis()
    val timeMin = dateFormat.format(currDateTime)
    timeMin.substring(0, timeMin.length - 1) + "0:00"
  }

  //时间向前推或向后推 amountHour 后的时间段
  def getPreTimeIndex(currTimeIndex: String, timeHour: Int): String = {
    val dateFormat = new SimpleDateFormat("yyyyMMddHHmm")
    dateFormat.setTimeZone(TIMEZONE_SHANGHAI)
    val date = dateFormat.parse(currTimeIndex)
    val outDateStamp = (date.getTime / 1000) - timeHour * HOUR_SECONDE
    dateFormat.format(outDateStamp * 1000)
  }

  def getCurrDate(): String = {
    val dateFormat = new SimpleDateFormat("yyyyMMdd")
    dateFormat.setTimeZone(TIMEZONE_SHANGHAI)
    val currDateTime: Long = System.currentTimeMillis()
    val timeMin = dateFormat.format(currDateTime).toLong
    timeMin.toString
  }

  def getYesterday(): String = {
    val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyyMMdd")
    dateFormat.setTimeZone(TIMEZONE_SHANGHAI)
    val cal: Calendar = Calendar.getInstance()
    cal.add(Calendar.DATE, -1)
    dateFormat.format(cal.getTime)
  }

  def getTimeIndex(): String = {
    //2016-11-24 21:38:11
    val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    dateFormat.setTimeZone(TIMEZONE_SHANGHAI)
    val cal: Calendar = Calendar.getInstance()
    cal.add(Calendar.MINUTE, -30)
    dateFormat.format(cal.getTime)
  }

  def getToday(): String = {
    val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyyMMdd")
    dateFormat.setTimeZone(TIMEZONE_SHANGHAI)
    val cal: Calendar = Calendar.getInstance()
    dateFormat.format(cal.getTime)
  }

  def getTimeIndexFormat(currTimeIndex: String, format: String = "yyyy-MM-ddT HH:mm:00Z"): String = {
    val dateFormat = new SimpleDateFormat(format)
    dateFormat.setTimeZone(TIMEZONE_SHANGHAI)
    val date = dateFormat.parse(currTimeIndex)
    date.toString
  }

  def getHour(index: Int = 0): String = {
    val dateFormat = new SimpleDateFormat("yyyyMMddHHmm")
    dateFormat.setTimeZone(TIMEZONE_SHANGHAI)
    val currDateTime = System.currentTimeMillis() - 60 * 1000 * 10 * (2 + index)
    val timeMin = dateFormat.format(currDateTime).toLong
    val min = (timeMin / 10 * 10).toString
    min.substring(min.length - 4, min.length - 2)
  }

  def getMin(index: Int = 0): String = {
    val dateFormat = new SimpleDateFormat("yyyyMMddHHmm")
    dateFormat.setTimeZone(TIMEZONE_SHANGHAI)
    val currDateTime = System.currentTimeMillis() - 60 * 1000 * 10 * (2 + index)
    val timeMin = dateFormat.format(currDateTime).toLong
    val min = (timeMin / 10 * 10).toString
    min.substring(min.length - 2, min.length)
  }



}
