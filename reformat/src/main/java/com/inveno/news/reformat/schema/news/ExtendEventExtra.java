package com.inveno.news.reformat.schema.news;

import com.alibaba.fastjson.JSON;

public class ExtendEventExtra {
	
	private String action_name = null;

	private Object action_value = null;
	
	private String action_type = null;
	
	public void setAction_name(String action_name) {
		this.action_name = action_name;
	}
	
	public String getAction_name() {
		return action_name;
	}

	public void setAction_value(Object action_value) {
		this.action_value = action_value;
	}

	public Object getAction_value() {
		return action_value;
	}
	
	public void setAction_type(String action_type) {
		this.action_type = action_type;
	}

	public String getAction_type() {
		return action_type;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
