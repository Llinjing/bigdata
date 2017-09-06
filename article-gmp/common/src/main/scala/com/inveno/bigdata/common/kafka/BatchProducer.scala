package com.inveno.bigdata.common.kafka

import java.util.{ArrayList, List}
import com.github.panhongan.util.kafka.KafkaUtil
import kafka.producer.KeyedMessage
import org.slf4j.LoggerFactory
import com.inveno.bigdata.common.CommonConfig

class BatchProducer(zk_list: String, kafka_broker_list: String, kafka_batch_size: Int=2000, sync: Boolean=false) {

    private val logger = LoggerFactory.getLogger(this.getClass.getSimpleName)
    private val messages: List[KeyedMessage[String, String]] = new ArrayList[KeyedMessage[String, String]](kafka_batch_size)
    private val producer = KafkaUtil.createProducer(zk_list, kafka_broker_list, sync)

    private def send(): Unit = {
        try {
            producer.send(messages)
            messages.clear()
        } catch {
            case e: Exception => logger.warn("BatchProducer send messages exception ! => ", e.printStackTrace())
        }
    }
    
    def send(topic: String, msg: String): Unit = {
        messages.add(new KeyedMessage(topic, msg))
        if (!messages.isEmpty && messages.size() % kafka_batch_size == 0) {
            send()
        }
    }
    
   def close() = {
        if (!messages.isEmpty) {
            send()
        }
        producer.close
    }
}
