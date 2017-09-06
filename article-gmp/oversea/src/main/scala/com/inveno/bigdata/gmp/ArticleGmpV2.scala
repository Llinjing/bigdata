package com.inveno.article.gmp

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
import com.inveno.bigdata.gmp.DataParser
import com.inveno.bigdata.gmp.ArticleGmpConfig
import com.inveno.bigdata.common.TimeUtil
import com.inveno.bigdata.common.reformat.CompoundJson
import com.inveno.bigdata.common.reformat.CombinationKey
import com.inveno.bigdata.common.kafka.BatchProducer
import com.inveno.bigdata.common.ZooKeeperUtil

object ArticleGmpV2 {
    val CLASS_NAME = ArticleGmpV2.getClass.getSimpleName

    val logger = LoggerFactory.getLogger(CLASS_NAME)
    private val config = ArticleGmpConfig.getInstance();

    /**
     * input:
     * calculate GMP - (articleId###productId, (total_request, total_impression, request_gmp, impression_gmp))
     * output:
     * Json Data -
     * [{"coolpad":{"":"gmp_value","impression":"impression_value","click":"click_value"}},
     *  {"emui":{"ctr":"gmp_value1","impression":"impression_value1","click":"click_value1"}}]
     */
    def writeIntoRedis(current_gmp: DStream[(String, (Long, Long, Float, Float))]) {
        val conf = config.getConfig
        val hdfs_redis = conf.getString("hdfs.redis")
        
        val write_stream = current_gmp.map(msg => {
            val key = msg._1.split(conf.getString("key_tag"))
            val article_id = key(0)
            val product_id = key(1)
            val (total_request, total_impression, request_gmp, impression_gmp) = msg._2
            (article_id, Set((product_id, total_request, total_impression, request_gmp, impression_gmp)))
        }).reduceByKey(ReduceUtil.reduceMergeSet[(String, Long, Long, Float, Float)] _, 18)
        .flatMap(msg=>{
            val gmp_json = CompoundJson.getArticleGmpRedisJsonV2(msg._2)
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
                        val article_gmp = msg._2
                        // write to redis
                        if (!jedis_cmd.isEmpty) {                      
                            jedis_cmd.get.hset(hash_key, article_id, article_gmp)
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
        val hdfs_impression = conf.getString("hdfs.impression")
        val hdfs_article_gmp = conf.getString("hdfs.article.gmp")
        val hdfs_total_decay_feedback = conf.getString("hdfs.total.decay.feedback")
                
        val topic_request = conf.getString("topic.request")
        val topic_impression = conf.getString("topic.impression")
        val topic_click = conf.getString("topic.click")
        val topic_article_gmp = conf.getString("topic.article.gmp")
        
        val window_num = conf.getInt("sparkstreaming.window.num")
        val window_size = conf.getInt("sparkstreaming.batch.size.minutes")
        var kafka_param = Map[String, String]()
        kafka_param += ("metadata.broker.list" -> conf.getString("src.kafka.broker.list"))
        kafka_param += ("group.id" -> conf.getString("src.kafka.consumer.group"))
        kafka_param += ("auto.offset.reset" -> "largest")
        val kafka_broker_list = conf.getString("src.kafka.broker.list")
        val kafka_batch_size = conf.getInt("src.kafka.batch.size")
      
        val is_debug = conf.getBoolean("debug", false)
        val hdfs_debug = conf.getString("hdfs_debug")
        // keep history from zk offset
        val zk_list = conf.getString("src.kafka.zk.list")
        val zk_offset_path = conf.getString("sparkstreaming.zk.offset.path")
        val key_tag = conf.getString("key_tag", "###")
        
        // click - (articleId###productId, (request, impression, click, total_request, total_impression, curr_date))
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
        }).reduceByKey(ReduceUtil.reduceDistinctLong _, 18).flatMap(msg => {
            val key_arr = msg._1.split(conf.getString("key_tag"))
            val key = CombinationKey.makeArticleCombinationKey(key_tag, key_arr(0), key_arr(1))
            List((key, 1L))
        }).reduceByKey(ReduceUtil.reduceAddLong _, 18).map(x => (x._1, (0f, 0f, x._2.toFloat, 0L, 0L, TimeUtil.getCurrDate()))).checkpoint(Minutes(window_size))

        // impression - (articleId###productId, (request, impression, click, total_request, impression, curr_date))
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
            val key_arr = msg._1.split(conf.getString("key_tag"))
            val key = CombinationKey.makeArticleCombinationKey(key_tag, key_arr(0), key_arr(1))
            List((key, 1L))
        }).reduceByKey(ReduceUtil.reduceAddLong _, 18).map(x => (x._1, (0f, x._2.toFloat, 0f, 0L, x._2, TimeUtil.getCurrDate()))).checkpoint(Minutes(window_size))
        
        // request - (articleId###productId, (request, impression, click, request, total_impression, curr_date))
        val request_partition_num = conf.getInt("topic.request.partition")
        val request_stream = StreamingFactory.createDirectStreamByOffset(zk_list, zk_offset_path, topic_request, request_partition_num, kafka_param, ssc, true)
        val request_window = request_stream.window(Minutes(window_size * window_num), Minutes(window_size))
        val request_stat = request_window.flatMap(msg => {
            val (unique_key, isValid) = DataParser.parseRequest(msg._2)
            isValid match {
                case true =>
                    List((unique_key, 1L))
                case _ => Nil
            }
        }).reduceByKey(ReduceUtil.reduceDistinctLong _, 18).flatMap(msg => {
            val key_arr = msg._1.split(conf.getString("key_tag"))
            val key = CombinationKey.makeArticleCombinationKey(key_tag, key_arr(0), key_arr(1))
            List((key, 1L))
        }).reduceByKey(ReduceUtil.reduceAddLong _, 18).map(x => (x._1, (x._2.toFloat, 0f, 0f, x._2, 0L, TimeUtil.getCurrDate()))).checkpoint(Minutes(window_size))
        
