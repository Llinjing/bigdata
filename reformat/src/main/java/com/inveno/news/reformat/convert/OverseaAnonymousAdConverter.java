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
import com.inveno.news.reformat.constant.ArticleSource;
import com.inveno.news.reformat.constant.ConfigID;
import com.inveno.news.reformat.constant.ContentType.ContentTypeValueDesc;
import com.inveno.news.reformat.constant.EventID;
import com.inveno.news.reformat.constant.KafkaTopic;
import com.inveno.news.reformat.constant.ProductID;
import com.inveno.news.reformat.constant.Strategy.StrategyValueDesc;
import com.inveno.news.reformat.schema.CommonSchema;
import com.inveno.news.reformat.schema.ContentPacket;
import com.inveno.news.reformat.schema.UserPacket;
import com.inveno.news.reformat.schema.news.ArticleClickExtra;
import com.inveno.news.reformat.schema.news.ArticleImpressionExtra;

import redis.clients.jedis.JedisCluster;

public class OverseaAnonymousAdConverter extends AbstractConverter {
	
	private static final Logger logger = LoggerFactory.getLogger(OverseaAnonymousAdConverter.class);
	
	private static final String LOG_TAG = "ad_report";
	
	private static final int LOG_MIN_COLUMNS = 25;
	
	private static final String QUERY_CONFIG_FILE = "../conf/query-upack.properties";

	private static Map<String, String> EVENT_ID_TO_KAFKA_TOPIC = new HashMap<String, String>();
	
	static {
		EVENT_ID_TO_KAFKA_TOPIC.put(EventID.IMPRESSION.toString(), KafkaTopic.IMPRESSION_REFORMAT.toString());
		EVENT_ID_TO_KAFKA_TOPIC.put(EventID.CLICK.toString(), KafkaTopic.CLICK_REFORMAT.toString());
	}

	private static Config query_upack_config = new Config();
	
	private static JedisCluster jedis_cluster = null;
	
	// optimized
	private Map<String, List<String>> map = new HashMap<String, List<String>>();
		
	private List<String> list = new ArrayList<String>();
	
	private int recv_count = 0;
	
	private int reformat_count = 0;
	
	@Override
	public boolean init() {
		boolean ret = false;
		
		synchronized(OverseaAnonymousAdConverter.class) {
			if (jedis_cluster == null) {
				ret = query_upack_config.parse(QUERY_CONFIG_FILE);
				if (!ret) {
					logger.warn("parse conf file failed : {}", QUERY_CONFIG_FILE);
				} else {
					jedis_cluster = JedisUtil.createJedisCluster(query_upack_config);
					ret = (jedis_cluster != null);
				}
			} else {
				ret = true;
			}
		}
		
		return ret;
	}
	
	@Override
	public void uninit() {
		synchronized(OverseaAnonymousAdConverter.class) {
			if (jedis_cluster != null) {
				JedisUtil.closeJedisCluster(jedis_cluster);
				jedis_cluster = null;
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
		
		String [] arr_raw = str.replace("&", "|").split("[|]");
		String [] arr = ArrayUtil.expand(arr_raw, LOG_MIN_COLUMNS, "");
		if (arr != null && arr[0].endsWith(LOG_TAG) && arr.length >= LOG_MIN_COLUMNS) {
			if (!"0".equals(arr[3]) &&
					!"1".equals(arr[3])) {
				//logger.warn("ignore event : {}", str);
				return map;
			}
			
			CommonSchema schema = new CommonSchema();
			schema.setLog_type(arr[0]);
			schema.setGate_ip(arr[1]);
			schema.setLog_time(arr[2]);
			if ("0".contentEquals(arr[3])) {
				schema.setEvent_id(EventID.IMPRESSION.toString());
			} else if ("1".contentEquals(arr[3])) {
				schema.setEvent_id(EventID.CLICK.toString());
			}
			schema.setEvent_time(String.valueOf(TimeUtil.dateToSec(schema.getLog_time(), "yyyy-MM-dd HH:mm:ss")));
			schema.setUid(arr[6]);
			schema.setProduct_id(arr[7].toLowerCase());
			schema.setApp_ver(arr[8]);
			if (ProductID.HOTODAY.equals(schema.getProduct_id())) {
				String [] ver_arr = arr[8].split("[.]");
				if (ver_arr != null && ver_arr.length >= 6) {
					schema.setApp_ver(ver_arr[0] + "." + ver_arr[1] + "." + ver_arr[2] + 
							"." + ver_arr[3] + "." + ver_arr[4] + "." + ver_arr[5]);
				}
			}
			
			schema.setOsv(arr[10]);
			schema.setMcc(arr[11]);
			schema.setLanguage(arr[12]);
			schema.setProtocol(arr[13]);
			schema.setPromotion(arr[14]);
			schema.setModel(arr[15]);
			schema.setSdk_ver(arr[16]);
			schema.setAd_source(arr[17]);

			String content_id = arr[21];
			
			int event_id = Integer.valueOf(schema.getEvent_id());
			
			if (EventID.CLICK.equals(event_id)) {
				ArticleClickExtra extra = new ArticleClickExtra();
				extra.setContent_id(content_id);
				extra.setContent_type(ContentTypeValueDesc.ADVERTISEMENT_THIRD_PARTY.toString());
				
				ContentPacket cpack = new ContentPacket();
				cpack.setStrategy(StrategyValueDesc.UNKNOWN.toString());
				cpack.setSource(ArticleSource.UNKNOWN.toString());
				extra.setCpack(cpack);
				schema.setArticle_click_extra(extra);
			} else if (EventID.IMPRESSION.equals(event_id)) {
				ArticleImpressionExtra extra = new ArticleImpressionExtra();
				extra.setContent_id(content_id);
				extra.setContent_type(ContentTypeValueDesc.ADVERTISEMENT_THIRD_PARTY.toString());
				
				ContentPacket cpack = new ContentPacket();
				cpack.setStrategy(StrategyValueDesc.UNKNOWN.toString());
				cpack.setSource(ArticleSource.UNKNOWN.toString());
				extra.setCpack(cpack);
				schema.setArticle_impression_extra(extra);
			}
			
			String upack_str = this.queryUpack(schema.getUid(), schema.getProduct_id());
			if (!StringUtil.isEmpty(upack_str)) {
				try {
					UserPacket upack = JSONObject.parseObject(upack_str, UserPacket.class);
					schema.setUpack(upack);
				} catch (Exception e) {
					logger.warn(e.getMessage(), e);
				}
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
		return new OverseaAnonymousAdConverter();
	}
	
	private void addMessage(CommonSchema schema) {
		if (schema != null) {
			String dst_kafka_topic = EVENT_ID_TO_KAFKA_TOPIC.get(schema.getEvent_id());
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
			String hash_key = query_upack_config.getString("redis.hash.key.prefix") + "-" + 
		               SimplePartitioner.partition(key, query_upack_config.getInt("redis.hash.key.partitions"));   
			upack = jedis_cluster.hget(hash_key, key);
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		}
		
		return upack;
	}
}
