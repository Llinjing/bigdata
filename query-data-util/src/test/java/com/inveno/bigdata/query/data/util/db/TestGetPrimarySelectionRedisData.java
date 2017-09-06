package com.inveno.bigdata.query.data.util.db;

import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class TestGetPrimarySelectionRedisData {

	public static void main(String[] args) {
		String language = "{\"v1\": {\"Hindi\": {\"weight\": 0.9713286713286713},\"zh_CN\": {\"weight\": 10},\"English\": {\"weight\": 100}},\"v2\": {\"Hindi\": {\"weight\": 0.8461538461538461},\"zh_CN\": {\"weight\": 20},\"English\": {\"weight\": 0}},\"v3\": {\"Hindi\": {\"weight\": 0.9879089615931721},\"zh_CN\": {\"weight\": 20}}}";
		JSONObject language_json_object = (JSONObject) JSON.parse(language);
		JSONObject language_v1_json_object = language_json_object.getJSONObject("v1");
		JSONObject language_v2_json_object = language_json_object.getJSONObject("v2");
		JSONObject language_v3_json_object = language_json_object.getJSONObject("v3");

		System.out.println(getMaxWeightLanguage(language_v1_json_object));
		System.out.println(getMaxWeightLanguage(language_v2_json_object));
		System.out.println(getMaxWeightLanguage(language_v3_json_object));

		System.out.println(language);

		String categories = "{\"v8\":{\"100\":{\"weight\":1}},\"v23\":{\"121\":{\"weight\":0.15876537542347702},\"111\":{\"weight\":0.60679840731209844},\"101\":{\"weight\":0.015725405539728246},\"112\":{\"weight\":0.29632646079932301},\"103\":{\"weight\":0.0075490607463585407},\"114\":{\"weight\":0.51709896291478863},\"115\":{\"weight\":0.10383633158262033},\"116\":{\"weight\":0.1493310976020745},\"118\":{\"weight\":0.18186855847033134},\"108\":{\"weight\":0.12544688661101189}},\"v22\":{\"111\":{\"weight\":1}},\"v14\":{\"121\":{\"weight\":0.31167024196727661},\"111\":{\"weight\":0.31897022420530807},\"101\":{\"weight\":0.27245216644807124},\"112\":{\"weight\":0.2576758582237082},\"115\":{\"weight\":0.2566455727969677},\"105\":{\"weight\":0.32854371101853541},\"116\":{\"weight\":0.3766526685152003}},\"v25\":{\"114\":{\"weight\":1}},\"v13\":{\"120\":{\"weight\":0.13617333421566374},\"110\":{\"weight\":0.1760307081896072},\"121\":{\"weight\":0.31167024196727661},\"111\":{\"weight\":0.31897022420530807},\"101\":{\"weight\":0.27245216644807124},\"112\":{\"weight\":0.2576758582237082},\"102\":{\"weight\":0.22739764097810505},\"113\":{\"weight\":0.21756263878517001},\"103\":{\"weight\":0.13805731465958704},\"114\":{\"weight\":0.21186329836780757},\"104\":{\"weight\":0.1862808678542314},\"115\":{\"weight\":0.2566455727969677},\"105\":{\"weight\":0.32854371101853541},\"116\":{\"weight\":0.3766526685152003},\"106\":{\"weight\":0.15911653746608703},\"107\":{\"weight\":0.1537771778036153},\"118\":{\"weight\":0.14145620874172454},\"108\":{\"weight\":0.083377150717501083},\"119\":{\"weight\":0.2352073985826742},\"109\":{\"weight\":0.20456989277832902}},\"v27\":{\"121\":{\"weight\":0.42944982735385784},\"112\":{\"weight\":0.41874131028634409},\"114\":{\"weight\":0.88882603602013399},\"115\":{\"weight\":0.12196839643209374},\"105\":{\"weight\":0.2956014717352542},\"118\":{\"weight\":0.57925243727242059},\"108\":{\"weight\":0.25122527047675158}},\"v1\":{\"100\":{\"weight\":1}},\"v26\":{\"121\":{\"weight\":0.8529132511157511},\"114\":{\"weight\":1.0},\"118\":{\"weight\":0.77396267490842}},\"v28\":{\"114\":{\"weight\":1}}}";
		System.out.println(categories);
		System.out.println(getMaxWeightCategory(categories, "v25"));
	}


	public static String getMaxWeightLanguage(JSONObject language_version_json_object){
		String max_weight_language = "";
		double max_weight = 0D;
		
		try {
			Set<String> key_set = language_version_json_object.keySet();
			for(String key : key_set){
				double weight = language_version_json_object.getJSONObject(key).getDoubleValue("weight");  
				if(weight >= max_weight){
					max_weight_language = key;
					max_weight = weight;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return max_weight_language;
	}
	
	public static String getMaxWeightCategory(String categories, String version){
		String max_weight_category = "";
		double max_weight = 0D;
		
		try {
			JSONObject categories_json_object = (JSONObject) JSON.parse(categories);
			JSONObject categories_version = categories_json_object.getJSONObject(version);
			Set<String> key_set = categories_version.keySet();
			for(String key : key_set){
				double weight = categories_version.getJSONObject(key).getDoubleValue("weight");  
				if(weight >= max_weight){
					max_weight_category = key;
					max_weight = weight;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return max_weight_category;
	}
}
