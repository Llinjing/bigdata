#!/bin/bash

function check_blocked_task()
{
	source ../conf/project.conf
	source ../conf/warning.conf

	task_path=${LOCAL_DATA_PATH}/uploading/
	mkdir -p ${task_path}
                
	local task_num=`ls ${task_path} | wc -l`
	if [ $task_num -gt ${BLOCKED_TASK_NUM} ]
	then
		python ${LOCAL_BIN_PATH}/zabbix_sendsms.py "ip_${LOG_SERVER_IP}_gbdt_data_put_to_cluster_tasks_blocked_:_${task_num}" ${SMS_USER[$ERROR]}
	fi
}

check_blocked_task
