package com.inveno.bigdata.lockscreen.card;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LockScreenCardServiceShutdownHook extends Thread {
	
	private static final Logger logger = LoggerFactory.getLogger(LockScreenCardServiceShutdownHook.class);
	
	private static final String CLASS_NAME = LockScreenCardServiceShutdownHook.class.getSimpleName();
	
	private LockScreenCardService service = null;
	
	public LockScreenCardServiceShutdownHook(LockScreenCardService service) {
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
