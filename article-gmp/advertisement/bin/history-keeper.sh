#!/bin/bash

function prepare()
{
	source ../conf/project.conf
	return 0
}

function keep_hdfs_data()
{
        local start_time=1472918400000
        ##`date +%s`000
        local size=20
        for ((i=0; i<${size}; i++))
        do
		tmp_time=$[start_time-i*10*60*1000]
		hadoop fs -rm -r ${DEBUG_HDFS_PATH}/history-total-decay-feedback-${tmp_time}
		hadoop fs -rm -r ${DEBUG_HDFS_PATH}/click-${tmp_time}
		hadoop fs -rm -r ${IMPRESSION_HDFS_PATH}/impression-${tmp_time}
		hadoop fs -rm -r ${ARTICLE_GMP_HDFS_PATH}/article-gmp-${tmp_time}
		hadoop fs -rm -r ${TOTAL_DECAY_FEEDBACK}/total-decay-feedback-${tmp_time}
        done

	return 0
}

prepare
keep_hdfs_data


