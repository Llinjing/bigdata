package com.inveno.bigdata.common;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.panhongan.util.conf.Config;
import com.github.panhongan.util.StringUtil;

public class CommonConfig {

	private static final Logger logger = LoggerFactory.getLogger(CommonConfig.class);
	
	private static CommonConfig instance = new CommonConfig();

	private Config config = new Config();
	
	public static CommonConfig getInstance() {
		return instance;
	}
	
	private CommonConfig() {
		try {
			Properties prop = new Properties();
			prop.load(this.getClass().getResourceAsStream("/common.properties"));
			for (Object key : prop.keySet()) {
				config.addProperty((String)key, (String)prop.get(key));
			}
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		}
		
		if (!this.isValid()) {
			logger.warn("config is invalid");
			System.exit(1);
		}
	}
	
	public Config getConfig() {
		return config;
	}
	
	private boolean isValid() {
		return (!StringUtil.isEmpty(config.getString("key_tag")) &&
				!StringUtil.isEmpty(config.getString("product_key_tag")));
	}
	
	@Override
	public String toString() {
		return config.toString();
	}
	
}