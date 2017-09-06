package com.inveno.bigdata.history;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.panhongan.util.db.MysqlSession;
import com.github.panhongan.util.db.MysqlUtil;
import com.github.panhongan.util.sql.SqlUtil;

public class DefaultTaskSelector extends AbstractTaskSelector {
	
	private static Logger logger = LoggerFactory.getLogger(DefaultTaskSelector.class);
	
	private MysqlSession session = null;
	
	public DefaultTaskSelector(MysqlSession session) {
		this.session = session;
	}

	@Override
	public List<Task> select() {
		List<Task> tasks = new ArrayList<Task>();
		
		if (session != null) {
			ResultSet rs = null;
			
			try {
				String sql = "select taskid, hql from " + Constants.HISTORY_TASK_TABLE_NAME + 
						" where status='" + TaskStatus.ACCEPTED.toString() + "' limit 1;";
				rs = session.executeQuery(sql);
				while (rs.next()) {
					Task t = new Task();
					t.taskid = rs.getString(1);
					t.hql = rs.getString(2);
					tasks.add(t);
				}
			} catch (Exception e) {
				logger.warn(e.getMessage(), e);
			} finally {
				SqlUtil.closeResultSet(rs);
				MysqlUtil.closeMysqlSession(session);
			}
		}
		
		return tasks;
	}

}
