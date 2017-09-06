package com.inveno.advertisement.common

/**
 * @author Administrator
 */
object CommonUtil {
  
    def mergeKey(arr: Array[String]): String = {
      var str = ""
      for (ele <- arr) {
        if (str.length() > 0) {
          str = str+(Constant.KEY_TAG)+(ele)
        } else {
          str = ele
        }
      }
      str
    }
  
}