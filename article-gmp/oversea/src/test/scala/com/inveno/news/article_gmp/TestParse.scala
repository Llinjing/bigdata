package com.inveno.news.article_gmp

import com.inveno.bigdata.gmp.DataParser


object TestParse {
      def main(args: Array[String]) {
//          val click = """{"article_click_extra":{"content_id":"37330143","content_type":"news","cpack":{"strategy":"recommendation"}},"event_time":"1472612231","gate_ip":"192.168.1.73","language":"zh_cn","log_time":"2016-08-31 10:57:11","log_type":"click","model":"Coolpad 9976T","network":"","product_id":"ali1","promotion":"coolpad","protocol":"http","scenario":{"channel":"0","desc":"relevant_recommendation","position":"1","position_type":"0"},"uid":"863061028489860","upack":{"abtest_ver":"129","ad_configid":"160","news_configid":"129"}}"""
          val click = """{
              	"article_click_extra": {
              		"click_type": "2",
              		"content_id": "1024312299",
              		"content_type": "news"
              	},
              	"event_time": "1478505593",
              	"language": "en_gb",
              	"log_type": "report",
              	"product_id": "hotoday",
              	"promotion": "gp",
              	"protocol": "https",
              	"report_time": "1478505593",
              	"scenario": {
              		"channel": "0x00",
              		"channel_desc": "foryou",
              		"desc": "long_listpage",
              		"position": "0x01",
              		"position_desc": "long_listpage",
              		"position_type": "0x01",
              		"position_type_desc": "long_listpage"
              	},
              	"seq": "75",
              	"sid": "997526983",
              	"tk": "6a2efdb4ed52846c3cf1ba48738101f2",
              	"uid": "01011611052024434801000024067209",
              	"upack": {
              		"ad_configid": "17",
              		"biz_configid": "50",
              		"news_configid": "85"
              	}
              }"""
          println(DataParser.parseClick(click))
          
//          val impression = """{"article_impression_extra":{"content_id":"37615297","content_type":"news","cpack":{"strategy":"mixed_insert"},"server_time":"1472652057"},"brand":"","event_id":"2","event_time":"1472652071","gate_ip":"192.168.1.103","log_time":"2016-08-31 22:03:20","log_type":"report","product_id":"ali1","promotion":"ali","protocol":"https","scenario":{"channel":"0x00","channel_desc":"foryou","desc":"relevant_recommendation","position":"0x01","position_desc":"ali_left_first_screen","position_type":"0x01","position_type_desc":"long_listpage"},"seq":"1400200839","sid":"1472652000","tk":"06292f5b126a1f2793eb89af17a7c0fa","uid":"EEE83D0314F852C3AF50595B3004CAC3","upack":{"abtest_ver":"48"}}"""
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