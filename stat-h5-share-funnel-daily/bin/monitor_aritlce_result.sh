#!/bin/bash
function prepare()
{
    source ../conf/mysql.conf
    source ../conf/project.conf
    timestamp_hour=${TODAY:0:4}'-'${TODAY:4:2}'-'${TODAY:6:2}" ${HOUR}:00:00"
}

function stat_result()
{
    mysql_deal="mysql -h${MYSQL_HOST[4]} -P${MYSQL_PORT[4]} -u${MYSQL_USER[4]} -p${MYSQL_PWD[4]} -Ddashboard"
    cnt=`${mysql_deal} -N -e "select count(1) from t_daily_h5_link_share_pv where timestamp_hour='${timestamp_hour}'"`
    if [ $cnt -eq 0 ]
    then
       ${RNING_BIN} "${timestamp_hour},h5_link_share_pv no data,please check out" ${PHONENUMS}
    fi
}

prepare

stat_result
