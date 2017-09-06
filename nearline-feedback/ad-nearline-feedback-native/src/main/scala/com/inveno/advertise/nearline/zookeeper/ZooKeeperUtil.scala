package com.inveno.advertise.nearline.zookeeper

import com.github.panhongan.util.zookeeper.ZKUtil
import com.inveno.advertise.nearline.log.LogSupport
import org.apache.spark.streaming.kafka.OffsetRange
import org.apache.zookeeper.ZooKeeper


object ZooKeeperUtil extends LogSupport {

  def usingZooKeeper(zk_hosts: String)(op: ZooKeeper => Unit): Unit = {
    val zk = ZKUtil.connectZK(zk_hosts, 30000, null)
    try {
      op(zk)
    } catch {
      case e: Exception => log.error(s"ZooKeeper option failed ！zookeeper hosts => $zk_hosts" + e.printStackTrace())
    } finally {
      ZKUtil.closeZK(zk)
    }
  }

  def writeOffset(zk_hosts: String,
                  base_path: String,
                  group: String,
                  offset: OffsetRange): Boolean = {
    var ret = false
    usingZooKeeper(zk_hosts) {
      zk =>
        if (zk != null) {
          val offset_path = base_path + "/" + group + "/" + offset.topic + "_" + offset.partition
          val value = offset.fromOffset + "_" + offset.untilOffset
          if (zk.exists(offset_path, false) == null) ZKUtil.createNodeRecursively(zk, offset_path, true)
          zk.setData(offset_path, value.getBytes, -1)
          ret = true
        }
    }
    ret
  }

  def readOffset(zk_hosts: String,
                 base_path: String,
                 group: String,
                 topic: String,
                 partition_num: Int): List[OffsetRange] = {
    var offset = List[OffsetRange]()
    usingZooKeeper(zk_hosts) {
      zk =>
        for (partition <- 0 until partition_num) {
          val offset_path = base_path + "/" + group + "/" + topic + "_" + partition

          if (zk.exists(offset_path, false) != null) {
            val value = new String(zk.getData(offset_path, false, null))
            val arr = value.split("_")
            if (arr != null && arr.length == 2) {
              offset ::= OffsetRange.create(topic, partition, arr(0).toLong, arr(1).toLong)
            }
          }
        }
    }
    offset
  }

  def writeValue(zk_hosts: String, path: String, value: String): Boolean = {
    var ret = false
    usingZooKeeper(zk_hosts) {
      zk =>
        if (zk != null) {
          if (zk.exists(path, false) == null) {
            ZKUtil.createNodeRecursively(zk, path, true)
          }
          zk.setData(path, value.getBytes, -1)
          ret = true
        }
    }
    ret
  }

}
