package com.inveno.bigdata.sync.convert;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import com.alibaba.fastjson.JSONObject;

public class TestImpressionStatConverter {
	
	private static SimpleDateFormat date_format = new SimpleDateFormat("yyyyMMddHH");
	
	public static void main(String [] args) {
		String str1 = "2017030711";
		String str2 = "2017030710";
		System.out.println(str1.compareTo(str2));
		
		String data_json = "{\"content_id\":\"1034604157\",\"product_id\":\"hotoday\",\"total_impression\":1000,\"update_date\":\"2017040809\",\"language\":\"hindi\"}";
		parseData(data_json);
		
		System.out.println(getValidRedisDate());
	}
    public static String getValidRedisDate() {
        TimeZone TIMEZONE_UTC = TimeZone.getTimeZone("UTC");
        date_format.setTimeZone(TIMEZONE_UTC);
                
        long system_time_stamp = System.currentTimeMillis();
        String res = date_format.format(system_time_stamp - 60 * 60 * 1000 * 1).toString();
        return res;
    }
    
	public static void parseData(String str){
    	try {
			JSONObject json_obj = JSONObject.parseObject(str);
			System.out.println(json_obj.toString());
			
			if (!json_obj.isEmpty()) {
				String product_id = json_obj.getString("product_id");
				int total_impression = json_obj.getInteger("total_impression");
				String valid_redis_date = getValidRedisDate();
				
				if (!product_id.contains("noticias") && 
						total_impression < 100000 && 
						json_obj.getString("update_date").compareTo(valid_redis_date) >= 0) {
					
                    String content_id = json_obj.getString("content_id");
					String language = json_obj.getString("language");
                    language = language.substring(0,1).toUpperCase() + language.substring(1);

                    String redis_key = "";
                    if(language.contentEquals("Zh_cn")){
                        redis_key = "expinfo_" + product_id + "_impressionOfInfoIdKey";
                    }else{
                        redis_key = "expinfo_" + product_id + "_" + language + "_impressionOfInfoIdKey";
                    }
                    
                    System.out.println(redis_key);
                    System.out.println(content_id);
                    System.out.println(Integer.toString(total_impression));
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
