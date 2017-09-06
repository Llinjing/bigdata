#!/bin/bash

#-------------------------
# huanghaifeng
# 2016-12-26
#-------------------------

function prepare()
{
    source ../conf/project.conf
    source ../conf/warning.conf
}

function monitor()
{
    local service_pid=`ps -ef | grep ${SERVICE_NAME} | grep -v "grep" | grep -v tail | awk '{print $2}' | head -1`
    if [ ${service_pid} ] ; then
        echo ${SERVICE_NAME}" is running, everything is ok"
    else
        python ${LOCAL_BIN_PATH}/zabbix_sendsms.py "${LOG_SERVER_IP} ${SERVICE_NAME} is not running, will restart" ${SMS_USER[ERROR]}
        nohup sh -x ${LOCAL_BIN_PATH}/upload_gbdt_data_service.sh 1>../log/upload_gbdt_data_service.log 2>&1 & 
    fi
}

prepare
monitor
