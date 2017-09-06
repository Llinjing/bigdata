package com.inveno.bigdata.lockscreen;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.panhongan.util.kafka.AbstractKafkaMessageProcessor;
import com.github.panhongan.util.kafka.MessageLocalWriter;
import com.inveno.bigdata.lockscreen.acs.AcsClient;
import com.inveno.feeder.thrift.Status;
import com.inveno.feeder.thrift.SysType;

public class LockScreenImpressionMessageProcessor extends AbstractKafkaMessageProcessor {

	private static Logger logger = LoggerFactory.getLogger(LockScreenImpressionMessageProcessor.class);

	private static final String SCENARIO_DESC_LOCK_SCREEN = "lockscreen";

	private static final String SCENARIO_ID_LOCK_SCREEN = "463871";

	private static LockScreenConfig config = LockScreenConfig.getInstance();

	private AcsClient client = null;

	private MessageLocalWriter failed_data_writer = null;

	private List<String> key_list = new ArrayList<String>();

	@Override
	public boolean init() {
		boolean ret = false;
		
		try {
			// acs server
			String[] acs_arr = config.getConfig().getString("acs.server").split(":");
			String acs_server = acs_arr[0];
			int acs_port = Integer.valueOf(acs_arr[1]).intValue();

			client = new AcsClient(acs_server, acs_port);
			ret = client.init();
			if (!ret) {
				logger.warn("AcsClient init failed");
				return ret;
			} else {
				logger.info("AcsClient init ok");
			}
			
			// acs failed data writer
			failed_data_writer = new MessageLocalWriter(config.getConfig().getString("acs.failed.data.dir"),
					Integer.valueOf(config.getConfig().getString("acs.failed.data.window.minutes")));
			ret = failed_data_writer.init();
			if (!ret) {
				logger.warn("MessageLocalWriter init failed");
				return ret;
			} else {
				logger.info("MessageLocalWriter init ok");
			}
			
			ret = true;
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		}

		return ret;
	}

	@Override
	public void uninit() {
		if (client != null) {
			client.uninit();
			client = null;
		}
		
		if (failed_data_writer != null) {
			failed_data_writer.uninit();
			failed_data_writer = null;
		}
	}

	@Override
	public Object processMessage(String topic, int partition_id, String message) {
		try {
			JSONObject json_obj = JSON.parseObject(message);
			String scenario_desc = json_obj.getJSONObject("scenario").getString("position_desc");
			if (SCENARIO_DESC_LOCK_SCREEN.contentEquals(scenario_desc)) {
				String uid = json_obj.getString("uid");
				String product_id = json_obj.getString("product_id");
				String content_id = json_obj.getJSONObject("article_impression_extra").getString("content_id");
				String key = uid + product_id + content_id + "#" + SCENARIO_ID_LOCK_SCREEN + "#impression";

				key_list.clear();
				key_list.add(key);
				
				Status status = null;
				try {
					status = client.checkAndInsertMul(SysType.USER_READ, key_list, 
							config.getConfig().getInt("acs.timeout.ms", 2000)).get(0);
				} catch (Exception e) {
					logger.warn(e.getMessage(), e);
				}
				
				if (status != Status.YES && status != Status.NO) {
					logger.warn("write to acs failed : key = {}", key);
					failed_data_writer.processMessage(topic, partition_id, key);
					client.reset();
				} else {
					logger.info("write to acs succeed : key = {}", key);
				}
			}
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		}

		return true;
	}

	@Override
	public Object processMessage(String topic, int partition_id, List<String> msg_list) {
		for (String msg : msg_list) {
			this.processMessage(topic, partition_id, msg);
		}

		return true;
	}
}
