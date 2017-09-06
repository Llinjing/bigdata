import com.inveno.news.common.ReformatParser

/**
 * Created by admin on 2017/3/10.
 */
object TestParser {
  def main(args: Array[String]) {
    val line1 = "{\"api_ver\":\"h5_unknown\",\"app_lan\":\"spanish\",\"app_ver\":\"h5_unknown\",\"brand\":\"h5_unknown\",\"event_id\":\"7\",\"event_time\":\"1488991878\",\"extend_event_extra\":{\"action_name\":\"h5_share_click\",\"action_type\":\"2\",\"action_value\":{\"share_to\":\"h5_unknown\",\"content_type\":\"0x00000001\",\"content_id\":\"1042723428\",\"scenario\":\"0x010100\",\"fb_uid\":\"h5_unknown\",\"platform\":\"android_h5\",\"fb_gid\":\"h5_unknown\"}},\"gate_ip\":\"10.10.20.123\",\"log_time\":\"2017-03-09 00:51:18\",\"log_type\":\"report\",\"model\":\"h5_unknown\",\"network\":\"unknown\",\"osv\":\"\",\"platform\":\"android_h5\",\"product_id\":\"noticias\",\"promotion\":\"h5_unknown\",\"protocol\":\"https\",\"report_time\":\"1488991878387\",\"seq\":\"0\",\"sid\":\".733517775\",\"tk\":\"6ca6bf8717c07e04005d483ea749e1e2\",\"uid\":\"01011701061041175201000126949507\",\"upack\":{\"ad_configid\":\"unknown\",\"biz_configid\":\"unknown\",\"news_configid\":\"unknown\"}}"
    println(ReformatParser.parseShareFunnelArticle(line1).length)
    val line2 = "{\"api_ver\":\"h5_unknown\",\"app_lan\":\"hello,world\",\"app_ver\":\"h5_unknown\",\"brand\":\"h5_unknown\",\"event_id\":\"7\",\"event_time\":\"1488991917\",\"extend_event_extra\":{\"action_name\":\"h5_share_click\",\"action_type\":\"2\",\"action_value\":{\"share_to\":\"h5_unknown\",\"content_type\":\"0x00000001\",\"content_id\":\"1042723428\",\"scenario\":\"0x010100\",\"fb_uid\":\"h5_unknown\",\"platform\":\"android_h5\",\"fb_gid\":\"h5_unknown\"}},\"gate_ip\":\"10.10.20.123\",\"log_time\":\"2017-03-09 00:51:57\",\"log_type\":\"report\",\"model\":\"h5_unknown\",\"network\":\"unknown\",\"osv\":\"\",\"platform\":\"android_h5\",\"product_id\":\"noticias\",\"promotion\":\"h5_unknown\",\"protocol\":\"https\",\"report_time\":\"1488991917252\",\"seq\":\"0\",\"sid\":\".609494529\",\"tk\":\"eea0bb4dc590c7b92d91508e86f1797c\",\"uid\":\"01011611291850174801000000676401\",\"upack\":{\"ad_configid\":\"unknown\",\"biz_configid\":\"unknown\",\"news_configid\":\"unknown\"}}"
    println(ReformatParser.parseShareFunnelArticle(line2).mkString("##"))
  }
}