        // history data - (articleId###productId, (decay_request, decay_impression, decay_click, total_request, total_impression, update_date)) 
        val article_gmp_partition_num = conf.getInt("topic.article.gmp.partition")
        val history_gmp_stream = StreamingFactory.createDirectStreamByOffset(zk_list, zk_offset_path, topic_article_gmp, article_gmp_partition_num, kafka_param, ssc, true)
        val history_gmp_window = history_gmp_stream.window(Minutes(window_size * window_num), Minutes(window_size))
        val history_gmp_stat = history_gmp_window.flatMap(msg => {
            val (content_id, product_id, decay_request, decay_impression, decay_click, total_request, total_impression, update_date, isValid) = DataParser.parseHistoryGmpV2(msg._2)
            isValid match {
                case true =>
                    val key = CombinationKey.makeArticleCombinationKey(key_tag, content_id, product_id)
                    val decay_ratio = conf.getFloat("decay_ratio")
                    List((key, (decay_ratio*decay_request, decay_ratio*decay_impression, decay_ratio*decay_click, total_request, total_impression, update_date)))
                case _ => Nil
            }
        }).checkpoint(Minutes(window_size))

        // current data - (articleId###productId, (decay_request, decay_impression, decay_click, total_request, total_impression, update_date))
        val current_feedback_result = history_gmp_stat.union(request_stat).union(impression_stat).union(click_stat)
            .reduceByKey((a, b) => (a._1+b._1, a._2+b._2, a._3+b._3, a._4+b._4, a._5+b._5, if(a._6>b._6){a._6}else{b._6}))
      
        // calculate GMP - (articleId###productId, (decay_click, decay_request, decay_impression, request_gmp, impression_gmp))
        val current_gmp = current_feedback_result.flatMap(msg => {
            val key = msg._1.split(conf.getString("key_tag")).head + conf.getString("key_tag") +"-1" 
            List(msg):::List((key, msg._2))
        }).reduceByKey((a, b) => (a._1 + b._1, a._2 + b._2, a._3 + b._3, a._4 + b._4, a._5 + b._5, a._6)).flatMap(msg => {
            val key = msg._1.split(conf.getString("key_tag"))
            val product_id = key(1)
            val (decay_request, decay_impression, decay_click, total_request, total_impression, update_date) = msg._2

            if ((decay_click<decay_impression) && (decay_impression>0f)){
                product_id match {
                    case _ => {
                        // request gmp
                        var request_gmp = -1f
                        if (total_request >= conf.getInt("request_threshold")) {
                            request_gmp = decay_click/decay_request
                        } else {
                            Nil
                        }
                                                
                        // impression gmp
                        var impression_gmp = -1f
                        if (total_impression >= conf.getInt("impression_threshold")) {
                            impression_gmp = decay_click/decay_impression
                        } else {
                            Nil
                        }
                        
                        if ((request_gmp+impression_gmp) > -1.0) {
                            List((msg._1, (total_request, total_impression, request_gmp, impression_gmp)))  
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
        val stream_tmp = click_stat.map(x => (1L, 0L)).reduceByKey((a, b) => a+b).repartition(1)
        stream_tmp.foreachRDD((rdd) => {
              val timestamp = TimeUtil.getCurrTimestamp()
              logger.warn("zk_finish_mark_path : " + zk_finish_mark_path + ", value : " + timestamp)
              ZooKeeperUtil.writeValue(zk_list, zk_finish_mark_path, timestamp)
        })
        
        //current data - (articleId###productId, (decay_request, decay_impression, decay_click, total_request, total_impression, update_date))
        val current_feedback_result_json = current_feedback_result.flatMap(msg => {
            val data_json = CompoundJson.getArticleGmpHistoryJsonV2(key_tag, msg._1, msg._2._1, msg._2._2, msg._2._3, msg._2._4, msg._2._5, msg._2._6)
            List(data_json)
        })
        //save history data
        current_feedback_result_json.saveAsTextFiles(hdfs_total_decay_feedback  + "/total-decay-feedback")
        current_feedback_result_json.foreachRDD((rdd: RDD[String]) => {
            rdd.foreachPartition(partitionOfRecords => {
                val batch_producer = new BatchProducer(zk_list, kafka_broker_list, kafka_batch_size)
                partitionOfRecords.foreach(msg => {
                    try {
                         batch_producer.send(topic_article_gmp, msg)
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
        impression_stat.saveAsTextFiles(hdfs_impression + "/impression")
        current_gmp.saveAsTextFiles(hdfs_article_gmp + "/article-gmp")
        
        if(is_debug){
            request_stat.saveAsTextFiles(hdfs_debug +  "/request")  
            click_stat.saveAsTextFiles(hdfs_debug +  "/click")
            history_gmp_stat.saveAsTextFiles(hdfs_debug + "/history-total-decay-feedback")
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
