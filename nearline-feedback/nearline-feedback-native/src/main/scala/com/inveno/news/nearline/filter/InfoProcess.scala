package com.inveno.news.nearline.filter

trait InfoProcess {

  def IsAdvertise(contenttype: String): Boolean = {
    contenttype == "advertisement_third_party" ||
      contenttype == "advertisement_soft" ||
      contenttype == "advertisement_hard"
  }

  def getFlag(value: Long): String = {
    if (value > 0) "true" else "false"
  }

}
