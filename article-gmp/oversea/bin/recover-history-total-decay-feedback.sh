#!/bin/bash
# huanghaifeng
# 2016-09-03 

function get_data()
{
	hadoop fs -cat /inveno-projects/article-gmp-sparkstreaming/data/total-decay-feedback/total-decay-feedback-1472919600000/part-* > total-decay-feedback.list
}

function parse_data()
{
	touch ./total-decay-feedback.list
	cat ./total-decay-feedback.list | awk 'BEGIN{
		FS = ","
		OFS = "\t"
		OFMT="%.8f"
	}{
		split($1, arr, "###")
        article_id = substr(arr[1], 2, length(arr[1]))
		product_id = arr[2]
		decay_click = substr($2, 2, length($2))
		decay_impression = $3
		total_impression = $4
		update_date = substr($5, 1, length($5)-2)
        print "{\"content_id\":\""article_id"\",\"product_id\":\""product_id"\",\"decay_click\":"decay_click+0.0",\"decay_impression\":"decay_impression+0.0",\"total_impression\":"total_impression",\"update_date\":\""update_date"\"}"
	}END{
	}' > total-decay-feedback-parse.tmp
}

function send_kafka_data()
{
	touch ./total-decay-feedback-parse.tmp
	cat ./total-decay-feedback-parse.tmp  | /usr/local/kafka/bin/kafka-console-producer.sh --broker-list 192.168.1.60:9092 --topic article-gmp
}

get_data
parse_data
#send_kafka_data
