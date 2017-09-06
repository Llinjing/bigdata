package com.inveno.news.reformat;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.panhongan.util.StringUtil;
import com.github.panhongan.util.conf.Config;
import com.github.panhongan.util.control.Lifecycleable;
import com.github.panhongan.util.kafka.AbstractMessageProcessor;
import com.github.panhongan.util.kafka.HighLevelConsumerGroup;
import com.inveno.news.reformat.ReformatConfig;
import com.inveno.news.reformat.convert.AbstractConverter;
import com.inveno.news.reformat.convert.ConverterFactory;


public class ReformatService implements Lifecycleable {
	
	private static final Logger logger = LoggerFactory.getLogger(ReformatService.class);
	
	private static final String CLASS_NAME = ReformatService.class.getSimpleName();
	
	private static ReformatConfig config = ReformatConfig.getInstance();
	
	private List<AbstractMessageProcessor> processors = new ArrayList<AbstractMessageProcessor>();
	
	private HighLevelConsumerGroup group = null;
	
	@Override
	public boolean init() {
		boolean is_ok = false;
		
		Config conf = config.getConfig();
		
		String topic_partition = conf.getString("src.kafka.topic.partition");
		if (topic_partition != null) {
			String [] arr = topic_partition.split(":");
			if (arr != null && arr.length == 2) {
				String topic = arr[0];
				int partitions = Integer.valueOf(arr[1]);
				
				for (int i = 0; i < partitions; ++i) {
					AbstractConverter converter = ConverterFactory.getConverter(topic);
					if (converter == null || !converter.init()) {
						logger.warn("converter init failed, topic = {}", topic);
						return false;
					}
					
					MessageReformatWorkflow workflow = new MessageReformatWorkflow(converter);
					workflow.setName(workflow.getClass().getSimpleName() + "_" + topic + "_" + i);
					if (workflow.init()) {
						processors.add(workflow);
					} else {
						logger.warn(workflow.getName() + " init failed");
						return false;
					}
				}
				
				is_ok = (processors.size() == partitions);
				if (!is_ok) {
					logger.warn("init MessageReformatWorkflow failed");
					return false;
				}
				
				group = new HighLevelConsumerGroup(conf.getString("src.kafka.zk.list"), 
						conf.getString("src.kafka.consumer.group"),
						topic, partitions,
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
		
		for (AbstractMessageProcessor processor : processors) {
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
		
		// config
		String conf_file = args[0];
		ReformatConfig config = ReformatConfig.getInstance();
		if (!config.parse(conf_file)) {
			logger.warn("parse conf file failed : {}", conf_file);
			return;
		}
		
		logger.info(config.toString());
		
		if (!config.isValid()) {
			logger.warn("config is invalid");
			return;
		}
		
		// request reformat service
		ReformatService service = new ReformatService();
		if (service.init()) {
			logger.info("{} init ok", CLASS_NAME);
			
			Runtime.getRuntime().addShutdownHook(new ReformatServiceShutdownHook(service));
		} else {
			logger.warn("{} init failed", CLASS_NAME);
			service.uninit();
		}
	}

}
