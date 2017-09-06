package com.inveno.news.reformat.convert;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.panhongan.util.StringUtil;
import com.github.panhongan.util.conf.Config;
import com.inveno.news.reformat.ReformatConfig;
import com.inveno.news.reformat.constant.ActionName;
import com.inveno.news.reformat.constant.ContentType;
import com.inveno.news.reformat.constant.Dwelltime;
import com.inveno.news.reformat.constant.EventID;
import com.inveno.news.reformat.constant.KafkaTopic;
import com.inveno.news.reformat.constant.Network;
import com.inveno.news.reformat.constant.ProductID;
import com.inveno.news.reformat.constant.Protocol;
import com.inveno.news.reformat.constant.ScenarioType;
import com.inveno.news.reformat.constant.Strategy;
import com.inveno.news.reformat.schema.CommonSchema;
import com.inveno.news.reformat.schema.ContentPacket;
import com.inveno.news.reformat.schema.news.ActivityDwelltimeExtra;
import com.inveno.news.reformat.schema.news.ArticleClickExtra;
import com.inveno.news.reformat.schema.news.ArticleCompletenessExtra;
import com.inveno.news.reformat.schema.news.ArticleDwelltimeExtra;
import com.inveno.news.reformat.schema.news.ArticleImpressionExtra;
import com.inveno.news.reformat.schema.news.BackendServiceExtra;
import com.inveno.news.reformat.schema.news.ExtendEventExtra;
import com.inveno.news.reformat.schema.news.ListpageDwelltimeExtra;
import com.inveno.news.reformat.schema.news.Refer;
import com.inveno.news.reformat.schema.news.Scenario;
import com.inveno.news.reformat.thrift.ABDyeingClient;

public class ReportZhiziV2Converter extends AbstractConverter {
	
	private static final Logger logger = LoggerFactory.getLogger(ReportZhiziV2Converter.class);

	private static final String LOG_TAG = "report";
	
	private static final int LOG_MIN_COLUMNS = 31;
	
	private static final String ADVERTISEMENT = "advertisement";
	
	private static final String OPEN_SCREEN_AD_POSITION = "0x11";
	
	private static Map<String, String> event_id_to_kafka_topic = new HashMap<String, String>();
	
	private static Base64 base64 = new Base64(); 
	
	private int recv_count = 0;
	
	private int reformat_count = 0;
	
	static {
		event_id_to_kafka_topic.put(EventID.LISTPAGE_DWELLTIME.toString(), KafkaTopic.LISTPAGE_DWELLTIME_REFORMAT.toString());
		event_id_to_kafka_topic.put(EventID.IMPRESSION.toString(), KafkaTopic.IMPRESSION_REFORMAT.toString());
		event_id_to_kafka_topic.put(EventID.CLICK.toString(), KafkaTopic.CLICK_REFORMAT.toString());
		event_id_to_kafka_topic.put(EventID.DWELLTIME.toString(), KafkaTopic.DWELLTIME_REFORMAT.toString());
		event_id_to_kafka_topic.put(EventID.COMPLETENESS.toString(), KafkaTopic.COMPLETENESS_REFORMAT.toString());
		event_id_to_kafka_topic.put(EventID.ACTIVITY_DWELLTIME.toString(), KafkaTopic.ACTIVITY_DWELLTIME_REFORMAT.toString());
		event_id_to_kafka_topic.put(EventID.BACKEND_SERVICE.toString(), KafkaTopic.BACKEND_SERVICE_REFORMAT.toString());		
		event_id_to_kafka_topic.put(EventID.EXTEND_EVENT.toString(), KafkaTopic.EXTEND_EVENT_REFORMAT.toString());
	}
	
	// optimized
	private Map<String, List<String>> map = new HashMap<String, List<String>>();

	private static ReformatConfig config = ReformatConfig.getInstance();
	
	private static Config conf = config.getConfig();
	
	// ABDyeingClient 
	private ABDyeingClient abDyeingClient = null;
	
	@Override
	public boolean init() {
		boolean res = false;
		
		try{
			String ad_dyeing_service_server = conf.getString("ad.dyeing.service.server");
			int ad_dyeing_service_port = conf.getInt("ad.dyeing.service.port");
			
			abDyeingClient = new ABDyeingClient(ad_dyeing_service_server, ad_dyeing_service_port);
			res = abDyeingClient.init();
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		}	
		
		return res;
	}
	
