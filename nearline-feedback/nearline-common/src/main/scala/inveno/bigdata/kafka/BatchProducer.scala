package inveno.bigdata.kafka

import java.util.{ArrayList, List}

import inveno.bigdata.log.LogSupport
import com.github.panhongan.util.kafka.KafkaUtil
import kafka.producer.KeyedMessage

class BatchProducer(zk_list: String, brokers: String, batch_size: Int, sync: Boolean = false) extends LogSupport {

  protected val batchSize = batch_size

  private val producer = KafkaUtil.createProducer(zk_list, brokers, sync)

  private val messages: List[KeyedMessage[String, String]] = new ArrayList[KeyedMessage[String, String]](batchSize)

  protected def send(): Unit = {
    try {
      producer.send(messages)
      messages.clear()
    } catch {
      case e: Exception => log.warn("BatchProducer send messages exception ! => ", e.printStackTrace())
    }
  }

  def send(topic: String, msg: String): Unit = {
    messages.add(new KeyedMessage(topic, msg))
    if (!messages.isEmpty && messages.size() % batchSize == 0) {
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
