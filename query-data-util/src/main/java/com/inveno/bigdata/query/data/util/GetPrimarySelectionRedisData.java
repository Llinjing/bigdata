package com.inveno.bigdata.query.data.util;

import java.util.Set;

import org.apache.thrift.TDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.panhongan.util.conf.Config;
import com.github.panhongan.util.db.JedisUtil;
import com.inveno.bigdata.query.data.util.conf.RedisConfig;
import com.inveno.bigdata.query.data.util.file.WriteData;
import com.inveno.bigdata.query.data.util.thrift.ufs.FeederInfo;

import redis.clients.jedis.JedisCluster;

public class GetPrimarySelectionRedisData {
	private static final Logger logger = LoggerFactory.getLogger(GetPrimarySelectionRedisData.class);

	private static final String CLASS_NAME = GetPrimarySelectionRedisData.class.getName();

	private static final String LANGUAGE_SPANISH = "Spanish";

	private static JedisCluster jedis_cluster = null;

	private static RedisConfig redis_config = RedisConfig.getInstance();

	private static Config config = null;

	private static Set<String> all_ids_set = null;

	private static TDeserializer deserializer = new TDeserializer();

	public boolean init() {
		boolean ret = false;

		config = redis_config.getConfig();

		synchronized (GetPrimarySelectionRedisData.class) {
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
		synchronized (GetPrimarySelectionRedisData.class) {
			if (jedis_cluster != null) {
				JedisUtil.closeJedisCluster(jedis_cluster);
				jedis_cluster = null;
			}
		}
	}

	private void queryAllInfoIDs() {
		try {
			all_ids_set = jedis_cluster.zrange(config.getString("redis.key.all.info.ids"), 0, -1);

			//WriteData.processMessage(config.getString("local.path.all.info.ids"), all_ids_set);
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn(e.getMessage(), e);
		}

	}

	private String getMaxWeightLanguage(JSONObject language_version_json_object) {
		String max_weight_language = "";
		double max_weight = 0D;

		try {
			Set<String> key_set = language_version_json_object.keySet();
			for (String key : key_set) {
				double weight = language_version_json_object.getJSONObject(key).getDoubleValue("weight");
				if (weight >= max_weight) {
					max_weight_language = key;
					max_weight = weight;
				}
			}
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		}

		return max_weight_language;
	}

	private String getMaxWeightCategory(String categories, String version) {
		String max_weight_category = "";
		double max_weight = 0D;

		try {
			JSONObject categories_json_object = (JSONObject) JSON.parse(categories);
			JSONObject categories_version = categories_json_object.getJSONObject(version);
			if (categories_version != null) {
				Set<String> key_set = categories_version.keySet();
				for (String key : key_set) {
					double weight = categories_version.getJSONObject(key).getDoubleValue("weight");
					if (weight >= max_weight) {
						max_weight_category = key;
						max_weight = weight;
					}
				}
			}
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		}

		return max_weight_category;
	}

	private Set<String> queryPredictionInfo() {
		try {
			for (String content_id : all_ids_set) {
				String key = config.getString("redis.key.content.prediction.prefix") + "_" + content_id;
				String field = config.getString("redis.key.content.prediction.field");

				byte[] result_tmp = jedis_cluster.hget(key.getBytes(), field.getBytes());

				if (result_tmp != null) {
					FeederInfo feederInfo = new FeederInfo();
					deserializer.deserialize(feederInfo, result_tmp);

					// grep Spanish
					JSONObject language_json_object = (JSONObject) JSON.parse(feederInfo.getLanguage());
					;
					JSONObject language_v2_json_object = language_json_object.getJSONObject("v2");
					JSONObject language_v3_json_object = language_json_object.getJSONObject("v3");
					String language_v2 = getMaxWeightLanguage(language_v2_json_object);
					String language_v3 = getMaxWeightLanguage(language_v3_json_object);

					// content_type
					// 2: video
					// 0x80: big image
					String content_type = feederInfo.getContent_type();

					// category
					String category = getMaxWeightCategory(feederInfo.getCategories(), "v25");

					if (LANGUAGE_SPANISH.contentEquals(language_v2) && LANGUAGE_SPANISH.contentEquals(language_v3)) {// Spanish
						if (!content_type.contentEquals("2")) {// not video
							if (!(content_type.contentEquals("0x80") && category.contentEquals("133"))) {// not memes
								WriteData.processMessage(config.getString("local.path.content.info"),
										feederInfo.toString());
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.warn(e.getMessage(), e);
		}

		return null;
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

		if (!redis_config.isValid()) {
			logger.warn("config is invalid");
			return;
		}

		// request reformat service
		GetPrimarySelectionRedisData service = new GetPrimarySelectionRedisData();
		if (service.init()) {
			logger.info("{} init ok", CLASS_NAME);

			service.queryAllInfoIDs();
			service.queryPredictionInfo();

			service.uninit();
		} else {
			logger.warn("{} init failed", CLASS_NAME);
			service.uninit();
		}

	}
}
