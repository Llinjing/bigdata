package com.inveno.news.reformat.convert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.panhongan.util.StringUtil;
import com.github.panhongan.util.TimeUtil;
import com.github.panhongan.util.collection.ArrayUtil;
import com.inveno.news.reformat.constant.Strategy;
import com.inveno.news.reformat.constant.ArticleSource;
import com.inveno.news.reformat.constant.ConfigID;
import com.inveno.news.reformat.constant.EventID;
import com.inveno.news.reformat.constant.KafkaTopic;
import com.inveno.news.reformat.constant.ProductID;
import com.inveno.news.reformat.constant.Protocol;
import com.inveno.news.reformat.schema.CommonSchema;
import com.inveno.news.reformat.schema.ContentPacket;
import com.inveno.news.reformat.schema.UserPacket;
import com.inveno.news.reformat.schema.news.ArticleImpressionExtra;
import com.inveno.news.reformat.schema.news.ArticleRequestExtra;


public class RequestLogConverter extends AbstractConverter {
	
	private static final Logger logger = LoggerFactory.getLogger(RequestLogConverter.class);
	
	private static final String LOG_TAG = "request";
	
	private static final int LOG_MIN_COLUMNS = 30;	// 根据需要修改
	
	// optimized
	private Map<String, List<String>> map = new HashMap<String, List<String>>();
		
	private List<String> list = new ArrayList<String>();
	
	private Map<String, String> strategy_map = new HashMap<String, String>();
	private Map<String, String> source_map = new HashMap<String, String>();
	
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
		return new RequestLogConverter();
	}

	@Override
	public Map<String, List<String>> convert(String str) {
		if (++recv_count >= 10000) {
			logger.info("recv_count = {}", recv_count);
			recv_count = 0;
		}
		
		map.clear();
		list.clear();
		strategy_map.clear();
		source_map.clear();
		
		if (str == null) {
			logger.warn("invalid parameter");
			return map;
		}
		
		if (str.startsWith(LOG_TAG)) {	//prefix: request or request_s
			String [] arr_raw = str.split("&");
			String [] arr = ArrayUtil.expand(arr_raw, LOG_MIN_COLUMNS, "");
			if (arr != null && arr.length >= LOG_MIN_COLUMNS) {
				if (!MessageValidator.validate(arr[7], arr[3], str)) {
					return map;
				}
				
				if (ProductID.MEIZU.equals(arr[3])) {
					logger.warn("ignore meizu");
					return map;
				}

				try {
					JSONObject json_obj = JSONObject.parseObject(arr[arr_raw.length - 1]);
					JSONArray json_arr = json_obj.getJSONArray("info");
					for (int i = 0; i < json_arr.size(); ++i) {
						JSONObject ele = json_arr.getJSONObject(i);
						try {
							strategy_map.put(ele.getString("id"), 
								Strategy.getStrategy(ele.getJSONObject("cpack").getString("strategy")));
						} catch (Exception e) {
						}
						
						try {
							source_map.put(ele.getString("id"), ele.getJSONObject("cpack").getString("source"));
						} catch (Exception e) {
							source_map.put(ele.getString("id"), ArticleSource.UNKNOWN.toString());
						}
					}
				} catch (Exception e) {
				}
				
				String [] ids = arr[11].split("[, ]");
				if (ids != null && ids.length > 0) {
					for (int i = 0; i < ids.length; ++i) {
						if (ids[i].isEmpty()) {
							continue;
						}

						String content_type = ContentIDHelper.getContentType(ids[i]);
						if (ProductID.ALI.equals(arr[3]) && !content_type.startsWith("advertisement")) {
							logger.warn("ignore ali : newsid = {}", ids[i]);
							continue;
						}
						
						CommonSchema schema = new CommonSchema();
						schema.setOrder_num(Integer.toString(i+1));
						schema.setLog_type(arr[0]);
						schema.setProtocol(Protocol.getProtocol(arr[0]));
						schema.setGate_ip(arr[1]);
						schema.setLog_time(arr[2]);
						schema.setApp(arr[3]);
						schema.setModel(arr[4]);
						schema.setApp_ver(arr[5]);
						schema.setUid(arr[7]);
						schema.setNetwork(arr[9].toLowerCase());
						schema.setMcc(arr[16]);
						schema.setOsv(arr[17]);
						schema.setEvent_id(EventID.REQUEST.toString());
						schema.setEvent_time(String.valueOf(TimeUtil.dateToSec(arr[2], "yyyy-MM-dd HH:mm:ss")));
						
						// fill special fields
						SpecialFieldsFiller.fillSpecialFields(arr[0], arr[3], arr[5], 
								arr[18].contentEquals("recomm"), schema);
						
						this.fillRequestExtra(arr, arr_raw.length, ids[i], schema);
						
						list.add(schema.toString());
					}
				}
			}
		} // end startsWith("request")
		
		if (!list.isEmpty()) {
			this.divideAdAndNews(list);
		} else {
			logger.warn("no contentid : {}", str);
		}
		
		// reformat counter
		reformat_count += list.size();
		if(reformat_count >= 10000) {
			logger.info("reformat_count = {}", reformat_count);
			reformat_count = 0;
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
			map.put(KafkaTopic.REQUEST_REFORMAT.toString(), article_list);
		}
		if (!ad_list.isEmpty()) {
			map.put(KafkaTopic.GATE_AD.toString(), ad_list);
		}
	}
	
	private void fillRequestExtra(String [] arr, int configid_col, String newsid, CommonSchema schema) {
		ArticleImpressionExtra extra = new ArticleImpressionExtra();
		extra.setContent_id(newsid);
		extra.setContent_type(ContentIDHelper.getContentType(newsid));
		extra.setServer_time(arr[2]);

		ContentPacket cpack = new ContentPacket();
		String strategy = strategy_map.get(newsid);
		if (strategy == null) {
			strategy = Strategy.StrategyValueDesc.UNKNOWN.toString();
		}
		cpack.setStrategy(strategy);
		String source = source_map.get(newsid);
		if (StringUtil.isEmpty(source)) {
			cpack.setSource(ArticleSource.UNKNOWN.toString());
		}else {
			cpack.setSource(source);
		}
		
		extra.setCpack(cpack);
		schema.setArticle_impression_extra(extra);
		
		// upack
		String news_configid = ConfigID.UNKNOWN;
		String ad_configid = ConfigID.UNKNOWN;
		String biz_configid = ConfigID.UNKNOWN;
		
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
		} catch (Exception e) {
		}
		UserPacket upack = new UserPacket();
		upack.setNews_configid(news_configid);
		upack.setAd_configid(ad_configid);
		upack.setBiz_configid(biz_configid);
		schema.setUpack(upack);
		
		// request
		schema.setArticle_request_extra(JSONObject.parseObject(extra.toString(), ArticleRequestExtra.class));
	}

}
