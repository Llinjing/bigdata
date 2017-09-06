package com.inveno.bigdata.emotion.gmp


object TestParse {
      def main(args: Array[String]) {
          //advertisement_hard
          //advertisement_third_party
          //advertisement_soft
          //news
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
              		"position_desc": "push",
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
          
          val emotion = """
{"ballot":{"Bored":-1,"Like":0,"Angry":0,"Sad":0},"uid":"01011612181716025101000038987806","content_id":1030819626,"log_ip":"172.31.27.218","product_id":"mata","language":"Indonesian","timestamp":1482482928}
          """
          println(DataParser.parseEmotion(emotion))
          
          val decay_history = """
{"decay_like":180034988,"decay_bored":1.6204128311,"content_id":"1030695","product_id":"noticiasboom","decay_total_click":5759.247,"decay_sad":2.1414602,"decay_angry":1.620315119,"update_date":"2016122315","total_click":6945}
            """
          println(DataParser.parseHistoryEmotionGmp(decay_history))
          
      }
}