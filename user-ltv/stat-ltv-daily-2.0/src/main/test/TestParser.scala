import com.inveno.news.common.ReformatParser

/**
 * Created by dory on 2016/12/30.
 */
object TestParser {
  def main(args: Array[String]) {
    val imp_test = "{\"aid\":\"8d5dc3f648e1dcbc\",\"api_ver\":\"2.0.0\",\"app_lan\":\"spanish\",\"app_ver\":\"1.0.8.0.0.3\",\"article_impression_extra\":{\"content_id\":\"1026334552\",\"content_type\":\"advertisement_hard\",\"cpack\":{\"strategy\":\"push\"},\"server_time\":\"1479564003\",\"view_mode\":\"1\"},\"brand\":\"TCT\",\"event_id\":\"2\",\"event_time\":\"1479600035\",\"gate_ip\":\"172.31.22.87\",\"imei\":\"014183008788984\",\"language\":\"es_mx\",\"log_time\":\"2016-11-19 23:59:54\",\"log_type\":\"report\",\"mcc\":\"MX\",\"mnc\":\"020\",\"model\":\"ONE TOUCH 4033A\",\"network\":\"wifi\",\"osv\":\"4.2.2\",\"platform\":\"android\",\"product_id\":\"noticias\",\"promotion\":\"gp\",\"protocol\":\"https\",\"report_time\":\"1479600035\",\"scenario\":{\"channel\":\"0xff\",\"channel_desc\":\"push\",\"desc\":\"push\",\"position\":\"0x08\",\"position_desc\":\"push\",\"position_type\":\"0x03\",\"position_type_desc\":\"push\"},\"seq\":\"4\",\"sid\":\"131789234\",\"tk\":\"2ba8d2302eb2d2dc6f357f1d01cb68d9\",\"uid\":\"01011611181143305201000104298602\"}"
    val imp_ret = ReformatParser.parseImp(imp_test).mkString("##")
    println(imp_ret)

    val click_test = "{\"aid\":\"f1656f146185e6fd\",\"api_ver\":\"2.0.0\",\"app_lan\":\"spanish\",\"app_ver\":\"1.0.8.0.0.3\",\"article_click_extra\":{\"click_type\":\"2\",\"content_id\":\"1026262230\",\"content_type\":\"news\",\"cpack\":{\"strategy\":\"push\"},\"view_mode\":\"1\"},\"brand\":\"lge\",\"event_id\":\"3\",\"event_time\":\"1479599993\",\"gate_ip\":\"172.31.3.37\",\"imei\":\"354597073550987\",\"language\":\"es_mx\",\"log_time\":\"2016-11-19 23:59:48\",\"log_type\":\"report\",\"mcc\":\"MX\",\"mnc\":\"020\",\"model\":\"LG-X180g\",\"network\":\"wifi\",\"osv\":\"5.1\",\"platform\":\"android\",\"product_id\":\"noticias\",\"promotion\":\"gp\",\"protocol\":\"https\",\"report_time\":\"1479599993\",\"scenario\":{\"channel\":\"0xff\",\"channel_desc\":\"push\",\"desc\":\"push\",\"position\":\"0x08\",\"position_desc\":\"push\",\"position_type\":\"0x03\",\"position_type_desc\":\"push\"},\"seq\":\"2\",\"sid\":\"846747409\",\"tk\":\"04ec2b6a6e96c2d0941553a2697af82f\",\"uid\":\"01011609241037175201000006854706\",\"upack\":{\"ad_configid\":\"24\",\"biz_configid\":\"50\",\"news_configid\":\"99\"}}"
    val click_ret = ReformatParser.parseClick(click_test).mkString("#")
    println(click_ret)

    val list_time = "{\"aid\":\"c1dac4371aff3908\",\"api_ver\":\"2.0.0\",\"app_lan\":\"spanish\",\"app_ver\":\"1.0.9.0.0.3\",\"brand\":\"ZTE\",\"event_id\":\"1\",\"event_time\":\"1480118345\",\"gate_ip\":\"172.31.6.161\",\"imei\":\"868504021100773\",\"language\":\"es_us\",\"listpage_dwelltime_extra\":{\"dwelltime\":\"\",\"view_mode\":\"1\"},\"log_time\":\"2016-11-25 23:59:14\",\"log_type\":\"report\",\"mcc\":\"MX\",\"model\":\"Z959\",\"network\":\"wifi\",\"osv\":\"5.1.1\",\"platform\":\"android\",\"product_id\":\"noticias\",\"promotion\":\"gp\",\"protocol\":\"https\",\"report_time\":\"1480118348\",\"scenario\":{\"channel\":\"0x00\",\"channel_desc\":\"foryou\",\"desc\":\"long_listpage\",\"position\":\"0x01\",\"position_desc\":\"long_listpage\",\"position_type\":\"0x01\",\"position_type_desc\":\"long_listpage\"},\"seq\":\"11\",\"sid\":\"884916758\",\"tk\":\"842c443b4b085c801de7ba4372b2fcdd\",\"uid\":\"01011611192248455201000114428508\",\"upack\":{\"ad_configid\":\"24\",\"biz_configid\":\"50\",\"news_configid\":\"103\"}}"
    val list_ret = ReformatParser.parseListDwelltime(list_time).mkString("#")
    println(list_ret)


    val detail_time = "{\"aid\":\"982a26642fa0e609\",\"api_ver\":\"2.0.0\",\"app_lan\":\"spanish\",\"app_ver\":\"1.0.9.0.0.3\",\"article_dwelltime_extra\":{\"content_id\":\"1027303987\",\"content_type\":\"news\",\"cpack\":{\"strategy\":\"push\"},\"dwelltime\":\"97\",\"play_time\":\"1\",\"view_mode\":\"1\"},\"brand\":\"motorola\",\"event_id\":\"4\",\"event_time\":\"1480118241\",\"gate_ip\":\"172.31.6.161\",\"language\":\"es_us\",\"log_time\":\"2016-11-25 23:59:11\",\"log_type\":\"report\",\"mcc\":\"MX\",\"mnc\":\"020\",\"model\":\"Moto G (4)\",\"network\":\"wifi\",\"osv\":\"6.0.1\",\"platform\":\"android\",\"product_id\":\"noticias\",\"promotion\":\"gp\",\"protocol\":\"https\",\"report_time\":\"1480118337\",\"scenario\":{\"channel\":\"0xff\",\"channel_desc\":\"push\",\"desc\":\"push\",\"position\":\"0x08\",\"position_desc\":\"push\",\"position_type\":\"0x03\",\"position_type_desc\":\"push\"},\"seq\":\"18\",\"sid\":\"463231466\",\"tk\":\"d3d0b6a8611f6050338d0e70c5753a38\",\"uid\":\"01011611131225405201000068698806\",\"upack\":{\"ad_configid\":\"24\",\"biz_configid\":\"50\",\"news_configid\":\"105\"}}"
    val detail_ret = ReformatParser.parseDetailDwelltime(detail_time).mkString("#")
    println(detail_ret)
  }
}
