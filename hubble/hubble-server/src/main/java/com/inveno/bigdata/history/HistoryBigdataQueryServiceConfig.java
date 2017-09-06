package com.inveno.bigdata.history;

import com.github.panhongan.util.StringUtil;
import com.github.panhongan.util.conf.Config;

public class HistoryBigdataQueryServiceConfig {
	
	private static HistoryBigdataQueryServiceConfig instance = new HistoryBigdataQueryServiceConfig();

	private Config config = new Config();
	
	public static HistoryBigdataQueryServiceConfig getInstance() {
		return instance;
	}
	
	private HistoryBigdataQueryServiceConfig() {
	}
	
	public boolean parse(String confFile) {
		return config.parse(confFile);
	}
	
	public Config getConfig() {
		return config;
	}
	
	public boolean isValid() {
		return (!StringUtil.isEmpty(config.getString("http.prefix")) &&
				!StringUtil.isEmpty(config.getString("max.running.tasks")) &&
				!StringUtil.isEmpty(config.getString("hive.local.data.path")) &&
				!StringUtil.isEmpty(config.getString("mysql.properties")) &&
				!StringUtil.isEmpty(config.getString("hive.properties")));
	}
	
	@Override
	public String toString() {
		return config.toString();
	}
	
}
