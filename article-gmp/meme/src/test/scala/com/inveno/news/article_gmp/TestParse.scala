package com.inveno.news.article_gmp

import com.inveno.bigdata.meme.gmp.DataParser


object TestParse {
      def main(args: Array[String]) {
          val meme = """{
                "uid":"01011703200658505501000251703101",
                "event_type":1,
                "content_type":"0x00000002",
                "content_id":"1044725831",
                "thumb":1,
                "product_id":"noticiasboomcolombia",
                "language":"Spanish",
                "platform":"android",
                "event_time":1489976095
            }"""
          println(DataParser.parseMeme(meme))
          
          val impression = """{
              	"article_impression_extra": {
              		"content_id": "1024332091",
              		"content_type": "short_video",
              		"cpack": {
              			"strategy": "recommendation"
              		},
              		"server_time": "1478505475",
              		"view_mode": "1"
              	},
              	"brand": "Xiaomi",
              	"event_id": "2",
              	"event_time": "1478505563",
              	"gate_ip": "172.31.3.37",
              	"imei": "861645037148608",
              	"language": "en_in",
              	"log_time": "2016-11-07 07:59:36",
              	"log_type": "report",
              	"product_id": "hotoday",
              	"promotion": "gp",
              	"protocol": "https",
              	"report_time": "1478505576",
              	"scenario": {
              		"channel": "0x0b",
              		"channel_desc": "video",
              		"desc": "long_listpage",
              		"position": "0x10",
              		"position_desc": "video_tab",
              		"position_type": "0x01",
              		"position_type_desc": "long_listpage"
              	},
              	"uid": "01011610271603094801000176997504"
              }"""
          println(DataParser.parseImpression(impression))
          
          val history = """{"update_date": "2017011123", "product_id": "noticiasboom", "decay_impression": 65.14259, "decay_click": 0.8866535, "content_id": "1033313097", "total_impression": 72}"""
          println(DataParser.parseHistoryGmp(history))
      }
}