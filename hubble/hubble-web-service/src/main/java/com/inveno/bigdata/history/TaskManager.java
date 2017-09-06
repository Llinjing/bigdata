package com.inveno.bigdata.history;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.github.panhongan.util.TimeUtil;
import com.github.panhongan.util.db.MysqlSession;
import com.github.panhongan.util.db.MysqlUtil;
import com.github.panhongan.util.db.MysqlUtil.MysqlPool;
import com.github.panhongan.util.thread.ControllableThread;

public class TaskManager extends ControllableThread {
	
	private static final Logger logger = LoggerFactory.getLogger(TaskManager.class);
	
	private static final int DIFF_TASK_TIME_INTERVAL = 5 * 1000;	// millionseconds
	
	private Map<String, List<Task>> tasks = 
			Collections.synchronizedMap(new HashMap<String, List<Task>>());
	
	private MysqlPool mysql_pool = null;
	
	private boolean can_finish = false;
	
	public TaskManager(MysqlPool mysql_pool) {
		this.mysql_pool = mysql_pool;
	}
	
	@Override
	public void uninit() {
		can_finish = true;
		
		while (!tasks.isEmpty()) {
			try {
				Thread.sleep(2 * 1000);
			} catch (Exception e) {
			}
		}
		
		super.uninit();
	}
	
	public boolean addTask(String key, Task task) {
		if (can_finish) {
			return false;
		}
		
		List<Task> list = tasks.get(key);
		if (list == null) {
			list = new ArrayList<Task>();
		}
		list.add(task);
		tasks.put(key, list);
		
		logger.info("add task : {}", task);
		return true;
	}

	@Override
	protected void work() {
		long curr_time = System.currentTimeMillis();
		
		List<String> canRemove = new ArrayList<String>();
		
		for (String sds : tasks.keySet()) {
			List<Task> list = tasks.get(sds);
			if (curr_time - list.get(0).submit_time_millsec > DIFF_TASK_TIME_INTERVAL) {
				if (this.mergeTasks(list)) {
					canRemove.add(sds);
					logger.info("merge tasks ok, size = {} : tasks = {}", list.size(), list);
				} else {
					logger.warn("merge tasks failed, size = {} : tasks = {}", list.size(), list);
				}
			}
		}
		
		for (String sds : canRemove) {
			tasks.remove(sds);
		}
	}
	
	private boolean mergeTasks(List<Task> merge_task_list) {
		boolean ret = false;
		
		String session_id = merge_task_list.get(0).session_id;
		String taskid = merge_task_list.get(0).taskid;
		String data_source = merge_task_list.get(0).data_source;
		
		Set<String> query_infos = new HashSet<String>();
		for (Task t : merge_task_list) {
			query_infos.add(t.query_info);
		}
		String query_info = this.mergeQueryInfo(query_infos);
		String hql = this.makeHQL(data_source, query_info);
		
		// write to db
		ret = this.writeToMysql(session_id, taskid, data_source, query_info, hql);
		return ret;
	}
	
	private String mergeQueryInfo(Set<String> query_infos) {
		Object [] arr = query_infos.toArray();
		JSONObject left = JSON.parseObject(arr[0].toString());
		for (int i = 1; i < arr.length; ++i) {
			JSONObject right = JSON.parseObject(arr[i].toString());
			left = this.mergeJson(left, right);
		}
		return left.toString();
	}
	
