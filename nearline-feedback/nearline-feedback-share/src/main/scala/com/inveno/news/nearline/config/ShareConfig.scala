package com.inveno.news.nearline.config

import inveno.bigdata.config.Configer

/**
 * Created by gaofeilu on 2017/3/1.
 */
object ShareConfig extends Configer{
  def appname = "share-app"

  setConfigFiles("service.conf")

  val zk_hosts = config.getString("zookeeper.hosts.overseas")
  val offset_basepath = config.getString("kafka.reformat.offset.path.base")
  val kafka_brokers = config.getString("kafka.brokers.overseas")
  val kafka_batchSize = config.getInt("kafka.producer.batchsize")

  val shareExtendTopic = TopicInfo(config.getString("kafka.reformat.topic.extend-event"),
    config.getInt("kafka.reformat.partition.extend-event"))

  //mysql
  val mysql_server = config.getString("mysql.server")
  val mysql_port = config.getInt("mysql.port")
  val mysql_db = config.getString("mysql.db")
  val mysql_user = config.getString("mysql.user")
  val mysql_pwd = config.getString("mysql.password")
  val mysql_chst = config.getString("mysql.charset")

}

case class TopicInfo(topic: String, partition: Int)
