package com.inveno.bigdata.kafka.sync;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.panhongan.util.StringUtil;
import com.github.panhongan.util.conf.Config;
import com.github.panhongan.util.control.Lifecycleable;
import com.github.panhongan.util.kafka.AbstractKafkaMessageHandler;
import com.github.panhongan.util.kafka.HighLevelConsumerGroup;
import com.github.panhongan.util.kafka.MessageKafkaWriter;

public class KafkaSyncService implements Lifecycleable {
	
	private static final Logger logger = LoggerFactory.getLogger(KafkaSyncService.class);
	
	private static final String CLASS_NAME = KafkaSyncService.class.getSimpleName();
	
	private static KafkaSyncConfig config = KafkaSyncConfig.getInstance();
	
	private MessageKafkaWriter kafka_writer = null;
	
	private List<AbstractKafkaMessageHandler> processors = new ArrayList<AbstractKafkaMessageHandler>();
	
	private HighLevelConsumerGroup group = null;
	
	@Override
	public boolean init() {
		boolean is_ok = false;		
		Config conf = config.getConfig();
		
		String dst_topic = conf.getString("dst.kafka.topic");
		is_ok = !StringUtil.isEmpty(dst_topic);
		if (!is_ok) {
			logger.warn("invalid config : dst.kafka.topic");
			return is_ok;
		}
		
		String send_failed_path = conf.getString("send.failed.local.data.dir", "../data/kafka-send-failed/" + dst_topic);

		// producer
		Config producer_config = new Config();
		producer_config.addProperty("bootstrap.servers", conf.getString("dst.kafka.broker.list"));
		producer_config.addProperty("metadata.broker.list", conf.getString("dst.kafka.broker.list"));
		producer_config.addProperty("zookeeper.connect", conf.getString("dst.kafka.zk.list"));
		kafka_writer = new MessageKafkaWriter(producer_config, dst_topic, send_failed_path);
		is_ok = kafka_writer.init();
		if (!is_ok) {
			logger.warn("KafkaWriter init failed");
			return is_ok;
		}
		
		String topic_partition = conf.getString("src.kafka.topic.partition");
		if (topic_partition != null) {
			String [] arr = topic_partition.split(":");
			if (arr != null && arr.length == 2) {
				String src_topic = arr[0];
				int partitions = Integer.valueOf(arr[1]);
				
				for (int i = 0; i < partitions; ++i) {
					MessageWorkflow workflow = new MessageWorkflow(kafka_writer);
					workflow.setName(workflow.getClass().getSimpleName() + "_" + src_topic + "_" + i);
					if (workflow.init()) {
						processors.add(workflow);
					} else {
						logger.warn(workflow.getName() + " init failed");
						return false;
					}
				}
				
				is_ok = (processors.size() == partitions);
				if (!is_ok) {
					logger.warn("init MessageWorkflow failed");
					return false;
				}
				
				group = new HighLevelConsumerGroup(conf.getString("src.kafka.zk.list"), 
						conf.getString("src.kafka.consumer.group"),
						src_topic, partitions,
						Boolean.valueOf(conf.getString("src.kafka.consumer.restart.offset.largest")), 
						processors);
				is_ok = group.init();
				if (is_ok) {
					logger.info("HighLevelConsumerGroup init ok");
				} else {
					logger.warn("HighLevelConsumerGroup init failed");
				}
			}
		} else {
			logger.warn("property not exist : {}", "src.kafka.topic.partition");
		}

		return is_ok;
	}
	
	@Override
	public void uninit() {
		if (group != null) {
			group.uninit();
			logger.info("HighLevelConsumerGroup uninit");
		}
		
		for (AbstractKafkaMessageHandler processor : processors) {
			processor.uninit();
			logger.info("MessageProcessor uninit : {}", StringUtil.toString(processor.getName()));
		}
		
		if (kafka_writer != null) {
			kafka_writer.uninit();
			logger.info("Kafka writer uninit");
		}
	}
	
	public static void usage() {
		System.out.println(CLASS_NAME + " <conf_file>");
	}
	
	public static void main(String [] args) {
		if (args.length != 1) {
			usage();
			return;
		}
		
		// config
		String conf_file = args[0];
		KafkaSyncConfig config = KafkaSyncConfig.getInstance();
		if (!config.parse(conf_file)) {
			logger.warn("parse conf file failed : {}", conf_file);
			return;
		}
		logger.info(config.toString());
		
		if (!config.isValid()) {
			logger.warn("config is invalid");
			return;
		}
		
		KafkaSyncService service = new KafkaSyncService();
		if (service.init()) {
			logger.info("{} init ok", CLASS_NAME);
			Runtime.getRuntime().addShutdownHook(new KafkaSyncServiceShutdownHook(service));
		} else {
			logger.warn("{} init failed", CLASS_NAME);
			service.uninit();
		}
	}

}
