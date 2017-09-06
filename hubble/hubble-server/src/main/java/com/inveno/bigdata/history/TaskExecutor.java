package com.inveno.bigdata.history;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.panhongan.util.StringUtil;
import com.github.panhongan.util.TimeUtil;
import com.github.panhongan.util.conf.Config;
import com.github.panhongan.util.db.MysqlSession;
import com.github.panhongan.util.db.MysqlUtil;
import com.github.panhongan.util.db.MysqlUtil.MysqlPool;
import com.github.panhongan.util.hive.HiveSession;
import com.github.panhongan.util.hive.HiveUtil;
import com.github.panhongan.util.process.RuntimeUtil;
import com.github.panhongan.util.sql.SqlUtil;
import com.github.panhongan.util.thread.ControllableThread;

public class TaskExecutor extends ControllableThread {
	
	private static final String CLASS_NAME = TaskExecutor.class.getSimpleName();
	
	private static Logger logger = LoggerFactory.getLogger(CLASS_NAME);
	
	private static HistoryBigdataQueryServiceConfig config = HistoryBigdataQueryServiceConfig.getInstance();
	
	private BlockingQueue<Task> queue = null;
	
	private MysqlPool mysql_pool = null;
	
	private Config hive_config = null;
	
	public TaskExecutor(BlockingQueue<Task> queue, MysqlPool mysql_pool, Config hive_config) {
		this.queue = queue;
		this.mysql_pool = mysql_pool;
		this.hive_config = hive_config;
		assert(queue != null);
		assert(mysql_pool != null);
		assert(hive_config != null);
	}

	@Override
	protected void work() {
		Task task = queue.poll();
		if (task != null) {
			String taskid = task.taskid;
			String hql = task.hql;
			
			Task finished_task = this.hasFinishedTask(taskid, hql);
			if (finished_task == null) {
				logger.info("need to schedule, taskid : {}", taskid);
				
				Map<String, String> map = new HashMap<String, String>();
				map.put("status", TaskStatus.RUNNING.toString());
				this.updateTask(taskid, map);
				
				this.queryFromHive(taskid, hql);
			} else {
				logger.info("The hql has been scheduled, no need to schedule again. finished taskid : {}, current taskid : {}", 
						finished_task.taskid, taskid);
				
				// set finished
				Map<String, String> map = new HashMap<String, String>();
				map.put("status", TaskStatus.SUCCEED.toString());
				map.put("finish_time", TimeUtil.currTime("yyyy-MM-dd HH:mm:ss"));
				map.put("output_path", this.makeFilePath(config.getConfig().getString("http.prefix"), taskid));
				
				// copy output
				String output_path = config.getConfig().getString("hive.local.data.path");
				String finished_task_output_file = this.makeFilePath(output_path, finished_task.taskid);
				String current_task_output_file = this.makeFilePath(output_path, taskid);
				
				String cmd = "cp -f " + finished_task_output_file + " " + current_task_output_file;
				boolean is_ok = RuntimeUtil.exec(cmd, null, null);
				if (is_ok) {
					logger.info("copy {} to {} succeed", finished_task_output_file, current_task_output_file);
				} else {
					logger.warn("copy {} to {} succeed", finished_task_output_file, current_task_output_file);
				}
			}
		}
	}
	
	private Task hasFinishedTask(String taskid, String hql) {
		Task task = null;
		
		if (mysql_pool != null) {
			MysqlSession session = null;
			
			try {
				session = new MysqlSession(mysql_pool.getConnection());
				String sql = "select taskid, output_path from " + Constants.HISTORY_TASK_TABLE_NAME +
						" where hql='" + hql + "'" +
						" and status='" + TaskStatus.SUCCEED.toString() + "'" +
						" and taskid!='" + taskid + "' limit 1;";
				ResultSet rs = session.executeQuery(sql);
				if (rs.next()) {
					task = new Task();
					task.taskid = rs.getString(1);
					task.output_path = rs.getString(2);
				}
				SqlUtil.closeResultSet(rs);
			} catch (Exception e) {
				logger.warn(e.getMessage(), e);
			} finally {
				MysqlUtil.closeMysqlSession(session);
			}
		}
		
		return task;
	}
	
	private void updateTask(String taskid, Map<String, String> key_values) {
		if (mysql_pool != null) {
			MysqlSession session = null;
			
			StringBuffer set = new StringBuffer();
			for (String key : key_values.keySet()) {
				set.append(key);
				set.append("=\"");
				set.append(key_values.get(key));
				set.append("\",");
			}
			
			try {
				session = new MysqlSession(mysql_pool.getConnection());
				String sql = "update " + Constants.HISTORY_TASK_TABLE_NAME + 
						" set " + StringUtil.trimSuffix(set.toString(), ",") + " where taskid='" + taskid + "';";
				if (session.executeUpdate(sql)) {
					logger.info("taskid = {}, set status = {} succeed", taskid, key_values.toString());
				} else {
					logger.warn("taskid = {}, set status = {} failed", taskid, key_values.toString());
				}
			} catch (Exception e) {
				logger.warn(e.getMessage(), e);
			} finally {
				MysqlUtil.closeMysqlSession(session);
			}
		}
	}
	
	private void queryFromHive(String taskid, String hql) {
		HiveSession session = null;
		ResultSet rs = null;
		BufferedWriter writer = null;
		boolean is_ok = false;
		
		String output_file = this.makeFilePath(config.getConfig().getString("hive.local.data.path"), taskid);
		
		try {
			writer = new BufferedWriter(new FileWriter(output_file));
			
			session = HiveUtil.createHiveSession(hive_config);
			rs = session.executeQuery(hql);
			ResultSetMetaData meta_data = rs.getMetaData();
			for (int i = 1; i <= meta_data.getColumnCount(); ++i) {
				writer.append(meta_data.getColumnName(i));
				writer.append(",");
			}
			writer.newLine();
			
			while (rs.next()) {
				for (int i = 1; i <= meta_data.getColumnCount(); ++i) {
					writer.append(rs.getString(i));
					writer.append(",");
				}
				writer.newLine();
			}
			
			is_ok = true;
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		} finally {
			SqlUtil.closeResultSet(rs);
			HiveUtil.closeHiveSession(session);
			
			if (writer != null) {
				try {
					writer.close();
				} catch (Exception e) {
				}
			}
			
			// update task
			Map<String, String> key_values = new HashMap<String, String>();
			key_values.put("finish_time", TimeUtil.currTime("yyyy-MM-dd HH:mm:ss"));
			
			if (is_ok) {
				logger.info("task {} run succeed", taskid);
				key_values.put("status", TaskStatus.SUCCEED.toString());
				key_values.put("output_path", this.makeFilePath(config.getConfig().getString("http.prefix"), taskid));
			} else {
				logger.warn("task {} run failed", taskid);
				key_values.put("status", TaskStatus.FAILED.toString());
			}
			
			this.updateTask(taskid, key_values);
		}
	}
	
	private String makeFilePath(String base_dir, String taskid) {
		return base_dir + "/" + taskid + ".csv";
	}

}
