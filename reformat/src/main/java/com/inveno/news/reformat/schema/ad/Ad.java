package com.inveno.news.reformat.schema.ad;

import com.alibaba.fastjson.JSON;

public class Ad {

	private String ad_id = null;
	
	private String ad_source = null;
	
	private String type = null;
	
	private String industry = null;
	
	private String advertiser_id = null;
	
	private String advertiser_name = null;

	private String ad_product_name = null;
	
	private String third_ad_source = null;
	
	public void setAd_id(String ad_id) {
		this.ad_id = ad_id;
	}

	public String getAd_id() {
		return ad_id;
	}
	
	public void setAd_source(String ad_source) {
		this.ad_source = ad_source;
	}
	
	public String getAd_source() {
		return ad_source;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getIndustry() {
		return industry;
	}
	
	public void setAdvertiser_id(String advertiser_id) {
		this.advertiser_id = advertiser_id;
	}
	
	public String getAdvertiser_id() {
		return advertiser_id;
	}
	
	public void setAdvertiser_name(String advertiser_name) {
		this.advertiser_name = advertiser_name;
	}
	
	public String getAdvertiser_name() {
		return advertiser_name;
	}

	public String getAd_product_name() {
		return ad_product_name;
	}

	public void setAd_product_name(String ad_product_name) {
		this.ad_product_name = ad_product_name;
	}

	public String getThird_ad_source() {
		return third_ad_source;
	}

	public void setThird_ad_source(String third_ad_source) {
		this.third_ad_source = third_ad_source;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
