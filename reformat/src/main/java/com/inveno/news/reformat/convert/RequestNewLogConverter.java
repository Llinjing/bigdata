package com.inveno.news.reformat.convert;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.panhongan.util.StringUtil;
import com.github.panhongan.util.collection.ArrayUtil;
import com.inveno.news.reformat.constant.ArticleSource;
import com.inveno.news.reformat.constant.ConfigID;
import com.inveno.news.reformat.constant.ContentType;
import com.inveno.news.reformat.constant.Display;
import com.inveno.news.reformat.constant.EventID;
import com.inveno.news.reformat.constant.KafkaTopic;
import com.inveno.news.reformat.constant.LinkType;
import com.inveno.news.reformat.constant.Network;
import com.inveno.news.reformat.constant.ProductID;
import com.inveno.news.reformat.constant.ScenarioType;
import com.inveno.news.reformat.constant.Strategy;
import com.inveno.news.reformat.schema.CommonSchema;
import com.inveno.news.reformat.schema.ContentPacket;
import com.inveno.news.reformat.schema.UserPacket;
import com.inveno.news.reformat.schema.news.ArticleImpressionExtra;
import com.inveno.news.reformat.schema.news.ArticleRequestExtra;
import com.inveno.news.reformat.schema.news.Scenario;

import org.apache.commons.codec.binary.Base64;

public class RequestNewLogConverter extends AbstractConverter {

	private static final Logger logger = LoggerFactory.getLogger(RequestNewLogConverter.class);

	private static final String LOG_TAG = "request_n";

	private static final int LOG_MIN_COLUMNS = 40; // 根据需要修改

	private static Base64 base64 = new Base64();

	private int recv_count = 0;

	private int reformat_count = 0;

	// optimized
	private Map<String, List<String>> map = new HashMap<String, List<String>>();

	private List<String> list = new ArrayList<String>();
	
	@Override
	public boolean init() {
		return true;
	}
	
	@Override
	public void uninit() {
	}

	@Override
	public Object clone() {
		return new RequestNewLogConverter();
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

		if (str.startsWith(LOG_TAG)) {
			String[] arr_raw = str.split("&");
			String[] arr = ArrayUtil.expand(arr_raw, LOG_MIN_COLUMNS, "");
			if (arr != null && arr.length >= LOG_MIN_COLUMNS) {
				if (!MessageValidator.validate(arr[6], arr[4], str)) {
					return map;
				}

				try {
					JSONObject json_obj = JSONObject.parseObject(URLDecoder.decode(arr[arr_raw.length - 1], "utf-8"));
					JSONArray json_arr = json_obj.getJSONArray("data");
					for (int i = 0; i < json_arr.size(); ++i) {
						CommonSchema schema = this.parseRequestExtra(arr, arr_raw.length, json_arr.getJSONObject(i), i+1);
						if (schema != null) {
							list.add(schema.toString());
						}
					}
				} catch (Exception e) {
					logger.warn(e.getMessage(), e);
				}
			} // end if
		} else {
			logger.warn("invalid log prefix : {}", str);
		}

		if (!list.isEmpty()) {
			map.put(KafkaTopic.REQUEST_REFORMAT.toString(), list);
		} else {
			logger.warn("no available data : {}", str);
		}

		// reformat counter
		reformat_count += list.size();
		if (reformat_count >= 10000) {
			logger.info("reformat_count = {}", reformat_count);
			reformat_count = 0;
		}

		return map;
	}

