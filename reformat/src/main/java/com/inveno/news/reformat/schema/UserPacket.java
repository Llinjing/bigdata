package com.inveno.news.reformat.schema;

import com.alibaba.fastjson.JSON;

public class UserPacket {
	
	private String news_configid = null;
	
	private String ad_configid = null;
	
	private String biz_configid = null;
	
	public void setNews_configid(String news_configid) {
		this.news_configid = news_configid;
	}
	
	public String getNews_configid() {
		return news_configid;
	}
	
	public void setAd_configid(String ad_configid) {
		this.ad_configid = ad_configid;
	}
	
	public String getAd_configid() {
		return ad_configid;
	}
	
	public void setBiz_configid(String biz_configid) {
		this.biz_configid = biz_configid;
	}
	
	public String getBiz_configid() {
		return biz_configid;
	}
	
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
