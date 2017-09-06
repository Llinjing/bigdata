package com.inveno.news.reformat.schema.ad;

import com.alibaba.fastjson.JSON;

public class AdScenario {
	
	private String channel_id = null;
	
	private String channel_desc = null;
	
	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}
	
	public String getChannel_id() {
		return channel_id;
	}
	
	public void setChannel_desc(String channel_desc) {
		this.channel_desc = channel_desc;
	}
	
	public String getChannel_desc() {
		return channel_desc;
	}
	
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
}
