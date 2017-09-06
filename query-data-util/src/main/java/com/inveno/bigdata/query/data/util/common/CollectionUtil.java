package com.inveno.bigdata.query.data.util.common;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeSet;

public class CollectionUtil {

	public static void insertDataToMap(Map<String, HashSet<String>> map, String key, String value) {
		HashSet<String> tmp_set = map.get(key);
		if (tmp_set == null) {
			tmp_set = new HashSet<String>();
		}

		tmp_set.add(value);

		map.put(key, tmp_set);
	}

	public static void insertDataToMap(Map<String, Long> map, String key, long value) {
		long tmp_long = 0;
		try {
			tmp_long = map.get(key);
		} catch (Exception e) {
		}

		tmp_long += value;

		map.put(key, tmp_long);
	}

	public static void PrintMap(Map<String, TreeSet<String>> log_data_map) {
		for (Object key : log_data_map.keySet()) {
			System.out.println(key + " : " + Arrays.toString(log_data_map.get(key).toArray()));
		}
	}

	public static long getMapValue(Map<String, Long> map, String key) {
		long tmp_long = 0;
		
		try {
			tmp_long = map.get(key);
		} catch (Exception e) {
		}

		return tmp_long;
	}

	public static String getMapValueSize(Map<String, HashSet<String>> map, String key) {
		long value_size_long = 0;
		
		HashSet<String> tmp_set = map.get(key);
		if (tmp_set != null) {
			value_size_long = tmp_set.size();
		}

		return Long.valueOf(value_size_long).toString();
	}
}
