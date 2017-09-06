import com.inveno.news.common.{FourEmotionUtil, ReformatParse}

/**
 * Created by dory on 2016/12/30.
 */
object TestParser {
  def main(args: Array[String]) {
    val str_test = "{\"ballot\":{\"Bored\":0,\"Like\":0,\"Angry\":1,\"Sad\":0},\"uid\":\"01011611221010585401000138056906\",\"content_id\":1030279252,\"log_ip\":\"10.10.20.115\",\"product_id\":\"noticiasboomchile\",\"language\":\"Spanish\",\"timestamp\":1482199202}"
    val Array(product_id, language,emotion, uid) = ReformatParse.parseFourEmotion(str_test)
    val ret = ReformatParse.parseFourEmotion(str_test)
    val ret3 = ReformatParse.parseEmotionArticle(str_test).mkString("##")
    println(ret3)
    val profile = "{\"app\":\"noticiasboom\",\"app_lan\":\"unknown\",\"app_ver\":\"1.0.6.0.0.3\",\"event_time\":\"2016-11-23 08:00:38\",\"gate_ip\":\"172.31.12.139\",\"log_type\":\"profile\",\"model\":\"pm\",\"network\":\"\",\"osv\":\"\",\"product_id\":\"hotoday\",\"promotion\":\"gp\",\"protocol\":\"https\",\"uid\":\"01011611230800385301000146327909\"}"
    println(ReformatParse.parseProfile(profile))

    println(ReformatParse.isNull(ret))
    println(ret.mkString("##"))
    val ret2 = FourEmotionUtil.makeCombineKey(uid,product_id,language,emotion)
    ret2.foreach(x=>println(x))
  }

}
