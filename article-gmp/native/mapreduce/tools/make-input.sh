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

	local click_path=${HDFS_CLICK_PATH}/${curr_date}/${curr_hour}/*.${curr_date}_${curr_hour}${curr_min}
        local request_path=${HDFS_REQUEST_PATH}/${curr_date}/${curr_hour}/*.${curr_date}_${curr_hour}${curr_min}
        local impression_path=${HDFS_IMPRESSION_PATH}/${curr_date}/${curr_hour}/*.${curr_date}_${curr_hour}${curr_min}

        local run_mark=1
        ${HADOOP_TEST} ${click_path}
        if [ $? -ne 0 ]
        then
                ${WARNING_BIN} "no_data_:_click_${curr_date}_${curr_hour}${curr_min}" ${SMS_USER[$FATAL]}
                run_mark=0
        fi

        ${HADOOP_TEST} ${request_path}
        if [ $? -ne 0 ]
        then
                ${WARNING_BIN} "no_data_:_request_${curr_date}_${curr_hour}${curr_min}" ${SMS_USER[$FATAL]}
                run_mark=0
        fi 

        ${HADOOP_TEST} ${impression_path}
        if [ $? -ne 0 ]
        then
                ${WARNING_BIN} "no_data_:_impression_${curr_date}_${curr_hour}${curr_min}" ${SMS_USER[$FATAL]}
                run_mark=0              
        fi

	local curr_time=${curr_date}${curr_hour}${curr_min}

	mkdir -p ../data/input
	echo "${click_path}" >../data/input/${curr_time}
	echo "${request_path}" >>../data/input/${curr_time}
	echo "${impression_path}" >>../data/input/${curr_time}

	return 0
}

end=10*6*0+10*2+20
for((i=20; i<${end}; i+=10))
do
	submit_task $i
done
