#!/bin/bash

function check_blocked_task()
{
	source ../conf/project.conf
	source ../conf/warning.conf

	mkdir -p ${LOCAL_WAITING_RUNNING_TASK_POOL_PATH}
                
	local task_num=`ls ${LOCAL_RUNNING_TASK_POOL_PATH} | wc -l`
	if [ $task_num -gt ${BLOCKED_TASK_NUM} ]
	then
		${WARNING_BIN} "${PRODUCT_LINE}_:_article-gmp_running_task_blocked_:_${task_num}" ${SMS_USER[$ERROR]}
	fi

        task_num=`ls ${LOCAL_WAITING_RUNNING_TASK_POOL_PATH} | wc -l`
        if [ $task_num -gt ${BLOCKED_TASK_NUM} ]
        then
                ${WARNING_BIN} "${PRODUCT_LINE}_:_article-gmp_waiting_run_task_blocked_:_${task_num}" ${SMS_USER[$ERROR]}
        fi
	return 0
}


check_blocked_task
