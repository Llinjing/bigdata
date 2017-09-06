package com.inveno.advertise.nearline.common

import org.specs2.mutable._

class FeedBackUtilUnitSpec extends Specification {

  "FeedBackUtil" should {

    "generateAdvertiseKey should get the right String" in {

      val expectRes = s"""ad_id###ad_type###ad_source###advertiser_name###ad_product_name###industry###app_ver###product_id###position_id###position_type###target_size###daily_count_limit###news_configid###biz_configid###ad_configid###sort_weight###pay_model###channel###scenario###unit_price###log_type"""
      val expectRes2 = s"""ad_id###ad_type###ad_source###advertiser_name###ad_product_name###industry###app_ver###hotoday###position_id###position_type###target_size###daily_count_limit###news_configid###biz_configid###ad_configid###sort_weight###pay_model###channel###scenario###unit_price###log_type"""

      val info = Array("ad_id", "uid", "ad_type", "ad_source", "advertiser_name", "ad_product_name", "industry",
        "product_id", "app_ver", "position_id", "position_type", "target_size", "daily_count_limit", "news_configid", "biz_configid",
        "ad_configid", "sort_weight", "pay_model", "channel", "scenario", "unit_price", "log_type")

      val info2 = Array("ad_id", "uid", "ad_type", "ad_source", "advertiser_name", "ad_product_name", "industry",
        "hotoday", "app_ver", "position_id", "position_type", "target_size", "daily_count_limit", "news_configid", "biz_configid",
        "ad_configid", "sort_weight", "pay_model", "channel", "scenario", "unit_price", "log_type")

      val advKey = FeedBackUtil.generateAdvertiseKey(info)
      val advKey2 = FeedBackUtil.generateAdvertiseKey(info2)

      advKey mustEqual expectRes
      advKey2 mustEqual expectRes2
    }

    "generateUserKey should get the right String" in {

      val expectRes = s"""uid###product_id###ad_source###log_type###news_configid###biz_configid###ad_configid"""
      val expectRes2 = s"""uid###hotoday###baidu###log_type###news_configid###biz_configid###ad_configid"""

      val info = Array("ad_id", "uid", "ad_type", "ad_source", "advertiser_name", "ad_product_name", "industry",
        "product_id", "app_ver", "position_id", "position_type", "target_size", "daily_count_limit", "news_configid", "biz_configid",
        "ad_configid", "sort_weight", "pay_model", "channel", "scenario", "unit_price", "log_type")

      val info2 = Array("ad_id", "uid", "ad_type", "baidu", "advertiser_name", "ad_product_name", "industry",
        "hotoday", "app_ver", "position_id", "position_type", "target_size", "daily_count_limit", "news_configid", "biz_configid",
        "ad_configid", "sort_weight", "pay_model", "channel", "scenario", "unit_price", "log_type")

      val advKey = FeedBackUtil.generateUserKey(info)
      val advKey2 = FeedBackUtil.generateUserKey(info2)

      advKey mustEqual expectRes
      advKey2 mustEqual expectRes2

    }

    "makeAdvertiseFeedBackResult should get the right Json String" in {

      val expectJson = s"""{"request":100,"ad_type":"ad_type","daily_count_limit":"1000000","channel":"channel","news_configid":"news_configid","industry":"industry","target_size":"target_size","ad_configid":"ad_configid","log_type":"log_type","ad_source":"ad_source","scenario":"scenario","biz_configid":"biz_configid","product_id":"product_id","position_type":"position_type","timestamp":"201612051100","complete_rate":"0.0000050000","budget_cost":"2.5000","ad_product_name":"ad_product_name","unit_price":"0.5","click":5,"sort_weight":"sort_weight","ad_id":"ad_id","pay_model":"cpc","app_ver":"app_ver","impression":20,"advertiser_name":"advertiser_name","position_id":"position_id"}"""
      val expectJson2 = s"""{"request":100,"ad_type":"ad_type","daily_count_limit":"1000000","channel":"channel","news_configid":"news_configid","industry":"industry","target_size":"target_size","ad_configid":"ad_configid","log_type":"log_type","ad_source":"ad_source","scenario":"scenario","biz_configid":"biz_configid","product_id":"hotoday","position_type":"position_type","timestamp":"201612051100","complete_rate":"0.0000050000","budget_cost":"2.5000","ad_product_name":"ad_product_name","unit_price":"0.5","click":5,"sort_weight":"sort_weight","ad_id":"ad_id","pay_model":"cpc","app_ver":"app_ver","impression":20,"advertiser_name":"advertiser_name","position_id":"position_id"}"""

      val key1 = s"""ad_id###ad_type###ad_source###advertiser_name###ad_product_name###industry###app_ver###product_id###position_id###position_type###target_size###1000000###news_configid###biz_configid###ad_configid###sort_weight###cpc###channel###scenario###0.5###log_type"""
      val key2 = s"""ad_id###ad_type###ad_source###advertiser_name###ad_product_name###industry###app_ver###hotoday###position_id###position_type###target_size###1000000###news_configid###biz_configid###ad_configid###sort_weight###cpc###channel###scenario###0.5###log_type"""

      val fields: Array[String] = key1.split("###")
      val fields2 = key2.split("###")

      val Json = FeedBackUtil.makeAdvertiseFeedBackResult("201612051100", fields, (100, 20, 5))
      val Json2 = FeedBackUtil.makeAdvertiseFeedBackResult("201612051100", fields2, (100, 20, 5))

      Json mustEqual expectJson
      Json2 mustEqual expectJson2

    }

    "makeUserFeedBackResult should get the right Json String" in {

      val expectJson = s"""{"request":100,"news_configid":"news_configid","click":5,"uid":"uid","ad_configid":"biz_configid","log_type":"log_type","click_valid":"true","ad_source":"ad_source","biz_configid":"ad_configid","product_id":"product_id","request_valid":"true","impression":20,"impression_valid":"true","timestamp":"201612051100"}"""
      val expectJson2 = s"""{"request":100,"news_configid":"news_configid","click":5,"uid":"uid","ad_configid":"biz_configid","log_type":"log_type","click_valid":"true","ad_source":"baidu","biz_configid":"ad_configid","product_id":"hotoday","request_valid":"true","impression":20,"impression_valid":"true","timestamp":"201612051100"}"""

      val key1 = s"""uid###product_id###ad_source###log_type###news_configid###biz_configid###ad_configid"""
      val key2 = s"""uid###hotoday###baidu###log_type###news_configid###biz_configid###ad_configid"""

      val fields: Array[String] = key1.split("###")
      val fields2 = key2.split("###")

      val Json = FeedBackUtil.makeUserFeedBackResult("201612051100", fields, (100, 20, 5))
      val Json2 = FeedBackUtil.makeUserFeedBackResult("201612051100", fields2, (100, 20, 5))

      Json mustEqual expectJson
      Json2 mustEqual expectJson2

    }

  }

}
