package com.inveno.bigdata.query.data.util.conf;

import com.github.panhongan.util.conf.Config;
import com.github.panhongan.util.StringUtil;

public class UfsConfig {

	private static UfsConfig instance = new UfsConfig();

	private Config config = new Config();
	
	public static UfsConfig getInstance() {
		return instance;
	}
	
	private UfsConfig() {
	}
	
	public boolean parse(String confFile) {
		return config.parse(confFile);
	}
	
	public Config getConfig() {
		return config;
	}
	
	public boolean isValid() {
		return (!StringUtil.isEmpty(config.getString("ufs.thrift.server.host")) &&
				!StringUtil.isEmpty(config.getString("ufs.thrift.server.port")) &&
				!StringUtil.isEmpty(config.getString("local.path.weighted.categories")) &&
				!StringUtil.isEmpty(config.getString("local.path.weighted.tags")) && 
				!StringUtil.isEmpty(config.getString("local.path.lda.topic"))
				);
	}
	
	@Override
	public String toString() {
		return config.toString();
	}
	
}
