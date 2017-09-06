package com.inveno.bigdata.query.data.util.conf;

import com.github.panhongan.util.StringUtil;
import com.github.panhongan.util.conf.Config;

public class RedisConfig {
	
	private static RedisConfig instance = new RedisConfig();

	private Config config = new Config();
	
	public static RedisConfig getInstance() {
		return instance;
	}
	
	private RedisConfig() {
	}
	
	public boolean parse(String confFile) {
		return config.parse(confFile);
	}
	
	public Config getConfig() {
		return config;
	}
	
	public boolean isValid() {
		return (!StringUtil.isEmpty(config.getString("redis.servers")) &&
				!StringUtil.isEmpty(config.getString("redis.key.all.info.ids")) &&
				!StringUtil.isEmpty(config.getString("redis.key.content.prediction.prefix")) &&
				!StringUtil.isEmpty(config.getString("redis.key.content.prediction.field")) &&
				!StringUtil.isEmpty(config.getString("local.path.all.info.ids")) &&
				!StringUtil.isEmpty(config.getString("local.path.content.info"))
				);
	}
	
	@Override
	public String toString() {
		return config.toString();
	}
	
}
