package com.inveno.news.reformat.schema.news;

import com.alibaba.fastjson.JSON;

public class ActivityDwelltimeExtra {
	
	private String page_name = null;
	
	private String dwelltime = null;
	
	public void setPage_name(String page_name) {
		this.page_name = page_name;
	}
	
	public String getPage_name() {
		return page_name;
	}
	
	public void setDwelltime(String dwelltime) {
		this.dwelltime = dwelltime;
	}
	
	public String getDwelltime() {
		return dwelltime;
	}
	
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
