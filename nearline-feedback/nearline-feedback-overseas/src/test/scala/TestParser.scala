import com.inveno.news.nearline.common.{OverseasJson, ReformatParser}

/**
 * Created by gaofeilu on 2017/2/27.
 */
object TestParser {

  def main(args: Array[String]) {
    val str = "{\"aid\":\"5f445d0aeb6761e6\",\"api_ver\":\"2.0.0\",\"app_lan\":\"indonesian\",\"app_ver\":\"\",\"article_impression_extra\":{\"content_id\":\"1041587110\",\"content_type\":\"news\",\"cpack\":{\"source\":\"unknown\",\"strategy\":\"force_insert\"},\"display\":\"one_photo\",\"link_type\":\"native\"},\"article_request_extra\":{\"content_id\":\"1041587110\",\"content_type\":\"news\",\"cpack\":{\"source\":\"unknown\",\"strategy\":\"force_insert\"},\"display\":\"one_photo\",\"link_type\":\"native\"},\"brand\":\"asus\",\"event_id\":\"0\",\"event_time\":\"1488188944\",\"gate_ip\":\"172.31.22.87\",\"imei\":\"357877065361446\",\"language\":\"in_id\",\"log_time\":\"2017-02-27 09:49:09\",\"log_type\":\"request_n\",\"mcc\":\"ID\",\"mnc\":\"89\",\"model\":\"ASUS_Z007\",\"network\":\"3g\",\"osv\":\"4.4.2\",\"platform\":\"android\",\"product_id\":\"mata\",\"promotion\":\"gp\",\"scenario\":{\"channel\":\"0x00\",\"channel_desc\":\"foryou\",\"desc\":\"long_listpage\",\"position\":\"0x01\",\"position_desc\":\"long_listpage\",\"position_type\":\"0x01\",\"position_type_desc\":\"long_listpage\"},\"tk\":\"838fef6c82ce7df7382c8f4777002689\",\"uid\":\"01011612271619595101000024211302\",\"upack\":{\"ad_configid\":\"23\",\"biz_configid\":\"50\",\"news_configid\":\"124\"}}"
    val line: OverseasJson = ReformatParser.parseOverSeasRequest(str)
    println(line)
    val str2 = "{\"aid\":\"4c0708a8d1539cb\",\"api_ver\":\"1.0.0\",\"app_lan\":\"hindi\",\"app_ver\":\"2.1.4.0.0.2\",\"article_impression_extra\":{\"content_id\":\"1045091966\",\"content_type\":\"news\",\"cpack\":{\"source\":\"jagran\",\"strategy\":\"relative_recommendation\"},\"refer\":{\"content_id\":\"1045197432\",\"content_type\":\"unknown\"},\"server_time\":\"1490165911\"},\"brand\":\"Micromax\",\"event_id\":\"2\",\"event_time\":\"1490166109\",\"gate_ip\":\"172.31.22.87\",\"imei\":\"911521000331200\",\"language\":\"en_gb\",\"log_time\":\"2017-03-22 07:02:17\",\"log_type\":\"report\",\"mcc\":\"IN\",\"mnc\":\"876\",\"model\":\"Micromax Q4201\",\"network\":\"4g\",\"osv\":\"6.0\",\"platform\":\"android\",\"product_id\":\"hotoday\",\"promotion\":\"mmx_h\",\"protocol\":\"https\",\"report_time\":\"1490166136\",\"scenario\":{\"channel\":\"0xff\",\"channel_desc\":\"relative_recommendation\",\"desc\":\"relevant_recommendation\",\"position\":\"0x0c\",\"position_desc\":\"relevant_recommendation\",\"position_type\":\"0x05\",\"position_type_desc\":\"relevant_recommendation\"},\"seq\":\"0\",\"sid\":\"111284447\",\"tk\":\"868a1da5b4f9e0785287e260a31dcd0c\",\"uid\":\"01011612081624374801000010239105\",\"upack\":{\"ad_configid\":\"33\",\"biz_configid\":\"50\",\"news_configid\":\"128\"}}"
  }


}
