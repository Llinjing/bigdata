#!/bin/bash

function prepare()
{
    source ../conf/project.conf
    source ../conf/warning.conf

    return 0
}

function monitor_kafka_sync_service()
{
    for((i=0;i<${KAFKA_SYNC_TASK_NUM};++i))
    do
        local pid=`ps -ef | grep ${KAFKA_SYNC_TASK_NAME[$i]} | grep -v "grep" | awk '{print $2}'`
        if [ -z "${pid}" ]
        then
            ${WARNING_BIN} "${PRODUCT_LINE} : ${KAFKA_SYNC_TASK_NAME[$i]} is not running, will restart" ${SMS_USER[$INFO]}
            sh -x ${LOCAL_BIN_PATH}/stop-${KAFKA_SYNC_TASK_NAME[$i]}.sh
            sh -x ${LOCAL_BIN_PATH}/start-${KAFKA_SYNC_TASK_NAME[$i]}.sh
        fi
    done

    return 0
}

function monitor_kafka_queue_full()
{
    for((i=0;i<${KAFKA_SYNC_TASK_NUM};++i))
    do
        local info=`tail -10000 ../log/${KAFKA_SYNC_TASK_NAME[$i]}.log | grep -a "queue is full, need clear"`
        if [ -n "${info}" ]
        then
            ${WARNING_BIN} "${PRODUCT_LINE} : ${KAFKA_SYNC_TASK_NAME[$i]} queue is full, need clear" ${SMS_USER[$INFO]}
        fi
    done

    return 0
}

prepare
monitor_kafka_sync_service
monitor_kafka_queue_full
