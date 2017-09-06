#!/bin/bash

function prepare()
{
        source ../conf/project.conf
        source ../conf/warning.conf

        return 0
}

function monitor_export_service()
{
        for((i=0;i<${EXPORT_TASK_NUM};++i))
        do
                local pid=`ps -ef | grep ${EXPORT_TASK_NAME[$i]} | grep -v "grep" | awk '{print $2}'`
                if [ -z "${pid}" ]
                then
                        ${WARNING_BIN} "${PRODUCT_LINE} : ${EXPORT_TASK_NAME[$i]} is not running" ${SMS_USER[$FATAL]}
                fi
        done

        return 0
}

function check_data_blocked()
{
        for((i=0;i<${TOPIC_NUM};++i))
        do
                local file_num=`ls -l ${LOCAL_TOPIC_DATA_PATH}/${TOPIC[$i]} | awk '{
                        if (NF == 9)
                        {
                                dict[substr($9, length($9) - 13 + 1, 13)]
                        }
                }END{
                        print length(dict)
                }'`

                if [ $file_num -gt 2 ]
                then
                        ${WARNING_BIN} "${PRODUCT_LINE} : ${TOPIC[$i]} data to hdfs blocked" ${SMS_USER[$FATAL]}
                elif [ $file_num -le 0 ]
                then
                        ${WARNING_BIN} "${PRODUCT_LINE} : ${TOPIC[$i]} no data" ${SMS_USER[$FATAL]}
                fi
        done

        return 0
}

prepare

monitor_export_service

check_data_blocked