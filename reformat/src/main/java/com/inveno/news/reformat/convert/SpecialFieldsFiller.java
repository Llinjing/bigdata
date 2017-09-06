package com.inveno.news.reformat.convert;


import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.github.panhongan.util.StringUtil;
import com.inveno.news.reformat.constant.ArticleSource;
import com.inveno.news.reformat.constant.ConfigID;
import com.inveno.news.reformat.constant.Language;
import com.inveno.news.reformat.constant.ProductID;
import com.inveno.news.reformat.constant.Promotion;
import com.inveno.news.reformat.constant.ScenarioType;
import com.inveno.news.reformat.constant.Strategy;
import com.inveno.news.reformat.schema.CommonSchema;
import com.inveno.news.reformat.schema.ContentPacket;
import com.inveno.news.reformat.schema.UserPacket;
import com.inveno.news.reformat.schema.news.Scenario;
import com.inveno.news.reformat.thrift.ABDyeingClient;

public class SpecialFieldsFiller {

	private static final Logger logger = LoggerFactory.getLogger(SpecialFieldsFiller.class);

	private static Base64 base64 = new Base64();
	
	public static void fillSpecialFields(String log_type, String app, String app_ver, 
			boolean is_recommend, CommonSchema schema) {		
		Scenario scenario = new Scenario();
		
		if (app.startsWith("coolpad")){
			schema.setApp_lan(Language.CHINESE.toString());
			schema.setLanguage(Language.CHINESE.toString());
			schema.setPromotion(Promotion.COOLPAD.toString());
			schema.setProduct_id(ProductID.COOLPAD.toString());

			if (is_recommend) {
				scenario.setPosition_type(ScenarioType.PositionType.PositionTypeValue.RELEVANT_RECOMMENTDATION.toString());
				scenario.setDesc(ScenarioType.PositionType.PositionTypeValueDesc.RELEVANT_RECOMMENTDATION.toString());
				scenario.setPosition("3");
				scenario.setChannel("0");
			} else {
				scenario.setPosition_type(ScenarioType.PositionType.PositionTypeValue.WATERFALL.toString());
				scenario.setDesc(ScenarioType.PositionType.PositionTypeValueDesc.WATERFALL.toString());
				scenario.setPosition("1");
				scenario.setChannel("0");
			}
			
		} else if (app.startsWith("emui")) {
			schema.setApp_lan(Language.CHINESE.toString());
			schema.setLanguage(Language.CHINESE.toString());
			schema.setPromotion(Promotion.HUAWEI.toString());
			
			if (app.contentEquals("emui")) {
				schema.setProduct_id(ProductID.HUAWEI_APK.toString());
			} else if (app.contentEquals("emuihd")) {
				schema.setProduct_id(ProductID.HUAWEI_PAD.toString());
			} else if (app.contentEquals("emuiqg")) {
				schema.setProduct_id(ProductID.HUAWEI_QINGGAN.toString());
			}

			if (is_recommend) {
				scenario.setPosition_type(ScenarioType.PositionType.PositionTypeValue.RELEVANT_RECOMMENTDATION.toString());
				scenario.setDesc(ScenarioType.PositionType.PositionTypeValueDesc.RELEVANT_RECOMMENTDATION.toString());
				scenario.setPosition("2");
				scenario.setChannel("0");
			} else {
				scenario.setPosition_type(ScenarioType.PositionType.PositionTypeValue.WATERFALL.toString());
				scenario.setDesc(ScenarioType.PositionType.PositionTypeValueDesc.WATERFALL.toString());
				scenario.setPosition("1");
				scenario.setChannel("0");
			}
			
		} else if (app.contentEquals("tianyu") || app.contentEquals("tianyuapk") ||
				app.contentEquals("huiyueapp") || app.contentEquals("huiyue") ||
				app.contentEquals("tcl") || app.contentEquals("xiaolajiao") || 
				app.contentEquals("duowei")) {
			schema.setApp_lan(Language.CHINESE.toString());
			schema.setLanguage(Language.CHINESE.toString());
			if (app.contentEquals("tianyu")) {
				schema.setPromotion(Promotion.TIANYU.toString());
				schema.setProduct_id(ProductID.TIANYU.toString());
			} else if (app.contentEquals("tianyuapk")) {
				schema.setPromotion(Promotion.TIANYU.toString());
				schema.setProduct_id(ProductID.TIANYUAPK.toString());
			} else if (app.contentEquals("huiyueapp") || app.contentEquals("huiyue")) {
				schema.setPromotion(Promotion.MONEYLOCKER.toString());
				schema.setProduct_id(ProductID.HUISUOPING.toString());
			} else if (app.contentEquals("tcl")) {
				schema.setPromotion(Promotion.TCL.toString());
				schema.setProduct_id(ProductID.TCL.toString());
			} else if (app.contentEquals("xiaolajiao")) {
				schema.setPromotion(Promotion.XIAOLAJIAO.toString());
				schema.setProduct_id(ProductID.XIAOLAJIAO.toString());
			} else if (app.contentEquals("duowei")) {
				schema.setPromotion(Promotion.DUOWEI.toString());
				schema.setProduct_id(ProductID.DUOWEI.toString());
			}
			
			if (is_recommend) {
				scenario.setPosition_type(ScenarioType.PositionType.PositionTypeValue.RELEVANT_RECOMMENTDATION.toString());
				scenario.setDesc(ScenarioType.PositionType.PositionTypeValueDesc.RELEVANT_RECOMMENTDATION.toString());
				scenario.setPosition("3");
				scenario.setChannel("0");
			} else {
				scenario.setPosition_type(ScenarioType.PositionType.PositionTypeValue.WATERFALL.toString());
				scenario.setDesc(ScenarioType.PositionType.PositionTypeValueDesc.WATERFALL.toString());
				scenario.setPosition("1");
				scenario.setChannel("0");
			}
			
		} else if (app.contentEquals("angda")) {
			schema.setApp_lan(Language.CHINESE.toString());
			schema.setLanguage(Language.CHINESE.toString());
			schema.setPromotion(Promotion.ANGDA.toString());
			schema.setProduct_id(ProductID.ANGDA.toString());
			
			if (is_recommend) {
				scenario.setPosition_type(ScenarioType.PositionType.PositionTypeValue.RELEVANT_RECOMMENTDATION.toString());
				scenario.setDesc(ScenarioType.PositionType.PositionTypeValueDesc.RELEVANT_RECOMMENTDATION.toString());
				scenario.setPosition("2");
				scenario.setChannel("0");
			} else {
				scenario.setPosition_type(ScenarioType.PositionType.PositionTypeValue.WATERFALL.toString());
				scenario.setDesc(ScenarioType.PositionType.PositionTypeValueDesc.WATERFALL.toString());
				scenario.setPosition("1");
				scenario.setChannel("0");
			}
			
		} else if (app.contentEquals("lenovowid")) {
			schema.setApp_lan(Language.CHINESE.toString());
			schema.setLanguage(Language.CHINESE.toString());
			schema.setPromotion(Promotion.LENOVO.toString());
			schema.setProduct_id(ProductID.LENOVO_WIDGET.toString());
			scenario.setPosition_type(ScenarioType.PositionType.PositionTypeValue.SHORT_LISTPAGE.toString());
			scenario.setDesc(ScenarioType.PositionType.PositionTypeValueDesc.SHORT_LISTPAGE.toString());
			scenario.setPosition("1");
			scenario.setChannel("0");
		} else if (app.contentEquals("hotoday_India_Hindi") || app.contentEquals("hotoday_India_English")) {
			if (app.contentEquals("hotoday_India_Hindi")) {
				schema.setApp_lan(Language.HINDI.toString());
				schema.setLanguage(Language.HINDI.toString());
			} else {
				schema.setApp_lan(Language.ENGLISH.toString());
				schema.setLanguage(Language.ENGLISH.toString());
			}
			
			if (StringUtil.isEmpty(schema.getPromotion())) {
				int pos = app_ver.lastIndexOf('_');
				if (pos > 0) {
					schema.setPromotion(app_ver.substring(pos + 1));
					
					String [] ver_arr = app_ver.substring(0, pos).split("[.]");
					if (ver_arr != null && ver_arr.length >= 6) {
						schema.setApp_ver(ver_arr[0] + "." + ver_arr[1] + "." + ver_arr[2] + 
								"." + ver_arr[3] + "." + ver_arr[4] + "." + ver_arr[5]);
					}
				} else {
					schema.setPromotion(Promotion.UNKNOWN.toString());
				}
			}
			
			schema.setProduct_id(ProductID.HOTODAY.toString());
			scenario.setPosition_type(ScenarioType.PositionType.PositionTypeValue.LONG_LISTPAGE.toString());
			scenario.setDesc(ScenarioType.PositionType.PositionTypeValueDesc.LONG_LISTPAGE.toString());
			scenario.setPosition("1");
			scenario.setChannel("0");
			
			// hotoday 2.1.3开始，部分相关推荐的展现走了request接口
			if (app_ver.compareTo("2.1.3") > 0) {
				scenario.setDesc(ScenarioType.PositionType.PositionTypeValueDesc.RELEVANT_RECOMMENTDATION.toString());
			}
		} else if (app.contentEquals("fyp-ginee-h5") || app.contentEquals("fuyiping-gionee")) {
			schema.setApp_lan(Language.CHINESE.toString());
			schema.setLanguage(Language.CHINESE.toString());
			schema.setPromotion(Promotion.GIONEE.toString());
			schema.setProduct_id(ProductID.GIONEE_FUYIPING.toString());
			
			if (is_recommend) {
				scenario.setPosition_type(ScenarioType.PositionType.PositionTypeValue.RELEVANT_RECOMMENTDATION.toString());
				scenario.setDesc(ScenarioType.PositionType.PositionTypeValueDesc.RELEVANT_RECOMMENTDATION.toString());
				scenario.setPosition("3");
				scenario.setChannel("0");
			} else {
				scenario.setPosition_type(ScenarioType.PositionType.PositionTypeValue.SHORT_LISTPAGE.toString());
				scenario.setDesc(ScenarioType.PositionType.PositionTypeValueDesc.SHORT_LISTPAGE.toString());
				scenario.setPosition("1");
				scenario.setChannel("0");
			}
			
		} /*else if (app.contentEquals("xiaozhi")) {
			schema.setApp_lan(Language.CHINESE.toString());
			schema.setLanguage(Language.CHINESE.toString());  	// need delete
			schema.setProduct_id(ProductID.XIAOZHI.toString());
			schema.setPromotion(Promotion.INVENO.toString());
			
			if (is_recommend) {
				scenario.setPosition_type(ScenarioType.PositionType.PositionTypeValue.RELEVANT_RECOMMENTDATION.toString());
				scenario.setDesc(ScenarioType.PositionType.PositionTypeValueDesc.RELEVANT_RECOMMENTDATION.toString());
				scenario.setPosition("2");
				scenario.setChannel("0");
			} else {
				scenario.setPosition_type(ScenarioType.PositionType.PositionTypeValue.LONG_LISTPAGE.toString());
				scenario.setDesc(ScenarioType.PositionType.PositionTypeValueDesc.LONG_LISTPAGE.toString());
				scenario.setPosition("1");
				scenario.setChannel("0");
			}
			
		} */else if (app.contentEquals("H5")) {
			schema.setApp_lan(Language.CHINESE.toString());
			schema.setLanguage(Language.CHINESE.toString());
			schema.setProduct_id(ProductID.H5_SDK.toString());
			schema.setPromotion(Promotion.INVENO.toString());
			
			if (is_recommend) {
				scenario.setPosition_type(ScenarioType.PositionType.PositionTypeValue.RELEVANT_RECOMMENTDATION.toString());
				scenario.setDesc(ScenarioType.PositionType.PositionTypeValueDesc.RELEVANT_RECOMMENTDATION.toString());
				scenario.setPosition("2");
				scenario.setChannel("0");
			} else {
				scenario.setPosition_type(ScenarioType.PositionType.PositionTypeValue.LONG_LISTPAGE.toString());
				scenario.setDesc(ScenarioType.PositionType.PositionTypeValueDesc.LONG_LISTPAGE.toString());
				scenario.setPosition("1");
				scenario.setChannel("0");
			}
		} else if (app.contentEquals("0febb9b4-486")) {	// wifiyaoshi
			schema.setApp_lan(Language.CHINESE.toString());
			schema.setLanguage(Language.CHINESE.toString());
			schema.setPromotion(Promotion.WIFIYAOSHI.toString());
			schema.setProduct_id(ProductID.WIFIYAOSHI.toString());
			scenario.setPosition_type(ScenarioType.PositionType.PositionTypeValue.LONG_LISTPAGE.toString());
			scenario.setDesc(ScenarioType.PositionType.PositionTypeValueDesc.LONG_LISTPAGE.toString());
			scenario.setPosition("1");
			scenario.setChannel("0");
		} else {
			schema.setApp_lan(Language.UNKNOWN.toString());
			schema.setLanguage(Language.UNKNOWN.toString());
			schema.setProduct_id(app);
			schema.setPromotion(app);
			scenario.setPosition_type(ScenarioType.PositionType.PositionTypeValue.WATERFALL.toString());
			scenario.setDesc(ScenarioType.PositionType.PositionTypeValueDesc.WATERFALL.toString());
			scenario.setPosition("1");
			scenario.setChannel("0");
		}
		
		if (StringUtil.isEmpty(schema.getApp_ver())) {
			schema.setApp_ver("unknown");
		}
		if (StringUtil.isEmpty(schema.getPromotion())) {
			schema.setPromotion("unknown");
		}
		
		schema.setScenario(scenario);
	}

