#!/bin/bash

source ../conf/project.conf
source ../conf/warning.conf
export HADOOP_HOME=/usr/lib/hadoop
export PATH=/usr/local/sbin:/sbin:/bin:/usr/sbin:/usr/bin:/usr/lib/hadoop/bin:/usr/lib/hive//bin:/usr/lib/spark/bin:/usr/lib/hadoop-yarn/bin:/usr/local/zookeeper/bin:/root/bin

function check_job()
{
	app_id=`yarn application -list | grep "${APP_NAME[0]}" | awk '{print $1}'`
    if [ -z "${app_id}" ]
    then
        return 1
    else
        return 2
    fi
}

function restart_job()
{
    ${WARNING_BIN} "${PRODUCT_LINE}: spark streaming ${APP_NAME[$i]} is not running, will restart" "${SMS_USER[$ERROR]}"
    nohup sh -x article-gmp.sh online 1>../log/article-gmp.err 2>&1 &
}

function monitor()
{
    for ((i=1; i<=3; i++)) 
    do  
        echo ${i}
        check_job
        if [ $? -gt 1 ] ; then
            return
        fi
        sleep 30s
    done

    restart_job
}

monitor
