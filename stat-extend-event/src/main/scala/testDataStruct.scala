import java.util

import scala.collection.mutable
import scala.collection.mutable.HashMap
import scala.collection.mutable.LinkedList

/**
 * Created by dory on 2016/11/28.
 */
object testDataStruct {

  def main(args: Array[String]) {
    val map = new HashMap[String, Long]()
    val link_list = LinkedList("11","22","33","11")
    link_list.foreach(ele=>{
      map(ele) = map.get(ele).getOrElse(0L) + 1L
    })
    println(map)
    println(link_list.distinct.length)

  }
}
