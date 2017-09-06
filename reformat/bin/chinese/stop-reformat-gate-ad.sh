#!/bin/bash

source ../conf/project.conf

TASK_NAME=${REFORMAT_TASK_NAME[$REFORMAT_GATE_AD_TASK_ID]}

pid=`ps -ef | grep "ReformatService" | grep "${TASK_NAME}" | grep -v "grep" | awk '{print $2}'`
if [ -n "${pid}" ]
then
	kill -15 ${pid}
        
	curr_time=`date +%Y-%m-%d_%H:%M:%S`

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
