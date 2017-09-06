#!/bin/bash

function prepare()
{
    cd "$( dirname "${BASH_SOURCE[0]}" )"
    source ../conf/project.conf

    if [ -z "${JAVA_HOME}" ]
    then
        echo "JAVA_HOME is not set" >&2
        exit 1
    fi
}

function get_uid()
{
    global_uid=01011703091132175201000215526203
    hadoop fs -cat ${CLUSTER_CLICK_REFORMAT_PATH} > tmp.list
    global_uid=`tail -1 tmp.list | python ${LOCAL_BIN_PATH}/parse-click-json.py`
    tail -1 tmp.list > ${LOCAL_DATA_PATH}/user-click-log.list
    rm tmp.list
}

function get_categories()
{
    rm ${LOCAL_DATA_PATH}/user-weighted-categories.list
    local uid=$1
    local version=$2
    local TASK_NAME=query-thrift-info
    local JAVA_OPTS="-XX:PermSize=64M -XX:MaxPermSize=512m"

    $JAVA_HOME/bin/java ${JAVA_OPTS} \
        -Djava.ext.dirs=${LOCAL_LIB_PATH} \
        -Dlog4j.configuration=file:${LOCAL_CONF_PATH}/log4j-${TASK_NAME}.properties \
        com.inveno.bigdata.query.data.util.thrift.ufs.GetUfsThriftData ${LOCAL_CONF_PATH}/${TASK_NAME}.properties \
        GetWeightedCategories ${uid} ${version}\
        1>${LOCAL_LOG_PATH}/${TASK_NAME}.err 2>&1 &
}

function get_tags()
{
    rm ${LOCAL_DATA_PATH}/user-weighted-tags.list
    local uid=$1
    local version=$2
    local TASK_NAME=query-thrift-info
    local JAVA_OPTS="-XX:PermSize=64M -XX:MaxPermSize=512m"

    $JAVA_HOME/bin/java ${JAVA_OPTS} \
        -Djava.ext.dirs=${LOCAL_LIB_PATH} \
        -Dlog4j.configuration=file:${LOCAL_CONF_PATH}/log4j-${TASK_NAME}.properties \
        com.inveno.bigdata.query.data.util.thrift.ufs.GetUfsThriftData ${LOCAL_CONF_PATH}/${TASK_NAME}.properties \
        GetWeightedTags ${uid} ${version}\
        1>${LOCAL_LOG_PATH}/${TASK_NAME}.err 2>&1 &
}

function get_lda_topic()
{
    rm ${LOCAL_DATA_PATH}/user-lda-topic.list
    local uid=$1
    local version=$2
    local TASK_NAME=query-thrift-info
    local JAVA_OPTS="-XX:PermSize=64M -XX:MaxPermSize=512m"

    $JAVA_HOME/bin/java ${JAVA_OPTS} \
        -Djava.ext.dirs=${LOCAL_LIB_PATH} \
        -Dlog4j.configuration=file:${LOCAL_CONF_PATH}/log4j-${TASK_NAME}.properties \
        com.inveno.bigdata.query.data.util.thrift.ufs.GetUfsThriftData ${LOCAL_CONF_PATH}/${TASK_NAME}.properties \
        GetLdaTopic ${uid} ${version}\
        1>${LOCAL_LOG_PATH}/${TASK_NAME}.err 2>&1 &
}

prepare
get_uid 
get_categories ${global_uid} v8
get_tags ${global_uid} v2
get_lda_topic ${global_uid} v2


#get_lda_topic 01011704232259095501000061608103 v2
