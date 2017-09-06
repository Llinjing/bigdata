package com.inveno.bigdata.lockscreen.card;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.thrift.TServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.panhongan.util.StringUtil;
import com.github.panhongan.util.conf.Config;
import com.github.panhongan.util.control.Lifecycleable;
import com.github.panhongan.util.kafka.AbstractKafkaMessageProcessor;
import com.github.panhongan.util.kafka.HighLevelConsumerGroup;
import com.inveno.bigdata.lockscreen.acs.AcsClientPoolFactory;
import com.inveno.feeder.thrift.AcsService;
import com.inveno.feeder.thrift.Status;
import com.inveno.feeder.thrift.SysType;

public class LockScreenCardService implements Lifecycleable {
	
	private static final String CLASS_NAME = LockScreenCardService.class.getSimpleName();
	
	private static final Logger logger = LoggerFactory.getLogger(LockScreenCardService.class);
	
	private static LockScreenCardConfig lock_screen_card_config = LockScreenCardConfig.getInstance();
	
	private List<AbstractKafkaMessageProcessor> processors = new ArrayList<AbstractKafkaMessageProcessor>();
	
	private HighLevelConsumerGroup group = null;
	
	public static ObjectPool<TServiceClient> acs_client_pool = null;

	private boolean heartbeat_flag = true;
	
	private List<String> heartbeat_list = new ArrayList<String>();

	@Override
	public boolean init() {
		boolean is_ok = false;
		
		heartbeat_list.add("01011700000000000000000000000000noticias1000000000#463871#impression");
		
		Config config = lock_screen_card_config.getConfig();
		
		try {
			// acs tools init
			AcsClientPoolFactory acs_client_pool_factory = new AcsClientPoolFactory(config);
			
			GenericObjectPoolConfig pool_config = new GenericObjectPoolConfig();
			pool_config.setMaxTotal(50);
			pool_config.setMaxIdle(25);
			pool_config.setMinIdle(1);
			pool_config.setMaxWaitMillis(3000);
			
			acs_client_pool = new GenericObjectPool<TServiceClient>(acs_client_pool_factory, pool_config);
			
			// topic & partitions
			String [] arr = config.getString("kafka.topic.partition").split(":");
			String topic = arr[0];
			int partitions = Integer.valueOf(arr[1]).intValue();
			
			for (int i = 0; i < partitions; ++i) {
				LockScreenClickMessageProcessor processor = new LockScreenClickMessageProcessor();
				processor.setName(processor.getClass().getSimpleName() + "_" + i);
				if (processor.init()) {
					processors.add(processor);
				} else {
					logger.warn("LockScreenClickMessageProcessor init failed");
				}
			}
			
			is_ok = (processors.size() == partitions);
			if (is_ok) {
				group = new HighLevelConsumerGroup(config.getString("kafka.zk.list"), 
						config.getString("kafka.consumer.group"),
						topic, partitions, 
						Boolean.valueOf(config.getString("kafka.consumer.group.restart.offset.largest")), 
						processors);
				if (group.init()) {
					logger.info("HighLevelConsumerGroup init ok");
					is_ok = true;
				} else {
					logger.warn("HighLevelConsumerGroup init failed");
				}
			}
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		}
		
		return is_ok;
	}
	
	@Override
	public void uninit() {
		if (group != null) {
			group.uninit();
			logger.info("HighLevelConsumerGroup uninit");
		}
		
		for (AbstractKafkaMessageProcessor processor : processors) {
			processor.uninit();
			logger.info("MessageProcessor uninit : {}", StringUtil.toString(processor.getName()));
		}
		
		heartbeat_flag = false;
		if(heartbeat_list != null){
			heartbeat_list = null;
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
		
		// config file
		String conf_file = args[0];
		LockScreenCardConfig config = LockScreenCardConfig.getInstance();
		if (!config.parse(conf_file)) {
			logger.warn("parse conf file failed : {}", conf_file);
			return;
		}
		
		logger.info(config.toString());
		
		if (!config.isValid()) {
			logger.warn("config is invalid");
			return;
		}
		
		// service
		LockScreenCardService service = new LockScreenCardService();
		if (service.init()) {
			logger.info("{} init ok", CLASS_NAME);
			
			while(service.heartbeat_flag){
				AcsService.Client client = null;
				Status status = null;
				
				try {
					client = (AcsService.Client)acs_client_pool.borrowObject();					
					status = client.checkAndInsertMul(SysType.USER_READ, service.heartbeat_list,
							config.getConfig().getInt("acs.timeout.ms", 2000)).get(0);
				} catch (Exception e) {
					logger.warn(e.getMessage(), e);
				} finally {
					try {
						acs_client_pool.returnObject(client);
					} catch (Exception e2) {
						logger.error(e2.getMessage(), e2);
					}
				}
				logger.warn("write to acs heartbeat_flag : key_list = {}, status is {}", service.heartbeat_list.get(0), status.equals(Status.NO));
				
				try {
					Thread.sleep(config.getConfig().getInt("acs.heart.beat.time.ms", 3000));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			Runtime.getRuntime().addShutdownHook(new LockScreenCardServiceShutdownHook(service));
		} else {
			logger.warn("{} init failed", CLASS_NAME);
			service.uninit();
		}
	}
	
}
