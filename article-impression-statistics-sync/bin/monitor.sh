#!/bin/bash

function prepare()
{
	source ../conf/project.conf
	source ../conf/warning.conf

	return 0
}

function monitor_impression_stat_sync_service()
{
	for((i=0;i<${IMPRESSION_STAT_SYNC_TASK_NUM};++i))
	do
		local pid=`ps -ef | grep ${IMPRESSION_STAT_SYNC_TASK_NAME[$i]} | grep -v "grep" | awk '{print $2}'`
		if [ -z "${pid}" ]
		then
			${WARNING_BIN} "${PRODUCT_LINE} : ${IMPRESSION_STAT_SYNC_TASK_NAME[$i]} is not running, will restart" ${SMS_USER[$INFO]}
		        ${WARNING_CALL_BIN} "IMPRESSION_STAT_SYNC not running" ${CALL_USER[$INFO]}
		        sh -x ${LOCAL_BIN_PATH}/stop-${IMPRESSION_STAT_SYNC_TASK_NAME[$i]}.sh
		        sh -x ${LOCAL_BIN_PATH}/start-${IMPRESSION_STAT_SYNC_TASK_NAME[$i]}.sh
		fi
	done

	return 0
}


prepare

monitor_impression_stat_sync_service


