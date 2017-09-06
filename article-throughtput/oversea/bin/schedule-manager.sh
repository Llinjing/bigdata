#!/bin/bash

function schedule()
{
	source ../conf/project.conf

	mkdir -p ${LOCAL_DATA_PATH}
	mkdir -p ${LOCAL_LOG_PATH}

	if [ -s ${LOCAL_DATA_PATH}/running-flg ]
	then
		exit 0
	fi

	date +%Y%m%d%H%M >${LOCAL_DATA_PATH}/running-flg

	sh -x ${LOCAL_BIN_PATH}/schedule-task.sh 1>${LOCAL_LOG_PATH}/schedule-task.err 2>&1

	rm -f ${LOCAL_DATA_PATH}/running-flg

	return 0
}


schedule

