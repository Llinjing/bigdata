package com.inveno.news.reformat.schema.news;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.inveno.news.reformat.schema.ContentPacket;

public class ArticleDwelltimeExtra {

	private String content_id = null;

	private String content_type = null;

	private String display = null;

	private ContentPacket cpack = null;

	private String dwelltime = null;

	private Refer refer = null;
	
	private String play_time = null;
	
	private String view_mode = null;

	private JSONObject extra_msg = null;

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

	public void setDisplay(String display) {
		this.display = display;
	}

	public String getDisplay() {
		return display;
	}

	public void setCpack(ContentPacket cpack) {
		this.cpack = cpack;
	}

	public ContentPacket getCpack() {
		return cpack;
	}

	public void setDwelltime(String dwelltime) {
		this.dwelltime = dwelltime;
	}

	public String getDwelltime() {
		return dwelltime;
	}

	public void setRefer(Refer refer) {
		this.refer = refer;
	}

	public Refer getRefer() {
		return refer;
	}
	
	public void setPlay_time(String play_time) {
		this.play_time = play_time;
	}
	
	public String getPlay_time() {
		return play_time;
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
