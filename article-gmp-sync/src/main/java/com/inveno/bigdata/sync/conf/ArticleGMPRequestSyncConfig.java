package com.inveno.bigdata.sync.conf;

import com.github.panhongan.util.StringUtil;
import com.github.panhongan.util.conf.Config;

public class ArticleGMPRequestSyncConfig {
	
	private static ArticleGMPRequestSyncConfig instance = new ArticleGMPRequestSyncConfig();

	private Config config = new Config();
	
	public static ArticleGMPRequestSyncConfig getInstance() {
		return instance;
	}
	
	private ArticleGMPRequestSyncConfig() {
	}
	
	public boolean parse(String confFile) {
		return config.parse(confFile);
	}
	
	public Config getConfig() {
		return config;
	}
	
	public boolean isValid() {
		return (!StringUtil.isEmpty(config.getString("redis.properties")) &&
				!StringUtil.isEmpty(config.getString("article.gmp.columns")) &&
				!StringUtil.isEmpty(config.getString("article.gmp.redis.hash.key")) &&
				!StringUtil.isEmpty(config.getString("article.gmp.redis.is_cluster")) &&
				!StringUtil.isEmpty(config.getString("article.gmp.redis.db")));
	}
	
	@Override
	public String toString() {
		return config.toString();
	}
	
}
