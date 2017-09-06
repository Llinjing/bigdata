package com.inveno.bigdata.sync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceShutdownHook extends Thread {
	
	private static final Logger logger = LoggerFactory.getLogger(ServiceShutdownHook.class);
	
	private static final String CLASS_NAME = ServiceShutdownHook.class.getSimpleName();
	
	private ImpressionStatSyncService service = null;
	
	public ServiceShutdownHook(ImpressionStatSyncService service) {
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
