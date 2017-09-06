package com.inveno.bigdata.meme.gmp

import java.util.HashMap

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.{Minutes, StreamingContext, Time}
import org.apache.spark.streaming.dstream.DStream
import org.slf4j.LoggerFactory

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;

import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.JSONArray
import com.github.panhongan.util.db.JedisUtil
import com.github.panhongan.util.sparkstreaming.StreamingFactory
import com.inveno.bigdata.common.ReduceUtil
import com.inveno.bigdata.common.TimeUtil
import com.inveno.bigdata.common.reformat.CompoundJson
import com.inveno.bigdata.common.reformat.CombinationKey
import com.inveno.bigdata.common.kafka.BatchProducer
import com.inveno.bigdata.common.ZooKeeperUtil
import com.github.panhongan.util.hash.SimplePartitioner

object ArticleMemeGmp {
    val CLASS_NAME = ArticleMemeGmp.getClass.getSimpleName

    val logger = LoggerFactory.getLogger(CLASS_NAME)
    private val config = ArticleMemeGmpConfig.getInstance();

    /**
     * input:
     * calculate GMP - (articleId###productId, (decay_meme, total_impression, gmp))
     * output:
     * Json Data -
     * [{"coolpad":{"ctr":"gmp_value","impression":"impression_value","meme":"meme_value"}},
     *  {"emui":{"ctr":"gmp_value1","impression":"impression_value1","meme":"meme_value1"}}]
     */
    def writeIntoRedis(current_gmp: DStream[(String, (Float, Long, Float))]) {
        val conf = config.getConfig
        val hdfs_redis = conf.getString("hdfs.redis")
        val key_tag = conf.getString("key_tag")
        
        val write_stream = current_gmp.map(msg => {
            val key = msg._1.split(key_tag)
            val article_id = key(0)
            val product_id = key(1)
            val (decay_meme, total_impression, gmp) = msg._2
            (article_id, Set((product_id, decay_meme, total_impression, gmp)))
        }).reduceByKey(ReduceUtil.reduceMergeSet[(String, Float, Long, Float)] _, 18)
        .flatMap(msg=>{
            val gmp_json = CompoundJson.getArticleMemeGmpRedisJson(msg._2)
            List((msg._1, gmp_json))
        })
        
        try {
            write_stream.saveAsTextFiles(hdfs_redis + "/redis")          
        } catch {
            case ex : Exception => logger.warn(ex.getMessage, ex)
        }
        
        try {
            // jedis session
            var jedis_cmd : Option[JedisCommands] = None
            val is_cluster = conf.getBoolean("redis.cluster")
            val hash_key_prefix = conf.getString("redis.hash.key.prefix")
            val hash_key_partitions = conf.getInt("redis.hash.key.partitions")
            
            write_stream.foreachRDD(rdd => {
                rdd.foreachPartition(partition => {
                    if (jedis_cmd.isEmpty){
                        if (is_cluster) {
                            jedis_cmd = Option(JedisUtil.createJedisCluster(conf))
                        } else {
                            jedis_cmd = Option(JedisUtil.createJedis(conf))
                        }
                    }
                    
                    // make redis value
                    partition.foreach(msg => {
                        val article_id = msg._1
                        val article_meme_gmp = msg._2
                        // write to redis
                        if (!jedis_cmd.isEmpty) { 
                            val hash_key = hash_key_prefix + "-" + SimplePartitioner.partition(article_id, hash_key_partitions)
                            jedis_cmd.get.hset(hash_key, article_id, article_meme_gmp)
                        }
                    }) // end partition.foreach  
            
                    if (!jedis_cmd.isEmpty) {
                        if (is_cluster) {
                            JedisUtil.closeJedisCluster(jedis_cmd.get.asInstanceOf[JedisCluster])
                        } else {
                            JedisUtil.closeJedis(jedis_cmd.get.asInstanceOf[Jedis])
                        }
                    }
                }) // end rdd.foreachPartition
            }) // dstream.foreachRDD
            
        } catch {
            case ex : Exception => logger.warn(ex.getMessage, ex)
        }

    }
    
