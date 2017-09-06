package com.inveno.news.reformat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReformatServiceShutdownHook extends Thread {
	
	private static final Logger logger = LoggerFactory.getLogger(ReformatServiceShutdownHook.class);
	
	private static final String CLASS_NAME = ReformatServiceShutdownHook.class.getSimpleName();
	
	private ReformatService service = null;
	
	public ReformatServiceShutdownHook(ReformatService service) {
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
