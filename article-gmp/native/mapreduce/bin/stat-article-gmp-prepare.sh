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

prepare
