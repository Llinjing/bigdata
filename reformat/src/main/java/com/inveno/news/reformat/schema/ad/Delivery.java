package com.inveno.news.reformat.schema.ad;

import com.alibaba.fastjson.JSON;

public class Delivery {
	
	private String delivery_id = null;
	
	private String material_id = null;

	private Ad ad = null;
	
	private Marketing marketing = null;
	
	private OperationStrategy operation_strategy = null;
	
	private AdScenario ad_scenario = null;
	
	public void setDelivery_id(String delivery_id) {
		this.delivery_id = delivery_id;
	}

	public String getDelivery_id() {
		return delivery_id;
	}

	public void setMaterial_id(String material_id) {
		this.material_id = material_id;
	}

	public String getMaterial_id() {
		return material_id;
	}
	
	public void setAd(Ad ad) {
		this.ad = ad;
	}

	public Ad getAd() {
		return ad;
	}

	public void setMarketing(Marketing marketing) {
		this.marketing = marketing;
	}

	public Marketing getMarketing() {
		return marketing;
	}

	public void setOperation_strategy(OperationStrategy operation_strategy) {
		this.operation_strategy = operation_strategy;
	}

	public OperationStrategy getOperation_strategy() {
		return operation_strategy;
	}
	
	public void setAd_scenario(AdScenario ad_scenario) {
		this.ad_scenario = ad_scenario;
	}
	
	public AdScenario getAd_scenario() {
		return ad_scenario;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
