package com.inveno.news.advertisement.info.aggregator.util;

import com.github.panhongan.util.conf.Config;
import com.github.panhongan.util.db.MysqlSession;
import com.github.panhongan.util.db.MysqlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;

/**
 * Created by dory on 2016/10/10.
 */
public class MySqlDeal {

    static Connection conn = null;
    static MysqlSession session=null;

    final static int CONFIG_ERR = -1;
    final static int SESSION_ERR = -2;

    public static final String CLASS_NAME = MysqlUtil.class.getSimpleName();

    public static final Logger logger = LoggerFactory.getLogger(CLASS_NAME);

    public static int init(String mysql_conf_file){
        Config config = new Config();
        if (!config.parse(mysql_conf_file)) {
            logger.warn("parse mysql_conf_file failed : {}", mysql_conf_file);
            return CONFIG_ERR;
        }
        conn = MysqlUtil.createConnection(config);
        if(conn != null){
            session = new MysqlSession(conn);
            return 0;
        }else {
            return SESSION_ERR;
        }

    }
    
    public static ResultSet querySql(String sql){
        ResultSet rs = null;
        try {
            if(session != null) {
                rs = session.executeQuery(sql);
            }
        }catch (Exception e) {
			logger.warn(e.getMessage(), e);
        }
        return rs;
    }

    public static void close(){
        if(session != null){
            MysqlUtil.closeMysqlSession(session);
        }
        if(conn != null){
            MysqlUtil.closeConnection(conn);
        }
    }
}
