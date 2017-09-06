package com.inveno.bigdata.gmp;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.panhongan.util.conf.Config;
import com.github.panhongan.util.StringUtil;

public class ArticleGmpConfig {

	private static final Logger logger = LoggerFactory.getLogger(ArticleGmpConfig.class);
	
	private static ArticleGmpConfig instance = new ArticleGmpConfig();

	private Config config = new Config();
	
	public static ArticleGmpConfig getInstance() {
		return instance;
	}
	
	private ArticleGmpConfig() {
		try {
			Properties prop = new Properties();
			prop.load(this.getClass().getResourceAsStream("/article-gmp-us-east.properties"));
			for (Object key : prop.keySet()) {
				config.addProperty((String)key, (String)prop.get(key));
			}
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		}
		
		if (!this.isValid()) {
			logger.warn("config is invalid");
			System.exit(1);
		}
	}
	
	public Config getConfig() {
		return config;
	}
	
	private boolean isValid() {
		return (!StringUtil.isEmpty(config.getString("config.type")) &&
				
				!StringUtil.isEmpty(config.getString("src.kafka.zk.list")) &&
				!StringUtil.isEmpty(config.getString("src.kafka.broker.list")) &&
				!StringUtil.isEmpty(config.getString("src.kafka.consumer.group")) &&
				!StringUtil.isEmpty(config.getString("src.kafka.batch.size")) &&
				!StringUtil.isEmpty(config.getString("src.kafka.sync")) &&
				
				!StringUtil.isEmpty(config.getString("topic.impression")) &&
				!StringUtil.isEmpty(config.getString("topic.click")) &&
				!StringUtil.isEmpty(config.getString("topic.article.gmp")) &&
				!StringUtil.isEmpty(config.getString("topic.impression.partition")) &&
				!StringUtil.isEmpty(config.getString("topic.click.partition")) &&
				!StringUtil.isEmpty(config.getString("topic.article.gmp.partition")) &&
				
				!StringUtil.isEmpty(config.getString("sparkstreaming.batch.size.minutes")) &&
				!StringUtil.isEmpty(config.getString("sparkstreaming.window.num")) &&
				!StringUtil.isEmpty(config.getString("sparkstreaming.checkpoint")) &&
				!StringUtil.isEmpty(config.getString("sparkstreaming.zk.offset.path")) &&
				!StringUtil.isEmpty(config.getString("sparkstreaming.zk.finish.mark.path")) &&
				
				!StringUtil.isEmpty(config.getString("hdfs.impression")) &&
				!StringUtil.isEmpty(config.getString("hdfs.article.gmp")) &&
				!StringUtil.isEmpty(config.getString("hdfs.total.decay.feedback")) &&
				!StringUtil.isEmpty(config.getString("hdfs.redis")) &&
				
				!StringUtil.isEmpty(config.getString("decay_ratio")) &&
				!StringUtil.isEmpty(config.getString("impression_threshold")) &&
				
				!StringUtil.isEmpty(config.getString("date_format")) &&
				!StringUtil.isEmpty(config.getString("valid_hour")) &&
				
				!StringUtil.isEmpty(config.getString("key_tag")) &&
				
				!StringUtil.isEmpty(config.getString("redis.servers")) &&
				!StringUtil.isEmpty(config.getString("redis.port")) &&
				!StringUtil.isEmpty(config.getString("redis.cluster")) &&
				!StringUtil.isEmpty(config.getString("redis.hash.key.prefix")) &&
				!StringUtil.isEmpty(config.getString("redis.hash.key.partitions")) &&
				!StringUtil.isEmpty(config.getString("redis.increment.write")) &&
				
				!StringUtil.isEmpty(config.getString("debug")) &&
				!StringUtil.isEmpty(config.getString("hdfs_debug")));
	}
	
	@Override
	public String toString() {
		return config.toString();
	}
	
}