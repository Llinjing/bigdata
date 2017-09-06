package com.inveno.advertise.nearline.config

import inveno.bigdata.config.Configer


object AdvConfig extends Configer {

  def appname = "ad-nearline-feedback"

  setConfigFiles("service.conf")

  val zk_hosts = config.getString("zookeeper.hosts.china")
  val offset_basepath = config.getString("kafka.reformat.offset.path.base")
  val kafka_brokers = config.getString("kafka.brokers.china")
  val kafka_batchSize = config.getInt("kafka.producer.batchsize")

  val hash_key = config.getString("redis.hashkey")

  val redisGateInfo = RedisInfo(config.getString("redis.server"),config.getInt("redis.port"),config.getString("redis.hashkey"))
  val redisMalaccaInfo = RedisInfo(config.getString("redis.mserver"),config.getInt("redis.mport"),config.getString("redis.hashkey"))

  val advertiseRequestTopic = TopicInfo(config.getString("kafka.reformat.topic.advertiserequest"),
    config.getInt("kafka.reformat.partition.advertiserequest"))

  val advertiseImpressionTopic = TopicInfo(config.getString("kafka.reformat.topic.advertiseimpression"),
    config.getInt("kafka.reformat.partition.advertiseimpression"))

  val advertiseClickTopic = TopicInfo(config.getString("kafka.reformat.topic.advertiseclick"),
    config.getInt("kafka.reformat.partition.advertiseclick"))

}

case class TopicInfo(name: String, partition: Int)

case class RedisInfo(server:String, port:Int, hashkey:String)

