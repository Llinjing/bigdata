package com.inveno.news.advertisement.info.aggregator;

import java.util.Queue;

import com.inveno.news.advertisement.info.aggregator.util.HashKeyMaker;
import com.inveno.news.advertisement.info.aggregator.util.RedisConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.util.Pool;

import com.github.panhongan.util.db.JedisUtil;
import com.github.panhongan.util.thread.ControllableThread;

public class RedisUpdateThread extends ControllableThread {
	
	private static final String CLASS_NAME = RedisUpdateThread.class.getSimpleName();
	
	private static final RedisConfig config = RedisConfig.getInstance();
	
	private static Logger logger = LoggerFactory.getLogger(CLASS_NAME);
	
	private static int DEFAULT_REDIS_DB = 1;
	
	private Pool<Jedis> jedis_pool = null;
	
	private JedisCluster jedis_cluster = null;
	
	private Queue<String> queue = null;
	
	public RedisUpdateThread(Queue<String> queue, JedisCluster jedis_cluster, Pool<Jedis> jedis_pool) {
		this.queue = queue;
		this.jedis_cluster = jedis_cluster;
		this.jedis_pool = jedis_pool;
	}
	
	@Override
	protected void work() {
		String str = queue.poll();
		if (str != null) {
			String [] arr = str.split("\t");
			if (arr != null && arr.length == 2) {
				this.update(arr[0], arr[1]);
			}
		}
	}
	
	private void update(String field, String value) {
		if (jedis_cluster != null) {
			try {
				jedis_cluster.hset(HashKeyMaker.make(field), field, value);
			} catch (Exception e) {
				logger.warn(e.getMessage(), e);
			}
		} else if (jedis_pool != null) {
			Jedis jedis = null;
			try {
				jedis = jedis_pool.getResource();
				if (jedis != null) {
					jedis.select(config.getConfig().getInt("redis.db", DEFAULT_REDIS_DB));
					jedis.hset(HashKeyMaker.make(field), field, value);
				}
			} catch (Exception e) {
				logger.warn(e.getMessage(), e);
			} finally {
				JedisUtil.returnSource(jedis_pool, jedis, true);
			}
		}
	}

}
