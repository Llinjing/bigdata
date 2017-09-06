import com.inveno.news.nearline.common.ReformatParser
import inveno.bigdata.common.TimeUtil

/**
 * Created by gaofeilu on 2017/2/24.
 */
object TestParser {

  def main(args: Array[String]) {
    //print(ReformatParser.isValidAction("share_facebook"))
    val line = "{\"aid\":\"64c1aefb0d245841\",\"api_ver\":\"2.0.0\",\"app_lan\":\"indonesian\",\"app_ver\":\"1.0.6.0.0.3\",\"brand\":\"Xiaomi\",\"event_id\":\"7\",\"event_time\":\"1489579278\",\"extend_event_extra\":{\"action_name\":\"share_facebook\",\"action_type\":\"2\",\"action_value\":{\"content_id\":\"1044204667\",\"from\":\"article_share\"}},\"gate_ip\":\"172.31.6.161\",\"language\":\"in_id\",\"log_time\":\"2017-03-15 12:01:20\",\"log_type\":\"report\",\"mcc\":\"IN\",\"mnc\":\"10\",\"model\":\"Redmi+4\",\"network\":\"3g\",\"osv\":\"6.0.1\",\"platform\":\"android\",\"product_id\":\"mata\",\"promotion\":\"360os2_m\",\"protocol\":\"https\",\"report_time\":\"1489579278\",\"seq\":\"13\",\"sid\":\"594270777\",\"tk\":\"c50ea9fac7427596f5f8906fcffb36f4\",\"uid\":\"01011701261056485101000036474700\",\"upack\":{\"ad_configid\":\"37\",\"biz_configid\":\"50\",\"news_configid\":\"124\"}}"
    println(ReformatParser.parse(line).mkString("##"))
    //val timestamp = TimeUtil.getCurrTimeIndex()

  }


}
