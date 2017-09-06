package com.inveno.bigdata.sync.conf;

import com.github.panhongan.util.StringUtil;
import com.github.panhongan.util.conf.Config;

public class ArticleGMPImpressionSyncConfig {
	
	private static ArticleGMPImpressionSyncConfig instance = new ArticleGMPImpressionSyncConfig();

	private Config config = new Config();
	
	public static ArticleGMPImpressionSyncConfig getInstance() {
		return instance;
	}
	
	private ArticleGMPImpressionSyncConfig() {
	}
	
	public boolean parse(String confFile) {
		return config.parse(confFile);
	}
	
	public Config getConfig() {
		return config;
	}
	
	public boolean isValid() {
		return (!StringUtil.isEmpty(config.getString("redis.article.gmp.columns")) &&
				!StringUtil.isEmpty(config.getString("redis.article.gmp.key.prefix")) &&
				!StringUtil.isEmpty(config.getString("redis.article.gmp.key.partitions")) &&
				!StringUtil.isEmpty(config.getString("redis.is_cluster")) &&
				!StringUtil.isEmpty(config.getString("redis.servers")));
	}
	
	@Override
	public String toString() {
		return config.toString();
	}
	
}
