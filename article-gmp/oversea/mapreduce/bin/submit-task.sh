#!/bin/bash

function submit_task()
{
	source ../conf/project.conf
	source ../conf/hadoop.conf
	source ../conf/warning.conf
		
	local curr_time=`date +%Y%m%d%H%M -d"-10 min"`  # max latency 10 minutes
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
        request=`echo ${HDFS_REQUEST_PATH}`

        local click_path=${click}/${curr_date}/${curr_hour}/*.${curr_date}_${curr_hour}${curr_min}
        local impression_path=${impression}/${curr_date}/${curr_hour}/*.${curr_date}_${curr_hour}${curr_min}
        local request_path=${request}/${curr_date}/${curr_hour}/*.${curr_date}_${curr_hour}${curr_min}

	## click impression request
        local click_mark=0
        local impression_mark=0
        local request_mark=0
        for ((i=1; i<=50; i++))
        do
                echo "try-get-click-file-"${i}  
                ${HADOOP_TEST} ${click_path}
                if [ $? -eq 0 ]
                then
                        click_mark=1
                fi

                echo "try-get-impression-file-"${i}
                ${HADOOP_TEST} ${impression_path}
                if [ $? -eq 0 ]
                then
                        impression_mark=1
                fi

                echo "try-get-request-file-"${i}
                ${HADOOP_TEST} ${request_path}
                if [ $? -eq 0 ]
                then
                        request_mark=1
                fi

                if [ ${click_mark} -eq 1 -a ${impression_mark} -eq 1 -a ${request_mark} -eq 1 ]
                then
                        break
                else
                        sleep 10s
                fi
        done

	local run_mark=1
        if [ ${click_mark} -ne 1 ]
        then
                ${WARNING_BIN} "article_gmp_overseas_no_data_:_click_${curr_date}_${curr_hour}${curr_min}" ${SMS_USER[$FATAL]}
		run_mark=0
        fi

        if [ ${impression_mark} -ne 1 ]
        then
                ${WARNING_BIN} "article_gmp_overseas_no_data_:_impression_${curr_date}_${curr_hour}${curr_min}" ${SMS_USER[$FATAL]}
		run_mark=0
        fi

        if [ ${request_mark} -ne 1 ]
        then
                ${WARNING_BIN} "article_gmp_overseas_no_data_:_request_${curr_date}_${curr_hour}${curr_min}" ${SMS_USER[$FATAL]}
                run_mark=0
        fi

        local curr_time=${curr_date}${curr_hour}${curr_min}

	if [ ${run_mark} -eq 1 ]
	then
		mkdir -p ${LOCAL_WAITING_RUNNING_TASK_POOL_PATH}
		echo "${click_path}" > ${LOCAL_WAITING_RUNNING_TASK_POOL_PATH}/${curr_time}
		##echo "${impression_path}" >> ${LOCAL_WAITING_RUNNING_TASK_POOL_PATH}/${curr_time}
		echo "${request_path}" >> ${LOCAL_WAITING_RUNNING_TASK_POOL_PATH}/${curr_time}
	fi

	return 0
}


submit_task
