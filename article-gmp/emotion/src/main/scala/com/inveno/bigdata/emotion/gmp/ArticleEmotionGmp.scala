package com.inveno.bigdata.emotion.gmp

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
import com.inveno.bigdata.common.reformat.CombinationKey
import com.inveno.bigdata.common.kafka.BatchProducer
import com.inveno.bigdata.common.ZooKeeperUtil
import com.inveno.bigdata.common.reformat.CompoundJson

object ArticleEmotionGmp {
    val CLASS_NAME = ArticleEmotionGmp.getClass.getSimpleName

    val logger = LoggerFactory.getLogger(CLASS_NAME)
    val config = ArticleEmotionGmpConfig.getInstance();

    /**
     * input:
     * calculate GMP - (articleId###productId, (bored_gmp, like_gmp, angry_gmp, sad_gmp))
     * output:
     * Json Data -
     * [{"hotoday":{"bored_gmp":"bored_gmp_value","like_gmp":"like_gmp_value","angry_gmp":"angry_gmp_value","sad_gmp":"sad_gmp_value"}},
     *  {"-1":{"bored_gmp":"bored_gmp_value","like_gmp":"like_gmp_value","angry_gmp":"angry_gmp_value","sad_gmp":"sad_gmp_value"}}]
     */
    def writeIntoRedis(current_gmp: DStream[(String, (Float, Float, Float, Float))]) {
        val conf = config.getConfig
        val hdfs_redis = conf.getString("hdfs.redis")
        val key_tag = conf.getString("key_tag")
        val write_stream = current_gmp.map(msg => {
            val key = msg._1.split(key_tag)
            val article_id = key(0)
            val product_id = key(1)
            val (bored_gmp, like_gmp, angry_gmp, sad_gmp) = msg._2
            (article_id, Set((product_id, bored_gmp, like_gmp, angry_gmp, sad_gmp)))
        }).reduceByKey(ReduceUtil.reduceMergeSet[(String, Float, Float, Float, Float)] _, 18)
        .flatMap(msg=>{
            val gmp_json = CompoundJson.getEmotionGmpRedisJson(msg._2)
            List((msg._1, gmp_json))
        })
        write_stream.saveAsTextFiles(hdfs_redis + "/redis")
        
        try {
            // jedis session
            var jedis_cmd : Option[JedisCommands] = None
            val is_cluster = conf.getBoolean("redis.cluster")
            val hash_key = conf.getString("redis.hash.key")
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
                        val gmp_json = msg._2
                        // write to redis
                        if (!jedis_cmd.isEmpty) {                      
                            jedis_cmd.get.hset(hash_key, article_id, gmp_json)
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
        val hdfs_total_decay_feedback = conf.getString("hdfs.total.decay.feedback")
        val hdfs_article_emotion_gmp = conf.getString("hdfs_article_emotion_gmp")
        val hdfs_debug = conf.getString("hdfs_debug")
        // TOPIC
        val topic_click = conf.getString("topic.click")
        val topic_emotion = conf.getString("topic.emotion")
        val topic_article_emotion_gmp = conf.getString("topic.article.emotion.gmp")
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
        // keep history from zk offset
        val zk_list = conf.getString("src.kafka.zk.list")
        val zk_offset_path = conf.getString("sparkstreaming.zk.offset.path")
        // CONSTANTS
        val is_debug = conf.getBoolean("debug", false)
        val key_tag = conf.getString("key_tag", "###")
        val decay_ratio = conf.getFloat("decay_ratio")
        val date_format = conf.getString("date_format")
        
        // emotion - (articleId###productId, (bored, like, angry, sad, decay_total_click, total_click, curr_date)
        // emotion - (articleId###productId, (0f, 0f, 0f, 0f, 0f, 0L, curr_date)
        val emotion_partition_num = conf.getInt("topic.emotion.partition")
        val emotion_stream = StreamingFactory.createDirectStreamByOffset(zk_list, zk_offset_path, topic_emotion, emotion_partition_num, kafka_param, ssc, true)
        val emotion_window = emotion_stream.window(Minutes(window_size * window_num), Minutes(window_size))
        val emotion_stat = emotion_window.flatMap(msg=>{
            val (unique_key, isValid) = DataParser.parseEmotion(msg._2)
            isValid match{
                case true => 
                    List((unique_key, 1L))
                case _ => Nil
            }
        }).reduceByKey(ReduceUtil.reduceDistinctLong _, 18)
        .flatMap(msg => {
            val key_arr = msg._1.split(key_tag)
            val key = CombinationKey.makeArticleCombinationKey(key_tag, key_arr(0), key_arr(1))
            List((key, (key_arr(2).toInt, key_arr(3).toInt, key_arr(4).toInt, key_arr(5).toInt)))
        }).reduceByKey((a, b) =>(a._1+b._1, a._2+b._2, a._3+b._3, a._4+b._4))
        .map(x =>(x._1, (x._2._1.toFloat, x._2._2.toFloat, x._2._3.toFloat, x._2._4.toFloat, 0f, 0L, TimeUtil.getCurrDate(date_format))))
        .checkpoint(Minutes(window_size))
        
        // click - (articleId###productId, (bored, like, angry, sad, decay_total_click, total_click, curr_date)
        // click - (articleId###productId, (0f, 0f, 0f, 0f, click, click, curr_date))
        val click_partition_num = conf.getInt("topic.click.partition")
        val click_stream = StreamingFactory.createDirectStreamByOffset(zk_list, zk_offset_path, topic_click, click_partition_num, kafka_param, ssc, true)
        val click_window = click_stream.window(Minutes(window_size * window_num), Minutes(window_size))
        val click_stat = click_window.flatMap(msg => {
            val (unique_key, isValid) = DataParser.parseClick(msg._2)
            isValid match {
                case true =>
                    List((unique_key, 1L))
                case _ => Nil
            }
        }).reduceByKey(ReduceUtil.reduceDistinctLong _, 18)
        .flatMap(msg => {
            val key_arr = msg._1.split(key_tag)
            val key = CombinationKey.makeArticleCombinationKey(key_tag, key_arr(0), key_arr(1))
            List((key, 1L))
        }).reduceByKey(ReduceUtil.reduceAddLong _, 18)
        .map(x => (x._1, (0f, 0f, 0f, 0f, x._2.toFloat, x._2, TimeUtil.getCurrDate(date_format))))
        .checkpoint(Minutes(window_size))
        
        // history data - (articleId###productId, (decay_bored, decay_like, decay_angry, decay_sad, decay_total_click, total_click, update_date)
        // history data - (articleId###productId, (0f, 0f, 0f, 0f, 0f, 0L, update_date)
        val article_emotion_gmp_partition_num = conf.getInt("topic.article.emotion.gmp.partition")
        val history_emotion_gmp_stream = StreamingFactory.createDirectStreamByOffset(zk_list, zk_offset_path, topic_article_emotion_gmp, article_emotion_gmp_partition_num, kafka_param, ssc, true)
        val history_emotion_gmp_window = history_emotion_gmp_stream.window(Minutes(window_size * window_num), Minutes(window_size))
        val history_emotion_gmp_stat = history_emotion_gmp_window.flatMap(msg => {
            val (content_id, product_id, decay_bored, decay_like, decay_angry, decay_sad, decay_total_click, total_click, update_date, isValid) = DataParser.parseHistoryEmotionGmp(msg._2)
            isValid match {
                case true =>
                    val key = CombinationKey.makeArticleCombinationKey(key_tag, content_id, product_id)
                    List((key, (decay_ratio*decay_bored, decay_ratio*decay_like, decay_ratio*decay_angry, decay_ratio*decay_sad, decay_ratio*decay_total_click, total_click, update_date)))
                case _ => Nil
            }
        }).checkpoint(Minutes(window_size))

        // current data - (articleId###productId, (decay_bored, decay_like, decay_angry, decay_sad, decay_total_click, total_click, update_date)
        val current_feedback_result = history_emotion_gmp_stat.union(emotion_stat).union(click_stat)
            .reduceByKey((a, b) => (a._1+b._1, a._2+b._2, a._3+b._3, a._4+b._4, a._5+b._5, a._6+b._6, if(a._7>b._7){a._7}else{b._7}))
      
        // calculate GMP - (articleId###productId, (bored_gmp, like_gmp, angry_gmp, sad_gmp))
        // current_gmp: DStream[(String, (Float, Float, Float, Float))]
        val current_gmp = current_feedback_result.flatMap(msg => {
            val key = msg._1.split(key_tag).head + key_tag +"-1" 
            List(msg):::List((key, msg._2))
        }).reduceByKey((a, b) => (a._1+b._1, a._2+b._2, a._3+b._3, a._4+b._4, a._5+b._5, a._6+b._6, a._7))
        .flatMap(msg => {
            val key = msg._1.split(key_tag)
            val product_id = key(1)
            val (decay_bored, decay_like, decay_angry, decay_sad, decay_total_click, total_click, update_date) = msg._2

            if ((decay_total_click>0f) && (decay_bored<=decay_total_click) && (decay_like<=decay_total_click) && (decay_angry<=decay_total_click) && (decay_sad<=decay_total_click)) {
                product_id match {
                    case _ => {
                        var bored_gmp = -1f
                        if(total_click >= conf.getInt("bored_click_threshold")) {
                            bored_gmp = decay_bored/decay_total_click
                        }
                      
                        var like_gmp = -1f 
                        if(total_click >= conf.getInt("like_click_threshold")) {
                            like_gmp = decay_like/decay_total_click
                        }
                        
                        var angry_gmp = -1f
                        if(total_click >= conf.getInt("angry_click_threshold")) {
                            angry_gmp = decay_angry/decay_total_click
                        }
                         
                        var sad_gmp = -1f
                        if(total_click >= conf.getInt("sad_click_threshold")) {
                            sad_gmp = decay_sad/decay_total_click
                        }
                      
                        val sum_gmp = bored_gmp + like_gmp + angry_gmp + sad_gmp
                        
                        if(sum_gmp > -3) {
                            List((msg._1, (bored_gmp, like_gmp, angry_gmp, sad_gmp)))    
                        } else{
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
        val stream_tmp = click_stat.map(x => (1L, 0L)).reduceByKey((a, b) => a+b).repartition(1)
        stream_tmp.foreachRDD((rdd) => {
              val timestamp = TimeUtil.getCurrTimestamp()
              ZooKeeperUtil.writeValue(zk_list, zk_finish_mark_path, timestamp)
              logger.warn("zk_finish_mark_path : " + zk_finish_mark_path + ", value : " + timestamp)
        })

        //save history data into kafka topic
        //(articleId###productId, (decay_bored, decay_like, decay_angry, decay_sad, decay_total_click, total_click, update_date)
        val current_feedback_result_json = current_feedback_result.flatMap(msg => {
            val data_json = CompoundJson.getEmotionGmpHistoryJson(key_tag, msg._1, msg._2._1, msg._2._2, msg._2._3, msg._2._4, msg._2._5, msg._2._6, msg._2._7)
            List(data_json)
        })
        current_feedback_result_json.saveAsTextFiles(hdfs_total_decay_feedback  + "/total-decay-feedback")
        current_feedback_result_json.foreachRDD((rdd: RDD[String]) => {
            rdd.foreachPartition(partitionOfRecords => {
                val batch_producer = new BatchProducer(zk_list, kafka_broker_list, kafka_batch_size)
                partitionOfRecords.foreach(msg => {
                    try {
                         batch_producer.send(topic_article_emotion_gmp, msg)
                    } catch {
                        case ex: Exception => {
                            logger.warn(ex.getMessage, ex)
                        }
                    }
                })
                batch_producer.close()
            })
        })
        
        //save data into hdfs for check/monitor
        current_gmp.saveAsTextFiles(hdfs_article_emotion_gmp + "/article-emotion-gmp")
        
        if(is_debug){
            click_stat.saveAsTextFiles(hdfs_debug +  "/click")
            emotion_stat.saveAsTextFiles(hdfs_debug + "/emotion")
            history_emotion_gmp_window.saveAsTextFiles(hdfs_debug + "/history-total-decay-feedback")
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