	@Override
	public void uninit() {
		if (abDyeingClient != null){
			abDyeingClient.uninit();
		}
	}

	@Override
	public Map<String, List<String>> convert(String str) {
		if (++recv_count == 1000) {
			logger.info("recv_count = {}", recv_count);
			recv_count = 0;
		}
		
		map.clear();
		
		if (str == null) {
			logger.warn("invalid parameter");
			return map;
		}
		
		if (str.startsWith(LOG_TAG)) {
			String arr [] = str.split("&");
			if (arr != null && arr.length >= LOG_MIN_COLUMNS) {
				if (!MessageValidator.validate(arr[5], arr[3], str)) {
					return map;
				}
				
				try {
					JSONArray json_arr = JSONObject.parseArray(URLDecoder.decode(arr[arr.length - 1], "utf-8"));
					for (int i = 0; i < json_arr.size(); ++i) {
						JSONObject json_obj = json_arr.getJSONObject(i);
						if (json_obj.isEmpty()) {
							continue;
						}
						
						CommonSchema schema = parseExtra(arr, json_obj, arr[3]);
						if (schema != null) {
							this.addMessage(schema);
						} else {
							logger.warn("invalid event : {}", json_obj.toString());
						}
					}
				} catch (Exception e) {
					logger.warn(e.getMessage(), e);
				}
			} else {
				logger.warn("invalid data : {}, field_num = {}", str, arr.length);
			}
		} else {
			logger.warn("invalid data : {}", str);
		}
		
		// reformat counter
		for (String topic : map.keySet()) {
			reformat_count += map.get(topic).size();
		}
		if(reformat_count >= 1000) {
			logger.info("reformat_count = {}", reformat_count);
			reformat_count = 0;
		}
		
		return map;
	}

	@Override
	public Object clone() {
		return new ReportZhiziV2Converter();
	}
	
	private void divideAdAndNews(String dst_topic, List<String> data_list) {
		if (KafkaTopic.CLICK_REFORMAT.equals(dst_topic) ||
				KafkaTopic.IMPRESSION_REFORMAT.equals(dst_topic)) {
			List<String> ad_list = new ArrayList<String>();
			for (String str : data_list) {
				if (str.contains("advertisement_")) {
					ad_list.add(str);
				}
			}
			
			if (!ad_list.isEmpty()) {
				map.put(KafkaTopic.HONEYBEE_AD.toString(), ad_list);
			}
		}
	}
	
	private void addMessage(CommonSchema schema) {
		if (schema != null) {
			String dst_kafka_topic = event_id_to_kafka_topic.get(schema.getEvent_id());
			if (dst_kafka_topic != null) {
				List<String> curr_list = map.get(dst_kafka_topic);
				if (curr_list == null) {
					curr_list = new ArrayList<String>();
				}
				curr_list.add(schema.toString());
				map.put(dst_kafka_topic, curr_list);
				
				// divide ad and news
				//divideAdAndNews(map);
			}
		}
	}
	
