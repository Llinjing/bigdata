package com.inveno.news.article_gmp

import com.inveno.bigdata.gmp.DataParser
import com.inveno.bigdata.common.reformat.CombinationKey

object TestParse {
  def main(args: Array[String]) {
    val click = """{
    "aid": "9c6768b83a4c7d7c",
    "api_ver": "2.0.0",
    "app_lan": "hindi",
    "app_ver": "2.4.9.0.0.4",
    "article_click_extra": {
        "click_type": "3",
        "content_id": "Facebook_000001",
        "content_type": "advertisement",
        "cpack": {
            "ad_source": "Facebook",
            "category": -1,
            "source": "unknown",
            "strategy": "advertisement_recommendation"
        },
        "extra_msg": {
            "t_adspace_id": "509152332615946_544789942385518",
            "ad_md5": "4165afd8fe89bbf5a908676a5aced970",
            "ad_title": "Study Advanced Diploma of Engineering (Mechanical) in Sydney, Australia at TAFE NSW",
            "adspace_id": "9"
        }
    },
    "brand": "samsung",
    "event_id": "3",
    "event_time": "1496230202",
    "gate_ip": "172.31.2.155",
    "language": "en_gb",
    "log_time": "2017-05-31 11:30:03",
    "log_type": "report",
    "mcc": "IN",
    "mnc": "872",
    "model": "SM-G610F",
    "network": "4g",
    "osv": "6.0.1",
    "platform": "android",
    "product_id": "hotoday",
    "promotion": "gp",
    "protocol": "https",
    "report_time": "1496230202",
    "scenario": {
        "channel": "0x00",
        "channel_desc": "foryou",
        "desc": "long_listpage",
        "position": "0x01",
        "position_desc": "long_listpage",
        "position_type": "0x01",
        "position_type_desc": "long_listpage"
    },
    "seq": "21",
    "sid": "330594620",
    "tk": "303878536d3f38a299021bfc180c98f2",
    "uid": "01011610041513154801000064920805",
    "upack": {
        "ad_configid": "66",
        "biz_configid": "50",
        "news_configid": "199"
    }
}"""
    println(DataParser.parseClick(click))

    val impression = """{
    "aid": "e53f081c22c843b7",
    "api_ver": "2.0.0",
    "app_lan": "english",
    "app_ver": "2.4.9.0.0.4",
    "article_impression_extra": {
        "content_id": "AltaMobile_000001",
        "content_type": "advertisement",
        "cpack": {
            "ad_source": "AltaMobile",
            "category": -1,
            "source": "unknown",
            "strategy": "advertisement_recommendation"
        },
        "extra_msg": {
            "t_adspace_id": "1662684189370000_1769833153868434",
            "ad_md5": "7f9051e99913d59a4ce886261033b450",
            "ad_title": "Saavn Music & Radio",
            "adspace_id": "1"
        },
        "server_time": "1496224688"
    },
    "brand": "motorola",
    "event_id": "2",
    "event_time": "1496224746",
    "gate_ip": "172.31.6.161",
    "language": "en_gb",
    "log_time": "2017-05-31 09:59:55",
    "log_type": "report",
    "mcc": "IN",
    "mnc": "90",
    "model": "XT1562",
    "network": "4g",
    "osv": "6.0.1",
    "platform": "android",
    "product_id": "hotoday",
    "promotion": "gp",
    "protocol": "https",
    "report_time": "1496224795",
    "scenario": {
        "channel": "0x00",
        "channel_desc": "foryou",
        "desc": "long_listpage",
        "position": "0x01",
        "position_desc": "long_listpage",
        "position_type": "0x01",
        "position_type_desc": "long_listpage"
    },
    "seq": "45",
    "sid": "21778892",
    "tk": "8c9586ec8f7b2c621cb7156e46018519",
    "uid": "01011610252338224801000168222400",
    "upack": {
        "ad_configid": "66",
        "biz_configid": "50",
        "news_configid": "198"
    }
}"""
    println(DataParser.parseImpression(impression))

    val history = """
      {
    "decay_impression": 2,
    "decay_click": 0,
    "ad_md5": "476da0574808385e9576b555db8838f1",
    "product_id": "hotoday",
    "total_impression": 2,
    "update_date": "2017060220",
    "adspace_id": "2"
}
      """
    
        val key_arr = (DataParser.parseHistoryGmp(history)._1).split("###")
        val product_id = key_arr(0)
        val adspace_id = key_arr(1)
        val ad_md5 = key_arr(2)
        val decay_impression = key_arr(3).toFloat
        val decay_click = key_arr(4).toFloat
        val total_impression = key_arr(5).toLong
        val update_date = key_arr(6)
        val key = CombinationKey.makeArticleCombinationKey("###", product_id, adspace_id, ad_md5)
        val decay_ratio = 0.995
        println((key, (decay_ratio * decay_impression, decay_ratio * decay_click, total_impression, update_date, 0)))
    println()
    println(DataParser.parseHistoryGmp(history))
  }
}