package com.inveno.extend.common

import inveno.bigdata.config.Configer

/**
 * Created by dory on 2017/2/10.
 */
object ExtendEventConfig extends Configer {

  def appname = "nearline-feedback"

  setConfigFiles("service.conf")

  val zk_hosts = config.getString("zookeeper.hosts.overseas")

  val kafka_brokers = config.getString("kafka.brokers.overseas")

  val output_topic = config.getString("zookeeper.hosts.output_topic")

  val kafka_batchSize = config.getInt("kafka.producer.batchsize")

  val offset_base = config.getString("zookeeper.hosts.basepath")

  val extend_hdfs = config.getString("hdfs.topic.extend")

  val click_hdfs = config.getString("hdfs.topic.click")

  val extendTopicInfo = TopicInfo(config.getString("kafka.reformat.extend.topic"), config.getInt("kafka.reformat.extend.partition"))

  val clickTopicInfo = TopicInfo(config.getString("kafka.reformat.click.topic"), config.getInt("kafka.reformat.click.partition"))

}

case class TopicInfo(name: String, partition: Int)
