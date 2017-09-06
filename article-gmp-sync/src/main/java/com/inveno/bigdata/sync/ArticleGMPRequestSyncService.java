package com.inveno.bigdata.sync;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.panhongan.util.conf.Config;
import com.github.panhongan.util.db.JedisUtil;
import com.inveno.bigdata.sync.conf.ArticleGMPRequestSyncConfig;
import com.inveno.bigdata.sync.constant.GmpSchema;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;

public class ArticleGMPRequestSyncService {
	
	private static final Logger logger = LoggerFactory.getLogger(ArticleGMPRequestSyncService.class);
	
	private static final String CLASS_NAME = ArticleGMPRequestSyncService.class.getSimpleName();
	
	public static void makeAppJson(GmpSchema schema, JSONArray arr) {
		JSONObject item_json_obj = new JSONObject();
		item_json_obj.put("click", schema.click);
		item_json_obj.put("impression", schema.impression);
		item_json_obj.put("ctr", schema.ctr);
		
		JSONObject product_id_json_obj = new JSONObject();
		product_id_json_obj.put(schema.product_id, item_json_obj);
		
		arr.add(product_id_json_obj);
	}
	
	public static void processArticleGMPFile(String article_gmp_file, Config config, JedisCommands jedis_session) {
		BufferedReader reader = null;
		
		try {
			int columns = config.getInt("article.gmp.columns");
			String article_gmp_hash_key = config.getString("article.gmp.redis.hash.key");
			Map<String, List<GmpSchema>> map = new HashMap<String, List<GmpSchema>>();
			
			reader = new BufferedReader(new FileReader(article_gmp_file));
			String line = null;
			
			while ((line = reader.readLine()) != null) {
				String [] arr = line.split("\t");
				if (arr != null && arr.length == columns) {
					GmpSchema schema = new GmpSchema(arr[1], 
							Float.valueOf(arr[2]),
							Float.valueOf(arr[3]),
							Float.valueOf(arr[4]));
					
					List<GmpSchema> list = map.get(arr[0]);
					if (list == null) {
						list = new ArrayList<GmpSchema>();
					}
					list.add(schema);
					map.put(arr[0], list);
				} else {
					logger.warn("invalid columns : {}", line);
				}
			}
			
			for (String content_id : map.keySet()) {
				JSONArray arr = new JSONArray();
				for (GmpSchema schema: map.get(content_id)) {
					makeAppJson(schema, arr);
				}
				
				jedis_session.hset(article_gmp_hash_key, content_id, arr.toString());
				logger.debug("write : {}, {}", content_id, arr.toString());
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
    	
    	ArticleGMPRequestSyncConfig config = ArticleGMPRequestSyncConfig.getInstance();
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
    	boolean is_cluster = (conf.getInt("article.gmp.redis.is_cluster") == 1);
    	if (is_cluster) {
    		JedisCluster jedis_cluster = JedisUtil.createJedisCluster(conf.getString("redis.properties"));
    		processArticleGMPFile(article_gmp_file, conf, jedis_cluster);
    		JedisUtil.closeJedisCluster(jedis_cluster);
    	} else {
    		Jedis jedis = JedisUtil.createJedis(conf.getString("redis.properties"));
    		processArticleGMPFile(article_gmp_file, conf, jedis);
    		JedisUtil.closeJedis(jedis);
    	}
    }
    
}
