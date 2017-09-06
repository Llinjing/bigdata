#!/bin/bash

function prepare()
{
	source ../conf/project.conf
	source ../conf/warning.conf

	return 0
}

function monitor_reformat_service()
{
	for((i=0;i<${REFORMAT_TASK_NUM};++i))
	do
		local pid=`ps -ef | grep ${REFORMAT_TASK_NAME[$i]} | grep -v "grep" | awk '{print $2}'`
		if [ -z "${pid}" ]
		then
			${WARNING_BIN} "${PRODUCT_LINE} : ${REFORMAT_TASK_NAME[$i]} is not running" ${SMS_USER[$FATAL]}
            ${WARNING_CALL_BIN} "reformat not running" ${CALL_USER[$FATAL]}
            sh -x ${LOCAL_BIN_PATH}/stop-${REFORMAT_TASK_NAME[$i]}.sh
            sh -x ${LOCAL_BIN_PATH}/start-${REFORMAT_TASK_NAME[$i]}.sh
		fi
	done

	return 0
}


prepare

monitor_reformat_service


