#!/bin/bash

#-------------------------
# huanghaifeng
# 2016-12-26
# get data to upload
#-------------------------

function prepare()
{
    source ../conf/project.conf
    source ../conf/warning.conf
}

function get_data()
{
    #cp ${CICADA_SCORE_LOG_PATH} ${LOCAL_DATA_PATH}/uploading/
    #cp ${CICADA_FEATURE_LOG_PATH} ${LOCAL_DATA_PATH}/uploading/
    local time_stamp=`date +%Y%m%d%H%M%S`
    echo ${CICADA_SCORE_LOG_PATH} > ${LOCAL_DATA_PATH}/uploading/cicada_score.${time_stamp}
    echo ${CICADA_FEATURE_LOG_PATH} > ${LOCAL_DATA_PATH}/uploading/cicada_feature.${time_stamp}
}

prepare
get_data
