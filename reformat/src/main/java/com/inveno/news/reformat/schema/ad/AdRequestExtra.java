package com.inveno.news.reformat.schema.ad;

import com.alibaba.fastjson.JSON;

public class AdRequestExtra {
	
	private Delivery delivery = null;
	
	private String content_type = null;
	
	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}
	
	public Delivery getDelivery() {
		return delivery;
	}
	
	public void setContent_type(String content_type) {
		this.content_type = content_type;
	}
	
	public String getContent_type() {
		return content_type;
	}
	
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
