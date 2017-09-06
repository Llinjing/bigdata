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


prepare

keep_log

