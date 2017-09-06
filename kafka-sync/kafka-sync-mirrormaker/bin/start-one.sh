#!/bin/bash

function prepare()
{
    source ../conf/project.conf

    mkdir -p ${LOCAL_LOG_PATH}

    return 0
}

function start_sync()
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
        if [ ! -f ${PID_FILE[$i]} ]
        then
            ## crete topic
            ${KAFKA_HOME}/bin/kafka-topics.sh \
                --zookeeper ${DST_ZK_LIST} \
                --create \
                --topic ${TOPIC_NAME[$i]} \
                --partitions ${TOPIC_PARTITIONS[$i]} \
                --replication-factor 2

            ## sync
            nohup ${KAFKA_HOME}/bin/kafka-run-class.sh kafka.tools.MirrorMaker \
                --consumer.config ${CONSUMER_CONFIG[$i]} \
                --producer.config ${PRODUCER_CONFIG[$i]} \
                --num.streams ${TOPIC_PARTITIONS[$i]} \
                --whitelist  ${TOPIC_NAME[$i]} \
                1>${LOG_FILE[$i]} 2>&1 &

            echo $! >${PID_FILE[$i]}
        else
            echo "service is running" >&2
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

start_sync $1
