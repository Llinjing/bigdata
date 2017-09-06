#!/bin/bash
function prepare()
{
    source ../conf/mysql.conf
    source ../conf/project.conf
    timestamp_day=${YESTERDAY:0:4}'-'${YESTERDAY:4:2}'-'${YESTERDAY:6:2}" 00:00:00"
}

function stat_result()
{
    mysql_deal="mysql -h${MYSQL_HOST[4]} -P${MYSQL_PORT[4]} -u${MYSQL_USER[4]} -p${MYSQL_PWD[4]} -Ddashboard"
    cnt=`${mysql_deal} -N -e "select count(1) from t_daily_h5_link_share_uv where timestamp='${timestamp_day}'"`
    if [ $cnt -eq 0 ]
    then
       ${RNING_BIN} "${timestamp_day},h5_link_share_uv no data,please check out" ${PHONENUMS}
    fi
}

prepare

stat_result