	private CommonSchema parseExtra(String [] arr, JSONObject json_obj, String product_id) {
		CommonSchema schema = new CommonSchema();
		schema.setLog_type(LOG_TAG);
		schema.setGate_ip(arr[1]);
		schema.setLog_time(arr[2].replace("|", ""));
		schema.setProduct_id(arr[3]);
		schema.setPromotion(arr[4].toLowerCase());
		schema.setUid(arr[5]);
		if (!StringUtil.isEmpty(arr[6])) {
			schema.setFuid(arr[6]);
		}
		
		schema.setApp_ver(arr[7]);
		if (ProductID.HOTODAY.equals(arr[3])) {
			String [] ver_arr = arr[7].split("[.]");
			if (ver_arr != null && ver_arr.length >= 6) {
				schema.setApp_ver(ver_arr[0] + "." + ver_arr[1] + "." + ver_arr[2] + 
						"." + ver_arr[3] + "." + ver_arr[4] + "." + ver_arr[5]);
			}
		} else if (ProductID.ALI.equals(arr[3])) {
			int pos = arr[7].indexOf('*');
			if (pos != -1) {
				schema.setApp_ver(arr[7].substring(0, pos));
			}
		}
		
		if (!StringUtil.isEmpty(arr[8])) {
			schema.setSdk_ver(arr[8]);
		}
		schema.setApi_ver(arr[9]);
		schema.setTk(arr[10]);
		schema.setReport_time(arr[11]);
		schema.setNetwork(Network.getNetwork(arr[12]));
		schema.setSid(arr[13]);
		schema.setSeq(arr[14]);
		if (!StringUtil.isEmpty(arr[15])) {
			schema.setImei(arr[15]);
		}
		if (!StringUtil.isEmpty(arr[16])) {
			schema.setAid(arr[16]);
		}
		if (!StringUtil.isEmpty(arr[17])) {
			schema.setIdfa(arr[17]);
		}
		schema.setBrand(arr[18]);
		schema.setModel(arr[19]);
		schema.setOsv(arr[20]);
		schema.setPlatform(arr[21].toLowerCase());
		if (!StringUtil.isEmpty(arr[22])) {
			schema.setLanguage(arr[22].toLowerCase());
		}
		schema.setApp_lan(arr[23].toLowerCase());
		if (!StringUtil.isEmpty(arr[24])) {
			schema.setMcc(arr[24]);
		}
		if (!StringUtil.isEmpty(arr[25])) {
			schema.setMnc(arr[25]);
		}
		if (!StringUtil.isEmpty(arr[26])) {
			schema.setNmcc(arr[26]);
		}
		if (!StringUtil.isEmpty(arr[27])) {
			schema.setNmnc(arr[27]);
		}
		
		// upack
		SpecialFieldsFiller.fillUpack(arr[28], schema, abDyeingClient);
		
		schema.setEvent_id(json_obj.getString("event_id"));
		schema.setProtocol(Protocol.HTTPS.toString());
		
		try {
			int event_id = Integer.valueOf(json_obj.getString("event_id")).intValue();
			
			if (event_id == EventID.LISTPAGE_DWELLTIME.getValue()) {
				this.parseListpageDwelltimeExtra(json_obj, schema, product_id);
			} else if (event_id == EventID.IMPRESSION.getValue()) {
				this.parseArticleImpressionExtra(json_obj, schema, product_id);
			} else if (event_id == EventID.CLICK.getValue()) {
				this.parseArticleClickExtra(json_obj, schema, product_id);
			} else if (event_id == EventID.DWELLTIME.getValue()) {
				this.parseArticleDwelltimeExtra(json_obj, schema, product_id);
			} else if (event_id == EventID.COMPLETENESS.getValue()) {
				this.parseArticleCompletenessExtra(json_obj, schema, product_id);
			} else if (event_id == EventID.ACTIVITY_DWELLTIME.getValue()) {
				this.parseActivityExtra(json_obj, schema, product_id);
			} else if (event_id == EventID.BACKEND_SERVICE.getValue()) {
				this.parseBackendServiceExtra(json_obj, schema, product_id);
			} else if (event_id == EventID.EXTEND_EVENT.getValue()) {
				this.parseExtendEventExtra(json_obj, schema, product_id);
			} else {
				schema = null;
				
				logger.warn("invalid event_id : {}", event_id);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		}
		
		return schema;
	}
	
	private void parseListpageDwelltimeExtra(JSONObject json_obj, CommonSchema schema, String product_id) {
		ListpageDwelltimeExtra extra = new ListpageDwelltimeExtra();
		extra.setDwelltime(json_obj.getString("dwelltime"));
		try {
			String view_mode = json_obj.getJSONObject("extra_msg").getString("view_mode");
			extra.setView_mode(view_mode);
		} catch (Exception e) {
		}
		try {
			extra.setExtra_msg(json_obj.getJSONObject("extra_msg"));
		} catch (Exception e) {
		}
		
		schema.setListpage_dwelltime_extra(extra);
		schema.setEvent_time(json_obj.getString("event_time"));
		schema.setScenario(Scenario.parseScenario(product_id, json_obj.getString("scenario")));
	}

	private void parseArticleImpressionExtra(JSONObject json_obj, CommonSchema schema, String product_id) {
		ArticleImpressionExtra extra = new ArticleImpressionExtra();
		extra.setContent_id(json_obj.getString("content_id"));
		
		// cpack
		JSONObject cpack_json = null;
		try {
			String cpack_str = json_obj.getString("cpack");
			String cpack_decode_str = new String(base64.decode(cpack_str));
			cpack_json = JSONObject.parseObject(cpack_decode_str);
		} catch (Exception e) {
			cpack_json = new JSONObject();
		}
		ContentPacket cpack = SpecialFieldsFiller.fillCpack(cpack_json);
		extra.setCpack(cpack);
		
		try {
			extra.setContent_type(ContentType.getContentType(cpack_json.getString("content_type")));
		} catch (Exception e) {
			extra.setContent_type(ContentIDHelper.getContentType(extra.getContent_id()));
		}

		// refer
		try {
			JSONObject obj = JSONObject.parseObject(json_obj.getString("refer"));
			Refer refer = new Refer();
			refer.setContent_id(obj.getString("content_id"));
			refer.setContent_type(ContentType.getContentType(obj.getString("content_type")));
			extra.setRefer(refer);
		} catch (Exception e) {
		}
		
		// view_mode
		try {
			String view_mode = json_obj.getJSONObject("extra_msg").getString("view_mode");
			extra.setView_mode(view_mode);
		} catch (Exception e) {
		}
		try {
			extra.setExtra_msg(json_obj.getJSONObject("extra_msg"));
		} catch (Exception e) {
		}
		
		// server_time
		extra.setServer_time(json_obj.getString("server_time"));
		
		schema.setArticle_impression_extra(extra);
		schema.setEvent_time(json_obj.getString("event_time"));
		schema.setScenario(Scenario.parseScenario(product_id, json_obj.getString("scenario")));
		
		// revise scenario-channel
		Scenario scenario = schema.getScenario();
		if (schema.getScenario() != null && 
				ScenarioType.Channel.UNKNOWN.contentEquals(scenario.getChannel_desc())) {
			if (Strategy.StrategyValueDesc.PUSH.equals(cpack.getStrategy()) ||
					Strategy.StrategyValueDesc.RELATIVE_RECOMMENDATION.equals(cpack.getStrategy())) {
				scenario.setChannel_desc(cpack.getStrategy());
			}
		}
		
		// revise advertisement id
		if (extra.getContent_type().contains(ADVERTISEMENT)) {
			if (!OPEN_SCREEN_AD_POSITION.equals(scenario.getPosition())){
				extra.setContent_id(extra.getCpack().getAd_source() + "_000001");
				schema.setArticle_impression_extra(extra);
			}
		}
		
	}
	
	private void parseArticleClickExtra(JSONObject json_obj, CommonSchema schema, String product_id) {
		ArticleClickExtra extra = new ArticleClickExtra();
		extra.setContent_id(json_obj.getString("content_id"));
		
		// cpack
		JSONObject cpack_json = null;
		try {
			String cpack_str = json_obj.getString("cpack");
			String cpack_decode_str = new String(base64.decode(cpack_str));
			cpack_json = JSONObject.parseObject(cpack_decode_str);
		} catch (Exception e) {
			cpack_json = new JSONObject();
		}
		ContentPacket cpack = SpecialFieldsFiller.fillCpack(cpack_json);
		extra.setCpack(cpack);
		
		try {
			extra.setContent_type(ContentType.getContentType(cpack_json.getString("content_type")));
		} catch (Exception e) {
			extra.setContent_type(ContentIDHelper.getContentType(extra.getContent_id()));
		}
		
		// refer
		try {
			JSONObject obj = JSONObject.parseObject(json_obj.getString("refer"));
			Refer refer = new Refer();
			refer.setContent_id(obj.getString("content_id"));
			refer.setContent_type(ContentType.getContentType(obj.getString("content_type")));
			extra.setRefer(refer);
		} catch (Exception e) {
		}
		
		// view_mode
		try {
			String view_mode = json_obj.getJSONObject("extra_msg").getString("view_mode");
			extra.setView_mode(view_mode);
		} catch (Exception e) {
		}
		try {
			extra.setExtra_msg(json_obj.getJSONObject("extra_msg"));
		} catch (Exception e) {
		}
		
		// click_type
		extra.setClick_type(json_obj.getString("click_type"));
		
		schema.setArticle_click_extra(extra);
		schema.setEvent_time(json_obj.getString("event_time"));
		schema.setScenario(Scenario.parseScenario(product_id, json_obj.getString("scenario")));
		
		// revise scenario-channel
		Scenario scenario = schema.getScenario();
		if (schema.getScenario() != null && 
				ScenarioType.Channel.UNKNOWN.contentEquals(scenario.getChannel_desc())) {
			if (Strategy.StrategyValueDesc.PUSH.equals(cpack.getStrategy()) ||
					Strategy.StrategyValueDesc.RELATIVE_RECOMMENDATION.equals(cpack.getStrategy())) {
				scenario.setChannel_desc(cpack.getStrategy());
			}
		}
		
		// revise advertisement id
		if (extra.getContent_type().contains(ADVERTISEMENT)) {
			if (!OPEN_SCREEN_AD_POSITION.equals(scenario.getPosition())){
				extra.setContent_id(extra.getCpack().getAd_source() + "_000001");
				schema.setArticle_click_extra(extra);
			}
		}
	}
	
	private void parseArticleDwelltimeExtra(JSONObject json_obj, CommonSchema schema, String product_id) {
		ArticleDwelltimeExtra extra = new ArticleDwelltimeExtra();
		extra.setContent_id(json_obj.getString("content_id"));
		
		// cpack
		JSONObject cpack_json = null;
		try {
			String cpack_str = json_obj.getString("cpack");
			String cpack_decode_str = new String(base64.decode(cpack_str));
			cpack_json = JSONObject.parseObject(cpack_decode_str);
		} catch (Exception e) {
			cpack_json = new JSONObject();
		}
		ContentPacket cpack = SpecialFieldsFiller.fillCpack(cpack_json);
		extra.setCpack(cpack);
		
		try {
			extra.setContent_type(ContentType.getContentType(cpack_json.getString("content_type")));
		} catch (Exception e) {
			extra.setContent_type(ContentIDHelper.getContentType(extra.getContent_id()));
		}
		
		// dwelltime
		int dwelltime = 0;
		try {
			dwelltime = Integer.valueOf(json_obj.getString("stay_time")).intValue();
			if (dwelltime > Dwelltime.MAX_DWELLTIME) {
				dwelltime = Dwelltime.MAX_DWELLTIME;
			}
			if (dwelltime < Dwelltime.MIN_DWELLTIME) {
				dwelltime = Dwelltime.MIN_DWELLTIME;
			}
		} catch (Exception e) {
		}
		extra.setDwelltime(String.valueOf(dwelltime));
		
		// refer
		try {
			JSONObject obj = JSONObject.parseObject(json_obj.getString("refer"));
			Refer refer = new Refer();
			refer.setContent_id(obj.getString("content_id"));
			refer.setContent_type(ContentType.getContentType(obj.getString("content_type")));
			extra.setRefer(refer);
		} catch (Exception e) {
		}
		
		// view_mode
		try {
			String view_mode = json_obj.getJSONObject("extra_msg").getString("view_mode");
			extra.setView_mode(view_mode);
		} catch (Exception e) {
		}
		try {
			extra.setExtra_msg(json_obj.getJSONObject("extra_msg"));
		} catch (Exception e) {
		}
		
		// play_time
		extra.setPlay_time(json_obj.getString("play_time"));
		
		schema.setArticle_dwelltime_extra(extra);
		schema.setEvent_time(json_obj.getString("event_time"));
		schema.setScenario(Scenario.parseScenario(product_id, json_obj.getString("scenario")));
		
		// revise scenario-channel
		Scenario scenario = schema.getScenario();
		if (schema.getScenario() != null && 
				ScenarioType.Channel.UNKNOWN.contentEquals(scenario.getChannel_desc())) {
			if (Strategy.StrategyValueDesc.PUSH.equals(cpack.getStrategy()) ||
					Strategy.StrategyValueDesc.RELATIVE_RECOMMENDATION.equals(cpack.getStrategy())) {
				scenario.setChannel_desc(cpack.getStrategy());
			}
		}
	}
	
	private void parseArticleCompletenessExtra(JSONObject json_obj, CommonSchema schema, String product_id) {
		ArticleCompletenessExtra extra = new ArticleCompletenessExtra();
		extra.setContent_id(json_obj.getString("content_id"));
		
		// cpack
		JSONObject cpack_json = null;
		try {
			String cpack_str = json_obj.getString("cpack");
			String cpack_decode_str = new String(base64.decode(cpack_str));
			cpack_json = JSONObject.parseObject(cpack_decode_str);
		} catch (Exception e) {
			cpack_json = new JSONObject();
		}
		ContentPacket cpack = SpecialFieldsFiller.fillCpack(cpack_json);
		extra.setCpack(cpack);
		
		try {
			extra.setContent_type(ContentType.getContentType(cpack_json.getString("content_type")));
		} catch (Exception e) {
			extra.setContent_type(ContentIDHelper.getContentType(extra.getContent_id()));
		}
		
		// refer
		try {
			JSONObject obj = JSONObject.parseObject(json_obj.getString("refer"));
			Refer refer = new Refer();
			refer.setContent_id(obj.getString("content_id"));
			refer.setContent_type(ContentType.getContentType(obj.getString("content_type")));
			extra.setRefer(refer);
		} catch (Exception e) {
		}
		
		// view_mode
		try {
			String view_mode = json_obj.getJSONObject("extra_msg").getString("view_mode");
			extra.setView_mode(view_mode);
		} catch (Exception e) {
		}
		try {
			extra.setExtra_msg(json_obj.getJSONObject("extra_msg"));
		} catch (Exception e) {
		}
		
		// proportion
		extra.setProportion(json_obj.getString("proportion"));
		
		schema.setArticle_completeness_extra(extra);
		schema.setEvent_time(json_obj.getString("event_time"));
		schema.setScenario(Scenario.parseScenario(product_id, json_obj.getString("scenario")));
		
		// revise scenario-channel
		Scenario scenario = schema.getScenario();
		if (schema.getScenario() != null && 
				ScenarioType.Channel.UNKNOWN.contentEquals(scenario.getChannel_desc())) {
			if (Strategy.StrategyValueDesc.PUSH.equals(cpack.getStrategy()) ||
					Strategy.StrategyValueDesc.RELATIVE_RECOMMENDATION.equals(cpack.getStrategy())) {
				scenario.setChannel_desc(cpack.getStrategy());
			}
		}
	}
	
	private void parseActivityExtra(JSONObject json_obj, CommonSchema schema, String product_id) {
		ActivityDwelltimeExtra extra = new ActivityDwelltimeExtra();
		extra.setDwelltime(json_obj.getString("stay_time"));
		extra.setPage_name(json_obj.getString("page_name"));
		schema.setActivity_dwelltime_extra(extra);
		schema.setEvent_time(json_obj.getString("event_time"));
	}
	
	private void parseBackendServiceExtra(JSONObject json_obj, CommonSchema schema, String product_id) {
		BackendServiceExtra extra = new BackendServiceExtra();
		extra.setEvent_value(json_obj.getJSONObject("event_value"));
		schema.setBackend_service_extra(extra);
		schema.setEvent_time(json_obj.getString("event_time"));
	}
	
	private void parseExtendEventExtra(JSONObject json_obj, CommonSchema schema, String product_id) {
		ExtendEventExtra extra = new ExtendEventExtra();
		
		String action_name = json_obj.getString("action_name");
		extra.setAction_name(action_name);
		
		switch(action_name){
			case ActionName.VIDEO_PLAY_ERROR :
				try {
					JSONObject action_value_json = JSONObject.parseObject(json_obj.getString("action_value"));
					JSONObject obj = new JSONObject();
					obj.put("content_id", action_value_json.getString("content_id"));
					obj.put("error_code", action_value_json.getString("error_code"));
					extra.setAction_value(obj);
				} catch (Exception e) {
				}
				break;
				
			case ActionName.INDEX_GETUID_BACKGROUND :
				try {
					JSONObject action_value_json = new JSONObject();
					action_value_json.put("user_count", json_obj.getString("action_value"));
					extra.setAction_value(action_value_json);
				} catch (Exception e) {
				}
				break;
				
			case ActionName.REQUEST_SUCCESS_TIMEEXPEND :
			case ActionName.SHARE_FACEBOOK :
			case ActionName.SHARE_GOOGLE :
			case ActionName.SHARE_WHATSAPP :
			case ActionName.SHARE_MORE :
			case ActionName.SHARE_COPYLINK :
				try {
					JSONObject action_value_json = json_obj.getJSONObject("action_value");
					extra.setAction_value(action_value_json);
				} catch (Exception e) {
				}
				break;
			
			case ActionName.ARTICLE_SHARE :
			case ActionName.SHARE_SUCCESS_F :
			case ActionName.SHARE_SUCCESS_G :
			case ActionName.SHARE_SUCCESS_W :
				try {
					JSONObject action_value_json = json_obj.getJSONObject("action_value");
					extra.setAction_value(action_value_json);
				} catch (Exception e) {
					JSONObject action_value_json = new JSONObject();
					action_value_json.put("from", "unknown");
					action_value_json.put("content_id", "unknown");
					action_value_json.put("content_type", "unknown");
					action_value_json.put("scenario", "unknown");
					extra.setAction_value(action_value_json);
				}
				break;
				
			case ActionName.H5_SHARE_CLICK :
			case ActionName.H5_PAGE_REQUEST_CALL :
			case ActionName.H5_CLICK_DOWNLOAD_APP :
			case ActionName.DEEP_LINK_DEFERRED_H5 :
				try {
					JSONObject action_value_json = json_obj.getJSONObject("action_value");
					extra.setAction_value(action_value_json);
				} catch (Exception e) {
					JSONObject action_value_json = new JSONObject();
					action_value_json.put("fb_uid", "unknown");
					action_value_json.put("scenario", "unknown");
					action_value_json.put("content_id", "unknown");
					action_value_json.put("fb_gid", "unknown");
					action_value_json.put("plateform", "unknown");
					action_value_json.put("download_from", "unknown");
					action_value_json.put("share_to", "unknown");
					action_value_json.put("content_type", "unknown");
					extra.setAction_value(action_value_json);
				}
				break;
				
			case ActionName.ORIGINAL_AD_DEMAND :
				try {
					JSONObject action_value_json = json_obj.getJSONObject("action_value");
					String scenario_str = action_value_json.getString("scenario");
					Scenario scenario = Scenario.parseScenario(product_id, scenario_str);
					action_value_json.put("scenario", scenario);
					extra.setAction_value(action_value_json);
				} catch (Exception e) {
					JSONObject action_value_json = new JSONObject();
					action_value_json.put("adspace_id", "unknown");
					action_value_json.put("scenario", new Scenario());
					extra.setAction_value(action_value_json);
				}
				break;
				
			case ActionName.AD_REQUEST_RESPONSE :
				try {
					JSONObject action_value_json = json_obj.getJSONObject("action_value");
					extra.setAction_value(action_value_json);
				} catch (Exception e) {
					JSONObject action_value_json = new JSONObject();
					action_value_json.put("adspace_id", "unknown");
					action_value_json.put("request_count", "0");
					action_value_json.put("response_count", "0");
					action_value_json.put("ad_source", "unknown");
					action_value_json.put("t_adspace_id", "unknown");
					action_value_json.put("duration_time", "0");
					extra.setAction_value(action_value_json);
				}
				break;
				
			case ActionName.AD_DEMAND_FILL_SUCCESS :
				try {
					JSONObject action_value_json = json_obj.getJSONObject("action_value");
					String scenario_str = action_value_json.getString("scenario");
					Scenario scenario = Scenario.parseScenario(product_id, scenario_str);
					action_value_json.put("scenario", scenario);
					extra.setAction_value(action_value_json);
				} catch (Exception e) {
					JSONObject action_value_json = new JSONObject();
					action_value_json.put("scenario", new Scenario());
					action_value_json.put("adspace_id", "unknown");
					action_value_json.put("duration_time", "0");					
					extra.setAction_value(action_value_json);
				}
				break;

			case ActionName.AD_REQUEST :
			case ActionName.AD_REQUEST_FILL :
			case ActionName.ADSPACE_IMPRESSION :
			case ActionName.AD_DEMAND_NOFILL :
			case ActionName.AD_DEMAND_FILL_TIMEOUT :
				try {
					JSONObject action_value_json = json_obj.getJSONObject("action_value");
					extra.setAction_value(action_value_json);
				} catch (Exception e) {
					JSONObject action_value_json = new JSONObject();
					action_value_json.put("adspace_id", "unknown");
					extra.setAction_value(action_value_json);
				}
				break;
				
			default :
				extra.setAction_value(json_obj.getString("action_value"));
		}
		extra.setAction_type(json_obj.getString("action_type"));
		
		schema.setExtend_event_extra(extra);
		schema.setEvent_time(json_obj.getString("event_time"));
	}
	
}
