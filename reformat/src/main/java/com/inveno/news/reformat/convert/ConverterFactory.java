package com.inveno.news.reformat.convert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.panhongan.util.StringUtil;
import com.inveno.news.reformat.constant.KafkaTopic;

public class ConverterFactory {
	
	private static final Logger logger = LoggerFactory.getLogger(ConverterFactory.class);
	
	public static AbstractConverter getConverter(String name) {
		AbstractConverter converter = null;
		
		if (KafkaTopic.REQUEST.equals(name)) {
			converter = new RequestLogConverter();
		} else if (KafkaTopic.REQUEST_NEW.equals(name)) {
			converter = new RequestNewLogConverter();
		} else if (KafkaTopic.REQUEST_NEW_US_EAST.equals(name)) {
			converter = new RequestNewLogConverter();
		} else if (KafkaTopic.CLICK.equals(name)) {
			converter = new ClickLogConverter();
		} else if (KafkaTopic.UCLTM_TOPIC.equals(name)) {
			converter = new UcltmLogConverter();
		} else if (KafkaTopic.UAD.equals(name)) {
			converter = new UadLogConverter();
		} else if (KafkaTopic.PROFILE.equals(name)) {
			converter = new ProfileLogConverter();
		} else if (KafkaTopic.PROFILE_US_EAST.equals(name)) {
			converter = new ProfileLogConverter();
		} else if (KafkaTopic.REPORT.equals(name)) {
			converter = new ReportLogConverter();
		} else if (KafkaTopic.REPORT_US_EAST.equals(name)) {
			converter = new ReportLogConverter();
		} else if (KafkaTopic.MALACCA_AD.equals(name)) {
			converter = new MalaccaAdConverter();
		} else if (KafkaTopic.REPORT_AD.equals(name)) {
			converter = new OverseaAnonymousAdConverter();
		} else if (KafkaTopic.GATE_AD.equals(name)) {
			converter = new GateAdConverter();
		} else if (KafkaTopic.AD_ET_CLICK.equals(name)) { 
			converter = new AdEtClickConverter();
		} else if (KafkaTopic.MALACCA_AD_REQUEST.equals(name)) { 
			converter = new MalaccaAdRequestConverter();
		} else if (KafkaTopic.REQUEST_ZHIZI_V2.equals(name)) {
			converter = new RequestZhiziV2Converter();
		} else if (KafkaTopic.REPORT_ZHIZI_V2.equals(name)) {
			converter = new ReportZhiziV2Converter();
		} else {
			logger.warn("invalid name : {}", StringUtil.toString(name));
		}
		
		return converter;
	}

}
