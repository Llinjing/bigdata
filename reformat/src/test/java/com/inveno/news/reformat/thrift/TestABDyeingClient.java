package com.inveno.news.reformat.thrift;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestABDyeingClient {

	private static Logger logger = LoggerFactory.getLogger(TestABDyeingClient.class);
	
	public static void main(String [] args) {
		//online
		//ABDyeingClient client = new ABDyeingClient("172.31.22.87", 8893);
		//test
		ABDyeingClient client = new ABDyeingClient("172.31.22.87", 18893);
		if (client.init()) {
			logger.warn("init ok");
			//"upack":{"ad_configid":"40","biz_configid":"50","news_configid":"179"}
			System.out.println(client.getUpack("01011702210021155201000158815300", "noticias", "2.4.1.0.0.3", "spanish", "android"));
			System.out.println("------------");
			//"upack":{"ad_configid":"37","biz_configid":"50","news_configid":"124"}
			System.out.println(client.getUpack("01011610221146555101000157518600", "mata", "2.4.2.0.0.3", "indonesian", "android"));
			System.out.println("------------");
		} else {
			logger.warn("init failed");
		}
		client.uninit();
	}

}