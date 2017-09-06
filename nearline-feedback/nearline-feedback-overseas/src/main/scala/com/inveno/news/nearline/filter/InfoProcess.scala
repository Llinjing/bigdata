package com.inveno.news.nearline.filter

trait InfoProcess {

  def AdvertiseFilter(logType: String, appVer: String): Boolean = {
    logType == "overseas_ad_report" && appVer >= "2.2.1"
  }

  def IsAdvertise(contenttype: String): Boolean = {
    contenttype == "advertisement_third_party" ||
      contenttype == "advertisement_soft" ||
      contenttype == "advertisement_hard"
  }

  def getFlag(value: Long): String = {
    if (value > 0) "true" else "false"
  }

}
