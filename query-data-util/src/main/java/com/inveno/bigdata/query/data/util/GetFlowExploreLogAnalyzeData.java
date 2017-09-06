package com.inveno.bigdata.query.data.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.panhongan.util.conf.Config;
import com.inveno.bigdata.query.data.util.common.CollectionUtil;
import com.inveno.bigdata.query.data.util.file.WriteData;

public class GetFlowExploreLogAnalyzeData {
	private static final Logger logger = LoggerFactory.getLogger(GetFlowExploreLogAnalyzeData.class);

	private static final String CLASS_NAME = GetFlowExploreLogAnalyzeData.class.getName();

	private static Config config = new Config();

	private static final String LOG_TAG = "&";
	private static final String EVENT_NEW_PUBLISH_ARTICLE = "new-publish-article";
	private static final String EVENT_FIRST_INTO_EXPINFO = "first-into-expinfo";
	private static final String EVENT_FIRST_INTO_FLOW_EXPLORING = "first-into-flow-exploring";
	private static final String EVENT_INTO_FLOW_EXPLORING_AGAIN = "into-flow-exploring-again";
	private static final String EVENT_FINISH_FLOW_EXPLORE = "finish-flow-explore";
	private static final String EVENT_STOP_EXPLORE_IN_EAGERLY_TOO_LONG = "stop-explore-because-in-eagerly-too-long";
	private static final String EVENT_STOP_EXPLORE_PUBLISHTIME_TOO_OLD = "stop-explore-because-publishTime-too-old";
	private static final String EVENT_STOP_EXPLORE_NO_NEWS_INFO = "stop-explore-because-no-news-info";
	private static final String EVENT_WASTED_EXPLORE_COUNT = "wasted-explore-count";

	private Map<String, TreeSet<String>> log_data_map = new HashMap<String, TreeSet<String>>();
	private Map<String, HashSet<String>> avaliable_publish_content_map = new HashMap<String, HashSet<String>>();
	private Map<String, Long> first_explore_time_gap_map = new HashMap<String, Long>();
	private Map<String, Long> finish_explore_time_gap_map = new HashMap<String, Long>();

	private Map<String, Long> into_exploring_num_map = new HashMap<String, Long>();

	private Map<String, Long> finish_explore_request_num_map = new HashMap<String, Long>();
	private Map<String, Long> finish_explore_impression_num_map = new HashMap<String, Long>();

	private Map<String, HashSet<String>> finish_explore_content_ids_map = new HashMap<String, HashSet<String>>();
	private Map<String, HashSet<String>> not_finish_explore_content_ids_map = new HashMap<String, HashSet<String>>();

	private Map<String, HashSet<String>> stop_explore_because_no_news_info_map = new HashMap<String, HashSet<String>>();
	private Map<String, HashSet<String>> stop_explore_because_in_eagerly_too_long_map = new HashMap<String, HashSet<String>>();
	private Map<String, HashSet<String>> stop_explore_because_publishTime_too_old_map = new HashMap<String, HashSet<String>>();

	private Map<String, Long> wasted_explore_count = new HashMap<String, Long>();

	public static void usage() {
		System.out.println(CLASS_NAME + " <conf_file>");
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			usage();
			return;
		}

		// config
		String conf_file = args[0];
		if (!config.parse(conf_file)) {
			logger.warn("parse conf file failed : {}", conf_file);
			return;
		}
		logger.info(config.toString());

