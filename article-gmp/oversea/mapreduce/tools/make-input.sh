#!/bin/bash

function submit_task()
{
	source ../conf/project.conf
	source ../conf/hadoop.conf
	source ../conf/warning.conf
	
	local delt=$1
	
	local curr_time=`date +%Y%m%d%H%M -d"-${delt} min"`  # max latency 20 minutes
	local curr_date=`echo "${curr_time}" | awk '{
		print substr($1, 1, 8)
	}'`
	local curr_hour=`echo "${curr_time}" | awk '{
		print substr($1, 9, 2)
	}'`
	local curr_min=`echo "${curr_time}" | awk '{
		print sprintf("%02d", int(substr($1, 11, 2) / 10) * 10)
	}'`

	click=`echo ${HDFS_CLICK_PATH}`
	impression=`echo ${HDFS_IMPRESSION_PATH}`

	local click_path=${click}/${curr_date}/${curr_hour}/*.${curr_date}_${curr_hour}${curr_min}
	local impression_path=${impression}/${curr_date}/${curr_hour}/*.${curr_date}_${curr_hour}${curr_min}

	${HADOOP_TEST} ${click_path}
	if [ $? -ne 0 ]
	then
		${WARNING_BIN} "no data : ${click_path}" ${SMS_USER[$FATAL]}
	fi

	${HADOOP_TEST} ${impression_path}
	if [ $? -ne 0 ]
	then
		${WARNING_BIN} "no data : ${impression_path}" ${SMS_USER[$FATAL]}
	fi

	local curr_time=${curr_date}${curr_hour}${curr_min}

	mkdir -p ../data/input
	echo "${click_path}" >../data/input/${curr_time}
	echo "${impression_path}" >>../data/input/${curr_time}

	return 0
}

end=10*6*1+10*0+20
for((i=20; i<${end}; i+=10))
do
	submit_task $i
done
