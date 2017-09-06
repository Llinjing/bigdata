package com.inveno.news.reformat.convert;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.github.panhongan.util.StringUtil;
import com.github.panhongan.util.collection.ArrayUtil;
import com.github.panhongan.util.web.url.QueryStringParser;
import com.inveno.news.reformat.constant.KafkaTopic;
import com.inveno.news.reformat.constant.Language;
import com.inveno.news.reformat.constant.ProductID;
import com.inveno.news.reformat.constant.Promotion;
import com.inveno.news.reformat.constant.Protocol;
import com.inveno.news.reformat.schema.news.ProfileSchema;

public class ProfileLogConverter extends AbstractConverter {
	
	private static final Logger logger = LoggerFactory.getLogger(RequestLogConverter.class);
	
	public static final String LOG_TAG = "profile";
	
	public static final int LOG_MIN_COLUMNS = 26;
	
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
		return new ProfileLogConverter();
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
			logger.warn("invalid parameter");
			return map;
		}
		
		if (str.startsWith(LOG_TAG)) {	//prefix: profile or profile_s
			String [] arr_raw = str.split("&");
			String [] arr = ArrayUtil.expand(arr_raw, LOG_MIN_COLUMNS, "");
			if (arr != null && arr.length >= LOG_MIN_COLUMNS) {
				if (!MessageValidator.validate(arr[4], arr[3], str)) {
					return map;
				}
				
				ProfileSchema schema = new ProfileSchema();
				schema.setLog_type(LOG_TAG);
				schema.setProtocol(Protocol.HTTPS.toString());
				schema.setGate_ip(arr[1]);
				schema.setEvent_time(arr[2]);
				schema.setUid(arr[4]);
				schema.setApp(arr[3]);
				schema.setProduct_id(arr[3]);
				schema.setApp_ver(arr[12]);
				schema.setNetwork(arr[15].toLowerCase());
				schema.setPlatform(arr[16].toLowerCase());
				schema.setOsv(arr[17]);
				schema.setModel(arr[18]);
				schema.setImei(arr[5]);
				schema.setMac(arr[7]);
				schema.setAid(arr[21]);
				
				String promotion = arr[23];
				if (Promotion.GOOGLE_PLAY.equals(promotion)) {
					if (!StringUtil.isEmpty(arr[22])) {
						try {
							String referrer = new String(URLDecoder.decode(URLDecoder.decode(arr[22], "utf-8"), "utf-8"));
							JSONObject json_obj = JSONObject.parseObject(referrer);
							String pid = "";
							String af_siteid = "";
							String campaignid = "";
							
							if (referrer.contains("=")) {
								Map<String, String> map = QueryStringParser.parse(json_obj.getString("referrer"));
								pid = map.get("pid");
								af_siteid = map.get("af_siteid");
								campaignid = map.get("campaignid");
							} else {
								pid = json_obj.getString("pid");
								af_siteid = json_obj.getString("af_siteid");
								campaignid = json_obj.getString("campaignid");
							}
							
							if (!StringUtil.isEmpty(pid)) {
								promotion = promotion + "_" + pid;
							}						
							if (!StringUtil.isEmpty(af_siteid)) {
								promotion = promotion + "_" + af_siteid;
							}
							
							if (StringUtil.isEmpty(pid) && StringUtil.isEmpty(af_siteid)) {
								if (StringUtil.isEmpty(campaignid)) {
									promotion = promotion + "_" + "unknown";
								} else {
									promotion = promotion + "_gpAdwords_" + campaignid;
								}
							}
						} catch (Exception e) {
							logger.warn(e.getMessage(), e);
						}
					} else {
						promotion = promotion + "_" + "null";
					}
				}
				schema.setPromotion(promotion);
				
				/*
				// arr[20]: [version with promotion] : 2.1.2.0.2.0_gp
				String revised_app_ver = arr[20];
				
				if (StringUtil.isEmpty(schema.getPromotion())) {
					int pos = revised_app_ver.indexOf('_');
					if (pos > 0) {
						schema.setPromotion(revised_app_ver.substring(pos + 1));
						
						String [] ver_arr = revised_app_ver.substring(0, pos).split("[.]");
						if (ver_arr != null && ver_arr.length >= 6) {
							schema.setApp_ver(ver_arr[0] + "." + ver_arr[1] + "." + ver_arr[2] + 
									"." + ver_arr[3] + "." + ver_arr[4] + "." + ver_arr[5]);
						}
					}
				}*/
				

				if (StringUtil.isEmpty(schema.getPromotion())) {
					schema.setPromotion(Promotion.UNKNOWN.toString());
				}

				if (arr[3].contentEquals("hotoday_India_Hindi")) {
					schema.setApp_lan(Language.HINDI.toString());
					schema.setProduct_id(ProductID.HOTODAY.toString());
				} else if (arr[3].contentEquals("hotoday_India_English")){
					schema.setApp_lan(Language.ENGLISH.toString());
					schema.setProduct_id(ProductID.HOTODAY.toString());
				} else {
					schema.setApp_lan(Language.UNKNOWN.toString());
				}

				list.add(schema.toString());
				map.put(KafkaTopic.PROFILE_REFORMAT.toString(), list);
				
				// reformat counter
				if (++reformat_count >= 1000) {
					logger.info("reformat_count = {}", reformat_count);
					reformat_count = 0;
				}
			}
		} else {
			logger.warn("invalid log prefix : {}", str);
		}
		
		return map;
	}

}
