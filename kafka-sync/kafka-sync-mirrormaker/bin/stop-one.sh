#!/bin/bash

function prepare()
{
    source ../conf/project.conf

    mkdir -p ${LOCAL_LOG_PATH}

    return 0
}

function stop_sync()
{
    local topic=$1

    local i=0
    for((i=0;i<${TOPIC_NUM};++i))
    do
        if [ "${TOPIC_NAME[$i]}" == "${topic}" ]
        then
            break
        fi
    done

    if [ $i -lt ${TOPIC_NUM} ]
    then
        if [ -f ${PID_FILE[$i]} ]
        then
            read -r pid < ${PID_FILE[$i]}
            kill -15 ${pid}
            rm -f ${PID_FILE[$i]}
        else
            echo "service is not running" >&2
        fi
    else
        echo "topic:${topic} not configured" >&2
    fi

    return 0
}


if [ $# -ne 1 ]
then
    echo "${BASH_SOURCE[0]} <topic>" >&2
    exit 1
fi


prepare

stop_sync $1
