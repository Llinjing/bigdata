#!/bin/bash

function submit_task()
{
    source ../conf/project.conf
    source ../conf/hadoop.conf
    source ../conf/warning.conf

    local curr_time=$1
    local curr_date=`echo "${curr_time}" | awk '{
        print substr($1, 1, 8)
    }'`
    local curr_hour=`echo "${curr_time}" | awk '{
        print substr($1, 9, 2)
    }'`
    local curr_min=`echo "${curr_time}" | awk '{
        print sprintf("%02d", int(substr($1, 11, 2) / 10) * 10)
    }'`

    local request_path=${HDFS_REQUEST_PATH}/${curr_date}/${curr_hour}/*.${curr_date}_${curr_hour}${curr_min}_*
    local impression_path=${HDFS_IMPRESSION_PATH}/${curr_date}/${curr_hour}/*.${curr_date}_${curr_hour}${curr_min}_*

    local run_mark=1
    ${HADOOP_TEST} ${request_path}
    if [ $? -ne 0 ]
    then
        ${WARNING_BIN} "article_throughput_overseas_us_east_no_data_:_request_${curr_date}_${curr_hour}${curr_min}" ${SMS_USER[$INFO]}
        run_mark=0
    fi
    ${HADOOP_TEST} ${impression_path}
    if [ $? -ne 0 ]
    then
        ${WARNING_BIN} "article_throughput_overseas_us_east_no_data_:_impression_${curr_date}_${curr_hour}${curr_min}" ${SMS_USER[$INFO]}
        run_mark=0
    fi

    if [ ${run_mark} -eq 1 ]
    then
        mkdir -p ${LOCAL_WAITING_RUNNING_TASK_POOL_PATH}
        echo "${request_path}" >> ${LOCAL_WAITING_RUNNING_TASK_POOL_PATH}/${curr_date}${curr_hour}${curr_min}
        echo "${impression_path}" >> ${LOCAL_WAITING_RUNNING_TASK_POOL_PATH}/${curr_date}${curr_hour}${curr_min}
    fi

    return 0
}

time_stamp=`date +%s`
start_time=$[10*6*24*0+10*6*0+10*4+20]

for((i=${start_time}; 1<2; i-=10))
do
    time_stamp_tmp=$[time_stamp-60*i]
    curr_time_tmp=`date -d @${time_stamp_tmp} "+%Y%m%d%H%M"`
    submit_task ${curr_time_tmp}
    sh -x schedule-manager.sh 1>../log/schedule-manager.err 2>&1

    curr_time_stamp=`date +%s -d"-20 min"`
    if [ ${time_stamp_tmp} -ge ${curr_time_stamp} ] ; then
        break
    fi
done


