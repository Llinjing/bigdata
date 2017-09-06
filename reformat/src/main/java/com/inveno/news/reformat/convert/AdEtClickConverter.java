package com.inveno.news.reformat.convert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.github.panhongan.util.collection.ArrayUtil;
import com.github.panhongan.util.conf.Config;
import com.github.panhongan.util.db.JedisUtil;
import com.github.panhongan.util.hash.SimplePartitioner;
import com.inveno.news.reformat.constant.ConfigID;
import com.inveno.news.reformat.constant.EventID;
import com.inveno.news.reformat.constant.KafkaTopic;
import com.inveno.news.reformat.constant.LogType;
import com.inveno.news.reformat.schema.ad.Ad;
import com.inveno.news.reformat.schema.ad.AdClickExtra;
import com.inveno.news.reformat.schema.ad.AdScenario;
import com.inveno.news.reformat.schema.ad.Delivery;
import com.inveno.news.reformat.schema.ad.Marketing;
import com.inveno.news.reformat.schema.CommonSchema;
import com.inveno.news.reformat.schema.UserPacket;

import redis.clients.jedis.JedisCluster;

public class AdEtClickConverter extends AbstractConverter {
	
	private static final Logger logger = LoggerFactory.getLogger(AdEtClickConverter.class);
	
	private static final String LOG_TAG = "ad_et_click";
	
	private static final int LOG_MIN_COLUMNS = 7;
	
	private static final int LOG_HEAD_MIN_COLUMNS = 3;
	
	private static final String QUERY_AD_DELIVERY_CONFIG_FILE = "../conf/query-ad-delivery.properties";

	private static Config query_ad_config = new Config();
	
	private static JedisCluster jedis_cluster_ad = null;
	
	// optimized
	private Map<String, List<String>> map = new HashMap<String, List<String>>();
		
	private List<String> list = new ArrayList<String>();
	
	private int recv_count = 0;
	
	private int reformat_count = 0;
	
	@Override
	public boolean init() {
		boolean ret = false;
		
		synchronized(AdEtClickConverter.class) {
			// ad jedis cluster
			if (jedis_cluster_ad == null) {
				ret = query_ad_config.parse(QUERY_AD_DELIVERY_CONFIG_FILE);
				if (!ret) {
					logger.warn("parse conf file failed : {}", QUERY_AD_DELIVERY_CONFIG_FILE);
				} else {
					jedis_cluster_ad = JedisUtil.createJedisCluster(query_ad_config);
				}
			}
			
			ret = (jedis_cluster_ad != null);
			if (!ret) {
				logger.warn("connect to ad_delivery redis_cluster failed");
				return ret;
			}
		}
		
		return ret;
	}
	
	@Override
	public void uninit() {
		synchronized(AdEtClickConverter.class) {
			if (jedis_cluster_ad != null) {
				JedisUtil.closeJedisCluster(jedis_cluster_ad);
				jedis_cluster_ad = null;
			}
		}
	}
		
	@Override
	public Map<String, List<String>> convert(String str) {
		if (++recv_count >= 1000) {
			logger.info("recv_count = {}", recv_count);
			recv_count = 0;
		}
		
		map.clear();
		list.clear();
		
		if (str == null) {
			return map;
		}
		
		String [] arr_raw = str.split("[|]");
		String [] arr = ArrayUtil.expand(arr_raw, LOG_MIN_COLUMNS, "");
		String [] arr_head_raw = arr[0].split("[&]");
		String [] arr_head = ArrayUtil.expand(arr_head_raw, LOG_HEAD_MIN_COLUMNS, "");
		
		if (arr != null && arr_head != null && arr_head[0].endsWith(LOG_TAG) 
				&& arr.length >= LOG_MIN_COLUMNS && arr_head.length >= LOG_HEAD_MIN_COLUMNS) {
			CommonSchema schema = new CommonSchema();
			schema.setApp_ver("");
			schema.setEvent_id(EventID.CLICK.toString());
			schema.setEvent_time("");
			schema.setGate_ip(arr_head[1]);
			schema.setImei("");
			try {
				schema.setLog_time(arr_head[2]);
			} catch (Exception e) {
				schema.setLog_time("");
			}
			schema.setLog_type(LogType.ET_AD.toString());
			schema.setModel("");
			schema.setNetwork("");
			schema.setProduct_id("");
			schema.setProtocol("");
			schema.setUid(arr[6]);
			
			String content_id = arr[4];
			
			this.parseClick(arr, content_id, schema);
			
			// query upack
			UserPacket upack = new UserPacket();
			upack.setNews_configid(ConfigID.UNKNOWN);
			upack.setAd_configid(ConfigID.UNKNOWN);
			upack.setBiz_configid(ConfigID.UNKNOWN);
			schema.setUpack(upack);
			
			// save reformat data into kafka
			list.add(schema.toString());
			map.put(KafkaTopic.AD_ET_CLICK_REFORMAT.toString(), list);
			
			// reformat counter
			if (++reformat_count >= 1000) {
				logger.info("reformat_count = {}", reformat_count);
				reformat_count = 0;
			}
		} else {
			logger.warn("invalid data : {}", str);
		}
		
		return map;
	}

	@Override
	public Object clone() {
		return new AdEtClickConverter();
	}
	
	private void parseClick(String [] arr, String content_id, CommonSchema schema) {
		AdClickExtra extra = new AdClickExtra();
		extra.setContent_type(ContentIDHelper.getContentType(content_id));
		
		// query delivery
		try {
			String delivery_str = this.queryDelivery(content_id);
			if (delivery_str != null) {
				extra.setDelivery(JSONObject.parseObject(delivery_str, Delivery.class));
			} else {
				logger.warn("no delivery for ad : {}", content_id);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		}
		
		// delivery
		Delivery delivery = extra.getDelivery();
		if (delivery == null) {
			delivery = new Delivery();
		}
		delivery.setDelivery_id(content_id);
		
		// marketing
		Marketing marketing = delivery.getMarketing();
		if (marketing == null) {
			marketing = new Marketing();
		}
		marketing.setPosition_id("");
		marketing.setPosition_type("");
		delivery.setMarketing(marketing);
		
		// ad
		Ad ad = delivery.getAd();
		if (ad == null) {
			ad = new Ad();
		}
		ad.setAd_source(ContentIDHelper.getAdSource(content_id));
		delivery.setAd(ad);
		delivery.setDelivery_id(ContentIDHelper.getUnitAdId(ad.getAd_source(), content_id));
		
		// scenario
		AdScenario ad_scenario = new AdScenario();
		ad_scenario.setChannel_id(arr[5]);
		ad_scenario.setChannel_desc("");
		delivery.setAd_scenario(ad_scenario);
		
		extra.setDelivery(delivery);
		schema.setAd_click_extra(extra);
	}
	
	private String queryDelivery(String delivery_id) {
		String delivery = null;
		
		try {
			String hash_key = query_ad_config.getString("redis.hash.key.ad.prefix") + "-" +
					SimplePartitioner.partition(delivery_id, query_ad_config.getInt("redis.hash.key.ad.partitions"));
			delivery = jedis_cluster_ad.hget(hash_key, delivery_id);
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		}
		
		return delivery;
	}
	
}
