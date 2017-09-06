#!/bin/bash

cd "$( dirname "${BASH_SOURCE[0]}" )"
source ../conf/project.conf

if [ -z "${JAVA_HOME}" ]
then
	echo "JAVA_HOME is not set" >&2
	exit 1
fi

mkdir -p ${LOCAL_LOG_PATH}

TASK_NAME=${APP_NAME[$EXPORT_USER_FEEDBACK_1HOUR_TASK_ID]}

$JAVA_HOME/bin/java \
	-Djava.ext.dirs=${LOCAL_LIB_PATH} \
	-Dlog4j.configuration=file:${LOCAL_CONF_PATH}/log4j-${TASK_NAME}.properties \
	com.github.panhongan.util.mq.kafka.KafkaExporterService ${LOCAL_CONF_PATH}/${TASK_NAME}.properties \
	1>${LOCAL_LOG_PATH}/${TASK_NAME}.err 2>&1 &

