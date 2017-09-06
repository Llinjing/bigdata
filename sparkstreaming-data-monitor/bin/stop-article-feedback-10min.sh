#!/bin/bash

source ../conf/project.conf

TASK_NAME=${APP_NAME[$EXPORT_ARTICLE_FEEDBACK_10MIN_TASK_ID]}

pid=`ps -ef | grep "KafkaExporter" | grep "${TASK_NAME}" | grep -v "grep" | awk '{print $2}'`
if [ -n "${pid}" ]
then
        kill -15 ${pid}

	while true
	do
		grep -a "ShutdownHook is trigged" ${LOCAL_LOG_PATH}/${TASK_NAME}.log 1>/dev/null 2>/dev/null
		if [ $? -eq 0 ]
		then
			break
		else
			sleep 2
		fi
        done
else
	echo "No such JVM process" >&2
fi