	private CommonSchema parseRequestExtra(String [] arr, int configid_col, JSONObject json_obj, int order_num) {		
		CommonSchema schema = new CommonSchema();
		schema.setOrder_num(Integer.toString(order_num));
		schema.setLog_type(LOG_TAG);
		schema.setGate_ip(arr[1]);
		schema.setLog_time(arr[2].replace("|", ""));
		schema.setProduct_id(arr[4]);
		schema.setPromotion(arr[5].toLowerCase());
		schema.setUid(arr[6]);
		schema.setEvent_time(arr[7]);
		if (!StringUtil.isEmpty(arr[8])) {
			schema.setFuid(arr[8]);
		}
		schema.setApp_ver(arr[9]);
		if (ProductID.HOTODAY.equals(arr[4])) {
			String [] ver_arr = arr[9].split("[.]");
			if (ver_arr != null && ver_arr.length >= 6) {
				schema.setApp_ver(ver_arr[0] + "." + ver_arr[1] + "." + ver_arr[2] + 
						"." + ver_arr[3] + "." + ver_arr[4] + "." + ver_arr[5]);
			}
		}  else if (ProductID.ALI.equals(arr[4])) {
			int pos = arr[9].indexOf('*');
			if (pos != -1) {
				schema.setApp_ver(arr[9].substring(0, pos));
			}
		}
		
		if (!StringUtil.isEmpty(arr[10])) {
			schema.setSdk_ver(arr[10]);
		}
		schema.setApi_ver(arr[11]);
		schema.setTk(arr[12]);
		schema.setNetwork(Network.getNetwork(arr[13]));
		if (!StringUtil.isEmpty(arr[14])) {
			schema.setImei(arr[14]);
		}
		if (!StringUtil.isEmpty(arr[15])) {
			schema.setAid(arr[15]);
		}
		if (!StringUtil.isEmpty(arr[16])) {
			schema.setIdfa(arr[16]);
		}
		schema.setBrand(arr[17]);
		schema.setModel(arr[18]);
		if (!StringUtil.isEmpty(arr[19])) {
			schema.setOsv(arr[19]);
		}
		schema.setPlatform(arr[20]);
		if (!StringUtil.isEmpty(arr[21])) {
			schema.setLanguage(arr[21].toLowerCase());
		}
		schema.setApp_lan(arr[22].toLowerCase());
		if (!StringUtil.isEmpty(arr[23])) {
			schema.setMcc(arr[23]);
		}
		if (!StringUtil.isEmpty(arr[24])) {
			schema.setMnc(arr[24]);
		}
		if (!StringUtil.isEmpty(arr[25])) {
			schema.setNmcc(arr[25]);
		}
		if (!StringUtil.isEmpty(arr[26])) {
			schema.setNmnc(arr[26]);
		}
		
		schema.setEvent_id(EventID.REQUEST.toString());
		
		// upack
		String news_configid = ConfigID.UNKNOWN;
		String ad_configid = ConfigID.UNKNOWN;
		String biz_configid = ConfigID.UNKNOWN;

		try {
			JSONObject configid_json_obj = JSONObject.parseObject(URLDecoder.decode(arr[configid_col - 1], "utf-8"));
			String upack_decode_str = new String(base64.decode(configid_json_obj.getString("upack")));
			JSONObject obj = JSONObject.parseObject(upack_decode_str);

			// news_configid
			try {
				news_configid = obj.getString("news_configid");
				if (StringUtil.isEmpty(news_configid)) {
					news_configid = ConfigID.UNKNOWN;
				}
			} catch (Exception e) {
			}

			// ad_configid
			try {
				ad_configid = obj.getString("ad_configid");
				if (StringUtil.isEmpty(ad_configid)) {
					ad_configid = ConfigID.UNKNOWN;
				}
			} catch (Exception e) {
			}

			// biz_configid
			try {
				biz_configid = obj.getString("biz_configid");
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
			
		// scenario
		schema.setScenario(Scenario.parseScenario(arr[4], arr[32]));
		
		// extra
		ArticleImpressionExtra extra = new ArticleImpressionExtra();
		extra.setContent_id(json_obj.getString("content_id"));
		extra.setContent_type(ContentType.getContentType(json_obj.getString("content_type")));
		extra.setDisplay(Display.getDisplay(json_obj.getString("display")));
		extra.setLink_type(LinkType.getLinkType(json_obj.getString("link_type")));
		
		ContentPacket cpack = new ContentPacket();
		try {
			String cpack_str = json_obj.getString("cpack");
			String cpack_decode_str = new String(base64.decode(cpack_str));
			JSONObject cpack_json = JSONObject.parseObject(cpack_decode_str);
			
			cpack.setStrategy(Strategy.getStrategy(cpack_json.getString("strategy")));
			String source = cpack_json.getString("source");
			if (StringUtil.isEmpty(source)) {
				cpack.setSource(ArticleSource.UNKNOWN.toString());
			} else {
				cpack.setSource(source);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		}
		extra.setCpack(cpack);
		
		schema.setArticle_impression_extra(extra);
		
		// revise scenario-channel
		Scenario scenario = schema.getScenario();
		if (schema.getScenario() != null && 
				ScenarioType.Channel.UNKNOWN.contentEquals(scenario.getChannel_desc())) {
			if (Strategy.StrategyValueDesc.PUSH.equals(cpack.getStrategy()) ||
					Strategy.StrategyValueDesc.RELATIVE_RECOMMENDATION.equals(cpack.getStrategy())) {
				scenario.setChannel_desc(cpack.getStrategy());
			}
		}
		
		schema.setArticle_request_extra(JSONObject.parseObject(extra.toString(), ArticleRequestExtra.class));
		
		return schema;
	}

}