		GetFlowExploreLogAnalyzeData service = new GetFlowExploreLogAnalyzeData();
		service.getLogsData();
		service.analyse();
		service.printResult();
	}

	private void printResult() {
		WriteData.processMessage(config.getString("local.path.explore.log.analyse.middle.result"), "");

		for (Object key : log_data_map.keySet()) {
			WriteData.processMessage(config.getString("local.path.explore.log.analyse.middle.result"),
					key + " : " + Arrays.toString(log_data_map.get(key).toArray()));
		}

		WriteData.processMessage(config.getString("local.path.explore.log.analyse.middle.result"), "");
		WriteData.processMessage(config.getString("local.path.explore.log.analyse.middle.result"), "------------------------------------");
		WriteData.processMessage(config.getString("local.path.explore.log.analyse.middle.result"), "");

		String[] key_arr = finish_explore_content_ids_map.keySet().toArray(new String[0]);
		Arrays.sort(key_arr);

		for (String key : key_arr) {
			double finish_explore_content_ids_size = finish_explore_content_ids_map.get(key).size() * 1.0;

			if (finish_explore_content_ids_size > 0.0) {
				Object[] key_parms = { key, CollectionUtil.getMapValueSize(avaliable_publish_content_map, key),
						(int) finish_explore_content_ids_size,
						CollectionUtil.getMapValue(first_explore_time_gap_map, key) / finish_explore_content_ids_size,
						CollectionUtil.getMapValue(finish_explore_time_gap_map, key) / finish_explore_content_ids_size,
						CollectionUtil.getMapValue(into_exploring_num_map, key) / finish_explore_content_ids_size,
						CollectionUtil.getMapValue(finish_explore_request_num_map, key) / finish_explore_content_ids_size,
						CollectionUtil.getMapValue(finish_explore_impression_num_map, key) / finish_explore_content_ids_size,
						CollectionUtil.getMapValueSize(not_finish_explore_content_ids_map, key),
						CollectionUtil.getMapValueSize(stop_explore_because_no_news_info_map, key),
						CollectionUtil.getMapValueSize(stop_explore_because_in_eagerly_too_long_map, key),
						CollectionUtil.getMapValueSize(stop_explore_because_publishTime_too_old_map, key),
						CollectionUtil.getMapValue(wasted_explore_count, key) };
				String result = StringUtils.join(key_parms, "\t");
				WriteData.processMessage(config.getString("local.path.explore.log.analyse.middle.result"), result);
				System.out.println(result);

				Object[] result_key_parms = { key, CollectionUtil.getMapValueSize(avaliable_publish_content_map, key),
						(int) finish_explore_content_ids_size, CollectionUtil.getMapValue(first_explore_time_gap_map, key),
						CollectionUtil.getMapValue(finish_explore_time_gap_map, key),
						CollectionUtil.getMapValue(into_exploring_num_map, key),
						CollectionUtil.getMapValue(finish_explore_request_num_map, key),
						CollectionUtil.getMapValue(finish_explore_impression_num_map, key),
						CollectionUtil.getMapValueSize(not_finish_explore_content_ids_map, key),
						CollectionUtil.getMapValueSize(stop_explore_because_no_news_info_map, key),
						CollectionUtil.getMapValueSize(stop_explore_because_in_eagerly_too_long_map, key),
						CollectionUtil.getMapValueSize(stop_explore_because_publishTime_too_old_map, key) };
				result = StringUtils.join(result_key_parms, "\t");
				WriteData.processMessage(config.getString("local.path.explore.log.analyse.result"), result);
			}
		}
	}

	/**
	 * key   : noticias&news&1054876452
	 * value : [1495490844&first-into-expinfo, 1495490860&first-into-flow-exploring&45, 1495491574&into-flow-exploring-again&13, 1495492200&finish-flow-explore&30]
	 */
	private void analyse() {
		for (String product_language_content_key : log_data_map.keySet()) {
			String[] key_tmp_array = product_language_content_key.split(LOG_TAG);
			String product_language_key = key_tmp_array[0] + LOG_TAG + key_tmp_array[1];

			TreeSet<String> time_type_count_set = log_data_map.get(product_language_content_key);
			String time_type_count_string = Arrays.toString(time_type_count_set.toArray());

			// 探索失败
			if (time_type_count_string.contains("stop")) {
				analyseStopExploreEvent(time_type_count_string, product_language_key, product_language_content_key);
				continue;
			}

			// [1495490844&first-into-expinfo, 1495490860&first-into-flow-exploring&45, 1495491574&into-flow-exploring-again&13, 1495492200&finish-flow-explore&30]
			if (time_type_count_string.contains(EVENT_FIRST_INTO_EXPINFO) && time_type_count_string.contains(EVENT_FINISH_FLOW_EXPLORE)) {
				// 正常完成探索
				analyseSuccessExploreEvent(time_type_count_set, product_language_key, product_language_content_key);
			} else {
				// 未完成探索
				// 针对跨天的少部分探索情况，不计入统计范围				
				CollectionUtil.insertDataToMap(not_finish_explore_content_ids_map, product_language_key, product_language_content_key);
			}

		}
	}

	private void analyseStopExploreEvent(String time_type_count_string, String product_language_key, String product_language_content_key) {
		if (time_type_count_string.contains(EVENT_STOP_EXPLORE_IN_EAGERLY_TOO_LONG)) {

			CollectionUtil.insertDataToMap(stop_explore_because_in_eagerly_too_long_map, product_language_key,
					product_language_content_key);

		} else if (time_type_count_string.contains(EVENT_STOP_EXPLORE_NO_NEWS_INFO)) {

			CollectionUtil.insertDataToMap(stop_explore_because_no_news_info_map, product_language_key, product_language_content_key);

		} else if (time_type_count_string.contains(EVENT_STOP_EXPLORE_PUBLISHTIME_TOO_OLD)) {

			CollectionUtil.insertDataToMap(stop_explore_because_publishTime_too_old_map, product_language_key,
					product_language_content_key);

		}
	}

	private void analyseSuccessExploreEvent(TreeSet<String> time_type_count_set, String product_language_key,
			String product_language_content_key) {
		long first_expinfo_time_stamp_long = 0;

		for (String tmp_string : time_type_count_set) {
			String[] value_string_array = tmp_string.split(LOG_TAG);
			if (value_string_array.length >= 2) {
				long time_stamp_long = Long.valueOf(value_string_array[0]);
				String type = value_string_array[1];
				int value_int = 0;
				try {
					value_int = Integer.valueOf(value_string_array[2]);
				} catch (Exception e) {
				}

				switch (type) {
				case EVENT_FIRST_INTO_EXPINFO:
					first_expinfo_time_stamp_long = time_stamp_long;
					break;

				case EVENT_FIRST_INTO_FLOW_EXPLORING:
					if (first_expinfo_time_stamp_long > 0) {
						long gap = time_stamp_long - first_expinfo_time_stamp_long;
						CollectionUtil.insertDataToMap(first_explore_time_gap_map, product_language_key, gap);

						if (config.getBoolean("first.explore.time.gap.debug") && gap > config.getLong("first.explore.time.gap")) {
							WriteData.processMessage(config.getString("local.path.first.explore.time.gap"),
									product_language_content_key + "\t" + tmp_string + "\t" + gap);
						}
					}
					CollectionUtil.insertDataToMap(into_exploring_num_map, product_language_key, 1);
					CollectionUtil.insertDataToMap(finish_explore_request_num_map, product_language_key, value_int);
					break;

				case EVENT_INTO_FLOW_EXPLORING_AGAIN:
					CollectionUtil.insertDataToMap(into_exploring_num_map, product_language_key, 1);
					CollectionUtil.insertDataToMap(finish_explore_request_num_map, product_language_key, value_int);
					break;

				case EVENT_FINISH_FLOW_EXPLORE:
					long gap = time_stamp_long - first_expinfo_time_stamp_long;
					CollectionUtil.insertDataToMap(finish_explore_time_gap_map, product_language_key, gap);

					if (config.getBoolean("finish.explore.time.gap.debug") && gap > config.getLong("finish.explore.time.gap")) {
						WriteData.processMessage(config.getString("local.path.finish.explore.time.gap"),
								product_language_content_key + "\t" + tmp_string + "\t" + gap);
					}

					// attention
					if (value_int < 600) {
						CollectionUtil.insertDataToMap(finish_explore_impression_num_map, product_language_key, value_int);
					} else {
						CollectionUtil.insertDataToMap(finish_explore_impression_num_map, product_language_key, 600);
					}
					CollectionUtil.insertDataToMap(finish_explore_content_ids_map, product_language_key, product_language_content_key);
					break;

				default:
					break;
				}
			}
		}
	}

	/**
	 * 2017-05-22 16:25:23||&first-into-expinfo&expinfo_noticias_Spanish&1054776936#1#4352#1059#1495440121&1.85&1495441523276
	 * 2017-05-22 16:25:23||&
	 * first-into-expinfo&
	 * expinfo_noticias_Spanish&
	 * 1054776936#1#4352#1059#1495440121&
	 * 1.85&
	 * 1495441523276
	 * 
	 * [2017-05-21 00:00:57.923] lambda$add2ExpIngList$0 &first-into-flow-exploring&expinfo_noticiasboomchile_Spanish&1054610553#1#2#1059#1495295316&30&1495296057923
	 * [2017-05-21 00:00:57.923] lambda$add2ExpIngList$0 &
	 * first-into-flow-exploring&
	 * expinfo_noticiasboomchile_Spanish&
	 * 1054610553#1#2#1059#1495295316&
	 * 30&
	 * 1495296057923
	 * 
	 * [2017-05-21 00:00:00.032] remove &into-flow-exploring-again&noticias=Spanish=video&1054447415#2#64#4099#1495161259&17&1495296000032
	 * [2017-05-21 00:00:00.032] remove &
	 * into-flow-exploring-again&
	 * noticias=Spanish=video&
	 * 1054447415#2#64#4099#1495161259&
	 * 17&
	 * 1495296000032
	 * 
	 * [2017-05-21 00:02:19.131] lambda$add2ExpIngList$0 &into-flow-exploring-again&expinfo_noticias_Spanish&1054608689#1#4098#35#1495294432&12&1495296139131
	 * [2017-05-21 00:02:19.131] lambda$add2ExpIngList$0 &
	 * into-flow-exploring-again&
	 * expinfo_noticias_Spanish&
	 * 1054608689#1#4098#35#1495294432&
	 * 12&
	 * 1495296139131
	 * 
	 * [2017-05-22 16:05:12.799] lambda$add2ExpIngList$0 &finish-flow-explore&expinfo_noticias_Spanish&1054774585#1#4098#1059#1495437969&30&1495440312799
	 * [2017-05-22 16:05:12.799] lambda$add2ExpIngList$0 &
	 * finish-flow-explore&
	 * expinfo_noticias_Spanish&
	 * 1054774585#1#4098#1059#1495437969&
	 * 30&
	 * 1495440312799
	 * 
	 * [2017-05-21 00:00:00.086] remove &stop-explore-because-no-news-info&noticiasboomcolombia=Spanish=video&1054268502#2#64#4099#1495040615&1495296000086
	 * [2017-05-21 00:00:00.086] remove &
	 * stop-explore-because-no-news-info&
	 * noticias=Spanish=video&
	 * 1054268502#2#64#4099#1495040615&
	 * 1495296000086
	 * 
	 * [2017-05-21 05:15:00.009] remove &stop-explore-because-in-eagerly-too-long&noticias=Spanish=video&1054373640#2#64#4099#1495107738&1495314900009
	 * [2017-05-21 05:15:00.009] remove &
	 * stop-explore-because-in-eagerly-too-long&
	 * noticias=Spanish=video&
	 * 1054373640#2#64#4099#1495107738&
	 * 1495314900009
	 * 
	 * 1054192703#
	 * 1#          Type
	 * 4098#       Link_type
	 * 1063#       Display_type
	 * 1494968700  publishTime
	 * 
	 * 2017-05-24 17:04:27||&new-publish-article&noticiasboom_Spanish_1_1055101327&1495616667408
	 * 2017-05-24 17:04:27||&
	 * new-publish-article&
	 * noticiasboom_Spanish_1_1055101327&
	 * 1495616667408
	 * 
	 * [2017-05-25 12:26:51.627] doGetExpInfo &wasted-explore-count&expinfo_noticias_Spanish_video_ing&1495686411627
	 * [2017-05-25 12:26:51.627] doGetExpInfo &
	 * wasted-explore-count&
	 * expinfo_noticias_Spanish_video_ing&
	 * 1495686411627
	 */
	private void getLogsData() {
		String[] file_path_arr = config.getString("local.path.explore.log").split(";");
		for (String file_path : file_path_arr) {
			this.readFile(file_path);
		}
	}

	private void readFile(String file_path) {
		File file = new File(file_path);
		FileInputStream file_input = null;
		InputStreamReader reader = null;
		BufferedReader buffer_reader = null;
		try {
			file_input = new FileInputStream(file);
			reader = new InputStreamReader(file_input, "UTF-8");
			buffer_reader = new BufferedReader(reader);

			if (buffer_reader.ready()) {
				String tmp = "";
				while (true) {
					tmp = buffer_reader.readLine();
					if (tmp == null || tmp.contentEquals("")) {
						break;
					} else {
						insertDataToMap(tmp);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
				file_input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private String getType(String _type) {
		String type = "default";
		try {
			if (_type.contentEquals("1") || _type.contentEquals("ing")) {
				type = "news";
			} else if (_type.contentEquals("2")) {
				type = "short_video";
			} else if (_type.contentEquals("128")) {
				type = "meme";
			} else if (_type.contentEquals("video")) {
				type = "short_video";
			} else {
				type = _type;
			}
		} catch (Exception e) {
			System.out.println(_type);
			System.out.println(e.getMessage());
		}

		return type;
	}

	private void insertDataToMap(String data) {
		if (data.contains(EVENT_NEW_PUBLISH_ARTICLE)) {
			if (data.contains("Spanish")) {
				//2017-05-24 17:04:27||&new-publish-article&noticiasboom_Spanish_1_1055101327&1495616667408
				//2017-08-07 20:25:38||&new-publish-article&noticias_Spanish_lockscreen_1_1063729286&1502108738602
				String[] tmp_arr = data.split(LOG_TAG);
				String[] product_lan_content_info_arr = tmp_arr[2].split("_");
				String product_content_type = product_lan_content_info_arr[0] + LOG_TAG + getType(product_lan_content_info_arr[2]);

				if(product_lan_content_info_arr.length == 5){
					String content_id = product_lan_content_info_arr[4];
					CollectionUtil.insertDataToMap(avaliable_publish_content_map, product_content_type, content_id);
				} else {
					String content_id = product_lan_content_info_arr[3];
					CollectionUtil.insertDataToMap(avaliable_publish_content_map, product_content_type, content_id);
				}
			}
			return;
		} else if (data.contains(EVENT_WASTED_EXPLORE_COUNT)) {
			if (data.contains("Spanish")) {
				//[2017-05-25 12:26:51.627] doGetExpInfo &wasted-explore-count&expinfo_noticias_Spanish_video_ing&1495686411627
				//[2017-08-08 13:20:04.051] doExploreProcess &wasted-explore-count&expinfo_noticias_Spanish_lockscreen_ing&1502169604051
				String[] tmp_arr = data.split(LOG_TAG)[2].split("_");
				String product = tmp_arr[1];
				String content_type = getType(tmp_arr[3]);
				CollectionUtil.insertDataToMap(wasted_explore_count, product + LOG_TAG + content_type, 1);
			}
			return;
		}

		//[2017-08-08 10:59:49.173] checkAllResultList &first-into-flow-exploring&expinfo_noticias_Spanish_lockscreen&1063756436#1#4098#1195#1502124991&150&1502161189172
		//[2017-08-08 11:18:06.788] checkAllResultList &into-flow-exploring-again&expinfo_noticias_Spanish_lockscreen&1063756436#1#4098#1195#1502124991&126&1502162286788
		//[2017-08-08 13:29:09.741] lambda$checkExploringPool$12 &finish-flow-explore&expinfo_noticias_Spanish_lockscreen_eagerly&1063750547#1#4098#1199#1502121375&105&1502170149741
		String[] tmp_arr = data.split(LOG_TAG);
		if (tmp_arr.length >= 4) {
			String[] redis_key_arr = tmp_arr[2].split("[_|=]");
			String product_id = redis_key_arr[0];
			if (!product_id.contains("noticias")) {
				product_id = redis_key_arr[1];
			}

			String[] redis_content_key_arr = tmp_arr[3].split("#");
			String content_type_string = "";
			try {
				if(data.contains("lockscreen")){
					content_type_string = "lockscreen";
				}else{
					content_type_string = getType(redis_content_key_arr[1]);
				}
			} catch (Exception e) {
				//2017-08-07 22:20:06||&out-of-date-expinfo&noticias_Spanish_1_1063716896&1502115606094
				System.out.println(data);
				e.printStackTrace();
			}

			String content_id = redis_content_key_arr[0];

			String[] key_parms = { product_id, content_type_string, content_id };
			String key = StringUtils.join(key_parms, LOG_TAG);

			TreeSet<String> tmp_treeset = log_data_map.get(key);

			if (tmp_treeset == null) {
				tmp_treeset = new TreeSet<String>();
			}

			String event_time = tmp_arr[tmp_arr.length - 1].substring(0, 10);
			Object[] value_parms = null;
			int request_num = 0;
			int impression_num = 0;
			String log_type = tmp_arr[1];
			switch (log_type) {

			case EVENT_FIRST_INTO_FLOW_EXPLORING:
			case EVENT_INTO_FLOW_EXPLORING_AGAIN:
				request_num = Integer.valueOf(tmp_arr[4]);
				value_parms = new Object[] { event_time, log_type, request_num };
				break;

			case EVENT_FINISH_FLOW_EXPLORE:
				try {
					impression_num = Integer.valueOf(tmp_arr[4]);
				} catch (Exception e) {
				}
				value_parms = new Object[] { event_time, EVENT_FINISH_FLOW_EXPLORE, impression_num };
				break;

			case EVENT_FIRST_INTO_EXPINFO:
			case EVENT_STOP_EXPLORE_IN_EAGERLY_TOO_LONG:
			case EVENT_STOP_EXPLORE_NO_NEWS_INFO:
			case EVENT_STOP_EXPLORE_PUBLISHTIME_TOO_OLD:
				value_parms = new Object[] { event_time, log_type };
				break;
			default:
				return;
			}

			String value = StringUtils.join(value_parms, LOG_TAG);

			tmp_treeset.add(value);
			log_data_map.put(key, tmp_treeset);
		}
	}
}
