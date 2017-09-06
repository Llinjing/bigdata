package com.inveno.news.reformat.schema.news;

import com.alibaba.fastjson.JSON;

public class Refer {
	
	private String content_id = null;
	
	private String content_type = null;
	
	public void setContent_id(String content_id) {
		this.content_id = content_id;
	}
	
	public String getContent_id() {
		return content_id;
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
