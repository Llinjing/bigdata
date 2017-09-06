#!/bin/bash

function prepare()
{
    source ../conf/project.conf
    source ../conf/warning.conf

    mkdir -p ${LOCAL_DATA_PATH}
    mkdir -p ${LOCAL_LOG_PATH}

    return 0
}

function write_to_redis()
{
    local data_file=$1
    
    $JAVA_HOME/bin/java \
        -Djava.ext.dirs=${LOCAL_LIB_PATH} \
        -Dlog4j.configuration=file:${LOCAL_CONF_PATH}/log4j-article-gmp-request-sync-service.properties \
        com.inveno.bigdata.sync.ArticleGMPRequestSyncService ${LOCAL_CONF_PATH}/article-gmp-request-sync-service.properties \
        ${data_file} 1>${LOCAL_LOG_PATH}/article-gmp-request-sync-service-redis.err 2>&1

    return 0
}

function article_gmp_request_sync_service()
{
    if [ ! -s ${LOCAL_DATA_PATH}/article-gmp-request.list ] ; then
        echo "nothing to do"
        return 0
    fi

    if [ -s ${LOCAL_DATA_PATH}/article-gmp-request.list_running ] ; then
        echo "last time still running"
    else
        mv ${LOCAL_DATA_PATH}/article-gmp-request.list ${LOCAL_DATA_PATH}/article-gmp-request.list_running
        write_to_redis ${LOCAL_DATA_PATH}/article-gmp-request.list_running
        rm -f ${LOCAL_DATA_PATH}/article-gmp-request.list_running
    fi

    return 0
}

prepare
article_gmp_request_sync_service
