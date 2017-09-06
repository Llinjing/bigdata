#!/bin/bash

source ../conf/project.conf

TASK_NAME=${KAFKA_SYNC_TASK_NAME[$KAFKA_SYNC_REQUEST_NEW_TASK_ID]}

pid=`ps -ef | grep "KafkaSyncService" | grep "${TASK_NAME}" | grep -v "grep" | awk '{print $2}'`
if [ -n "${pid}" ]
then
	kill -15 ${pid}
        
	curr_time=`date +%Y-%m-%d_%H:%M:%S -d"-0 hour"`

	while true	
	do
		event_time=`tail -10000 ${LOCAL_LOG_PATH}/${TASK_NAME}.log | grep -a "ShutdownHook is trigged" 2>/dev/null | 
		tail -1 | awk '{
			print $1"_"$2
		}'`
		if [ "${event_time}" == "${curr_time}" -o "${event_time}" \> "${curr_time}" ]
		then
			break
 		else
			sleep 2
		fi
	done
else
	echo "No such JVM process" >&2
fi
