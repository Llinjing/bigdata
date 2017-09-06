#!/bin/bash
# huanghaifeng
# 2016-09-03 

## get data
/usr/local/kafka/bin/kafka-console-consumer.sh --zookeeper 192.168.1.60:2181,192.168.1.61:2181,192.168.1.62:2181 --topic article-gmp > article-gmp.list

function send_kafka_data()
{
	touch ./article-gmp.list
	cat ./article-gmp.list  | /usr/local/kafka/bin/kafka-console-producer.sh --broker-list 192.168.1.60:9092 --topic article-gmp
}

#send_kafka_data
