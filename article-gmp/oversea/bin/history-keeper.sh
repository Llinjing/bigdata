#!/bin/bash

function prepare()
{
	source ../conf/project.conf
	return 0
}

function keep_hdfs_data()
{
    local current=`date "+%Y-%m-%d 00:00:00" -d"-3 day"`
    local start_timeStamp=`date -d "$current" +%s`"000"
    for ((i=0; i<144; i++))
    do
        echo ${i}
		tmp_time=$[start_timeStamp+i*10*60*1000]
        hadoop fs -rm -r -skipTrash ${CLUSTER_WORK_PATH}/data/*/*${tmp_time}
    done

	return 0
}

prepare
keep_hdfs_data


