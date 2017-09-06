package com.inveno.news.reformat.schema.ad;

import com.alibaba.fastjson.JSON;

public class Marketing {

	private String marketing_id = null;
	
	private String position_id = null;
	
	private String position_type = null;
	
	private String target_size = null;
	
	private String pay_model = null;
	
	private String unit_price = null;
	
	private String daily_count_limit = null;
	
	private String third_position_id = null;
	
	public void setMarketing_id(String marketing_id) {
		this.marketing_id = marketing_id;
	}

	public String getMarketing_id() {
		return marketing_id;
	}

	public void setPosition_id(String position_id) {
		this.position_id = position_id;
	}

	public String getPosition_id() {
		return position_id;
	}
	
	public void setPosition_type(String position_type) {
		this.position_type = position_type;
	}

	public String getPosition_type() {
		return position_type;
	}

	public void setTarget_size(String target_size) {
		this.target_size = target_size;
	}

	public String getTarget_size() {
		return target_size;
	}

	public void setPay_model(String pay_model) {
		this.pay_model = pay_model;
	}

	public String getPay_model() {
		return pay_model;
	}

	public void setUnit_price(String unit_price) {
		this.unit_price = unit_price;
	}

	public String getUnit_price() {
		return unit_price;
	}

	public void setDaily_count_limit(String daily_count_limit) {
		this.daily_count_limit = daily_count_limit;
	}

	public String getDaily_count_limit() {
		return daily_count_limit;
	}

	public String getThird_position_id() {
		return third_position_id;
	}

	public void setThird_position_id(String third_position_id) {
		this.third_position_id = third_position_id;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
