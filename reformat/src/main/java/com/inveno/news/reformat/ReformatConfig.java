package com.inveno.news.reformat;

import com.github.panhongan.util.StringUtil;
import com.github.panhongan.util.conf.Config;

public class ReformatConfig {
	
	private static ReformatConfig instance = new ReformatConfig();

	private Config config = new Config();
	
	public static ReformatConfig getInstance() {
		return instance;
	}
	
	private ReformatConfig() {
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
				!StringUtil.isEmpty(config.getString("dst.kafka.zk.list")) &&
				!StringUtil.isEmpty(config.getString("dst.kafka.broker.list")) &&
				!StringUtil.isEmpty(config.getString("inner.queue.size")) &&
				!StringUtil.isEmpty(config.getString("inner.processors.per.queue")));
	}
	
	@Override
	public String toString() {
		return config.toString();
	}
	
}
