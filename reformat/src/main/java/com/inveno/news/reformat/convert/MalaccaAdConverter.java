package com.inveno.news.reformat.convert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.github.panhongan.util.StringUtil;
import com.github.panhongan.util.TimeUtil;
import com.github.panhongan.util.collection.ArrayUtil;
import com.github.panhongan.util.conf.Config;
import com.github.panhongan.util.db.JedisUtil;
import com.github.panhongan.util.hash.SimplePartitioner;
import com.inveno.news.reformat.constant.AdSource;
import com.inveno.news.reformat.constant.AdSpaceType;
import com.inveno.news.reformat.constant.ConfigID;
import com.inveno.news.reformat.constant.EventID;
import com.inveno.news.reformat.constant.KafkaTopic;
import com.inveno.news.reformat.constant.LogType;
import com.inveno.news.reformat.constant.Network;
import com.inveno.news.reformat.constant.ProductID;
import com.inveno.news.reformat.schema.ad.Ad;
import com.inveno.news.reformat.schema.ad.AdClickExtra;
import com.inveno.news.reformat.schema.ad.AdImpressionExtra;
import com.inveno.news.reformat.schema.ad.AdScenario;
import com.inveno.news.reformat.schema.ad.Delivery;
import com.inveno.news.reformat.schema.ad.Marketing;
import com.inveno.news.reformat.schema.CommonSchema;
import com.inveno.news.reformat.schema.UserPacket;

import redis.clients.jedis.JedisCluster;

public class MalaccaAdConverter extends AbstractConverter {
	
	private static final Logger logger = LoggerFactory.getLogger(MalaccaAdConverter.class);
	
	private static final String LOG_TAG = "ad_report";
	
	private static final int LOG_MIN_COLUMNS = 30;
	
	private static Map<String, String> product_id_map = new HashMap<String, String>();
	
	private static final String QUERY_UPACK_CONFIG_FILE = "../conf/query-upack.properties";
	
	private static final String QUERY_AD_DELIVERY_CONFIG_FILE = "../conf/query-ad-delivery.properties";

	private static Map<String, String> event_id_to_kafka_topic = new HashMap<String, String>();
	
	static {
		product_id_map.put("1", ProductID.XIAOZHI.toString());
		product_id_map.put("2", ProductID.H5CHN.toString());
		product_id_map.put("3", ProductID.COOLPAD.toString());
		product_id_map.put("8", ProductID.H5CHN.toString());
		product_id_map.put("10", ProductID.GIONEE_FUYIPING.toString());
		product_id_map.put("12", ProductID.GIONEE_FUYIPING.toString());
		product_id_map.put("13", ProductID.ANGDA.toString());
		product_id_map.put("14", ProductID.QYGB.toString());
		product_id_map.put("15", ProductID.FIREFOX_BROWSER_H5.toString());
		product_id_map.put("16", ProductID.GIONEE_CALENDAR_H5_LIST.toString());
		product_id_map.put("19", ProductID.LENOVO_FUYIPING_SDK.toString());
		product_id_map.put("20", ProductID.KUBI_SDK.toString());
		product_id_map.put("21", ProductID.QYSDK.toString());
		product_id_map.put("25", ProductID.QIEZIKUAICHUAN.toString());
		product_id_map.put("27", ProductID.FLASHLOCK.toString());
		product_id_map.put("28", ProductID.PJCOM.toString());
		product_id_map.put("29", ProductID.MEIZU.toString());
		product_id_map.put("30", ProductID.FLASHLOCK.toString());
		product_id_map.put("36", ProductID.ALI.toString());
		product_id_map.put("37", ProductID.ALI.toString());
		product_id_map.put("38", ProductID.BJTIANYI.toString());
		product_id_map.put("39", ProductID.XIAOLAJIAO.toString());
		product_id_map.put("41", ProductID.LENOVO_H5.toString());
		product_id_map.put("42", ProductID.GIONEE_FUYIPING.toString());
		product_id_map.put("43", ProductID.GIONEE_FUYIPING.toString());
		product_id_map.put("44", ProductID.MOXIU_ZHUOMIAN.toString());
		product_id_map.put("45", ProductID.KUBI_SDK.toString());
		product_id_map.put("46", ProductID.KUBI_SDK_H5.toString());
		product_id_map.put("47", ProductID.WIFIYAOSHI.toString());
		product_id_map.put("60", ProductID.ZUIMEITIANQI.toString());
		product_id_map.put("61", ProductID.MOXIU_ZHUOMIAN.toString());
		product_id_map.put("62", ProductID.MOXIU_ZHUOMIAN.toString());
		product_id_map.put("63", ProductID.FLASHLOCK.toString());
		product_id_map.put("64", ProductID.THEME_LOCK.toString());
		product_id_map.put("65", ProductID.TCL.toString());
		product_id_map.put("66", ProductID.DUOWEI.toString());
		product_id_map.put("67", ProductID.XIAOLAJIAO.toString());
		product_id_map.put("68", ProductID.FIREFOX.toString());
		product_id_map.put("69", ProductID.TIANYU.toString());
		product_id_map.put("70", ProductID.HUISUOPING.toString());
		product_id_map.put("71", ProductID.LENOVO_WIDGET.toString());
		product_id_map.put("72", ProductID.HUAWEI_APK.toString());
		product_id_map.put("73", ProductID.HUAWEI_PAD.toString());
		product_id_map.put("74", ProductID.MEIZU.toString());
		product_id_map.put("76", ProductID.ZUIMEITIANQI_NEW.toString());
		product_id_map.put("78", ProductID.NUBIABROWSER.toString());
		product_id_map.put("79", ProductID.MOXIU_SEARCH.toString());
		product_id_map.put("80", ProductID.MOXIU_SUOPING_NEW.toString());
		product_id_map.put("83", ProductID.MOXIU_ZHUOMIAN_NEW.toString());
		
		event_id_to_kafka_topic.put(EventID.IMPRESSION.toString(), KafkaTopic.AD_IMPRESSION_REFORMAT.toString());
		event_id_to_kafka_topic.put(EventID.CLICK.toString(), KafkaTopic.AD_CLICK_REFORMAT.toString());
	}

