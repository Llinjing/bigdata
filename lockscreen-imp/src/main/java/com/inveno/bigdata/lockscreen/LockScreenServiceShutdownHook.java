package com.inveno.bigdata.lockscreen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LockScreenServiceShutdownHook extends Thread {
	
	private static final Logger logger = LoggerFactory.getLogger(LockScreenServiceShutdownHook.class);
	
	private static final String CLASS_NAME = LockScreenServiceShutdownHook.class.getSimpleName();
	
	private LockScreenService service = null;
	
	public LockScreenServiceShutdownHook(LockScreenService service) {
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
