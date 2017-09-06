package com.inveno.news.reformat;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.panhongan.util.kafka.AbstractMessageProcessor;
import com.github.panhongan.util.kafka.HighLevelConsumerGroup;
import com.inveno.news.reformat.MessageReformatWorkflow;
import com.inveno.news.reformat.ReformatConfig;
import com.inveno.news.reformat.convert.TestAbstractConverter;

public class TestMessageReformatWorkflowGroup {
	
	private static final Logger logger = LoggerFactory.getLogger(TestMessageReformatWorkflowGroup.class);
	
	public static void main(String [] args) {
		String conf_file = "../conf/reformat-test.properties";
		ReformatConfig config = ReformatConfig.getInstance();
		if (!config.parse(conf_file)) {
			logger.warn("parse conf file failed : {}", conf_file);
			return;
		}
		
		logger.info(config.toString());
		
		String topic = "test";
		int partitions = 4;
		
		List<AbstractMessageProcessor> processors = new ArrayList<AbstractMessageProcessor>();
		for (int i = 0; i < partitions; ++i) {
		MessageReformatWorkflow work_flow = new MessageReformatWorkflow(new TestAbstractConverter());
			if (work_flow.init()) {
				processors.add(work_flow);
			}
		}
		
		HighLevelConsumerGroup group = new HighLevelConsumerGroup(config.getConfig().getString("src.kafka.zk.list"), 
				config.getConfig().getString("src.kafka.consumer.group"),
				topic, partitions, 
				Boolean.TRUE.toString().contentEquals(config.getConfig().getString("src.kafka.consumer.restart.offset.largest")),
				processors);
		
		if (group.init()) {
			logger.info("HighLevelConsumerGroup init ok");
		} else {
			logger.warn("HighLevelConsumerGroup init failed");
		}
		
		try {
			Thread.sleep(5 * 60 * 1000);
		} catch (Exception e) {
		}
		
		// uninit
		group.uninit();
		
		for (AbstractMessageProcessor processor : processors) {
			processor.uninit();
		}
	}

}
