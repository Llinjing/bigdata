package com.inveno.bigdata.common

import org.apache.spark.streaming.kafka.OffsetRange
import org.apache.zookeeper.ZooKeeper
import org.apache.log4j.Logger
import com.github.panhongan.util.zookeeper.ZKUtil

object ZooKeeperUtil {
  
    private val logger = Logger.getLogger(ZooKeeperUtil.getClass)
    val INVALID_OFFSET = -1
    
    def writeOffset(zk_list : String,
        base_path : String, 
        group : String, 
        offset : OffsetRange) : Boolean = {
            var zk : ZooKeeper = null
            var ret : Boolean = false
        
            try {
                zk = ZKUtil.connectZK(zk_list, 30000, null)
                if (zk != null) {
                    var offset_path = base_path + "/" + group + "/" + offset.topic + "_" + offset.partition
                    val value = offset.fromOffset + "_" + offset.untilOffset
                    
                    if (zk.exists(offset_path, false) == null) {
                        ret = ZKUtil.createNodeRecursively(zk, offset_path, true)
                        if (!ret) {
                            ret
                        }
                    }                    
                    zk.setData(offset_path, value.getBytes(), -1)
                    ret = true
                }
            } catch {
                case e : Exception => {
                    logger.warn(e.getMessage)
                    ret = false
                }
            } finally {
                ZKUtil.closeZK(zk)
            }
            ret
    }
      
    def readOffset(zk_list : String,
        base_path : String,
        group : String,
        topic : String,
        partition_num : Int) : List[OffsetRange] = {
        var offset = List[OffsetRange]()
        var zk : ZooKeeper = null
        
        try {
            zk = ZKUtil.connectZK(zk_list, 30000, null)
            
            var partition = 0
            while (partition < partition_num) {
                val offset_path = base_path + "/" + group + "/" + topic + "_" + partition
              
                if (zk.exists(offset_path, false) != null) {
                    var value = new String(zk.getData(offset_path, false, null))
                    var arr = value.split("_")
                    if (arr != null && arr.length == 2) {
                        offset ::= OffsetRange.create(topic, partition, arr(0).toLong, arr(1).toLong)
                    }
                }
                
                partition += 1
            }
        } catch {
            case e : Exception => logger.warn(e.getMessage)
        } finally {
            ZKUtil.closeZK(zk)
        }
        offset
    }
    
    def writeValue(zk_list : String, path : String, value : String) : Boolean = {
        var zk : ZooKeeper = null
        var ret : Boolean = false
    
        try {
            zk = ZKUtil.connectZK(zk_list, 30000, null)
            if (zk != null) {
                if (zk.exists(path, false) == null) {
                    ret = ZKUtil.createNodeRecursively(zk, path, true)
                    if (!ret) {
                        ret
                    }
                }                    
                zk.setData(path, value.getBytes(), -1)
                ret = true
            }
        } catch {
            case e : Exception => {
                logger.warn(e.getMessage)
                ret = false
            }
        } finally {
            ZKUtil.closeZK(zk)
        }
        ret
    }
}