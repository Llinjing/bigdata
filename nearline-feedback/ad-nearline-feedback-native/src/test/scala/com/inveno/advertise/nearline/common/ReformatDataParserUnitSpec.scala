package com.inveno.advertise.nearline.common

import org.specs2.mutable.Specification

class ReformatDataParserUnitSpec extends Specification {

  "ReformatDataParser" should {

    "parseAdvertiseRequest should get the right result" in {

      val adv_request = s"""{"ad_request_extra":{"content_type":"advertisement_third_party","delivery":{"ad":{"ad_source":"baidu_ad"},"ad_scenario":{"channel_desc":"detail_page","channel_id":"piemediah5"},"delivery_id":"1153760000000000000","marketing":{"position_id":"291","position_type":"native","target_size":"1024*618"}}},"app_ver":"1.0","event_id":"0","event_time":"","gate_ip":"192.168.1.246","imei":"034dfca57ca3fd191fa18a1df0d80bd1","log_time":"2016-12-04 23:59:03","log_type":"malacca-ad","model":"","network":"","product_id":"coolpad","protocol":"","result_code":"request_success","uid":"01011509161815180201000001168307","upack":{"ad_configid":"unknown","biz_configid":"unknown","news_configid":"unknown"}}"""

      val expectInfo = Array("1153760000000000000", "01011509161815180201000001168307", "unknown", "baidu_ad", "unknown", "unknown", "unknown", "coolpad", "1.0", "291", "native", "1024*618", "0", "unknown", "unknown", "unknown", "-1", "unknown", "piemediah5", "detail_page", "0.0", "malacca-ad")

      val info = ReformatDataParser.parseAdvertiseRequest(adv_request)

      info mustEqual expectInfo

    }

    "parseAdvertiseImpression should get the right result" in {

      val adv_impression = s"""{"ad_impression_extra":{"content_type":"advertisement_third_party","delivery":{"ad":{"ad_source":"baidu_ad"},"ad_scenario":{"channel_desc":"detail_page","channel_id":"piemediah5"},"delivery_id":"1153760000000000000","marketing":{"position_id":"291","position_type":"native"}}},"app_ver":"1.0","event_id":"2","event_time":"1480867151","gate_ip":"192.168.1.241","imei":"","log_time":"2016-12-04 23:58:57","log_type":"malacca-ad","model":"","network":"unknown","product_id":"coolpad","protocol":"1.0","uid":"866514022010881","upack":{"ad_configid":"unknown","biz_configid":"unknown","news_configid":"unknown"}}"""

      val expectInfo = Array("1153760000000000000", "866514022010881", "unknown", "baidu_ad", "unknown", "unknown", "unknown", "coolpad", "1.0", "291", "native", "unknown", "0", "unknown", "unknown", "unknown", "-1", "unknown", "piemediah5", "detail_page", "0.0", "malacca-ad")

      val info = ReformatDataParser.parseAdvertiseImpression(adv_impression)

      info mustEqual expectInfo

    }

    "parseAdvertiseClick should get the right result" in {

      val adv_click = s"""{"ad_click_extra":{"content_type":"advertisement_third_party","delivery":{"ad":{"ad_source":"taobao_ad"},"ad_scenario":{"channel_desc":"","channel_id":""},"delivery_id":"1153200000000000000","marketing":{}}},"app":"coolpad","app_lan":"zh_cn","app_ver":"unknown","event_id":"3","event_time":"1480953506","gate_ip":"192.168.1.109","language":"zh_cn","log_time":"2016-12-05 23:58:26","log_type":"gate-ad","model":"Coolpad 5892","network":"","product_id":"coolpad","promotion":"coolpad","protocol":"http","uid":"99000523159091","upack":{"ad_configid":"99999","biz_configid":"b1","news_configid":"278"}}"""

      val expectInfo = Array("1153200000000000000", "99000523159091", "unknown", "taobao_ad", "unknown", "unknown", "unknown", "coolpad", "unknown", "unknown", "unknown", "unknown", "0", "278", "b1", "99999", "-1", "unknown", "unknown", "unknown", "0.0", "gate-ad")

      val info = ReformatDataParser.parseAdvertiseClick(adv_click)

      info mustEqual expectInfo

    }

  }

}
