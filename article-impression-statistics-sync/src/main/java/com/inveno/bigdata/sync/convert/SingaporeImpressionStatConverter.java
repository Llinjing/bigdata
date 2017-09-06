package com.inveno.bigdata.sync.convert;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.github.panhongan.util.conf.Config;
import com.github.panhongan.util.db.JedisUtil;
import com.inveno.bigdata.sync.ImpressionStatSyncConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

public class SingaporeImpressionStatConverter extends AbstractConverter {
	
	private static final Logger logger = LoggerFactory.getLogger(SingaporeImpressionStatConverter.class);
	
	private static ImpressionStatSyncConfig config = ImpressionStatSyncConfig.getInstance();
	private static Config conf = config.getConfig();
	
	private static boolean is_cluster = (conf.getInt("redis.is_cluster") == 1);
	private static int valid_time = conf.getInt("impression.stat.data.valid.time", 1);
	private static int impression_threshold = conf.getInt("impression.threshold", 1000000);
	private static String impression_stat_redis_key_prefix = conf.getString("impression.stat.redis.key.prefix");
	
	private static SimpleDateFormat date_format = new SimpleDateFormat("yyyyMMddHH");
	
	private JedisCluster jedis_cluster = null;
	private Jedis jedis = null;
	
	@Override
	public boolean init() {
    	if (is_cluster) {
    		jedis_cluster = JedisUtil.createJedisCluster(conf);
    	} else {
    		jedis = JedisUtil.createJedis(conf);
    	}
		return true;
	}
	
	@Override
	public void uninit() {
    	if (is_cluster) {
    		JedisUtil.closeJedisCluster(jedis_cluster);
    	} else {
    		JedisUtil.closeJedis(jedis);
    	}
		
	}

	@Override
	public void convert(String str) {
		if (str == null) {
			logger.warn("invalid parameter");
		}else{
			parseData(str);
		}
	}

	@Override
	public Object clone() {
		return new SingaporeImpressionStatConverter();
	}

    public String getValidRedisDate() {
        TimeZone TIMEZONE_UTC = TimeZone.getTimeZone("Asia/Shanghai");
        date_format.setTimeZone(TIMEZONE_UTC);
                
        long system_time_stamp = System.currentTimeMillis();
        String res = date_format.format(system_time_stamp - 60 * 60 * 1000 * valid_time).toString();
        return res;
    }
    
    /**
     * 
     * @param str
     * {"content_id":"1034604157","product_id":"hotoday","total_impression":1,"update_date":"2017030709","language":"hindi"}
     */
    public void parseData(String str){
    	try {
			JSONObject json_obj = JSONObject.parseObject(str);
			logger.debug(json_obj.toString());
			
			if (!json_obj.isEmpty()) {
				String product_id = json_obj.getString("product_id");
				int total_impression = json_obj.getInteger("total_impression");
				String valid_redis_date = getValidRedisDate();
				
				if (!product_id.contains("noticias") && 
						total_impression < impression_threshold && 
						json_obj.getString("update_date").compareTo(valid_redis_date) >= 0) {
					
                    String content_id = json_obj.getString("content_id");
					String language = json_obj.getString("language");
                    language = language.substring(0,1).toUpperCase() + language.substring(1);

                    //expinfo_hotoday_Hindi_impressionOfInfoIdKey
                    String redis_key = "";
                    if(language.contentEquals("Zh_cn")){
                        redis_key = impression_stat_redis_key_prefix + "_" + product_id + "_impressionOfInfoIdKey";
                    }else{
                        redis_key = impression_stat_redis_key_prefix + "_" + product_id + "_" + language + "_impressionOfInfoIdKey";
                    }
                    
                	if(is_cluster) {
                		if(jedis_cluster == null){
                			jedis_cluster = JedisUtil.createJedisCluster(conf);
                		}
                        if(jedis_cluster.hexists(redis_key, content_id)){
                            jedis_cluster.hset(redis_key, content_id, Integer.toString(total_impression));
                            logger.warn(redis_key +"__"+ content_id +"__"+ total_impression);
                        }
                	} else {
                		if(jedis == null){
                			jedis = JedisUtil.createJedis(conf);
                		}
                        if(jedis.hexists(redis_key, content_id)){
                            jedis.hset(redis_key, content_id, Integer.toString(total_impression));
                            logger.warn(redis_key +"__"+ content_id +"__"+ total_impression);
                        }
                	}
                	
				}
			}
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		}
    }
}
