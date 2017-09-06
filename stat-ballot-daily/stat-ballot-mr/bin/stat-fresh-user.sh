#!/bin/bash

function prepare()
{
    source ../conf/mysql.conf
    source ../conf/project.conf
}

function stat()
{
    mysql_deal="mysql -h${MYSQL_HOST[3]} -u${MYSQL_USER[3]} -p${MYSQL_PWD[3]}" 
    ${mysql_deal} -e "select distinct uid from zhizi_user.daily_fresh_user where epoch_day= floor(unix_timestamp()/(24*3600)) -1" > ${LOCAL_DATA_PATH}/fresh_user.list

    ${HADOOP_CAT} ${MR_STAT_PROFILE_USER_OUTPUT_PATH}/* > ${LOCAL_DATA_PATH}/profile_user.list  
    cat ${LOCAL_DATA_PATH}/profile_user.list ${LOCAL_DATA_PATH}/fresh_user.list > ${LOCAL_DATA_PATH}/tmp_fresh_user.list
    cat ${LOCAL_DATA_PATH}/tmp_fresh_user.list|sort -u > ${LOCAL_DATA_PATH}/fresh_user.list
}

prepare

stat
