#!/bin/bash

function prepare()
{
	source ../conf/project.conf
	return 0
}

function keep_hdfs_data()
{
    local current=`date "+%Y%m%d" -d"-8 day"`
    hadoop fs -rm -r -skipTrash ${MR_HISTORY_DATA_PATH}/${current}
	return 0
}

prepare
keep_hdfs_data


