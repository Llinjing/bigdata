import com.inveno.news.common.{CombineUtil, ReformatParser}

//import scala.collection.immutable.HashMap

/**
 * Created by admin on 2017/3/24.
 */
object Test {
  def main(args: Array[String]) {
    val line = "{\"aid\":\"7846ff49ea0ddb30\",\"api_ver\":\"2.0.0\",\"app_lan\":\"spanish\",\"app_ver\":\"2.4.2.0.0.3\",\"article_impression_extra\":{\"content_id\":\"Google_000001\",\"content_type\":\"advertisement_third_party\",\"cpack\":{\"ad_source\":\"Google\",\"source\":\"unknown\",\"strategy\":\"push\"},\"server_time\":\"1490320730\"},\"brand\":\"Lenovo\",\"event_id\":\"2\",\"event_time\":\"1490320748\",\"gate_ip\":\"10.10.20.123\",\"imei\":\"862569038876190\",\"language\":\"es_us\",\"log_time\":\"2017-03-24 09:59:34\",\"log_type\":\"report\",\"mcc\":\"AR\",\"mnc\":\"103\",\"model\":\"Lenovo A2016b30\",\"network\":\"4g\",\"osv\":\"6.0\",\"platform\":\"android\",\"product_id\":\"noticiasboomcolombia\",\"promotion\":\"gp\",\"protocol\":\"https\",\"report_time\":\"1490320773\",\"scenario\":{\"channel\":\"0xff\",\"channel_desc\":\"unknown\",\"desc\":\"other\",\"position\":\"0x0d\",\"position_desc\":\"detail_textbottom\",\"position_type\":\"0x07\",\"position_type_desc\":\"other\"},\"seq\":\"5\",\"sid\":\"315659518\",\"tk\":\"2444b6ef3b5fcf6386306050e4acb9b8\",\"uid\":\"01011703221010035501000257941206\",\"upack\":{\"ad_configid\":\"46\",\"biz_configid\":\"50\",\"news_configid\":\"161\"}}"
    val imp_arr = ReformatParser.parseImpression(line)
    println(imp_arr.length)
//    val str_dwell = "{\"aid\":\"a863adc1186a756c\",\"api_ver\":\"2.0.0\",\"app_lan\":\"indonesian\",\"app_ver\":\"1.2.0.0.0.3\",\"article_dwelltime_extra\":{\"content_id\":\"1044405718\",\"content_type\":\"news\",\"cpack\":{\"source\":\"Babe\",\"strategy\":\"relative_recommendation\"},\"dwelltime\":\"10\",\"play_time\":\"1\",\"refer\":{\"content_id\":\"1044405718\",\"content_type\":\"news\"},\"view_mode\":\"1\"},\"brand\":\"Xiaomi\",\"event_id\":\"4\",\"event_time\":\"1490348962\",\"gate_ip\":\"172.31.6.161\",\"imei\":\"869161023368202\",\"language\":\"in_id\",\"log_time\":\"2017-03-24 09:49:43\",\"log_type\":\"report\",\"mcc\":\"ID\",\"mnc\":\"10\",\"model\":\"MI+5\",\"network\":\"3g\",\"osv\":\"6.0\",\"platform\":\"android\",\"product_id\":\"mata\",\"promotion\":\"gp\",\"protocol\":\"https\",\"report_time\":\"1490348973\",\"scenario\":{\"channel\":\"0xff\",\"channel_desc\":\"relative_recommendation\",\"desc\":\"relevant_recommendation\",\"position\":\"0x0c\",\"position_desc\":\"relevant_recommendation\",\"position_type\":\"0x05\",\"position_type_desc\":\"relevant_recommendation\"},\"seq\":\"250\",\"sid\":\"990573378\",\"tk\":\"c78b9c63fb3ad5a6149c30f541939cfd\",\"uid\":\"01011701071134285101000048000803\",\"upack\":{\"ad_configid\":\"38\",\"biz_configid\":\"50\",\"news_configid\":\"124\"}}"
//    println(ReformatParser.parseDwelltime(str_dwell).mkString("##"))

    val str_push="1045456533##noticiasboom"
    val arr = ReformatParser.parsePush(str_push)
    println(arr.apply(1))
    var list = new java.util.ArrayList[String]()
    var help_arr = new Array[String](32);

    CombineUtil.combine(imp_arr,help_arr, 0, imp_arr.length - 1,  list)
    var ret: List[String] = List()
    println(list.size())
    for(i<-0 until list.size()){
      val info = list.get(i).split("##")
      println(info.tail.mkString("##"))
      ret = (info.tail.mkString("##"))::ret
      //println("ret len " + ret.length)
    }
    println(ret.length)
   /* var ret: List[(String,Long)] = List()
    val test = ("hello", 2)*/

  }
}