	private static Config query_upack_config = new Config();
	
	private static Config query_ad_config = new Config();
	
	private static JedisCluster jedis_cluster_upack = null;
	
	private static JedisCluster jedis_cluster_ad = null;
	
	// optimized
	private Map<String, List<String>> map = new HashMap<String, List<String>>();
		
	private List<String> list = new ArrayList<String>();
	
	private int recv_count = 0;
	
	private int reformat_count = 0;
	
	@Override
	public boolean init() {
		boolean ret = false;
		
		synchronized(MalaccaAdConverter.class) {
			// upack jedis cluster
			if (jedis_cluster_upack == null) {
				ret = query_upack_config.parse(QUERY_UPACK_CONFIG_FILE);
				if (!ret) {
					logger.warn("parse conf file failed : {}", QUERY_UPACK_CONFIG_FILE);
				} else {
					jedis_cluster_upack = JedisUtil.createJedisCluster(QUERY_UPACK_CONFIG_FILE);
				}
			}
			
			ret = (jedis_cluster_upack != null);
			if (!ret) {
				logger.warn("connect to upack redis_cluster failed");
				return ret;
			}
			
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
		synchronized(MalaccaAdConverter.class) {
			if (jedis_cluster_upack != null) {
				JedisUtil.closeJedisCluster(jedis_cluster_upack);
				jedis_cluster_upack = null;
			}
			
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
		
		String [] arr_raw = str.split("[|&]");
		String [] arr = ArrayUtil.expand(arr_raw, LOG_MIN_COLUMNS, "");
		if (arr != null && arr[0].endsWith(LOG_TAG) && arr.length >= LOG_MIN_COLUMNS) {
			if (!"1".equals(arr[4]) &&
					!"2".equals(arr[4])) {
				//logger.warn("ignore event : {}", str);
				return map;
			}
			
			CommonSchema schema = new CommonSchema();
			if ("2".equals(arr[27])) {
				schema.setLog_type(LogType.THIRD_MALACCA_AD.toString());
			} else {
				schema.setLog_type(LogType.MALACCA_AD.toString());
			}
			schema.setGate_ip(arr[1]);
			schema.setProtocol(arr[3]);

			if ("1".contentEquals(arr[4])) {
				schema.setEvent_id(EventID.IMPRESSION.toString());
			} else if ("2".contentEquals(arr[4])) {
				schema.setEvent_id(EventID.CLICK.toString());
			}
			
			try {
				schema.setEvent_time(String.valueOf(Long.valueOf(arr[5]).longValue() / 1000));
			} catch (Exception e) {
				schema.setEvent_time(String.valueOf(TimeUtil.currTime()));
			}
			
			schema.setProduct_id(product_id_map.get(arr[8]));
			//schema.setPromotion(arr[9]);
			schema.setApp_ver(arr[10]);
			schema.setUid(arr[16]);
			schema.setModel(arr[17]);
			schema.setImei(arr[18]);
			schema.setNetwork(Network.getNetwork(arr[20]));
			
			try {
				schema.setLog_time(TimeUtil.secToDate(Long.valueOf(arr[21]).longValue() / 1000, "yyyy-MM-dd HH:mm:ss"));
			} catch (Exception e) {
				schema.setLog_time(TimeUtil.currTime("yyyy-MM-dd HH:mm:ss"));
			}

			String content_id = arr[7];
			
			int event_id = Integer.valueOf(schema.getEvent_id());
			
			if (EventID.CLICK.equals(event_id)) {
				this.parseClick(arr, content_id, schema);
			} else if (EventID.IMPRESSION.equals(event_id)) {
				this.parseImpression(arr, content_id, schema);
			}
			
			// query upack
			try {
				String upack_str = this.queryUpack(schema.getUid(), schema.getProduct_id());
				if (upack_str != null) {
					schema.setUpack(JSONObject.parseObject(upack_str, UserPacket.class));
				} else {
					logger.warn("no upack, uid = {}, product_id = {}", schema.getUid(), schema.getProduct_id());
				}
			} catch (Exception e) {
				logger.warn(e.getMessage(), e);
			}
			
			if (schema.getUpack() == null) {
				UserPacket upack = new UserPacket();
				upack.setNews_configid(ConfigID.UNKNOWN);
				upack.setAd_configid(ConfigID.UNKNOWN);
				upack.setBiz_configid(ConfigID.UNKNOWN);
				schema.setUpack(upack);
			}
			
			this.addMessage(schema);
		} else {
			logger.warn("invalid data : {}", str);
		}
		
		return map;
	}

	@Override
	public Object clone() {
		return new MalaccaAdConverter();
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
		marketing.setPosition_id(arr[11]);
		marketing.setPosition_type(AdSpaceType.getAdSpaceType(arr[12]));
		if (!StringUtil.isEmpty(arr[28])) {
			marketing.setThird_position_id(arr[28]);
		}
		delivery.setMarketing(marketing);
		
		// ad
		Ad ad = delivery.getAd();
		if (ad == null) {
			ad = new Ad();
		}
		ad.setAd_source(AdSource.getAdSource(arr[25]));
		if (!StringUtil.isEmpty(arr[29])) {
			ad.setThird_ad_source(arr[29]);
		}
		delivery.setAd(ad);
		delivery.setDelivery_id(ContentIDHelper.getUnitAdId(ad.getAd_source(), content_id));
		
		// scenario
		AdScenario ad_scenario = new AdScenario();
		ad_scenario.setChannel_id(arr[9]);
		if ("hotoday".contentEquals(arr[9]) ||
				"piemediah5".contentEquals(arr[9])) {
			ad_scenario.setChannel_desc("detail_page");
		} else {
			ad_scenario.setChannel_desc("longlist_page");
		}
		delivery.setAd_scenario(ad_scenario);
		
		extra.setDelivery(delivery);
		schema.setAd_click_extra(extra);
	}
	
	private void parseImpression(String [] arr, String content_id, CommonSchema schema) {
		AdImpressionExtra extra = new AdImpressionExtra();
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
		marketing.setPosition_id(arr[11]);
		marketing.setPosition_type(AdSpaceType.getAdSpaceType(arr[12]));
		if (!StringUtil.isEmpty(arr[28])) {
			marketing.setThird_position_id(arr[28]);
		}
		delivery.setMarketing(marketing);
		
		// ad
		Ad ad = delivery.getAd();
		if (ad == null) {
			ad = new Ad();
		}
		ad.setAd_source(AdSource.getAdSource(arr[25]));
		if (!StringUtil.isEmpty(arr[29])) {
			ad.setThird_ad_source(arr[29]);
		}
		delivery.setAd(ad);
		delivery.setDelivery_id(ContentIDHelper.getUnitAdId(ad.getAd_source(), content_id));

		// scenario
		AdScenario ad_scenario = new AdScenario();
		ad_scenario.setChannel_id(arr[9]);
		if ("hotoday".contentEquals(arr[9]) ||
				"piemediah5".contentEquals(arr[9])) {
			ad_scenario.setChannel_desc("detail_page");
		} else {
			ad_scenario.setChannel_desc("longlist_page");
		}
		delivery.setAd_scenario(ad_scenario);
		
		extra.setDelivery(delivery);
		schema.setAd_impression_extra(extra);
	}
	
	private void addMessage(CommonSchema schema) {
		if (schema != null) {
			String dst_kafka_topic = event_id_to_kafka_topic.get(schema.getEvent_id());
			if (dst_kafka_topic != null) {
				list.add(schema.toString());
				map.put(dst_kafka_topic, list);
				
				// reformat counter
				if (++reformat_count >= 1000) {
					logger.info("reformat_count = {}", reformat_count);
					reformat_count = 0;
				}
			}
		}
	}

	private String queryUpack(String uid, String product_id) {
		String upack = null;

		try {
			String key = uid + "###" + product_id;
			String hash_key = query_upack_config.getString("redis.hash.key.upack.prefix") + "-" + 
		               SimplePartitioner.partition(key, query_upack_config.getInt("redis.hash.key.upack.partitions"));   
			upack = jedis_cluster_upack.hget(hash_key, key);
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		}
		
		return upack;
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
