#!/bin/bash

function prepare()
{
    source ../conf/project.conf
    source ../conf/warning.conf

    return 0
}

function monitor_lost_data()
{
    for((i=0; i<${KAFKA_SYNC_TASK_NUM}; ++i))
    do
        for tmp_file in `ls ${LOCAL_LOST_DATA_PATH}/${KAFKA_SYNC_DST_TOPIC[$i]}`
        do
            cat ${LOCAL_LOST_DATA_PATH}/${KAFKA_SYNC_DST_TOPIC[$i]}/${tmp_file} | ${KAFKA_CONSOLE_PRODUCER_BIN} --broker-list ${KAFKA_BROKER_LIST} --topic ${KAFKA_SYNC_DST_TOPIC[$i]}
            mkdir -p ${LOCAL_LOST_DATA_BAK_PATH}/${KAFKA_SYNC_DST_TOPIC[$i]}
            mv ${LOCAL_LOST_DATA_PATH}/${KAFKA_SYNC_DST_TOPIC[$i]}/${tmp_file} ${LOCAL_LOST_DATA_BAK_PATH}/${KAFKA_SYNC_DST_TOPIC[$i]}/

            count=`cat ${LOCAL_LOST_DATA_BAK_PATH}/${KAFKA_SYNC_DST_TOPIC[$i]}/${tmp_file} | wc -l`
            if [ ${count} -gt 1000 ] ; then
                 ${WARNING_BIN} "${PRODUCT_LINE} [lost too much data] ${tmp_file} count is ${count}" ${SMS_USER[$INFO]}
            fi
        done
    done

    return 0
}

prepare
monitor_lost_data
