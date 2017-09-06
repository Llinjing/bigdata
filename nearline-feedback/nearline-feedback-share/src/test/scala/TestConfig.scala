import com.inveno.news.nearline.config.ShareConfig

/**
 * Created by admin on 2017/3/15.
 */
object TestConfig {
  def main(args: Array[String]) {
    println(ShareConfig.mysql_server)
    println(ShareConfig.mysql_pwd)
    println(ShareConfig.mysql_port)
  }
}
