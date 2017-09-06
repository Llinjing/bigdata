package com.inveno.bigdata.query.data.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.panhongan.util.conf.Config;
import com.github.panhongan.util.db.JedisUtil;
import com.inveno.bigdata.query.data.util.conf.RedisConfig;
import com.inveno.bigdata.query.data.util.file.WriteData;

import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.Tuple;

public class GetEagerlyExploreImpRedisData {
	private static final Logger logger = LoggerFactory.getLogger(GetEagerlyExploreImpRedisData.class);

	private static final String CLASS_NAME = GetEagerlyExploreImpRedisData.class.getName();

	private static JedisCluster jedis_cluster = null;

	private static RedisConfig redis_config = RedisConfig.getInstance();

	private static Config config = null;

	private static Set<Tuple> all_expinfo_ids_set = null;

	private static Map<String, Integer> expinfo_impression_map = new HashMap<String, Integer>();

	private static int null_count = 0;

	public boolean init() {
		boolean ret = false;

		config = redis_config.getConfig();

		synchronized (GetEagerlyExploreImpRedisData.class) {
			if (jedis_cluster == null) {
				jedis_cluster = JedisUtil.createJedisCluster(config);
			}

			ret = (jedis_cluster != null);
			if (!ret) {
				logger.warn("connect to upack redis_cluster failed");
				return ret;
			}
		}

		return ret;
	}

	public void uninit() {
		synchronized (GetEagerlyExploreImpRedisData.class) {
			if (jedis_cluster != null) {
				JedisUtil.closeJedisCluster(jedis_cluster);
				jedis_cluster = null;
			}
		}
	}

	private void queryAllInfoIDs() {
		try {
			//expinfo_noticias_Spanish_eagerly
			all_expinfo_ids_set = jedis_cluster.zrangeWithScores(config.getString("redis.key.expinfo.eagerly"), 0, -1);
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn(e.getMessage(), e);
		}

	}

	private void queryExporeIds() {
		try {
			for (Tuple tuple : all_expinfo_ids_set) {
				String impression = jedis_cluster.hget(config.getString("redis.key.expinfo.product.id"), tuple.getElement().split("#")[0]);
				try {
					Integer.valueOf(impression);
					// 如果是null，就单独处理
					this.insertDataToMap(impression);
				} catch (Exception e) {
					null_count++;
					if (config.getBoolean("redis.expinfo.debug")) {
						WriteData.processMessage(config.getString("local.path.redis.expinfo.debug"),
								config.getString("redis.key.expinfo.eagerly") + "\tcan't get data from \t"
										+ config.getString("redis.key.expinfo.product.id") + "\t" + tuple.getElement() + "\t"
										+ tuple.getScore() + "\t" + impression);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn(e.getMessage(), e);
		}

	}

	private void insertDataToMap(String key) {
		int tmp_int = 0;

		if (expinfo_impression_map.containsKey(key)) {
			tmp_int = expinfo_impression_map.get(key);
		}

		tmp_int++;

		expinfo_impression_map.put(key, tmp_int);
	}

	private void printResult() {
		WriteData.processMessage(config.getString("local.path.expinfo.count"), "");
		WriteData.processMessage(config.getString("local.path.expinfo.count"),
				"------------ analyse eagerly explore pool impression data ------------");
		WriteData.processMessage(config.getString("local.path.expinfo.count"), "------------" + (new Date()).toString() + "------------");
		WriteData.processMessage(config.getString("local.path.expinfo.count"), "all_expinfo_ids_set.size : " + all_expinfo_ids_set.size());
		WriteData.processMessage(config.getString("local.path.expinfo.count"), "null count : " + null_count);
		WriteData.processMessage(config.getString("local.path.expinfo.count"), expinfo_impression_map);
		WriteData.processMessage(config.getString("local.path.expinfo.count"), "");
	}

	public static void usage() {
		System.out.println(CLASS_NAME + " <conf_file>");
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			usage();
			return;
		}

		// config
		String conf_file = args[0];
		if (!redis_config.parse(conf_file)) {
			logger.warn("parse conf file failed : {}", conf_file);
			return;
		}

		logger.info(redis_config.toString());

		// request reformat service
		GetEagerlyExploreImpRedisData service = new GetEagerlyExploreImpRedisData();
		if (service.init()) {
			logger.info("{} init ok", CLASS_NAME);

			service.queryAllInfoIDs();
			service.queryExporeIds();
			service.printResult();

			service.uninit();
		} else {
			logger.warn("{} init failed", CLASS_NAME);
			service.uninit();
		}

	}
}
