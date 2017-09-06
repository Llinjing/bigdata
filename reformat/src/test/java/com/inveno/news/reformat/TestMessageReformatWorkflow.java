package com.inveno.news.reformat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inveno.news.reformat.MessageReformatWorkflow;
import com.inveno.news.reformat.ReformatConfig;
import com.inveno.news.reformat.convert.TestAbstractConverter;

public class TestMessageReformatWorkflow {
	
	private static final Logger logger = LoggerFactory.getLogger(TestMessageReformatWorkflow.class);
	
	public static void main(String [] args) {
		String conf_file = "../conf/reformat.properties";
		ReformatConfig config = ReformatConfig.getInstance();
		if (!config.parse(conf_file)) {
			logger.warn("parse conf file failed : {}", conf_file);
			return;
		}
		
		logger.info(config.toString());
		
		MessageReformatWorkflow work_flow = new MessageReformatWorkflow(new TestAbstractConverter());
		if (work_flow.init()) {
			work_flow.processMessage("test", 1, "abc");
		}
		
		work_flow.uninit();
	}

}
