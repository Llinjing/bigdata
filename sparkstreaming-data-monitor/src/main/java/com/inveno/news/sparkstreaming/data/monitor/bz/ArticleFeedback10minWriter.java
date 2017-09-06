package com.inveno.news.sparkstreaming.data.monitor.bz;

import java.sql.Connection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.panhongan.util.FileUtil;
import com.github.panhongan.util.conf.Config;
import com.github.panhongan.util.db.MysqlSession;
import com.github.panhongan.util.db.MysqlUtil;

public class ArticleFeedback10minWriter {
	
	public static final String CLASS_NAME = ArticleFeedback10minWriter.class.getSimpleName();
	
	public static final Logger logger = LoggerFactory.getLogger(CLASS_NAME);
	
	public static final int VALID_COLUMNS = 7;
	
	public static void usage() {
		System.out.println(CLASS_NAME + " <data_file> <mysql_conf_file>");
	}
	
	public static void processLine(String line, MysqlSession session) {
		String [] arr = line.split("\t");
		if (arr != null && arr.length == VALID_COLUMNS) {
			String process_time = arr[0];
			String content_id = arr[1];
			String product_id = arr[2];
			//String config_id = arr[3];
			String click = arr[4];
			String impression = arr[5];
			String dwelltime = arr[6];
			
			String time = process_time.substring(0, 4) + "-" +
					process_time.substring(4, 6) + "-" +
					process_time.substring(6, 8) + " " +
					process_time.substring(8, 10) + ":" +
					process_time.substring(10, 12) + ":00";
			String sql = "insert into t_stat_feedback_10min(content_id,content_type,product,process_time,click,impression,dwell_time) values(" +
					"'" + content_id + "','news'," +
					"'" + product_id + "'," +
					"'" + time + "'," +
					click + "," +
					impression + "," +
					dwelltime + ");";
			try {
				boolean ret = session.executeUpdate(sql);
				if (ret) {
					logger.info("succeed : {}", sql);
				} else {
					logger.warn("failed : {}", sql);
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