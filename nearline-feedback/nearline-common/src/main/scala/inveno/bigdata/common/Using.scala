package inveno.bigdata.common

import inveno.bigdata.log.LogSupport
import com.alibaba.fastjson.{JSON, JSONObject}

trait Using extends LogSupport {

  protected def usingJson(op: JSONObject => Unit): Unit = {
    val json = new JSONObject
    try {
      op(json)
    } catch {
      case e: Exception => log.error(s"${this.getClass.getSimpleName}: => failed to get json results !", e.printStackTrace())
    }
  }

  protected def parseJson(line: String)(op: JSONObject => Unit): Unit = {
    try {
      val json = JSON.parseObject(line)
      op(json)
    } catch {
      case ex: Exception => log.error(s"${this.getClass.getSimpleName}: => failed to parse Json ! [INFO] => $line" + ex.printStackTrace())
    }
  }

}
