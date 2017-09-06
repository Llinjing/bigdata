package com.inveno.news.advertisement.info.aggregator;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class TestJson {

	public static void main(String[] args) {
        // joint json
        JSONArray json_arr = new JSONArray();
        
        JSONObject json_product_id = new JSONObject();
        JSONObject json_detail = new JSONObject();
        json_detail.put("click", "click_value");
        json_detail.put("impression", "impression_value");
        json_detail.put("gmp", "gmp_value");
        json_product_id.put("coolpad", json_detail);
        json_arr.add(json_product_id);

        JSONObject json_product_id1 = new JSONObject();
        JSONObject json_detail1 = new JSONObject();
        json_detail1.put("click", "click_value1");
        json_detail1.put("impression", "impression_value1");
        json_detail1.put("gmp", "gmp_value1");
        json_product_id1.put("emui", json_detail1);
        json_arr.add(json_product_id1);
        
        System.out.println(json_arr.toString());

	}

}
