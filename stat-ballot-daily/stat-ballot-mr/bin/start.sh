#!/bin/bash

function start()
{
	source ../conf/project.conf

	${MR_FRAMEWORK_BIN} ${LOCAL_CONF_PATH}/framework-daily.conf
	
	if [ $? -eq 0 ]
	then
		echo ${YESTERDAY}, "overseas daily user job success" >> ${LOCAL_LOG_PATH}/monitor.log
	else
		#${WARNING_BIN} "overseas daily user job failed" "${PHONENUMS}"
		echo ${YESTERDAY}, "overseas daily user job failed" >> ${LOCAL_LOG_PATH}/monitor.log
	fi

	return 0
}

start
