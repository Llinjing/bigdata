#!/bin/bash

function prepare()
{
	source ../conf/project.conf

	return 0
}

function keep_log()
{
	## log
	local log_expired_day=`date +%Y-%m-%d -d"-${LOG_KEEPER_DAYS} day"`
	rm -f ${LOCAL_LOG_PATH}/*.${log_expired_day}

	return 0
}

function keep_hdfs_data()
{
	source ./hdfs-util.sh

	## format data
	for((i=0;i<${TOPIC_NUM};++i))
	do
		delete_by_date ${HDFS_TOPIC_DATA_PATH}/${TOPIC[$i]} "%Y%m%d" ${HDFS_DATA_KEEPER_DAYS}
	done

	return 0
}


prepare

keep_log

keep_hdfs_data


