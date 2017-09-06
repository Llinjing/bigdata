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
import com.inveno.news.reformat.constant.ArticleSource;
import com.inveno.news.reformat.constant.ConfigID;
import com.inveno.news.reformat.constant.EventID;
import com.inveno.news.reformat.constant.KafkaTopic;
import com.inveno.news.reformat.constant.ProductID;
import com.inveno.news.reformat.constant.Protocol;
import com.inveno.news.reformat.constant.Strategy;
import com.inveno.news.reformat.schema.CommonSchema;
import com.inveno.news.reformat.schema.ContentPacket;
import com.inveno.news.reformat.schema.UserPacket;
import com.inveno.news.reformat.schema.news.ArticleClickExtra;


public class ClickLogConverter extends AbstractConverter {
	
	private static final Logger logger = LoggerFactory.getLogger(RequestLogConverter.class);
	
	private static final String LOG_TAG = "click";
	
	private static final String IINTERFACE_LOG_TAG = "i";
	
	private static final int LOG_MIN_COLUMNS = 20;
	
	// optimized
	private Map<String, List<String>> map = new HashMap<String, List<String>>();
	
	private List<String> list = new ArrayList<String>();
	
	private int recv_count = 0;
	
	private int reformat_count = 0;
	
	@Override
	public boolean init() {
		return true;
	}
	
	@Override
	public void uninit() {
	}
	
	@Override
	public Object clone() {
		return new ClickLogConverter();
	}

	@Override
	public Map<String, List<String>> convert(String str) {
		if (++recv_count >= 10000) {
			logger.info("recv_count = {}", recv_count);
			recv_count = 0;
		}
		
		map.clear();
		list.clear();
		
		if (str == null) {
			logger.warn("invalid parameter");
			return map;
		}
		
		if (str.startsWith(LOG_TAG) || str.startsWith(IINTERFACE_LOG_TAG)) {	//prefix: click or click_s
			String [] arr_raw = str.split("&");
			String [] arr = ArrayUtil.expand(arr_raw, LOG_MIN_COLUMNS, "");
			if (arr != null && arr.length >= LOG_MIN_COLUMNS) {
				if (!MessageValidator.validate(arr[3], arr[4], arr[11], str)) {
					return map;
				}
				
				if ((arr[4].contentEquals("hotoday_India_Hindi") || arr[4].contentEquals("hotoday_India_English")) && 
						arr[13].compareTo("2.1.3") >= 0 &&
						arr[13].compareTo("2.1.7") < 0) {
					logger.warn("double click report : ", str);
					return map;
				}
				
				if (ProductID.MEIZU.equals(arr[4])) {
					logger.warn("ignore meizu");
					return map;
				}
				
				String content_type = ContentIDHelper.getContentType(arr[11]);
				if (ProductID.ALI.equals(arr[4]) && !content_type.startsWith("advertisement")) {
					logger.warn("ignore ali : {}", str);
					return map;
				}
				
				CommonSchema schema = new CommonSchema();
				schema.setLog_type(arr[0]);
				schema.setProtocol(Protocol.getProtocol(arr[0]));
				schema.setGate_ip(arr[1]);
				schema.setLog_time(arr[2]);
				schema.setUid(arr[3]);
				schema.setApp(arr[4]);
				schema.setModel(arr[5]);
				schema.setNetwork(arr[9].toLowerCase());
				schema.setApp_ver(arr[13]);
				schema.setPromotion(arr[16]);
				schema.setEvent_time(String.valueOf(TimeUtil.dateToSec(arr[2], "yyyy-MM-dd HH:mm:ss")));
				schema.setEvent_id(EventID.CLICK.toString());
				
				String recomm_tag = arr[15];
				if (StringUtil.isEmpty(recomm_tag)) {
					recomm_tag = arr[12];
				}
				
				SpecialFieldsFiller.fillSpecialFields(arr[0], arr[4], arr[13], 
						recomm_tag.contentEquals("2"), schema);
				
				this.fillClickExtra(arr, arr_raw.length, schema);
				
				list.add(schema.toString());

				// divide ad and news
				this.divideAdAndNews(list);
				
				// reformat counter
				if (++reformat_count >= 10000) {
					logger.info("reformat_count = {}", reformat_count);
					reformat_count = 0;
				}
			}
		} else {
			logger.warn("invalid log prefix : {}", str);
		}
		
		return map;
	}
	
	private void divideAdAndNews(List<String> data_list) {
		List<String> article_list = new ArrayList<String>();
		List<String> ad_list = new ArrayList<String>();
		for (String str : data_list) {
			if (str.contains("advertisement_")) {
				ad_list.add(str);
				article_list.add(str);	// need delete
			} else {
				article_list.add(str);
			}
		}
		
		if (!article_list.isEmpty()) {
			map.put(KafkaTopic.CLICK_REFORMAT.toString(), article_list);
		}
		
		if (!ad_list.isEmpty()) {
			map.put(KafkaTopic.GATE_AD.toString(), ad_list);
		}
	}
	
	private void fillClickExtra(String [] arr, int configid_col, CommonSchema schema) {
		ArticleClickExtra extra = new ArticleClickExtra();
		extra.setContent_id(arr[11]);
		extra.setContent_type(ContentIDHelper.getContentType(arr[11]));
		
		String strategy = Strategy.StrategyValueDesc.UNKNOWN.toString();
		String news_configid = ConfigID.UNKNOWN;
		String ad_configid = ConfigID.UNKNOWN;
		String biz_configid = ConfigID.UNKNOWN;
		String source = null;
		
		try {
			JSONObject json_obj = JSONObject.parseObject(arr[configid_col - 1]);
			
			// news_configid
			try {
				news_configid = json_obj.getJSONObject("upack").getString("news_configid");
				if (StringUtil.isEmpty(news_configid)) {
					news_configid = ConfigID.UNKNOWN;
				}
			} catch (Exception e) {
			}
			
			// ad_configid
			try {
				ad_configid = json_obj.getJSONObject("upack").getString("ad_configid");
				if (StringUtil.isEmpty(ad_configid)) {
					ad_configid = ConfigID.UNKNOWN;
				}
			} catch (Exception e) {
			}
			
			// biz_configid
			try {
				biz_configid = json_obj.getJSONObject("upack").getString("biz_configid");
				if (StringUtil.isEmpty(biz_configid)) {
					biz_configid = ConfigID.UNKNOWN;
				}
			} catch (Exception e) {
			}
			
			// strategy
			try {
				strategy = json_obj.getJSONObject("cpack").getString("strategy");
			} catch (Exception e) {
			}
			
			// source
			try {
				source = json_obj.getJSONObject("cpack").getString("source");
			} catch (Exception e) {
			} finally {
				if (StringUtil.isEmpty(source)) {
					source = ArticleSource.UNKNOWN.toString();
				}
			}
		} catch (Exception e) {
		}
		
		ContentPacket cpack = new ContentPacket();
		cpack.setStrategy(Strategy.getStrategy(strategy));
		cpack.setSource(source);
		extra.setCpack(cpack);
		schema.setArticle_click_extra(extra);
		
		UserPacket upack = new UserPacket();
		upack.setNews_configid(news_configid);
		upack.setAd_configid(ad_configid);
		upack.setBiz_configid(biz_configid);
		schema.setUpack(upack);
	}

}
