package com.inveno.bigdata.lockscreen.card;

import com.github.panhongan.util.StringUtil;
import com.github.panhongan.util.conf.Config;

public class LockScreenCardConfig {
	
	private static LockScreenCardConfig instance = new LockScreenCardConfig();

	private Config config = new Config();
	
	public static LockScreenCardConfig getInstance() {
		return instance;
	}
	
	private LockScreenCardConfig() {
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
				!StringUtil.isEmpty(config.getString("acs.heart.beat.time.ms")) &&				
				!StringUtil.isEmpty(config.getString("acs.failed.data.dir")) &&
				!StringUtil.isEmpty(config.getString("acs.failed.data.window.minutes")) &&
				!StringUtil.isEmpty(config.getString("sql.server")) &&
				!StringUtil.isEmpty(config.getString("sql.port")) &&
				!StringUtil.isEmpty(config.getString("sql.db")) &&
				!StringUtil.isEmpty(config.getString("sql.user")) &&
				!StringUtil.isEmpty(config.getString("sql.password")) &&
				!StringUtil.isEmpty(config.getString("sql.charset")) &&
				!StringUtil.isEmpty(config.getString("sql.login.timeout")) &&
				!StringUtil.isEmpty(config.getString("min.pool.size")) &&
				!StringUtil.isEmpty(config.getString("max.pool.size")) &&
				!StringUtil.isEmpty(config.getString("init.pool.size")) &&
				!StringUtil.isEmpty(config.getString("increment.num")) &&
				!StringUtil.isEmpty(config.getString("max.idle.time")));
	}
	
	@Override
	public String toString() {
		return config.toString();
	}
	
}
