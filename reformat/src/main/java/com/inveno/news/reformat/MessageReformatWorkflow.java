package com.inveno.news.reformat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.panhongan.util.kafka.AbstractMessageProcessor;
import com.github.panhongan.util.kafka.MessageKafkaWriter;
import com.github.panhongan.util.thread.ControllableThread;
import com.inveno.news.reformat.convert.AbstractConverter;

public class MessageReformatWorkflow extends AbstractMessageProcessor {
	
	private static final Logger logger = LoggerFactory.getLogger(MessageReformatWorkflow.class);
	
	private static ReformatConfig config = ReformatConfig.getInstance();
	
	private static final int QUEUE_CAPACITY = 100 * 1000;
	
	private List<InnerMessageProcessor> inner_processors = new ArrayList<InnerMessageProcessor>();
	
	private List<MessageKafkaWriter> kafka_writers = new ArrayList<MessageKafkaWriter>();
	
	private List<ArrayBlockingQueue<Message>> queues = new ArrayList<ArrayBlockingQueue<Message>>();
	
	private AbstractConverter converter = null;
	
	private Random random = new Random();
	
	private int queue_index = 0;
	
	private int inner_queue_num = 2;
	
	private int inner_processors_per_queue = 2;
	
	public MessageReformatWorkflow(AbstractConverter converter) {
		this.converter = converter;
		
		try {
			inner_queue_num = config.getConfig().getInt("inner.queue.size");
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		}
		
		try {
			inner_processors_per_queue = config.getConfig().getInt("inner.processors.per.queue");
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		}
	}
	
	@Override
	public boolean init() {
		boolean is_ok = (converter != null);
		if (!is_ok) {
			logger.warn("Converter is null");
			return is_ok;
		}
		
		for (int i = 0; i < inner_queue_num; ++i) {
			// inner queue
			ArrayBlockingQueue<Message> queue = new ArrayBlockingQueue<Message>(QUEUE_CAPACITY);
			queues.add(queue);
			
			// kafka writer
			MessageKafkaWriter kafka_writer = new MessageKafkaWriter(config.getConfig().getString("dst.kafka.zk.list"),
					config.getConfig().getString("dst.kafka.broker.list"));
			is_ok = kafka_writer.init();
			if (!is_ok) {
				logger.warn("MessageKafkaWriter init failed");
				return is_ok;
			}
			kafka_writers.add(kafka_writer);
			
			// InnerMessageProcessor
			for (int j = 0; j < inner_processors_per_queue; ++j) {
				AbstractConverter converter_clone = (AbstractConverter)converter.clone();
				if (converter_clone == null || !converter_clone.init()) {
					logger.warn("converter clone failed");
					break;
				}
				
				int instance_id = i * inner_processors_per_queue + j;
				InnerMessageProcessor processor = new InnerMessageProcessor(converter_clone, 
						queue, kafka_writer);
				processor.setName("InnerMessageProcessor_" + instance_id);
				processor.setSleepInterval(2);
				if (processor.init()) {
					inner_processors.add(processor);
				} else {
					logger.warn("{} init failed", processor.getName());
					break;
				}
			}
		}
		
		is_ok = (inner_processors.size() == inner_queue_num * inner_processors_per_queue);
		return is_ok;
	}
	
	@Override
	public void uninit() {
		// wait for all data to be processed
		logger.info("wait queue data to be processed completely...");
		
		while (true) {
			boolean can_quit = true;
			
			for (ArrayBlockingQueue<Message> queue : queues) {
				if (!queue.isEmpty()) {
					logger.info("left queue data size : {}", queue.size());
					can_quit = false;
				}
			}
			
			if (can_quit) {
				break;
			} else {
				try {
					Thread.sleep(1 * 1000);
				} catch (Exception e) {
				}
			}
		}
		
		for (InnerMessageProcessor processor : inner_processors) {
			processor.uninit();
		}
		logger.info("all InnerMessageProcessors unit");
		
		for (MessageKafkaWriter kafka_writer : kafka_writers) {
			kafka_writer.uninit();
		}
		logger.info("all MessageKafkaWriters uninit");
	}

	@Override
	public Object processMessage(String topic, int partition_id, String message) {
		try {
			ArrayBlockingQueue<Message> queue = queues.get(queue_index);
			
			if (random.nextInt(inner_queue_num) == 0) {
				logger.info("queue_index : {}, queue_size : {}", queue_index, queue.size());
			}
			
			if (queue.size() == QUEUE_CAPACITY) {
				logger.warn("queue is full, need clear");
				queue.clear();
			}
			
			queue.put(new Message(topic, partition_id, message));
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		}
		
		queue_index = (++queue_index % inner_queue_num);
		
		return true;
	}
	
	@Override
	public Object processMessage(String topic, int partition_id, List<String> msg_list) {
		for (String message : msg_list) {
			this.processMessage(topic, partition_id, message);
		}
		
		return true;
	}
	
	private class Message {
		public String topic = null;
		
		public int partition_id = -1;
		
		public String message = null;
		
		public Message(String topic, int partition_id, String message) {
			this.topic = topic;
			this.partition_id = partition_id;
			this.message = message;
		}
	}
	
	public class InnerMessageProcessor extends ControllableThread {
		private AbstractConverter converter = null;
		
		private MessageKafkaWriter kafka_writer = null;
		
		private ArrayBlockingQueue<Message> queue = null;
		
		public InnerMessageProcessor(AbstractConverter abstractConveter, ArrayBlockingQueue<Message> queue, 
				MessageKafkaWriter kafka_writer) {
			this.converter = abstractConveter;
			this.queue = queue;
			this.kafka_writer = kafka_writer;
		}

		@Override
		public boolean init() {
			boolean is_ok = (converter != null && queue != null && kafka_writer != null);
			if (!is_ok) {
				logger.warn("Converter or queue is null");
				return is_ok;
			}
			
			is_ok = super.init();
			
			return is_ok;
		}

		@Override
		public void uninit() {
			super.uninit();
			
			if (converter != null) {
				converter.uninit();
			}
		}
		
		@Override
		protected void work() {
			Message msg = queue.poll();
			if (msg != null) {
				try {
					Map<String, List<String>> convert_results = converter.convert(msg.message);
					for (String dst_topic : convert_results.keySet()) {
						if (kafka_writer != null) {
							kafka_writer.processMessage(dst_topic, msg.partition_id, convert_results.get(dst_topic));
						}
					}
				} catch (Exception e) {
					logger.warn(e.getMessage(), e);
				}
			}
		}
	} // end class InnerMessageProcessor

}
