#!/bin/bash

#-------------------------
# huanghaifeng
# 2016-12-24
#-------------------------

function prepare()
{
    source ../conf/project.conf
    source ../conf/hadoop.conf
    time_stamp=`date +%s`
    tmp_files_dir=/tmp/put-file-to-cluster-${time_stamp}
}

function split_file()
{
    mkdir -p ${tmp_files_dir}
    split -b 256m ${local_file_path} ${tmp_files_dir}/split_
}


function put_file_to_cluster()
{
    for i in `ls ${tmp_files_dir}/`
    do
        echo ${tmp_files_dir}/${i}
        ${HADOOP_APPEND} ${tmp_files_dir}/${i} ${cluster_file_path}
        date
        echo "sleep 3s ....."
        sleep 3s
    done
    rm -rf ${tmp_files_dir}
}

local_file_path=$1
cluster_file_path=$2
if [ -z ${local_file_path} ] ; then
    echo "local_file_path is null ;    ----    usage: <local_file_path> <cluster_file_path>"
    exit
elif [ -z ${cluster_file_path} ] ; then
    echo "cluster_file_path is null ;    ----    usage: <local_file_path> <cluster_file_path>"
    exit
fi

if [ -s ${local_file_path}  ] ; then
    prepare

    echo "start split file"
    start_time=`date +%s`
    split_file
    finish_split_file_time=`date +%s`
    echo "finish split file"

    echo "start put file"
    put_file_to_cluster
    finish_put_file_time=`date +%s`
    echo "finish all put tasts"

    split_time=$[finish_split_file_time-start_time]
    put_time=$[finish_put_file_time-finish_split_file_time]
    all_time=$[finish_put_file_time-start_time]
    echo "split file consume time "${split_time}" s"
    echo "put file consume time "${put_time}" s"
    echo "finish all tasts consume time "${all_time}" s"
else
    echo "local_file is null ;    ----    usage: <local_file_path> <cluster_file_path>"
    exit
fi