    def handler(ssc: StreamingContext) = {
        val conf = config.getConfig
        // HDFS
        val hdfs_meme = conf.getString("hdfs.meme")
        val hdfs_impression = conf.getString("hdfs.impression")
        val hdfs_article_meme_gmp = conf.getString("hdfs.article.meme.gmp")
        val hdfs_total_decay_feedback = conf.getString("hdfs.total.decay.feedback")
        val hdfs_debug = conf.getString("hdfs_debug")
        // TOPIC
        val topic_meme = conf.getString("topic.meme")
        val topic_impression = conf.getString("topic.impression")
        val topic_article_meme_gmp = conf.getString("topic.article.meme.gmp")
        // KAFKA
        val window_num = conf.getInt("sparkstreaming.window.num")
        val window_size = conf.getInt("sparkstreaming.batch.size.minutes")
        var kafka_param = Map[String, String]()
        kafka_param += ("metadata.broker.list" -> conf.getString("src.kafka.broker.list"))
        kafka_param += ("group.id" -> conf.getString("src.kafka.consumer.group"))
        kafka_param += ("auto.offset.reset" -> "largest")
        val kafka_broker_list = conf.getString("src.kafka.broker.list")
        val kafka_batch_size = conf.getInt("src.kafka.batch.size")
        // ZK
        val zk_list = conf.getString("src.kafka.zk.list")
        val zk_offset_path = conf.getString("sparkstreaming.zk.offset.path")
        // CONSTANTS
        val is_debug = conf.getBoolean("debug", false)
        val key_tag = conf.getString("key_tag", "###")
        val decay_ratio = conf.getFloat("decay_ratio")
        val date_format = conf.getString("date_format")

        // meme - (articleId###productId, (meme, impression, total_impression, curr_date))
        val meme_partition_num = conf.getInt("topic.meme.partition")
        val meme_stream = StreamingFactory.createDirectStreamByOffset(zk_list, zk_offset_path, topic_meme, meme_partition_num, kafka_param, ssc, true)
        val meme_window = meme_stream.window(Minutes(window_size * window_num), Minutes(window_size))
        val meme_stat = meme_window.flatMap(msg => {
            val (unique_key, isValid) = DataParser.parseMeme(msg._2)
            isValid match {
                case true =>
                    List((unique_key, 1L))
                case _ => Nil
            }
        }).reduceByKey(ReduceUtil.reduceDistinctLong _, 18).flatMap(msg => {
            val key_arr = msg._1.split(key_tag)
            val key = CombinationKey.makeArticleCombinationKey(key_tag, key_arr(0), key_arr(1))
            List((key, 1L))
        }).reduceByKey(ReduceUtil.reduceAddLong _, 18)
          .map(x => (x._1, (x._2.toFloat, 0f, 0L, TimeUtil.getCurrDate(date_format))))
          .checkpoint(Minutes(window_size))
        
        // impression - (articleId###productId, (meme, impression, total_impression, curr_date))
        val impression_partition_num = conf.getInt("topic.impression.partition")
        val impression_stream = StreamingFactory.createDirectStreamByOffset(zk_list, zk_offset_path, topic_impression, impression_partition_num, kafka_param, ssc, true)
        val impression_window = impression_stream.window(Minutes(window_size * window_num), Minutes(window_size))
        val impression_stat = impression_window.flatMap(msg => {
            val (unique_key, isValid) = DataParser.parseImpression(msg._2)
            isValid match {
                case true =>
                    List((unique_key, 1L))
                case _ => Nil
            }
        }).reduceByKey(ReduceUtil.reduceDistinctLong _, 18).flatMap(msg => {
            val key_arr = msg._1.split(key_tag)
            val key = CombinationKey.makeArticleCombinationKey(key_tag, key_arr(0), key_arr(1))
            List((key, 1L))
        }).reduceByKey(ReduceUtil.reduceAddLong _, 18)
          .map(x => (x._1, (0f, x._2.toFloat, x._2, TimeUtil.getCurrDate(date_format))))
          .checkpoint(Minutes(window_size))

        // history data - (articleId###productId, (decay_meme, decay_impression, total_impression, update_date)) 
        val article_meme_gmp_partition_num = conf.getInt("topic.article.meme.gmp.partition")
        val history_gmp_stream = StreamingFactory.createDirectStreamByOffset(zk_list, zk_offset_path, topic_article_meme_gmp, article_meme_gmp_partition_num, kafka_param, ssc, true)
        val history_gmp_window = history_gmp_stream.window(Minutes(window_size * window_num), Minutes(window_size))
        val history_gmp_stat = history_gmp_window.flatMap(msg => {
            //content_id, product_id, decay_meme, decay_impression, total_impression, update_date, isValid
            val (unique_key, isValid) = DataParser.parseHistoryGmp(msg._2)
            isValid match {
                case true =>
                    List((unique_key, 1L))
                case _ => Nil
            }
        }).reduceByKey(ReduceUtil.reduceDistinctLong _, 18).flatMap(msg => {
            val key_arr = msg._1.split(key_tag)
            val content_id = key_arr(0)
            val product_id = key_arr(1)
            val decay_impression = key_arr(2).toFloat
            val decay_meme = key_arr(3).toFloat
            val total_impression = key_arr(4).toLong
            val update_date = key_arr(5)
            val key = CombinationKey.makeArticleCombinationKey(key_tag, content_id, product_id)
            List((key, (decay_ratio*decay_meme, decay_ratio*decay_impression, total_impression, update_date)))
        }).checkpoint(Minutes(window_size))
                    
        // current data - (articleId###productId, (decay_meme, decay_impression, total_impression, update_date))
        val current_feedback_result = history_gmp_stat.union(impression_stat).union(meme_stat)
            .reduceByKey((a, b) => (a._1+b._1, a._2+b._2, a._3+b._3, if(a._4>b._4){a._4}else{b._4}))
      
        // calculate GMP - (articleId###productId, (decay_meme, decay_impression, impression_gmp))
        val current_gmp = current_feedback_result.flatMap(msg => {
            val key = msg._1.split(key_tag).head + key_tag +"-1" 
            List(msg):::List((key, msg._2))
        }).reduceByKey((a, b) => (a._1 + b._1, a._2 + b._2, a._3 + b._3, if(a._4>b._4){a._4}else{b._4})).flatMap(msg => {
            val key = msg._1.split(key_tag)
            val product_id = key(1)
            val (decay_meme, decay_impression, total_impression, update_date) = msg._2

            if ((decay_meme>0f) && (decay_meme<decay_impression) && (decay_impression>0f)){
                product_id match {
                    case _ => {
                        if (total_impression >= conf.getInt("impression_threshold")) {
                            List((msg._1, (decay_meme, total_impression, decay_meme/decay_impression))) 
                        } else {
                            Nil
                        }
                    }
                }
            }else {
                Nil
            }
        }).checkpoint(Minutes(window_size))

        //write into redis
        writeIntoRedis(current_gmp)

        //write mark into zookeeper, give feeder a notification
        val zk_finish_mark_path = conf.getString("sparkstreaming.zk.finish.mark.path")
        // create tmp dstream for save  
        val stream_tmp = meme_stat.map(x => (1L, 0L)).reduceByKey((a, b) => a+b).repartition(1)
        stream_tmp.foreachRDD((rdd) => {
              val timestamp = TimeUtil.getCurrTimestamp()
              ZooKeeperUtil.writeValue(zk_list, zk_finish_mark_path, timestamp)
              logger.warn("zk_finish_mark_path : " + zk_finish_mark_path + ", value : " + timestamp)
        })
        
        //current data - (articleId###productId, (decay_meme, decay_impression, total_impression, update_date))
        val current_feedback_result_json = current_feedback_result.flatMap(msg => {
            val data_json = CompoundJson.getArticleMemeGmpHistoryJson(key_tag, msg._1, msg._2._1, msg._2._2, msg._2._3, msg._2._4)
            List(data_json)
        })
        //save history data
        current_feedback_result_json.saveAsTextFiles(hdfs_total_decay_feedback  + "/total-decay-feedback")
        current_feedback_result_json.foreachRDD((rdd: RDD[String]) => {
            rdd.foreachPartition(partitionOfRecords => {
                val batch_producer = new BatchProducer(zk_list, kafka_broker_list, kafka_batch_size)
                partitionOfRecords.foreach(msg => {
                    try {
                         batch_producer.send(topic_article_meme_gmp, msg)
                    } catch {
                        case ex: Exception => {
                            logger.warn(ex.getMessage, ex)
                        }
                    }
                })
                batch_producer.close()
            })
        })
        
        try {
            //save data into hdfs for check/monitor
            impression_stat.saveAsTextFiles(hdfs_impression + "/impression")
            current_gmp.saveAsTextFiles(hdfs_article_meme_gmp + "/article-gmp")
            
            if(is_debug){
                meme_stat.saveAsTextFiles(hdfs_debug +  "/meme")
                history_gmp_window.saveAsTextFiles(hdfs_debug + "/history-total-decay-feedback")
            }          
        } catch {
            case ex : Exception => logger.warn(ex.getMessage, ex)
        }

    }

    def createStreamingContext(): StreamingContext = {
        val conf = config.getConfig()
        val window_size = conf.getInt("sparkstreaming.batch.size.minutes")
        val checkpoint_dir = conf.getString("sparkstreaming.checkpoint")
        
        val spark_conf = new SparkConf()
        val ssc = new StreamingContext(spark_conf, Minutes(window_size))
        
        handler(ssc)
        ssc.checkpoint(checkpoint_dir)
        ssc
    }

    def main(args: Array[String]) {
        val conf = config.getConfig()
        val checkpoint_dir = conf.getString("sparkstreaming.checkpoint")
        
        val ssc = StreamingContext.getOrCreate(checkpoint_dir, () => {createStreamingContext()})

        try {
            ssc.start()
            ssc.awaitTermination()
        } finally {
            ssc.stop()
        }
    }
}
