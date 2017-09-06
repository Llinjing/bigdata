#!/bin/bash

function start()
{
	source ../conf/project.conf
	source ../conf/warning.conf
	source /etc/profile

	${MR_FRAMEWORK_BIN} ${LOCAL_CONF_PATH}/framework-daily.conf
	
	if [ $? -eq 0 ]
	then
		echo ${YESTERDAY}, "chinese_offline_advertisement_stat_job_is_success" >> ${LOCAL_LOG_PATH}/monitor.log
	else
		echo ${YESTERDAY}, "chinese_offline_advertisement_stat_job_is_failed" >> ${LOCAL_LOG_PATH}/monitor.log
		${WARNING_BIN} "chinese_offline_advertisement_stat_job_is_failed"  "${SMS_USER[$FATAL]}"
	fi
	return 0
}

start


