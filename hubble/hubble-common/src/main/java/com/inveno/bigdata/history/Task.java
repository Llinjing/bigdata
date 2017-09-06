package com.inveno.bigdata.history;

import com.alibaba.fastjson.JSON;

public class Task {
	
	public String session_id = null;
	
	public String taskid = null;
	
	public String data_source = null;
	
	public String hql = null;
	
	public String query_info = null;
	
	public String output_path = null;
	
	public Long submit_time_millsec = null;
	
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
