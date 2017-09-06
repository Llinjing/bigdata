#!/bin/shell

# author: huanghaifeng

function prepare()
{
	source ../conf/project.conf
	source ../conf/warning.conf
}

function monitor()
{
	local time_stamp=`date +%s`
	time_stamp_current=$[time_stamp-${current_interval}*60]
	time_stamp_current=${time_stamp_current:0:8}
	time_stamp_last=$[time_stamp-${last_time_interval}*60]
	time_stamp_last=${time_stamp_last:0:8}
    hadoop fs -cat ${CLUSTER_DATA_PATH}/total-decay-feedback/total-decay-feedback-${time_stamp_last}00000/part* > ${LOCAL_DATA_PATH}/send_data_to_kafka.list
    hadoop fs -cat ${CLUSTER_DEBUG_PATH}/history-total-decay-feedback-${time_stamp_current}00000/part* > ${LOCAL_DATA_PATH}/get_history_from_kafka.list

	local send=`cat ${LOCAL_DATA_PATH}/send_data_to_kafka.list | wc -l`
	local get=`cat ${LOCAL_DATA_PATH}/get_history_from_kafka.list | wc -l`

    if [ ${send} -gt ${get} ] ; then
        ${WARNING_BIN} "${PRODUCT_LINE}: ${APP_NAME[0]} data exceptions[send more then receive], last time send ${send} lines, this time receive ${get} lines" "${SMS_USER[$ERROR]}"
    fi

    if [ ${send} -eq 0 ] ; then
        ${WARNING_BIN} "${PRODUCT_LINE}: ${APP_NAME[0]} data exceptions[send 0], last time send ${send} lines, this time receive ${get} lines" "${SMS_USER[$ERROR]}"
    fi

}

current_interval=$1
last_time_interval=$2

prepare
monitor
