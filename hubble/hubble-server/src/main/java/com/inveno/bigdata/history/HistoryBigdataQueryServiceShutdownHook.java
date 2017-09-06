package com.inveno.bigdata.history;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HistoryBigdataQueryServiceShutdownHook extends Thread {
	
	private static final String CLASS_NAME = HistoryBigdataQueryServiceShutdownHook.class.getSimpleName();
	
	private static final Logger logger = LoggerFactory.getLogger(CLASS_NAME);
	
	private HistoryBigdataQueryService service = null;
	
	public HistoryBigdataQueryServiceShutdownHook(HistoryBigdataQueryService service) {
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
