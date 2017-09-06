package com.inveno.news.nearline.common

import inveno.bigdata.config.Configer


object SourceConfig extends Configer {

  def appname = "nearline-feedback"

  setConfigFiles("service.conf")

  val zk_hosts = config.getString("zookeeper.hosts.china")
  val offset_basepath = config.getString("kafka.reformat.offset.path.base")
  val kafka_brokers = config.getString("kafka.brokers.china")
  val kafka_batchSize = config.getInt("kafka.producer.batchsize")

  val requestTopic = TopicInfo(config.getString("kafka.reformat.topic.request"),
    config.getInt("kafka.reformat.partition.request"))

  val impressionTopic = TopicInfo(config.getString("kafka.reformat.topic.impression"),
    config.getInt("kafka.reformat.partition.impression"))

  val clickTopic = TopicInfo(config.getString("kafka.reformat.topic.click"),
    config.getInt("kafka.reformat.partition.click"))

  val dwelltimeTopic = TopicInfo(config.getString("kafka.reformat.topic.dwelltime"),
    config.getInt("kafka.reformat.partition.dwelltime"))

}

case class TopicInfo(name: String, partition: Int)
