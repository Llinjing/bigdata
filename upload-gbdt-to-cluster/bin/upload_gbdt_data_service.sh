#!/bin/bash

#-------------------------
# huanghaifeng
# 2016-12-26
#-------------------------

function prepare()
{
    source ../conf/project.conf
    source ../conf/warning.conf
    source ../conf/hadoop.conf
}

function service()
{
    while true 
    do 
    {
        for log_path in `ls ${LOCAL_DATA_PATH}/uploading/`
        do
            source ../conf/project.conf
            absolute_log_path=${LOCAL_DATA_PATH}/uploading/${log_path}
            upload_log_file_path=`cat ${absolute_log_path}`
            echo ${upload_log_file_path}

            log_file_type=`echo ${upload_log_file_path} | grep cicada_feature`
            cluster_file_path=""
            if [ ${log_file_type} ] ; then
                echo "upload cicada_feature -- "
                ${HADOOP_MKDIR} ${CICADA_FEATURE_CLUSTER_PATH}
                cluster_file_path=${CICADA_FEATURE_CLUSTER_PATH}/INV.cicada_feature_${TODAY}${HOUR}.log_${LOG_SERVER_IP}
            else
                echo "upload cicada_score -- "
                ${HADOOP_MKDIR} ${CICADA_SCORE_CLUSTER_PATH}
                cluster_file_path=${CICADA_SCORE_CLUSTER_PATH}/INV.cicada_score_${TODAY}${HOUR}.log_${LOG_SERVER_IP}
            fi
            #sh -x ${LOCAL_BIN_PATH}/upload_gbdt_data_executor.sh ${upload_log_file_path} ${cluster_file_path}
            sh -x ${LOCAL_BIN_PATH}/upload_gbdt_data_executor.sh ${upload_log_file_path} ${cluster_file_path} 1>${LOCAL_LOG_PATH}/upload_gbdt_data_executor.log 2>&1
            rm ${absolute_log_path} 
        done
        sleep 60s
    }
    done
}

prepare
service
