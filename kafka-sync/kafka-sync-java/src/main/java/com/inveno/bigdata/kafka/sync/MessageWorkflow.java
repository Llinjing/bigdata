package com.inveno.bigdata.kafka.sync;

import java.util.List;

import com.github.panhongan.util.kafka.AbstractKafkaMessageHandler;
import com.github.panhongan.util.kafka.MessageKafkaWriter;

public class MessageWorkflow extends AbstractKafkaMessageHandler {

	private MessageKafkaWriter kafka_writer = null;

	public MessageWorkflow(MessageKafkaWriter kafka_writer) {
		this.kafka_writer = kafka_writer;
	}

	@Override
	public boolean init() {
		return (kafka_writer != null);
	}

	@Override
	public void uninit() {
	}

	@Override
	public Object processMessage(String topic, int partition_id, String message) {
		kafka_writer.processMessage(topic, partition_id, message);
		return true;
	}

	@Override
	public Object processMessage(String topic, int partition_id, List<String> msg_list) {
		kafka_writer.processMessage(topic, partition_id, msg_list);
		return true;
	}

}
