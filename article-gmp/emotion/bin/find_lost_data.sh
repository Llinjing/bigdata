#!/bin/bash

source ../conf/project.conf

awk 'BEGIN{
	FS = ",{"
	OFS = "\t"
}{
	if (ARGIND == 1)
	{
                tmp = substr($2, 0, length($2)-1)
		get["{"tmp]
	}
	else if(ARGIND == 2)
	{
		send[$0]
	}
}END{
	for (key in send)
	{
		if (key in get)
		{
			count++
		}
		else
		{
			print key
		}
	}
}' ${LOCAL_DATA_PATH}/get_history_from_kafka.list ${LOCAL_DATA_PATH}/send_data_to_kafka.list > ${LOCAL_DATA_PATH}/kafka_lost_data.list

cat ${LOCAL_DATA_PATH}/kafka_lost_data.list | python ${LOCAL_BIN_PATH}/parse-lost-data.py > ${LOCAL_DATA_PATH}/kafka_lost_data_decay.list
cat ${LOCAL_DATA_PATH}/kafka_lost_data_decay.list | ${KAFKA_CONSOLE_PRODUCER} --broker-list ${BROKER_LIST} -topic ${GMP_TOPIC}
