package com.inveno.bigdata.sync.convert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.panhongan.util.StringUtil;
import com.inveno.bigdata.sync.constant.KafkaTopic;

public class ConverterFactory {
	
	private static final Logger logger = LoggerFactory.getLogger(ConverterFactory.class);
	
	public static AbstractConverter getConverter(String name) {
		AbstractConverter converter = null;
		
		if (KafkaTopic.IMPRESSION_STATISTICS.equals(name)) {
			converter = new SingaporeImpressionStatConverter();
		} else {
			logger.warn("invalid name : {}", StringUtil.toString(name));
		}
		
		return converter;
	}

}
