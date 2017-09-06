package com.inveno.bigdata.query.data.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.panhongan.util.conf.Config;
import com.github.panhongan.util.db.JedisUtil;
import com.inveno.bigdata.query.data.util.conf.RedisConfig;
import com.inveno.bigdata.query.data.util.file.WriteData;

import redis.clients.jedis.JedisCluster;

public class GetFlowExploreTTLRedisData {
	private static final Logger logger = LoggerFactory.getLogger(GetFlowExploreTTLRedisData.class);

	private static final String CLASS_NAME = GetFlowExploreTTLRedisData.class.getName();

	private static JedisCluster jedis_cluster = null;

	private static RedisConfig redis_config = RedisConfig.getInstance();

	private static Config config = null;

	private static Map<String, String> all_expinfo_ids_map = null;

	private static Map<String, Integer> two_hour_expinfo_impression_map = new HashMap<String, Integer>();

	private static final int MAX_TTL = 30 * 24 * 60 * 60;
	private static final int HALF_HOUR_SECOND = 30 * 60;
	private static final int ONE_HOUR_SECOND = 1 * 60 * 60;
	private static final int TWO_HOUR_SECOND = 2 * 60 * 60;
	private static final int THREE_HOUR_SECOND = 3 * 60 * 60;
	private static final int SIX_HOUR_SECOND = 6 * 60 * 60;
	private static final int ONE_DAY_SECOND = 24 * 60 * 60;
	private static final int TWO_DAY_SECOND = 48 * 60 * 60;

	private static int expore_more_zero_count = 0;
	private static int expore_more_half_hour_count = 0;
	private static int expore_more_one_hour_count = 0;
	private static int expore_more_two_hour_count = 0;
	private static int expore_more_three_hour_count = 0;
	private static int expore_more_six_hour_count = 0;
	private static int expore_more_one_day_count = 0;
	private static int expore_more_two_day_count = 0;
	private static int expore_expired_count = 0;

	public boolean init() {
		boolean ret = false;

		config = redis_config.getConfig();

		synchronized (GetFlowExploreTTLRedisData.class) {
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
		synchronized (GetFlowExploreTTLRedisData.class) {
			if (jedis_cluster != null) {
				JedisUtil.closeJedisCluster(jedis_cluster);
				jedis_cluster = null;
			}
		}
	}

	private void queryAllInfoIDs() {
		try {
			all_expinfo_ids_map = jedis_cluster.hgetAll(config.getString("redis.key.expinfo.product.id"));
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn(e.getMessage(), e);
		}

	}

	private void queryExporeIds() {
		try {
			for (String content_id : all_expinfo_ids_map.keySet()) {
				String key = config.getString("redis.key.content.prefix") + "_" + content_id;

				long result_ttl_long = jedis_cluster.ttl(key);
				int ttl_gap = (int) (MAX_TTL - result_ttl_long);

				if (ttl_gap > TWO_DAY_SECOND) {
					expore_more_two_day_count++;
					WriteData.processMessage(config.getString("local.path.imp.stat.analyse.result"), content_id+"\t"+result_ttl_long+"\t"+all_expinfo_ids_map.get(content_id));
				} else if (ttl_gap > ONE_DAY_SECOND) {
					expore_more_one_day_count++;
				} else if (ttl_gap > SIX_HOUR_SECOND) {
					expore_more_six_hour_count++;
				} else if (ttl_gap > THREE_HOUR_SECOND) {
					expore_more_three_hour_count++;
				} else if (ttl_gap > TWO_HOUR_SECOND) {
					expore_more_two_hour_count++;
				} else if (ttl_gap > ONE_HOUR_SECOND) {
					expore_more_one_hour_count++;
					this.insertDataToMap(all_expinfo_ids_map.get(content_id));
				} else if (ttl_gap > HALF_HOUR_SECOND) {
					expore_more_half_hour_count++;
					this.insertDataToMap(all_expinfo_ids_map.get(content_id));
				} else if (ttl_gap > 0) {
					expore_more_zero_count++;
					this.insertDataToMap(all_expinfo_ids_map.get(content_id));
				} else {
					expore_expired_count++;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.warn(e.getMessage(), e);
		}

	}

	private void insertDataToMap(String key) {
		int tmp_int = 0;

		if (two_hour_expinfo_impression_map.containsKey(key)) {
			tmp_int = two_hour_expinfo_impression_map.get(key);
		}

		tmp_int++;

		two_hour_expinfo_impression_map.put(key, tmp_int);
	}

	private void printResult() {

		WriteData.processMessage(config.getString("local.path.imp.stat.analyse.result"),
				"------------" + (new Date()).toString() + "------------");
		WriteData.processMessage(config.getString("local.path.imp.stat.analyse.result"), "[-1, 0] : " + expore_expired_count);
		WriteData.processMessage(config.getString("local.path.imp.stat.analyse.result"), "(0min, 30min] : " + expore_more_zero_count);
		WriteData.processMessage(config.getString("local.path.imp.stat.analyse.result"),
				"(30min, 60min] : " + expore_more_half_hour_count);
		WriteData.processMessage(config.getString("local.path.imp.stat.analyse.result"),
				"(1hour, 2hour] : " + expore_more_one_hour_count);
		WriteData.processMessage(config.getString("local.path.imp.stat.analyse.result"),
				"(2hour, 3hour] : " + expore_more_two_hour_count);
		WriteData.processMessage(config.getString("local.path.imp.stat.analyse.result"),
				"(3hour, 6hour] : " + expore_more_three_hour_count);
		WriteData.processMessage(config.getString("local.path.imp.stat.analyse.result"),
				"(6hour, 24hour] : " + expore_more_six_hour_count);
		WriteData.processMessage(config.getString("local.path.imp.stat.analyse.result"),
				"(24hour, 48hour] : " + expore_more_one_day_count);
		WriteData.processMessage(config.getString("local.path.imp.stat.analyse.result"),
				"(48hour, ~ : " + expore_more_two_day_count);
		WriteData.processMessage(config.getString("local.path.imp.stat.analyse.result"),
				"all count is : " + all_expinfo_ids_map.size());

		WriteData.processMessage(config.getString("local.path.imp.stat.analyse.result"), "two hour expinfo data analyse");
		WriteData.processMessage(config.getString("local.path.imp.stat.analyse.result"), two_hour_expinfo_impression_map);

		WriteData.processMessage(config.getString("local.path.imp.stat.analyse.result"), "");

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
		GetFlowExploreTTLRedisData service = new GetFlowExploreTTLRedisData();
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
