import com.inveno.news.common.UntilParser

/**
 * Created by admin on 2017/3/14.
 */
object TestHistory {
  def main(args: Array[String]) {
    val his = "(01011703131039164801000180292200##hotoday##h5_unknown##gif##an,droid_h5,(0,0,0,14))"
    println(UntilParser.parseHistory(his))
  }
}
