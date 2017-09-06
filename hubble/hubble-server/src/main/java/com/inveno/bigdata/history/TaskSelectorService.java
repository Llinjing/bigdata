package com.inveno.bigdata.history;

import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.panhongan.util.db.MysqlSession;
import com.github.panhongan.util.db.MysqlUtil.MysqlPool;
import com.github.panhongan.util.thread.ControllableThread;

public class TaskSelectorService extends ControllableThread {
	
	private static final String CLASS_NAME = TaskSelectorService.class.getSimpleName();
	
	private static Logger logger = LoggerFactory.getLogger(CLASS_NAME);
	
	private static final int DEFAULT_MAX_BLOCKED_TASKS = 100;
	
	private int max_blocked_tasks = DEFAULT_MAX_BLOCKED_TASKS;
	
	private MysqlPool mysql_pool = null;
	
	private BlockingQueue<Task> queue = null;
	
	public TaskSelectorService(MysqlPool mysql_pool, BlockingQueue<Task> queue) {
		this(mysql_pool, queue, DEFAULT_MAX_BLOCKED_TASKS);
	}
	
	public TaskSelectorService(MysqlPool mysql_pool, BlockingQueue<Task> queue, int max_blocked_tasks) {
		this.mysql_pool = mysql_pool;
		this.queue = queue;
		this.max_blocked_tasks = max_blocked_tasks;
	}

	@Override
	public boolean init() {
		boolean is_ok = super.init();
		if (is_ok) {
			is_ok = (mysql_pool != null);
		}
		return is_ok;
	}

	@Override
	public void uninit() {
		super.uninit();
	}

	@Override
	protected void work() {
		int curr_blocked_tasks = queue.size();
		
		logger.info("blocked task num is : {}", curr_blocked_tasks);
		
		if (curr_blocked_tasks < max_blocked_tasks) {
			try {
				AbstractTaskSelector selector = new DefaultTaskSelector(new MysqlSession(mysql_pool.getConnection()));
				for (Task task : selector.select()) {
					queue.put(task);
					logger.info("receive task : {}", task.toString());
				}
			} catch (Exception e) {
				logger.warn(e.getMessage(), e);
			}
		}
	}

}
