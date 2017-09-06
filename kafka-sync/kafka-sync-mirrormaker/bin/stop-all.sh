#!/bin/bash

function stop()
{
    source ../conf/project.conf

    for((i=0;i<${TOPIC_NUM};++i))
    do
        sh -x ${LOCAL_BIN_PATH}/stop-one.sh ${TOPIC_NAME[$i]}
    done

    return 0
}


stop
