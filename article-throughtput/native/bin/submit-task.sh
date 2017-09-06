#!/bin/bash

function submit_task()
{
	source ../conf/project.conf
	source ../conf/hadoop.conf
	source ../conf/warning.conf
		
	local curr_time=`date +%Y%m%d%H%M -d"-20 min"`  # max latency 20 minutes
	local curr_date=`echo "${curr_time}" | awk '{
		print substr($1, 1, 8)
	}'`
	local curr_hour=`echo "${curr_time}" | awk '{
		print substr($1, 9, 2)
	}'`
	local curr_min=`echo "${curr_time}" | awk '{
		print sprintf("%02d", int(substr($1, 11, 2) / 10) * 10)
	}'`

	local impression_path=${HDFS_IMPRESSION_PATH}/${curr_date}/${curr_hour}/*.${curr_date}_${curr_hour}${curr_min}

	local run_mark=1
	${HADOOP_TEST} ${impression_path}
	if [ $? -ne 0 ]
	then
		${WARNING_BIN} "no_data_:_impression_${curr_date}_${curr_hour}${curr_min}" ${SMS_USER[$ERROR]}
		run_mark=0		
	fi

	if [ ${run_mark} -eq 1 ]
	then
		mkdir -p ${LOCAL_WAITING_RUNNING_TASK_POOL_PATH}
		echo "${impression_path}" >>${LOCAL_WAITING_RUNNING_TASK_POOL_PATH}/${curr_date}${curr_hour}${curr_min}
	fi

	return 0
}


submit_task
