package com.inveno.news.reformat.schema;

import com.alibaba.fastjson.JSON;

public class ContentPacket {
	
	private String strategy = null;
	
	private String ad_source = null;	// tmp
	
	private String source = null;
	
	private String push_explore_id = null;
	
	private int category = -1;
	
	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}
	
	public String getStrategy() {
		return strategy;
	}
	
	public void setAd_source(String ad_source) {
		this.ad_source = ad_source;
	}
	
	public String getAd_source() {
		return ad_source;
	}
	
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getPush_explore_id() {
		return push_explore_id;
	}

	public void setPush_explore_id(String push_explore_id) {
		this.push_explore_id = push_explore_id;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
