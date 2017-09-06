package com.inveno.bigdata.sync;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.panhongan.util.conf.Config;
import com.github.panhongan.util.db.JedisUtil;
import com.github.panhongan.util.hash.SimplePartitioner;
import com.inveno.bigdata.sync.conf.ArticleGMPImpressionSyncConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;

public class ArticleGMPImpressionSyncService {
	
	private static final Logger logger = LoggerFactory.getLogger(ArticleGMPImpressionSyncService.class);
	
	private static final String CLASS_NAME = ArticleGMPImpressionSyncService.class.getSimpleName();
	
	public static void processArticleGMPFile(String article_gmp_file, Config config, JedisCommands jedis_session) {
		BufferedReader reader = null;
		
		try {
			int columns = config.getInt("redis.article.gmp.columns");
			String redis_article_gmp_key_prefix = config.getString("redis.article.gmp.key.prefix");
			int redis_article_gmp_key_partitions = config.getInt("redis.article.gmp.key.partitions"); 
			Map<String, String> map = new HashMap<String, String>();			
			reader = new BufferedReader(new FileReader(article_gmp_file));
			
			String line = null;
			while ((line = reader.readLine()) != null) {
			    line = line.replace("(", "");
			    line = line.replace(")", "");
				String [] arr = line.split(",\\[");
				if (arr != null && arr.length == columns) {
					map.put(arr[0], arr[1]);
				} else {
					logger.warn("invalid columns : {}", line);
				}
			}
			
			for (String article_id : map.keySet()) {
				String hash_key = redis_article_gmp_key_prefix + "-" + SimplePartitioner.partition(article_id, redis_article_gmp_key_partitions);	
				jedis_session.hset(hash_key, article_id, "["+map.get(article_id));
				logger.debug("write : {}, {}", article_id, map.get(article_id));
			}
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e) {
				}
			}
		}
	}
	
	public static void usage() {
		System.out.println(CLASS_NAME + " <conf_file> <article_gmp_file>");
	}
	
    public static void main( String[] args ) {
    	if (args.length != 2) {
    		usage();
    		return;
    	}
    	
    	// config
    	String conf_file = args[0];
    	String article_gmp_file = args[1];
    	
    	ArticleGMPImpressionSyncConfig config = ArticleGMPImpressionSyncConfig.getInstance();
    	if (!config.parse(conf_file)) {
    		logger.warn("parse conf_file failed : {}", conf_file);
    		return;
    	}
    	logger.info(config.toString());
    	if (!config.isValid()) {
    		logger.warn("config is not valid");
    		return;
    	}
    	
    	Config conf = config.getConfig();
    	boolean is_cluster = (conf.getInt("redis.is_cluster") == 1);
    	if (is_cluster) {
    		JedisCluster jedis_cluster = JedisUtil.createJedisCluster(conf);
    		processArticleGMPFile(article_gmp_file, conf, jedis_cluster);
    		JedisUtil.closeJedisCluster(jedis_cluster);
    	} else {
    		Jedis jedis = JedisUtil.createJedis(conf);
    		processArticleGMPFile(article_gmp_file, conf, jedis);
    		JedisUtil.closeJedis(jedis);
    	}
    }
    
}
