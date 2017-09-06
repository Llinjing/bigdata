package com.inveno.bigdata.history;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.panhongan.util.conf.Config;
import com.github.panhongan.util.control.Lifecycleable;
import com.github.panhongan.util.db.MysqlUtil.MysqlPool;

public class TaskScheduler implements Lifecycleable {
	
	private static final String CLASS_NAME = TaskScheduler.class.getSimpleName();
	
	private static Logger logger = LoggerFactory.getLogger(CLASS_NAME);
	
	private static final int DEFAULT_MAX_RUNNING_TASKS = 2;
	
	private int max_running_tasks = DEFAULT_MAX_RUNNING_TASKS;
	
	private BlockingQueue<Task> queue = null;
	
	private MysqlPool mysql_pool = null;
	
	private Config hive_config = null;
	
	private List<Lifecycleable> executors = new ArrayList<Lifecycleable>();
	
	public TaskScheduler(BlockingQueue<Task> queue, MysqlPool mysql_pool, Config hive_config) {
		this(queue, mysql_pool, hive_config, DEFAULT_MAX_RUNNING_TASKS);
	}
	
	public TaskScheduler(BlockingQueue<Task> queue, MysqlPool mysql_pool, Config hive_config, int max_running_tasks) {
		this.queue = queue;
		this.mysql_pool = mysql_pool;
		this.hive_config = hive_config;
		this.max_running_tasks = max_running_tasks;
	}

	@Override
	public boolean init() {
		for (int i = 0; i < max_running_tasks; ++i) {
			TaskExecutor executor = new TaskExecutor(queue, mysql_pool, hive_config);
			executor.setName(executor.getClass().getSimpleName() + "_" + i);
			executor.setSleepInterval(5 * 1000);
			if (executor.init()) {
				executors.add(executor);
			}
		}
		return (executors.size() == max_running_tasks);
	}

	@Override
	public void uninit() {
		while (!queue.isEmpty()) {
			try {
				Thread.sleep(3 * 1000);
			} catch (Exception e) {
			}
		}
		
		logger.info("all tasks have been handled");
		
		for (Lifecycleable executor : executors) {
			executor.uninit();
		}
	}

}
