package com.inveno.bigdata.gmp

import com.inveno.bigdata.common.reformat.CombinationKey

object TestParse {
      private val config = ArticleGmpConfig.getInstance();
      val conf = config.getConfig
      val key_tag = conf.getString("key_tag", "###")
      val product_key_tag = conf.getString("product_key_tag", "___")
    
    
      def main(args: Array[String]) {
          val times = 22
      
          times match {
              case i if i == 1 => { println("one") }
              case i if i == 2 => println("two")
              case _ => println("some other number")
          }
        
//          val click = """{"article_click_extra":{"content_id":"37330143","content_type":"news","cpack":{"strategy":"recommendation"}},"event_time":"1472612231","gate_ip":"192.168.1.73","language":"zh_cn","log_time":"2016-08-31 10:57:11","log_type":"click","model":"Coolpad 9976T","network":"","product_id":"ali1","promotion":"coolpad","protocol":"http","scenario":{"channel":"0","desc":"relevant_recommendation","position":"1","position_type":"0"},"uid":"863061028489860","upack":{"abtest_ver":"129","ad_configid":"160","news_configid":"129"}}"""
          val click = """{"article_click_extra":{"click_type":"2","content_id":"1024312299","content_type":"news"},"event_time":"1478505593","language":"en_gb","log_type":"report","product_id":"xiaolajiao","promotion":"gp","protocol":"https","report_time":"1478505593","scenario":{"channel":"0x00","channel_desc":"foryou","desc":"long_listpage","position":"0x01","position_desc":"long_listpage","position_type":"0x01","position_type_desc":"long_listpage"},"seq":"75","sid":"997526983","tk":"6a2efdb4ed52846c3cf1ba48738101f2","uid":"01011611052024434801000024067209","upack":{"ad_configid":"17","biz_configid":"50","news_configid":"85"}}"""
          val click_parse = DataParser.parseClick(click) 
          println(click_parse)
          val product_key_arr = click_parse._1.split(product_key_tag)
          for(product_key_tmp <- product_key_arr) {
              val key_arr = product_key_tmp.split(key_tag)
              val key = CombinationKey.makeArticleCombinationKey(key_tag, key_arr(0), key_arr(1))
              println(key)
          }
          
//          val impression = """{"article_impression_extra":{"content_id":"37615297","content_type":"news","cpack":{"strategy":"mixed_insert"},"server_time":"1472652057"},"brand":"","event_id":"2","event_time":"1472652071","gate_ip":"192.168.1.103","log_time":"2016-08-31 22:03:20","log_type":"report","product_id":"ali1","promotion":"ali","protocol":"https","scenario":{"channel":"0x00","channel_desc":"foryou","desc":"relevant_recommendation","position":"0x01","position_desc":"ali_left_first_screen","position_type":"0x01","position_type_desc":"long_listpage"},"seq":"1400200839","sid":"1472652000","tk":"06292f5b126a1f2793eb89af17a7c0fa","uid":"EEE83D0314F852C3AF50595B3004CAC3","upack":{"abtest_ver":"48"}}"""
          val impression = """{"article_impression_extra":{"content_id":"1024332091","content_type":"short_video","cpack":{"strategy":"recommendation"},"server_time":"1478505475","view_mode":"1"},"brand":"Xiaomi","event_id":"2","event_time":"1478505563","gate_ip":"172.31.3.37","imei":"861645037148608","language":"en_in","log_time":"2016-11-07 07:59:36","log_type":"report","mcc":"IN","mnc":"870","model":"Redmi Note+3","network":"4g","osv":"5.1.1","platform":"android","product_id":"tcl","promotion":"gp","protocol":"https","report_time":"1478505576","scenario":{"channel":"0x0b","channel_desc":"video","desc":"long_listpage","position":"0x10","position_desc":"video_tab","position_type":"0x01","position_type_desc":"long_listpage"},"seq":"16","sid":"778234077","tk":"090f7156d3b04e4dae63e62139f72c7b","uid":"01011610271603094801000176997504","upack":{"ad_configid":"17","biz_configid":"50","news_configid":"83"}}"""
          println(DataParser.parseImpression(impression))
          
//          val request = """{"api_ver":"1.1","app_lan":"zh_cn","app_ver":"3.3.2.op","article_impression_extra":{"content_id":"40296181","content_type":"news","cpack":{"strategy":"recommendation"},"display":"one_photo","link_type":"webview"},"brand":"","event_time":"1473417366","gate_ip":"192.168.1.102","log_time":"2016-09-09 18:36:06","log_type":"request","model":"lephone W7","network":"wifi","platform":"YunOS","product_id":"meizu","promotion":"cloudcard","scenario":{"channel":"0x00","channel_desc":"foryou","desc":"long_listpage","position":"0x01","position_desc":"ali_left_first_screen","position_type":"0x01","position_type_desc":"long_listpage"},"tk":"d71ea6f1c983cfde89000a28ecd6e796","uid":"45DC3C1055858FE4A6E38145D8E0BE1A","upack":{"abtest_ver":"48","ad_configid":"unknown","biz_configid":"unknown","news_configid":"48"}}"""
          val request = """{"aid":"967b401db3587b36","api_ver":"2.0.0","app_lan":"hindi","app_ver":"2.2.7.0.0.4","article_impression_extra":{"content_id":"1024335472","content_type":"news","cpack":{"strategy":"recommendation"},"display":"one_photo","link_type":"native"},"brand":"Sony","event_time":"1478505517","gate_ip":"172.31.6.161","imei":"356872060386093","language":"en_in","log_time":"2016-11-07 07:58:37","log_type":"request_n","mcc":"IN","mnc":"45","model":"D2502","network":"1","osv":"5.1.1","platform":"android","product_id":"tcl","promotion":"gp","scenario":{"channel":"0x00","channel_desc":"foryou","desc":"long_listpage","position":"0x01","position_desc":"long_listpage","position_type":"0x01","position_type_desc":"long_listpage"},"tk":"17631e943d2c831ba80dc7c0561b2e93","uid":"01011610221031194801000157347408","upack":{"abtest_ver":"82","ad_configid":"17","biz_configid":"50","news_configid":"82"}}"""
          println(DataParser.parseRequest(request))
          
          val history = """{"update_date": "20161225", "product_id": "coolpad", "decay_impression": 0.9976004325000001, "decay_click": 0.0861961137, "content_id": "57920006", "total_impression": 9}"""
          println(DataParser.parseHistoryGmp(history))
      }
}