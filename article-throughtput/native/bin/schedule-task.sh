#!/bin/bash

function prepare()
{
	source ../conf/project.conf
	source ../conf/hadoop.conf
	source ../conf/warning.conf

	mkdir -p ${LOCAL_WAITING_RUNNING_TASK_POOL_PATH}
	mkdir -p ${LOCAL_RUNNING_TASK_POOL_PATH}

	return 0
}


function schedule_task()
{
	mark_running
	if [ $? -ne 0 ]		# no file
	then
		return 0
	fi
	
	## schedule
	${MR_FRAMEWORK_BIN} ${LOCAL_CONF_PATH}/framework-article-throughput.conf
	if [ $? -ne 0 ]
	then
		mark_failed

		${WARNING_BIN} "${PRODUCT_LINE}_stat-article-throughput_failed"  "${SMS_USER[$ERROR]}"
	else
		mark_succeed
	fi

	return 0
}

function mark_running()
{
	local ret=1

	for file in `ls ${LOCAL_WAITING_RUNNING_TASK_POOL_PATH}`
	do
		mv ${LOCAL_WAITING_RUNNING_TASK_POOL_PATH}/${file} ${LOCAL_RUNNING_TASK_POOL_PATH}
		ret=0
	done

	return ${ret}
}

function mark_failed()
{
	for file in `ls ${LOCAL_RUNNING_TASK_POOL_PATH}`
	do
		mv ${LOCAL_RUNNING_TASK_POOL_PATH}/${file} ${LOCAL_WAITING_RUNNING_TASK_POOL_PATH}
	done

	return 0
}

function mark_succeed()
{
	for file in `ls ${LOCAL_RUNNING_TASK_POOL_PATH}`
	do
		rm -f ${LOCAL_RUNNING_TASK_POOL_PATH}/${file}
	done

	return 0
}


prepare

schedule_task

