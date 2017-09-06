package com.inveno.bigdata.kafka.sync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaSyncServiceShutdownHook extends Thread {
	
	private static final Logger logger = LoggerFactory.getLogger(KafkaSyncServiceShutdownHook.class);
	
	private static final String CLASS_NAME = KafkaSyncServiceShutdownHook.class.getSimpleName();
	
	private KafkaSyncService service = null;
	
	public KafkaSyncServiceShutdownHook(KafkaSyncService service) {
		this.service = service;
	}

	@Override
	public void run() {
		if (service != null) {
			service.uninit();
		}
		
		logger.info("{} is trigged", CLASS_NAME);
	}
	
}
