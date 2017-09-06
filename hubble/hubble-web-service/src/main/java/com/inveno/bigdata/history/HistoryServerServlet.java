package com.inveno.bigdata.history;

import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.github.panhongan.util.StringUtil;
import com.github.panhongan.util.db.MysqlUtil;
import com.github.panhongan.util.db.MysqlUtil.MysqlPool;

import org.apache.commons.codec.binary.Base64;


public class HistoryServerServlet extends HttpServlet {

	private static final long serialVersionUID = -2397406729860365893L;

	private static final Logger logger = LoggerFactory.getLogger(HistoryServerServlet.class);
	
	private static Base64 base64 = new Base64();
	
	private MysqlPool mysql_pool = null;
	
	private TaskManager task_manager = null;
	
	@Override
	public void init() throws ServletException {
		String mysql_conf = System.getProperty("catalina.home") + "/webapps/hubble-web-service/conf/mysql.properties";
		logger.info("mysql config file : {}", mysql_conf);;
		mysql_pool = MysqlUtil.createMysqlPool(mysql_conf);
		if (mysql_pool != null) {
			logger.info("connect to mysql succeed");
		} else {
			logger.warn("connect to mysql failed, check mysql config");
			System.exit(1);
		}
		
		task_manager = new TaskManager(mysql_pool);
		task_manager.setSleepInterval(5 * 1000);
		if (task_manager.init()) {
			logger.info("TaskManager init ok");
		} else {
			logger.warn("TaskManager init failed");
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		// output
		JSONObject output_json = new JSONObject();
					
		try {
			request.setCharacterEncoding("UTF-8");
			
			String session_id = request.getParameter("session_id");
			if (StringUtil.isEmpty(session_id)) {
				logger.warn("no session");
				throw new Exception("no session");
			}

			String data_source = request.getParameter("data_source");
			if (!DataSource.isValid(data_source)) {
				logger.warn("invalid data source : {}", data_source);
				throw new Exception("invalid data source");
			}

			// parse query info
			String query_info = new String(base64.decode(request.getParameter("query_info")));
			logger.info("query_info : {}", query_info);
			
			
			String taskid = data_source + "_" + System.currentTimeMillis();
			
			Task task = new Task();
			task.submit_time_millsec = System.currentTimeMillis();
			task.session_id = session_id;
			task.taskid = taskid;
			task.data_source = data_source;
			task.query_info = query_info;
			
			boolean is_ok = task_manager.addTask(session_id + "#" + data_source, task);
			if (is_ok) {
				output_json.put("result", "ok");
				output_json.put("task_id", taskid);
			} else {
				output_json.put("result", "not_ok");
				output_json.put("desc", "service is not active");
			}
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
			output_json.put("result", "not_ok");
			output_json.put("desc", e.getMessage());
		}
		
		// output
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			writer.write(output_json.toString());
			writer.flush();
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
	
	@Override
	public void destroy() {
		if (task_manager != null) {
			task_manager.uninit();
		}
		
		if (mysql_pool != null) {
			MysqlUtil.closeMysqlPool(mysql_pool);
			logger.info("mysql pool closed");
		}
	}

}
