package com.inveno.news.nearline.config

import inveno.bigdata.config.Configer

object OverseasConfig extends Configer {

  def appname = "nearline-feedback"

  setConfigFiles("service.conf")

  val zk_hosts = config.getString("zookeeper.hosts.overseas")
  val offset_basePath = config.getString("kafka.reformat.offset.path.base")
  val kafka_brokers = config.getString("kafka.brokers.overseas")
  val kafka_batchSize = config.getInt("kafka.producer.batchsize")

  val requestTopic = OverseasTopicInfo(config.getString("kafka.reformat.topic.request"),
    config.getInt("kafka.reformat.partition.request"))

  val impressionTopic = OverseasTopicInfo(config.getString("kafka.reformat.topic.impression"),
    config.getInt("kafka.reformat.partition.impression"))

  val clickTopic = OverseasTopicInfo(config.getString("kafka.reformat.topic.click"),
    config.getInt("kafka.reformat.partition.click"))

  val dwellTimeTopic = OverseasTopicInfo(config.getString("kafka.reformat.topic.dwelltime"),
    config.getInt("kafka.reformat.partition.dwelltime"))

}

case class OverseasTopicInfo(name: String, partition: Int)