	private JSONObject mergeJson(JSONObject json_obj1, JSONObject json_obj2) {
		JSONObject json_obj = new JSONObject();
		
		json_obj.put("start_time", json_obj1.getString("start_time"));
		json_obj.put("end_time", json_obj1.getString("end_time"));
		json_obj.put("metrics", json_obj1.getJSONArray("metrics"));
		
		// dimensions
		JSONObject dimensions = new JSONObject();
		
		try {
			JSONObject dimensions1 = json_obj1.getJSONObject("dimensions");
			for (String key : dimensions1.keySet()) {
				dimensions.put(key, dimensions1.getJSONArray(key));
			}
			
			JSONObject dimensions2 = json_obj2.getJSONObject("dimensions");
			for (String key : dimensions2.keySet()) {
				JSONArray arr = dimensions.getJSONArray(key);
				arr.addAll(dimensions2.getJSONArray(key));
				dimensions.put(key, arr);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		}
		
		json_obj.put("dimensions", dimensions);
		
		// group by
		json_obj.put("groupby", json_obj1.getJSONArray("groupby"));
		
		return json_obj;
	}
	
	private String makeHQL(String data_source, String query_info) {
		JSONObject json_obj = JSONObject.parseObject(query_info, Feature.OrderedField);
		
		// select
		StringBuffer select = new StringBuffer("select date,");
		
		JSONArray groupby_arr = json_obj.getJSONArray("groupby");
		if (groupby_arr != null && groupby_arr.size() > 0) {
			for (int i = 0; i < groupby_arr.size(); ++i) {
				select.append(groupby_arr.get(i));
				select.append(",");
			}
		}
		
		JSONArray metrics = json_obj.getJSONArray("metrics");
		for (int i = 0; i < metrics.size() - 1; ++i) {
			select.append(metrics.getString(i));
			select.append(",");
		}
		select.append(metrics.getString(metrics.size() - 1));
		
		// where
		StringBuffer where = new StringBuffer("where ");
		where.append("date between \"");
		where.append(json_obj.getString("start_time").substring(0, 10));
		where.append("\" and \"");
		where.append(json_obj.getString("end_time").substring(0, 10));
		where.append("\"");
		
		JSONObject dimensions = json_obj.getJSONObject("dimensions");
		for (String key : dimensions.keySet()) {
			where.append(" and ");
			where.append(key);
			
			// dedup JSONArray
			Set<String> s = new HashSet<String>();
			Object [] obj_arr = dimensions.getJSONArray(key).toArray();
			for (int i = 0; i < obj_arr.length; ++i) {
				s.add(obj_arr[i].toString());
			}
			JSONArray arr = new JSONArray();
			arr.addAll(s);
			
			if (arr.isEmpty()) {
				where.append("=\"all\"");
			} else if (arr.size() == 1) {
				where.append("=");
				where.append("\"");
				where.append(arr.getString(0));
				where.append("\"");
			} else {
				where.append(" in (");
				for (int i = 0; i < arr.size(); ++i) {
					where.append("\"");
					where.append(arr.getString(i));
					where.append("\"");
					if (i != arr.size() - 1) {
						where.append(",");
					}
				}
				where.append(")");
			}
		}
		
		// group by
		StringBuffer groupby = new StringBuffer("group by date");
		if (groupby_arr != null && groupby_arr.size() > 0) {
			for (int i = 0; i < groupby_arr.size(); ++i) {
				groupby.append(",");
				groupby.append(groupby_arr.get(i));
			}
		}
		
		// hql
		String hql = select.toString() + " from " + data_source + " " + where.toString() + " " + groupby.toString();
		
		logger.info("hql : {}", hql);
		
		return hql;
	}
	
	private boolean writeToMysql(String session_id, String taskid, String data_source, String query_info, String hql) {
		boolean is_ok = false;
	
		if (mysql_pool != null) {
			MysqlSession session = null;
			
			try {
				session = new MysqlSession(mysql_pool.getConnection());
				String sql = "insert into " + Constants.HISTORY_TASK_TABLE_NAME + 
							"(session_id, taskid,submit_time,finish_time,status,data_source,query_info,hql,output_path) values(" +
							"'" + session_id + "'," +
							"'" + taskid + "'," +
							"'" + TimeUtil.currTime("yyyy-MM-dd HH:mm:ss") + "'," +
							"'0000-00-00 00:00:00'," +
							"'" + TaskStatus.ACCEPTED.toString() + "'," +
							"'" + data_source + "'," +
							"'" + query_info + "'," +
							"'" + hql + "'," +
							"'NULL');";
				if (session.executeUpdate(sql)) {
					is_ok = true;
					logger.info("write sql succeed : {}", sql);
				} else {
					logger.info("write sql failed : {}", sql);
				}
			} catch (Exception e) {
				logger.warn(e.getMessage(), e);
			} finally {
				MysqlUtil.closeMysqlSession(session);
			}
		}
		
		return is_ok;
	}

}
