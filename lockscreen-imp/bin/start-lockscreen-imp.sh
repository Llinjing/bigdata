#!/bin/bash

cd "$( dirname "${BASH_SOURCE[0]}" )"
source ../conf/project.conf

if [ -z "${JAVA_HOME}" ]
then
	echo "JAVA_HOME is not set" >&2
	exit 1
fi

mkdir -p ${LOCAL_LOG_PATH}

task_name=${TASK_NAME[$TASK_ID_LOCKSCREEN_IMP]}

JAVA_OPTS="-XX:PermSize=64M -XX:MaxPermSize=512m"

$JAVA_HOME/bin/java ${JAVA_OPTS} \
	-Djava.ext.dirs=${LOCAL_LIB_PATH} \
	-Dlog4j.configuration=file:${LOCAL_CONF_PATH}/log4j-${task_name}.properties \
	com.inveno.bigdata.lockscreen.LockScreenService ${LOCAL_CONF_PATH}/${task_name}.properties \
	1>${LOCAL_LOG_PATH}/${task_name}.err 2>&1 &
