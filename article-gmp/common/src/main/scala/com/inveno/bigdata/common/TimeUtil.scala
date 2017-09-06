package com.inveno.bigdata.common

import java.text.SimpleDateFormat
import java.util.TimeZone
import java.util.Date
object TimeUtil {

    val HOUR_MILLIS = 60 * 60 * 1000
    val TIMEZONE_SHANGHAI = TimeZone.getTimeZone("Asia/Shanghai")
   
    def getCurrDate(format: String="yyyyMMdd"): String = {
        val date_format = new SimpleDateFormat(format)
        date_format.setTimeZone(TIMEZONE_SHANGHAI);
        val curr_date = System.currentTimeMillis()
        val res = date_format.format(curr_date).toString
        res
    }

    //"yyyyMMddHH"
    def getValidDate(format: String="yyyyMMdd", valid_hour: Int=48): String = {
        val date_format = new SimpleDateFormat(format)
        date_format.setTimeZone(TIMEZONE_SHANGHAI)
        val timestamp = System.currentTimeMillis()
        val res = date_format.format(timestamp - HOUR_MILLIS*valid_hour).toString
        res
    }
          
    def getCurrTimestamp(): String = {
        val curr_timestamp = System.currentTimeMillis().toString()
        curr_timestamp
    }        
}