	public static void fillUpack(String upack_base64_str, CommonSchema schema, ABDyeingClient abDyeingClient){
		String news_configid = ConfigID.UNKNOWN;
		String ad_configid = ConfigID.UNKNOWN;
		String biz_configid = ConfigID.UNKNOWN;

		try {
			String upack_decode_str = new String(base64.decode(upack_base64_str));
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
			logger.warn("no upack data in log");
		} finally {
			if (news_configid.contentEquals(ConfigID.UNKNOWN) ||
					ad_configid.contentEquals(ConfigID.UNKNOWN) ||
					biz_configid.contentEquals(ConfigID.UNKNOWN)){
				if (abDyeingClient != null) {
					Map<String, String> config_map = abDyeingClient.getUpack(schema.getUid(), schema.getProduct_id(), 
							schema.getApp_ver(), schema.getApp_lan(), schema.getPlatform());
					// news_configid
					try {
						if (news_configid.contentEquals(ConfigID.UNKNOWN) && !StringUtil.isEmpty(config_map.get("news_configid"))) {
							news_configid = config_map.get("news_configid");
						}
					} catch (Exception e) {
					}

					// ad_configid
					try {
						if (ad_configid.contentEquals(ConfigID.UNKNOWN) && !StringUtil.isEmpty(config_map.get("ad_configid"))) {
							ad_configid = config_map.get("ad_configid");
						}
					} catch (Exception e) {
					}

					// biz_configid
					try {
						if (biz_configid.contentEquals(ConfigID.UNKNOWN) && !StringUtil.isEmpty(config_map.get("biz_configid"))) {
							biz_configid = config_map.get("biz_configid");
						}
					} catch (Exception e) {
					}
				} else {
					logger.warn("ABDyeingClient is null");
				}
				
			}
		}
		UserPacket upack = new UserPacket();
		upack.setNews_configid(news_configid);
		upack.setAd_configid(ad_configid);
		upack.setBiz_configid(biz_configid);
		schema.setUpack(upack);
	}

	public static ContentPacket fillCpack(JSONObject cpack_json){
		ContentPacket cpack = new ContentPacket();
		
		try {		
			// strategy
			try {
				cpack.setStrategy(Strategy.getStrategy(cpack_json.getString("strategy")));
			} catch (Exception e) {
			}
			
			// ad_source
			try {
				cpack.setAd_source(cpack_json.getString("ad_source"));
			} catch (Exception e) {				
			}
			
			// source
			try {
				String source = cpack_json.getString("source");
				if (StringUtil.isEmpty(source)) {
					cpack.setSource(ArticleSource.UNKNOWN.toString());
				} else {
					cpack.setSource(source);
				}
			} catch (Exception e) {				
			}
			
			// push_explore_id
			try {
				String push_explore_id = cpack_json.getString("push_explore_id");
				if (!StringUtil.isEmpty(push_explore_id)) {
					cpack.setPush_explore_id(push_explore_id);
				}
			} catch (Exception e) {				
			}
			
			// category
			try {
				String category_str = cpack_json.getString("categories");
				int category = Integer.valueOf(category_str);

				if (category > -1) {
					cpack.setCategory(category);
				}
			} catch (Exception e) {				
			}
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		}
		return cpack;
	}
}
