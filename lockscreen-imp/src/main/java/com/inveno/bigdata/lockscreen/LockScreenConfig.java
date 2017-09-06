package com.inveno.bigdata.lockscreen;

import com.github.panhongan.util.StringUtil;
import com.github.panhongan.util.conf.Config;

public class LockScreenConfig {
	
	private static LockScreenConfig instance = new LockScreenConfig();

	private Config config = new Config();
	
	public static LockScreenConfig getInstance() {
		return instance;
	}
	
	private LockScreenConfig() {
	}
	
	public boolean parse(String confFile) {
		return config.parse(confFile);
	}
	
	public Config getConfig() {
		return config;
	}
	
	public boolean isValid() {
		return (!StringUtil.isEmpty(config.getString("kafka.zk.list")) &&
				!StringUtil.isEmpty(config.getString("kafka.broker.list")) &&
				!StringUtil.isEmpty(config.getString("kafka.consumer.group")) &&
				!StringUtil.isEmpty(config.getString("kafka.consumer.group.restart.offset.largest")) &&
				!StringUtil.isEmpty(config.getString("kafka.topic.partition")) &&
				!StringUtil.isEmpty(config.getString("acs.server")) &&
				!StringUtil.isEmpty(config.getString("acs.timeout.ms")) &&
				!StringUtil.isEmpty(config.getString("acs.failed.data.dir")) &&
				!StringUtil.isEmpty(config.getString("acs.failed.data.window.minutes")));
	}
	
	@Override
	public String toString() {
		return config.toString();
	}
	
}
