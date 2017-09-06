#!/bin/bash

function prepare()
{
    source ../conf/project.conf
    source ../conf/warning.conf
    source ../conf/hadoop.conf

    mkdir -p ${LOCAL_DATA_PATH}
    mkdir -p ${LOCAL_LOG_PATH}
}

function write_into_redis()
{

    $JAVA_HOME/bin/java \
        -Djava.ext.dirs=${LOCAL_LIB_PATH} \
        -Dlog4j.configuration=file:${LOCAL_CONF_PATH}/log4j-article-gmp-impression-sync-service.properties \
        com.inveno.bigdata.sync.ArticleGMPImpressionSyncService ${LOCAL_CONF_PATH}/article-gmp-impression-sync-service.properties ${LOCAL_DATA_PATH}/article-gmp-impression.list \
        1>${LOCAL_LOG_PATH}/article-gmp-impression-sync-service-redis.log 2>&1

    return 0
}

function article_gmp_impression_sync_service()
{
    local time_stamp=`date +%s`
    time_stamp_current=$[time_stamp-${current_interval}*60]
    time_stamp_current=${time_stamp_current:0:8}
    ${HADOOP_CAT} ${S3_ARTICLE_GMP_PATH}/redis-${time_stamp_current}00000/part* | grep -v noticias > ${LOCAL_DATA_PATH}/article-gmp-impression.list
    if [ -s ${LOCAL_DATA_PATH}/article-gmp-impression.list ] ; then
        write_into_redis
    else
        ${WARNING_BIN} "${PRODUCT_LINE}: get ${S3_ARTICLE_GMP_PATH}/redis-${time_stamp_current}00000 failed" "${SMS_USER[$INFO]}"
    fi

    return 0
}

current_interval=$1
prepare
article_gmp_impression_sync_service
