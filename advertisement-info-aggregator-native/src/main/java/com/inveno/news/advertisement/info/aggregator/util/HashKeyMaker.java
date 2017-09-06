package com.inveno.news.advertisement.info.aggregator.util;


import com.github.panhongan.util.hash.SimplePartitioner;
import com.inveno.news.advertisement.info.aggregator.util.RedisConfig;

public class HashKeyMaker {
	
	private static final RedisConfig config = RedisConfig.getInstance();
	
	private static final int DEFAULT_PARTITIONS = 200;
	
	public static String make(String field) {
		String hash_key = config.getConfig().getString("redis.advertisement.info.hash.key");
		int partitions = config.getConfig().getInt("redis.advertisement.info.partitions", DEFAULT_PARTITIONS);
		int partition = SimplePartitioner.partition(field, partitions);
		return hash_key + "-" + partition;
	}

}
