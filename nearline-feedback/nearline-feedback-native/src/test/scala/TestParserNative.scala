import com.inveno.news.nearline.common.ReformatParser

/**
 * Created by gaofeilu on 2017/3/3.
 */
object TestParserNative {
  def main(args: Array[String]) {
    val line = "{\"api_ver\":\"1.1\",\"app_lan\":\"zh_cn\",\"app_ver\":\"3.5.0.oq\",\"article_request_extra\":{\"content_id\":\"70818761\",\"content_type\":\"news\",\"cpack\":{\"source\":\"中国青年网\",\"strategy\":\"mixed_insert_ali_special\"},\"display\":\"one_photo\",\"link_type\":\"webview\"},\"brand\":\"\",\"event_id\":\"0\",\"event_time\":\"1488523783\",\"gate_ip\":\"192.168.1.103\",\"log_time\":\"2017-03-03 14:49:43\",\"log_type\":\"request_n\",\"model\":\"Lovme-T12\",\"network\":\"unknown\",\"platform\":\"YunOS\",\"product_id\":\"ali\",\"promotion\":\"cloudcard\",\"scenario\":{\"channel\":\"0x00\",\"channel_desc\":\"foryou\",\"desc\":\"long_listpage\",\"position\":\"0x01\",\"position_desc\":\"ali_left_first_screen\",\"position_type\":\"0x01\",\"position_type_desc\":\"long_listpage\"},\"tk\":\"1b34c7fcce5e58a5341656e3de9b9123\",\"uid\":\"714E9E82C152235AE81BE6794EEF45A8\",\"upack\":{\"ad_configid\":\"unknown\",\"biz_configid\":\"unknown\",\"news_configid\":\"338\"}}"
    val str = "\"article_impression_extra\":{\"content_id\":\"70818761\",\"content_type\":\"news\",\"cpack\":{\"source\":\"中国青年网\",\"strategy\":\"mixed_insert_ali_special\"},\"display\":\"one_photo\",\"link_type\":\"webview\"},"
    println(ReformatParser.parseRequest(line))
    val line_imp = "{\"api_ver\":\"1.0.0\",\"app_lan\":\"zh_cn\",\"app_ver\":\"6.3.3.9\",\"article_impression_extra\":{\"content_id\":\"75026399\",\"content_type\":\"news\",\"cpack\":{},\"server_time\":\"1490004056\"},\"brand\":\"\",\"event_id\":\"2\",\"event_time\":\"1490004058\",\"gate_ip\":\"192.168.1.77\",\"log_time\":\"2017-03-20 18:01:16\",\"log_type\":\"report\",\"model\":\"msm8998\",\"network\":\"wifi\",\"osv\":\"\",\"platform\":\"yunos\",\"product_id\":\"ali\",\"promotion\":\"ali\",\"protocol\":\"https\",\"report_time\":\"1490004076\",\"scenario\":{\"channel\":\"0x00\",\"channel_desc\":\"foryou\",\"desc\":\"long_listpage\",\"position\":\"0x01\",\"position_desc\":\"ali_left_first_screen\",\"position_type\":\"0x01\",\"position_type_desc\":\"long_listpage\"},\"seq\":\"2\",\"sid\":\"2457182148\",\"tk\":\"28fe20ae6b493d9ca28f0fb8f4a3409b\",\"uid\":\"6E0A098050677136602333070247D442\",\"upack\":{\"ad_configid\":\"unknown\",\"biz_configid\":\"unknown\",\"news_configid\":\"unknown\"}}"
    println(ReformatParser.parseImpression(line_imp))
  }
}
