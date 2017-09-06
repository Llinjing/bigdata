#!/bin/bash

#!/bin/bash

function prepare()
{
    source ../conf/project.conf
    source ../conf/warning.conf

    mkdir -p ${LOCAL_ACS_FAILED_DATA_PATH}
    mkdir -p ${LOCAL_ACS_FAILED_HISTORY_DATA_PATH}
    mkdir -p ${LOCAL_TMP_PATH}

    return 0
}

function monitor()
{
    local curr_date=`date +%Y%m%d -d"-1 min"`
    local curr_min=`date +%Y%m%d_%H%M -d"-1 min"`
    local files=${LOCAL_ACS_FAILED_DATA_PATH}/impression-reformat_*_${curr_min}
    local merge_file=${LOCAL_TMP_PATH}/failed.list

    ## rewrite to acs
    cat ${files} >${merge_file}
    sh -x ${LOCAL_BIN_PATH}/write-keys.sh ${merge_file}
    if [ $? -eq 0 ]
    then
        rm -f ${files}
    fi

    ## sum to daily history
    cat ${LOCAL_TMP_PATH}/failed.list | awk 'BEGIN{
       OFS = "\t"
    }{
        print "'${curr_min}'", $0
    }' >>${LOCAL_ACS_FAILED_HISTORY_DATA_PATH}/history.${curr_date}
    
    return 0
}


prepare

monitor
