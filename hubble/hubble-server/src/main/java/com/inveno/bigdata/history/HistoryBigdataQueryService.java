package com.inveno.bigdata.history;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.panhongan.util.conf.Config;
import com.github.panhongan.util.control.Lifecycleable;
import com.github.panhongan.util.db.MysqlUtil;
import com.github.panhongan.util.db.MysqlUtil.MysqlPool;
import com.github.panhongan.util.hive.HiveUtil;
import com.github.panhongan.util.path.PathUtil;

public class HistoryBigdataQueryService implements Lifecycleable {
	
	private static final String CLASS_NAME = HistoryBigdataQueryService.class.getSimpleName();
	
	private static final Logger logger = LoggerFactory.getLogger(HistoryBigdataQueryService.class);

	private static HistoryBigdataQueryServiceConfig config = HistoryBigdataQueryServiceConfig.getInstance();
	
	private static final int DEFAULT_MAX_RUNNING_TASKS = 2;
	
	private TaskSelectorService task_selector_service = null;
	
	private TaskScheduler task_scheduler = null;
	
	private MysqlPool mysql_pool = null;
	
	private BlockingQueue<Task> queue = new ArrayBlockingQueue<Task>(1000);

	@Override
	public boolean init() {
		// local data path
		Config conf = config.getConfig();
		String local_data_path = conf.getString("hive.local.data.path");
		PathUtil.createRecursiveDir(local_data_path);
		
		// hive config
		Config hive_conf = new Config();
		String hive_conf_file = conf.getString("hive.properties");
		if (!hive_conf.parse(hive_conf_file)) {
			logger.warn("parse hive config file failed : {}", hive_conf_file);
			return false;
		}
		
		if (!HiveUtil.validateHiveConfig(hive_conf)) {
			logger.warn("connect to hive, check config");
			return false;
		}
		
		// mysql pool
		mysql_pool = MysqlUtil.createMysqlPool(conf.getString("mysql.properties"));
		if (mysql_pool == null) {
			logger.warn("create mysql pool failed");
			return false;
		}
		
		// task selector
		task_selector_service = new TaskSelectorService(mysql_pool, queue);
		task_selector_service.setSleepInterval(10 * 1000);
		if (!task_selector_service.init()) {
			logger.warn("TaskSelectorService init failed");
			return false;
		}
		logger.info("TaskSelectorService init succeed");
		
		// task scheduler
		int max_running_tasks = conf.getInt("max.running.tasks", DEFAULT_MAX_RUNNING_TASKS);
		task_scheduler = new TaskScheduler(queue, mysql_pool, hive_conf, max_running_tasks);
		if (!task_scheduler.init()) {
			logger.warn("TaskScheduler init failed");
			return false;
		}
		logger.info("TaskScheduler init succeed");
		
		return true;
	}

	@Override
	public void uninit() {
		if (task_selector_service != null) {
			task_selector_service.uninit();
			logger.info("TaskSelectorService uninit");
		}
		
		if (task_scheduler != null) {
			task_scheduler.uninit();
			logger.info("TaskScheduler uninit");
		}
		
		if (mysql_pool != null) {
			MysqlUtil.closeMysqlPool(mysql_pool);
			logger.info("MysqlPool closed");
		}
	}

	public static void usage() {
		System.out.println(CLASS_NAME + " <conf_file>");
	}
	
	public static void main(String [] args) {
		if (args.length != 1) {
			usage();
			return;
		}
		
		//check config
		String conf_file = args[0];
		if (!config.parse(conf_file)) {
			logger.warn("parse config file failed : {}", conf_file);
			return;
		}
		
		logger.info(config.toString());
		
		if (!config.isValid()) {
			logger.warn("config is invalid");
			return;
		}
		
		// service
		HistoryBigdataQueryService service = new HistoryBigdataQueryService();
		if (service.init()) {
			Runtime.getRuntime().addShutdownHook(new HistoryBigdataQueryServiceShutdownHook(service));
			logger.info("{} init ok", CLASS_NAME);
		} else {
			logger.warn("{} init failed", CLASS_NAME);
			service.uninit();          
		}
	}
	
}
