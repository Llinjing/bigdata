#!/bin/bash

cd "$( dirname "${BASH_SOURCE[0]}" )"
source ../conf/project.conf

if [ -z "${JAVA_HOME}" ]
then
	echo "JAVA_HOME is not set" >&2
	exit 1
fi

mkdir -p ${LOCAL_LOG_PATH}

TASK_NAME=${SERVICE_TASKS[$HISTORY_QUERY_SERVICE_TASK_ID]}

JAVA_OPTS="-XX:PermSize=64M -XX:MaxPermSize=512m"
CLASSPATH=${CLASSPATH}:.:${LOCAL_LIB_PATH}/*:${HADOOP_HOME}/*:${HADOOP_HOME}/lib/*:${HIVE_HOME}/lib/*

$JAVA_HOME/bin/java ${JAVA_OPTS} \
	-Dlog4j.configuration=file:${LOCAL_CONF_PATH}/log4j-${TASK_NAME}.properties \
	-classpath .:${LOCAL_LIB_PATH}/*:/usr/lib/hadoop/*:/usr/lib/hadoop/lib/*:/usr/lib/hive/lib/* \
    com.inveno.bigdata.history.HistoryBigdataQueryService ${LOCAL_CONF_PATH}/${TASK_NAME}.properties \
	1>${LOCAL_LOG_PATH}/${TASK_NAME}.err 2>&1 &

