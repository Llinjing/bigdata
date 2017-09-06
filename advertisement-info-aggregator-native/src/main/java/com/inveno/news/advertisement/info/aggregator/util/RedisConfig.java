package com.inveno.news.advertisement.info.aggregator.util;

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
		return (!StringUtil.isEmpty(config.getString("redis.server")) &&
				!StringUtil.isEmpty(config.getString("redis.port")) &&
				!StringUtil.isEmpty(config.getString("redis.connection.timeout")) &&
				!StringUtil.isEmpty(config.getString("redis.is_cluster")) &&
				!StringUtil.isEmpty(config.getString("redis.db")) &&
				!StringUtil.isEmpty(config.getString("redis.advertisement.info.partitions")) &&
				!StringUtil.isEmpty(config.getString("redis.advertisement.info.hash.key")));
	}
	
	@Override
	public String toString() {
		return config.toString();
	}
	
}
