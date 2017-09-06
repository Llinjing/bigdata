package com.inveno.bigdata.lockscreen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TestLockScreenImpressionMessageProcessor {
	
	private static Logger logger = LoggerFactory.getLogger(TestLockScreenImpressionMessageProcessor.class);

	public static void main(String[] args) {
		String str = "{\"aid\":\"1c0d309f0c87d07d\",\"api_ver\":\"2.0.0\",\"app_lan\":\"spanish\",\"app_ver\":\"2.4.5.0.0.4\",\"article_impression_extra\":{\"content_id\":\"1046248750\",\"content_type\":\"news\",\"cpack\":{\"source\":\"Milenio\",\"strategy\":\"recommendation\"},\"server_time\":\"1490758161\",\"view_mode\":\"1\"},\"brand\":\"Sony\",\"event_id\":\"2\",\"event_time\":\"1490758445\",\"gate_ip\":\"10.10.20.122\",\"imei\":\"353256065966492\",\"language\":\"es_mx\",\"log_time\":\"2017-03-29 11:42:08\",\"log_type\":\"report\",\"mcc\":\"MX\",\"mnc\":\"020\",\"model\":\"D2306\",\"network\":\"wifi\",\"osv\":\"5.1.1\",\"platform\":\"android\",\"product_id\":\"noticias\",\"promotion\":\"gp\",\"protocol\":\"https\",\"report_time\":\"1490758928\",\"scenario\":{\"channel\":\"0xff\",\"channel_desc\":\"unknown\",\"desc\":\"other\",\"position\":\"0x13\",\"position_desc\":\"lockscreen\",\"position_type\":\"0x07\",\"position_type_desc\":\"other\"},\"seq\":\"16\",\"sid\":\"536063188\",\"tk\":\"f59af42868474e019bef10df6fe4e0d1\",\"uid\":\"01011701080252345201000141498800\",\"upack\":{\"ad_configid\":\"39\",\"biz_configid\":\"50\",\"news_configid\":\"172\"}}";
		
		// config file
		String conf_file = args[0];
		LockScreenConfig config = LockScreenConfig.getInstance();
		if (!config.parse(conf_file)) {
			logger.warn("parse conf file failed : {}", conf_file);
			return;
		}
		
		logger.info(config.toString());
		
		if (!config.isValid()) {
			logger.warn("config is invalid");
			return;
		}
		
		LockScreenImpressionMessageProcessor handler = new LockScreenImpressionMessageProcessor();
		handler.processMessage("test", 0, str);
	}

}
