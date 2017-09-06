#!/bin/bash

source ../conf/project.conf
source ../conf/warning.conf

function check_job()
{
    app_id=`yarn application -list | grep "${APP_NAME[$1]}" | awk '{print $1}'`
    if [ -z "${app_id}" ]
    then
        return 1
    else
        return 2
    fi
}

function restart_job()
{
    ${WARNING_BIN} "${PRODUCT_LINE}:_${APP_NAME[$1]}_is_not_running, SERVICE WILL RESTART !" "${SMS_USER[$FATAL]}"
    `nohup sh -x ${APP_SCRIPT[$1]}.sh 1> ../log/${APP_SCRIPT[$1]}.log 2>&1 &`
}

function monitor()
{
    for((i=1; i<=2; i++))
    do
        check_job $1
        if [ $? -gt 1 ]
        then
            return
        fi
        sleep 30
    done

    restart_job $1
}

for((m=0;m<${APP_NUM};++m))
do
    echo $m
    monitor $m
done
