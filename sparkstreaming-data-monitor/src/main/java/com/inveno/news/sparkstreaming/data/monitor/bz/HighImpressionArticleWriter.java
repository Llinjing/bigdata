package com.inveno.news.sparkstreaming.data.monitor.bz;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.panhongan.util.FileUtil;
import com.github.panhongan.util.TimeUtil;
import com.github.panhongan.util.conf.Config;
import com.github.panhongan.util.db.MysqlSession;
import com.github.panhongan.util.db.MysqlUtil;

public class HighImpressionArticleWriter {
	
	public static final String CLASS_NAME = HighImpressionArticleWriter.class.getSimpleName();
	
	public static final Logger logger = LoggerFactory.getLogger(CLASS_NAME);
	
	public static final int VALID_COLUMNS = 11;
	
	public static void usage() {
		System.out.println(CLASS_NAME + " <data_file> <mysql_conf_file>");
	}
	
	public static void processLine(String line, MysqlSession session) {
		String [] arr = line.split("\t");
		if (arr != null && arr.length == VALID_COLUMNS) {
			String process_time = arr[0];
			String content_id = arr[1];
			String product_id = arr[2];
			String config_id = arr[3];
			String language = arr[4];
			String content_type = arr[5];
			String scenario_channel = arr[6];
			String click = arr[7];
			String impression = arr[8];
			String dwelltime = arr[9];
			String feedback = arr[10];
			String sql = "select content_id, check_status from t_high_impression where " + 
					"content_id='" + content_id + "' " + 
					"and product='" + product_id + "' " + 
					"and config_id=" + config_id + " " + 
					"and content_type='" + content_type + "' " + 
					"and scenario_channel='" + scenario_channel + "' " + 
					"and feedback_type=" + feedback + ";";
			logger.debug(sql);
			
			try {
				boolean is_exist = false;
				boolean is_checked = false;
				ResultSet rs = session.executeQuery(sql);
				while (rs.next()) {
					is_exist = true;
					
					if (rs.getInt(2) == 1) {
						is_checked = true;
						break;
					}
				}
				
				if (is_exist) {
					if (is_checked) {
						logger.info("{},{},{} has been checked", product_id, config_id, content_id, content_type);
						// ignore, do nothing
					} else {
						sql = "update t_high_impression set click=" + click +
								",impression=" + impression +
								",dwelltime=" + dwelltime +
								",process_time='" + process_time + "' where " + 
								"content_id='" + content_id + "' " + 
								"and product='" + product_id + "' " + 
								"and config_id=" + config_id + " " + 
								"and content_type='" + content_type + "' " + 
								"and scenario_channel='" + scenario_channel + "' " + 
								"and feedback_type=" + feedback + ";";
						logger.debug(sql);
						
						session.executeUpdate(sql);
					}
				} else {
					sql = "select content_id, check_status from t_high_impression where " + 
							"content_id='" + content_id + "' and check_status=1;";
					logger.debug(sql);
					
					rs = session.executeQuery(sql);
					if (rs.next()) {
						sql = "insert into t_high_impression" + 
								"(content_id,dispatched,check_status,process_time,product,config_id,impression,click,dwelltime,feedback_type,check_time,operator,language,content_type,scenario_channel) values(" +
								"'" + content_id + "',1,1," +
								"'" + process_time + "'," +
								"'" + product_id + "'," +
								config_id + "," +
								impression + "," +
								click + "," +
								dwelltime + "," +
								feedback + "," + 
								"'" + TimeUtil.currTime("yyyy-MM-dd HH:mm:ss") + "'," +
								"'robot_auto','" + 
								language + "','" + 
								content_type + "','" + 
								scenario_channel+"');";
						logger.debug(sql);
						session.executeUpdate(sql);
					} else {
						sql = "insert into t_high_impression" + 
								"(content_id,dispatched,check_status,process_time,product,config_id,impression,click,dwelltime,feedback_type,language,content_type,scenario_channel) values(" +
								"'" + content_id + "',0,0," +
								"'" + process_time + "'," +
								"'" + product_id + "'," +
								config_id + "," +
								impression + "," +
								click + "," +
								dwelltime + "," +
								feedback + ",'" +
								language + "','" + 
								content_type + "','" + 
								scenario_channel + "');";
						logger.debug(sql);
						session.executeUpdate(sql);
					}
				}
			} catch (Exception e) {
				logger.warn(e.getMessage(), e);
			}
		}
	}
	
	public static void main(String [] args) {
		if (args.length != 2) {
			usage();
			return;
		}
		
		String data_file = args[0];
		String mysql_conf_file = args[1];
		
		Config config = new Config();
		if (!config.parse(mysql_conf_file)) {
			logger.warn("parse mysql_conf_file failed : {}", mysql_conf_file);
			return;
		}
		
		logger.info(config.toString());
		
		List<String> list = FileUtil.readFile(data_file);
		
		MysqlSession session = null;
		
		try {
			Connection conn = MysqlUtil.createConnection(config);
			if (conn != null) {
				session = new MysqlSession(conn);
				if (session != null) {
					for (String str : list) {
						processLine(str, session);
					}
				}
			} else {
				logger.warn("connection is null");
			}
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		} finally {
			MysqlUtil.closeMysqlSession(session);
		}
	}

}