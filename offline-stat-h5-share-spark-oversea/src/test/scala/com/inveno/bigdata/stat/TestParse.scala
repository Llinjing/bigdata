package com.inveno.bigdata.stat

import com.inveno.bigdata.common.DataParser

object TestParse {
      def main(args: Array[String]) {
          val data = """{
    "uid":"01011702022128145101000072106708",
    "tm":"1487067476",
    "share":"1",
    "scenario":"0x0409ff",
    "content_id":"1039052942",
    "product_id":"mata",
    "theme":"1",
    "app_lan":"Indonesian",
    "log_ip":"172.31.12.138",
    "user_ip":"120.188.6.63, 172.31.12.140",
    "log_time":"2017-02-14 10:18:18"
}"""
          val result = DataParser.parseH5Share(data)
          println(result)
      }
}