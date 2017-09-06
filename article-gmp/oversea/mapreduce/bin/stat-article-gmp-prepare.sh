#!/bin/bash

function prepare()
{
	source ../conf/project.conf
	source ../conf/hadoop.conf
	source ../conf/warning.conf
	source ../conf/decay.conf

	${HADOOP_MKDIR} ${MR_WORK_PATH}
	${HADOOP_MKDIR} ${MR_OUTPUT_PATH}
	${HADOOP_MKDIR} ${MR_INPUT_PATH}
	
	mkdir -p ${LOCAL_DATA_PATH}
	mkdir -p ${LOCAL_LOG_PATH}

	return 0
}

function mv_old_monitor_file()
{
        ## KEEP HISTORY DATA
        local curr_time=`date +%Y%m%d%H%M -d"-10 min"`
        local curr_date=`echo "${curr_time}" | awk '{
               print substr($1, 1, 8)
        }'`
        local curr_hour=`echo "${curr_time}" | awk '{
               print substr($1, 9, 2)
        }'`
        local curr_min=`echo "${curr_time}" | awk '{
               print sprintf("%02d", int(substr($1, 11, 2) / 10) * 10)
        }'`
        local monitor_time=${curr_date}${curr_hour}${curr_min}
        mv ${LOCAL_DATA_PATH}/article-gmp-monitor-high-gmp.list ${LOCAL_DATA_PATH}/monitor-data/article-gmp-monitor-high-gmp.list_${monitor_time}
}


prepare
mv_old_monitor_file
