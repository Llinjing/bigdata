package com.inveno.bigdata.lockscreen.card;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.panhongan.util.db.MysqlSession;
import com.github.panhongan.util.db.MysqlUtil;
import com.github.panhongan.util.kafka.AbstractKafkaMessageProcessor;
import com.github.panhongan.util.kafka.MessageLocalWriter;
import com.inveno.feeder.thrift.AcsService;
import com.inveno.feeder.thrift.Status;
import com.inveno.feeder.thrift.SysType;

public class LockScreenClickMessageProcessor extends AbstractKafkaMessageProcessor {

	private static Logger logger = LoggerFactory.getLogger(LockScreenClickMessageProcessor.class);

	private static final String SCENARIO_DESC_LOCK_SCREEN = "lockscreen";

	private static final String STRATEGY_FIRSTSCREEN_FORCE_INSERT = "firstscreen_force_insert";

	private static final String SCENARIO_ID_LOCK_SCREEN = "463871";

	private static LockScreenCardConfig config = LockScreenCardConfig.getInstance();

	// private AcsClient client = null;

	private MessageLocalWriter failed_data_writer = null;

	private List<String> key_list = new ArrayList<String>();

	@Override
	public boolean init() {
		boolean ret = false;

		try {
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
			String strategy = json_obj.getJSONObject("article_click_extra").getJSONObject("cpack")
					.getString("strategy");

			if (SCENARIO_DESC_LOCK_SCREEN.contentEquals(scenario_desc)
					&& STRATEGY_FIRSTSCREEN_FORCE_INSERT.contentEquals(strategy)) {
				String uid = json_obj.getString("uid");
				String product_id = json_obj.getString("product_id");
				String content_id = json_obj.getJSONObject("article_click_extra").getString("content_id");

				key_list.clear();
				// select the same card group ids from db_mta
				MysqlSession session = null;
				try {
					session = MysqlUtil.createMysqlSession(config.getConfig());

					String sql = "select group_id from db_mta.t_lockscreen_daily_topic_groupid_mapping where topic_id in "
							+ "(select topic_id from db_mta.t_lockscreen_daily_topic_groupid_mapping where group_id='"
							+ content_id + "');";
					logger.info("query sql is : " + sql);

					ResultSet rs = session.executeQuery(sql);

					while (rs.next()) {
						String content_id_tmp = rs.getString("group_id");
						if (!content_id_tmp.isEmpty()) {
							String key = uid + product_id + content_id_tmp + "#" + SCENARIO_ID_LOCK_SCREEN
									+ "#impression";
							key_list.add(key);
						}
					}

					if (rs != null) {
						try {
							rs.close();
						} catch (Exception e) {
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					MysqlUtil.closeMysqlSession(session);
				}

				AcsService.Client client = null;
				Status status = null;
				try {
					client = (AcsService.Client) LockScreenCardService.acs_client_pool.borrowObject();
					
					status = client.checkAndInsertMul(SysType.USER_READ, key_list,
							config.getConfig().getInt("acs.timeout.ms", 2000)).get(0);
				} catch (Exception e) {
					logger.warn(e.getMessage(), e);
				} finally {
					try {
						LockScreenCardService.acs_client_pool.returnObject(client);
					} catch (Exception e2) {
						logger.error(e2.getMessage(), e2);
					}
				}

				if (status != Status.YES && status != Status.NO) {
					for (String tmp : key_list) {
						logger.warn("write to acs failed : key_list = {}", tmp);
					}
					failed_data_writer.processMessage(topic, partition_id, key_list);
				} else {
					for (String tmp : key_list) {
						logger.info("write to acs succeed : key_list = {}", tmp);
					}
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
