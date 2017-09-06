package com.inveno.news.reformat.convert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.github.panhongan.util.conf.Config;
import com.github.panhongan.util.db.JedisUtil;
import com.github.panhongan.util.hash.SimplePartitioner;
import com.inveno.news.reformat.constant.EventID;
import com.inveno.news.reformat.constant.KafkaTopic;
import com.inveno.news.reformat.schema.ad.AdClickExtra;
import com.inveno.news.reformat.schema.ad.AdImpressionExtra;
import com.inveno.news.reformat.schema.ad.Delivery;
import com.inveno.news.reformat.schema.news.ArticleClickExtra;
import com.inveno.news.reformat.schema.news.ArticleImpressionExtra;
import com.inveno.news.reformat.schema.CommonSchema;
import redis.clients.jedis.JedisCluster;

public class HoneybeeAdConverter extends AbstractConverter {
	
	private static final Logger logger = LoggerFactory.getLogger(HoneybeeAdConverter.class);
	
	private static final String QUERY_AD_DELIVERY_CONFIG_FILE = "../conf/query-ad-delivery.properties";

	private static Map<String, String> event_id_to_kafka_topic = new HashMap<String, String>();
	
	static {
		event_id_to_kafka_topic.put(EventID.IMPRESSION.toString(), KafkaTopic.AD_IMPRESSION_REFORMAT.toString());
		event_id_to_kafka_topic.put(EventID.CLICK.toString(), KafkaTopic.AD_CLICK_REFORMAT.toString());
	}
	
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
		
		synchronized(HoneybeeAdConverter.class) {
			// ad redis cluster
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
			}
		}
		
		return ret;
	}
	
	@Override
	public void uninit() {
		synchronized(HoneybeeAdConverter.class) {
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
		
		try {
			CommonSchema schema = JSONObject.parseObject(str, CommonSchema.class);
			
			if (schema.getArticle_click_extra() != null) {
				ArticleClickExtra article_click_extra = schema.getArticle_click_extra();
				String content_id = article_click_extra.getContent_id();
				
				AdClickExtra ad_click_extra = new AdClickExtra();
				ad_click_extra.setContent_type(article_click_extra.getContent_type());
				
				// query delivery
				try {
					String delivery_str = this.queryDelivery(content_id);
					if (delivery_str != null) {
						ad_click_extra.setDelivery(JSONObject.parseObject(delivery_str, Delivery.class));
						schema.setAd_click_extra(ad_click_extra);
					} else {
						logger.warn("no delivery for ad : {}", content_id);
					}
				} catch (Exception e) {
					logger.warn(e.getMessage(), e);
				}
				
				schema.setArticle_click_extra(null);
			} else if (schema.getArticle_impression_extra() != null) {
				ArticleImpressionExtra article_impression_extra = schema.getArticle_impression_extra();
				String content_id = article_impression_extra.getContent_id();
				
				AdImpressionExtra ad_impression_extra = new AdImpressionExtra();
				ad_impression_extra.setContent_type(article_impression_extra.getContent_type());
				
				// query delivery
				try {
					String delivery_str = this.queryDelivery(content_id);
					if (delivery_str != null) {
						ad_impression_extra.setDelivery(JSONObject.parseObject(delivery_str, Delivery.class));
						schema.setAd_impression_extra(ad_impression_extra);
					} else {
						logger.warn("no delivery for ad : {}", content_id);
					}
				} catch (Exception e) {
					logger.warn(e.getMessage(), e);
				}
				
				schema.setArticle_impression_extra(null);
			} // end if
			
			this.addMessage(schema);
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		}

		return map;
	}

	@Override
	public Object clone() {
		return new HoneybeeAdConverter();
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
