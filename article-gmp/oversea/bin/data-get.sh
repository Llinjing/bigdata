#!/bin/sh

# author: huanghaifeng

function prepare()
{
	source ../conf/project.conf
	source ../conf/mysql.conf
	source ../conf/warning.conf
}


function get_data_info()
{
	export HADOOP_HOME=/usr/lib/hadoop
	export PATH=/usr/local/sbin:/sbin:/bin:/usr/sbin:/usr/bin:/usr/lib/hadoop/bin:/usr/lib/hive//bin:/usr/lib/spark/bin:/usr/lib/hadoop-yarn/bin:/usr/local/zookeeper/bin:/root/bin

    local curr_time=`date +%Y%m%d%H%M -d"-${1} min"`  
    local curr_date=`echo "${curr_time}" | awk '{
            print substr($1, 1, 8)
    }'`
    local curr_hour=`echo "${curr_time}" | awk '{
            print substr($1, 9, 2)
    }'`
    local curr_min=`echo "${curr_time}" | awk '{
            print sprintf("%02d", int(substr($1, 11, 2) / 10) * 10)
    }'`
    local path=s3://bigdata-east/inveno-data/format-data/topic/impression-reformat/${curr_date}/${curr_hour}/impression-reformat.${curr_date}_${curr_hour}${curr_min}*
	echo ${path}
	hadoop fs -cat ${path} > ${LOCAL_DATA_PATH}/impression-reformat.list
	cat ${LOCAL_DATA_PATH}/impression-reformat.list | python ${LOCAL_BIN_PATH}/data-monitor-impression-reformat-parse.py 1>>${LOCAL_DATA_PATH}/article-info.list 2>${LOCAL_LOG_PATH}/article-info.err
    sort ${LOCAL_DATA_PATH}/article-info.list | uniq | sort -o ${LOCAL_DATA_PATH}/article-info.list
}


function get_more_data()
{
    for ((i=1; i<=100; i++))
    do
        get_data_info $[i*10]
    done
}


prepare
get_more_data 
