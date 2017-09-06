import com.inveno.news.common.{CombineUtil, ReformatParser}

/**
 * Created by gaofeilu on 2017/3/7.
 */
object Test {

  def main(args: Array[String]) {
    val arr = Array("head","second","third")
    println(arr.head)
    println(arr.tail.mkString("##"))

    val str_user = "{\"api_ver\":\"undefined\",\"app_lan\":\"indonesian\",\"app_ver\":\"1.2.1.0.0.3\",\"brand\":\"android\",\"event_id\":\"7\",\"event_time\":\"1488941350\",\"extend_event_extra\":{\"action_name\":\"h5_page_request_call\",\"action_type\":\"2\",\"action_value\":{\"share_to\":\"h5_unknown\",\"content_type\":\"0x00000001\",\"content_id\":\"1043061576\",\"scenario\":\"0x0409ff\",\"plateform\":\"h5_unknown\",\"fb_uid\":\"h5_unknown\",\"fb_gid\":\"h5_unknown\"}},\"gate_ip\":\"172.31.3.37\",\"log_time\":\"2017-03-08 02:49:05\",\"log_type\":\"report\",\"model\":\"other\",\"network\":\"3g\",\"osv\":\"\",\"platform\":\"android\",\"product_id\":\"mata\",\"promotion\":\"gp\",\"protocol\":\"https\",\"report_time\":\"1488941349\",\"seq\":\"1\",\"sid\":\"2873805182\",\"tk\":\"71a039fbb471d7d8415e537800ad7fd2\",\"uid\":\"01011701310829275101000059445400\",\"upack\":{\"ad_configid\":\"unknown\",\"biz_configid\":\"unknown\",\"news_configid\":\"unknown\"}}"
    var list = new java.util.ArrayList[String]()
    var help_arr = new Array[String](32)
    val info = ReformatParser.parseShareFunnelUser(str_user)
    list.clear()
    CombineUtil.combine(info, help_arr, 0, info.length - 1, list)
    for(i <- 0 until list.size()){
      println(list.get(i))
    }
    println(info(7))
    var arr_test: Array[String] = Array()
    println("arr_test is " + arr_test.length)
    val key = ""
    println("key is " + key.length)
  }

}
