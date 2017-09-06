package com.inveno.news.reformat.schema.news;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class BackendServiceExtra {
	
	private JSONObject event_value = null;
	
	public void setEvent_value(JSONObject event_value) {
		this.event_value = event_value;
	}
	
	public JSONObject getEvent_value() {
		return event_value;
	}
	
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
