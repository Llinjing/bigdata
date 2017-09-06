package com.inveno.news.reformat.schema.news;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ListpageDwelltimeExtra {

	private String dwelltime = null;
	
	private String view_mode = null;

	private JSONObject extra_msg = null;

	public void setDwelltime(String dwelltime) {
		this.dwelltime = dwelltime;
	}

	public String getDwelltime() {
		return dwelltime;
	}
	
	public void setView_mode(String view_mode) {
		this.view_mode = view_mode;
	}
	
	public String getView_mode() {
		return view_mode;
	}
	
	public JSONObject getExtra_msg() {
		return extra_msg;
	}

	public void setExtra_msg(JSONObject extra_msg) {
		this.extra_msg = extra_msg;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
