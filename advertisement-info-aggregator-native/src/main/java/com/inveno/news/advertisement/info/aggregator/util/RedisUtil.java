package com.inveno.news.advertisement.info.aggregator.util;

import com.github.panhongan.util.db.JedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.util.Pool;
/**
 * Created by dory on 2016/10/11
 */
public class RedisUtil {
    public static final String CLASS_NAME =  RedisUtil.class.getSimpleName();

    public static final Logger logger = LoggerFactory.getLogger(CLASS_NAME);

    private static final RedisConfig conf = RedisConfig.getInstance();

    private static int DEFAULT_REDIS_DB = 1;

    private static Pool<Jedis> jedis_pool = null;

    private static JedisCluster jedis_cluster = null;
    
    public static boolean init(String conf_file){
        if (!conf.parse(conf_file)) {
            logger.warn("parse conf_file failed : {}", conf_file);
            return false;
        }
        boolean is_ok = false;
        if (Boolean.valueOf(conf.getConfig().getString("redis.is_cluster"))) {
            jedis_cluster = JedisUtil.createJedisCluster(conf.getConfig());
            if(jedis_cluster != null){
            	is_ok = true;
            }
        } else {
            jedis_pool = JedisUtil.createJedisPool(conf.getConfig());
            if(jedis_pool != null){
            	is_ok = true;
            }
        }

        return is_ok;
    }

    public static void update(String field, String value) {
        if (jedis_cluster != null) {
            try {
                jedis_cluster.hset(HashKeyMaker.make(field), field, value);
                logger.debug(HashKeyMaker.make(field) + "   " + field + "   " + value);
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
            }
        } else if (jedis_pool != null) {
            Jedis jedis = null;
            try {
                jedis = jedis_pool.getResource();
                if (jedis != null) {
                    jedis.select(conf.getConfig().getInt("redis.db", DEFAULT_REDIS_DB));
                    jedis.hset(HashKeyMaker.make(field), field, value);
                }
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
            } finally {
                JedisUtil.returnSource(jedis_pool, jedis, true);
            }
        }
    }

    public static void uninit()
    {
        if (jedis_cluster != null) {
            JedisUtil.closeJedisCluster(jedis_cluster);
        } else {
            JedisUtil.closeJedisPool(jedis_pool);
        }
    }
}
