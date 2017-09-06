import inveno.bigdata.common.TimeUtil

/**
 * Created by gaofeilu on 2017/3/1.
 */
object TestTimes {

  def main(args: Array[String]) {
    val times = TimeUtil.getCurrCharTimeIndex()
    //val time2 = TimeUtil.getPreTimeIndex("20113",10)

    println(times)
  }
}
