#!/bin/bash

function prepare()
{
    source ../conf/project.conf

    mkdir -p ${LOCAL_LOG_PATH}

    return 0
}

function start()
{
    for((i=0;i<${TOPIC_NUM};++i))
    do
        sh -x ${LOCAL_BIN_PATH}/start-one.sh ${TOPIC_NAME[$i]}
    done

    return 0
}


prepare

start
