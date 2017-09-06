package com.inveno.news.advertisement.info.aggregator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.panhongan.util.conf.Config;
import com.github.panhongan.util.db.MysqlSession;
import com.github.panhongan.util.db.MysqlUtil;

public class TestMysql {

	public static final String CLASS_NAME = TestMysql.class.getSimpleName();
	
	public static final Logger logger = LoggerFactory.getLogger(CLASS_NAME);

    public static String getDateTime(ResultSet rs, String col_str) throws SQLException {
        String ret = "";
        ret = rs.getDate(col_str).toString() + " " + rs.getTime(col_str);
        return ret;
    }

	public static void search(MysqlSession session) {
		String sql = "select  case when b.create_time is null then '' else b.create_time end create_time,b.update_time from t_dsp_ad a left join t_dsp_pms_adapter b on a.ad_id=b.ad_id  where a.ad_id=1153062925322715136;";
		//String sql = "select * from t_dsp_ad limit 10";
        logger.debug(sql);
		
		try {
			ResultSet rs = session.executeQuery(sql);
			while (rs.next()) {
                //Date time1 = rs.getDate("create_time");
                System.out.println("create_time " + rs.getString("create_time").split("\\.")[0]);
                //String create_time = getDateTime(rs,"create_time");
                //System.out.println("create_time " + create_time);
               // logger.info("id:" + rs.getString(1) + "  product_id" + rs.getString(5));
			}
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		}
	}
	
	public static void usage() {
		System.out.println(CLASS_NAME + " <mysql_conf_file>");
	}
	
	public static void main(String[] args) {
		/*if (args.length != 1) {
			usage();
			return;
		}*/
		
		//String mysql_conf_file = args[0];
        String mysql_conf_file = "E:\\code\\advertisement\\mysql.properties";
		Config conf = new Config();
		
		if (!conf.parse(mysql_conf_file)) {
			logger.warn("parse mysql_conf_file failed : {}", mysql_conf_file);
			return;
		}
		logger.info(conf.toString());

		MysqlSession session = null;
		try {
			Connection conn = MysqlUtil.createConnection(conf);
			if (conn != null) {
				session = new MysqlSession(conn);
				if (session != null) {
					search(session);
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
