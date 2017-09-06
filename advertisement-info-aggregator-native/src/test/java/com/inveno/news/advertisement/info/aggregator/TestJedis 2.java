package com.inveno.news.advertisement.info.aggregator;

import com.inveno.news.advertisement.info.aggregator.util.HashKeyMaker;
import com.inveno.news.advertisement.info.aggregator.util.RedisConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.util.Pool;

import com.github.panhongan.util.db.JedisUtil;

public class TestJedis{
	
	private static final String CLASS_NAME = TestJedis.class.getSimpleName();
	
	private static final Logger logger = LoggerFactory.getLogger(CLASS_NAME);

	private static final RedisConfig conf = RedisConfig.getInstance();
	
	private static int DEFAULT_REDIS_DB = 1;
	
	private Pool<Jedis> jedis_pool = null;
	
	private JedisCluster jedis_cluster = null;
	
	public boolean init() {
		boolean is_ok = false;
		if (Boolean.valueOf(conf.getConfig().getString("redis.is_cluster"))) {
			jedis_cluster = JedisUtil.createJedisCluster(conf.getConfig());
			is_ok = (jedis_cluster != null);
		} else {
			jedis_pool = JedisUtil.createJedisPool(conf.getConfig());
			is_ok = (jedis_pool != null);
		}

		return is_ok;
	}

	public void uninit() {
		if (jedis_cluster != null) {
			JedisUtil.closeJedisCluster(jedis_cluster);
		} else {
			JedisUtil.closeJedisPool(jedis_pool);
		}
	}
	
	public void update(String field, String value) {
		if (jedis_cluster != null) {
			try {
				jedis_cluster.hset(HashKeyMaker.make(field), field, value);
				logger.debug(HashKeyMaker.make(field) + "   " + field + "   " + value);
			} catch (Exception e) {
				logger.warn(e.getMessage(), e);
			}
		} else if (jedis_pool != null) {
			Jedis jedis = null;
			try {
				jedis = jedis_pool.getResource();
				if (jedis != null) {
					jedis.select(conf.getConfig().getInt("redis.db", DEFAULT_REDIS_DB));
					jedis.hset(HashKeyMaker.make(field), field, value);
				}
			} catch (Exception e) {
				logger.warn(e.getMessage(), e);
			} finally {
				JedisUtil.returnSource(jedis_pool, jedis, true);
			}
		}
	}
	
	public static void usage() {
		System.out.println(CLASS_NAME + " <conf_file>");
	}
	
	public static void main(String [] args) {
		if (args.length != 1) {
			usage();
			return;
		}
		
		// config
    	String conf_file = args[0];
    	RedisConfig conf = RedisConfig.getInstance();
    	if (!conf.parse(conf_file)) {
    		logger.warn("parse conf_file failed : {}", conf_file);
    		return;
    	}
    	logger.info(conf.toString());
    	if (!conf.isValid()) {
    		logger.warn("config is not valid");
    		return;
    	}
		
    	// query
    	TestJedis writer = new TestJedis();
    	String test_json_id = "101585931015859364";
    	String test_json = "{\"aid\":\"101585931015859364\",\"api_ver\":\"1.0.0\",\"app_lan\":\"hindi\",\"app_ver\":\"2.2.2.0.0.3\",\"brand\":\"samsung\",\"event_id\":\"7\",\"event_time\":\"1473219991\",\"gate_ip\":\"172.31.6.161\",\"imei\":\"355261073046871\",\"language\":\"en_us\",\"log_time\":\"2016-09-07 03:46:44\",\"log_type\":\"report\",\"mcc\":\"IN\",\"mnc\":\"856\",\"model\":\"SM-G531F\",\"video_play_failed_extra\":{\"content_id\":\"1015859364\",\"error_code\":\"SUCCESS\"}}";
    	if (writer.init()) {
    		logger.info(CLASS_NAME + " init ok");
    		writer.update(test_json_id, test_json);
    		writer.uninit();
    		logger.info(CLASS_NAME + " uninit ok");
    	} else {
    		logger.info(CLASS_NAME + " init failed");
    		writer.uninit();
    	}
	}
}
