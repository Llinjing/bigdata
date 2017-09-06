package com.inveno.bigdata.lockscreen;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.panhongan.util.StringUtil;
import com.github.panhongan.util.conf.Config;
import com.github.panhongan.util.control.Lifecycleable;
import com.github.panhongan.util.kafka.AbstractKafkaMessageProcessor;
import com.github.panhongan.util.kafka.HighLevelConsumerGroup;

public class LockScreenService implements Lifecycleable {
	
	private static final String CLASS_NAME = LockScreenService.class.getSimpleName();
	
	private static final Logger logger = LoggerFactory.getLogger(LockScreenService.class);
	
	private static LockScreenConfig lock_screen_config = LockScreenConfig.getInstance();
	
	private List<AbstractKafkaMessageProcessor> processors = new ArrayList<AbstractKafkaMessageProcessor>();
	
	private HighLevelConsumerGroup group = null;
	
	@Override
	public boolean init() {
		boolean is_ok = false;
		
		Config config = lock_screen_config.getConfig();
		
		try {
			// topic & partitions
			String [] arr = config.getString("kafka.topic.partition").split(":");
			String topic = arr[0];
			int partitions = Integer.valueOf(arr[1]).intValue();
			
			for (int i = 0; i < partitions; ++i) {
				LockScreenImpressionMessageProcessor processor = new LockScreenImpressionMessageProcessor();
				processor.setName(processor.getClass().getSimpleName() + "_" + i);
				if (processor.init()) {
					processors.add(processor);
				} else {
					logger.warn("LockScreenImpressionMessageProcessor init failed");
				}
			}
			
			is_ok = (processors.size() == partitions);
			if (is_ok) {
				group = new HighLevelConsumerGroup(config.getString("kafka.zk.list"), 
						config.getString("kafka.consumer.group"),
						topic, partitions, 
						Boolean.valueOf(config.getString("kafka.consumer.group.restart.offset.largest")), 
						processors);
				if (group.init()) {
					logger.info("HighLevelConsumerGroup init ok");
					is_ok = true;
				} else {
					logger.warn("HighLevelConsumerGroup init failed");
				}
			}
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		}
		
		return is_ok;
	}
	
	@Override
	public void uninit() {
		if (group != null) {
			group.uninit();
			logger.info("HighLevelConsumerGroup uninit");
		}
		
		for (AbstractKafkaMessageProcessor processor : processors) {
			processor.uninit();
			logger.info("MessageProcessor uninit : {}", StringUtil.toString(processor.getName()));
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
		
		// config file
		String conf_file = args[0];
		LockScreenConfig config = LockScreenConfig.getInstance();
		if (!config.parse(conf_file)) {
			logger.warn("parse conf file failed : {}", conf_file);
			return;
		}
		
		logger.info(config.toString());
		
		if (!config.isValid()) {
			logger.warn("config is invalid");
			return;
		}
		
		// service
		LockScreenService service = new LockScreenService();
		if (service.init()) {
			logger.info("{} init ok", CLASS_NAME);
			Runtime.getRuntime().addShutdownHook(new LockScreenServiceShutdownHook(service));
		} else {
			logger.warn("{} init failed", CLASS_NAME);
			service.uninit();
		}
	}
	
}
