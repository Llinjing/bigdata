package com.inveno.bigdata.sync;

import com.github.panhongan.util.StringUtil;
import com.github.panhongan.util.conf.Config;

public class ImpressionStatSyncConfig {
	
	private static ImpressionStatSyncConfig instance = new ImpressionStatSyncConfig();

	private Config config = new Config();
	
	public static ImpressionStatSyncConfig getInstance() {
		return instance;
	}
	
	private ImpressionStatSyncConfig() {
	}
	
	public boolean parse(String confFile) {
		return config.parse(confFile);
	}
	
	public Config getConfig() {
		return config;
	}
	
	public boolean isValid() {
		return (!StringUtil.isEmpty(config.getString("src.kafka.zk.list")) &&
				!StringUtil.isEmpty(config.getString("src.kafka.broker.list")) &&
				!StringUtil.isEmpty(config.getString("src.kafka.consumer.group")) &&
				!StringUtil.isEmpty(config.getString("src.kafka.topic.partition")) &&
				!StringUtil.isEmpty(config.getString("src.kafka.consumer.restart.offset.largest")) &&
				!StringUtil.isEmpty(config.getString("inner.queue.size")) &&
				!StringUtil.isEmpty(config.getString("inner.processors.per.queue")) &&
				!StringUtil.isEmpty(config.getString("redis.is_cluster")) &&
				!StringUtil.isEmpty(config.getString("redis.servers")) &&
				!StringUtil.isEmpty(config.getString("impression.stat.redis.key.prefix")) &&
				!StringUtil.isEmpty(config.getString("impression.stat.data.valid.time")) &&
				!StringUtil.isEmpty(config.getString("impression.threshold")));
	}
	
	@Override
	public String toString() {
		return config.toString();
	}
	
}
