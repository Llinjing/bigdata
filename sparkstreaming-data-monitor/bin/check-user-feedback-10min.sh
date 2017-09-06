#!/bin/bash

function check_data()
{
	local topic=$1

	source ../conf/hadoop.conf
	source ../conf/project.conf
	source ../conf/warning.conf

	mkdir -p ${LOCAL_TMP_PATH}
	mkdir -p ${LOCAL_TOPIC_DATA_PATH}

	local curr_time=`date +%Y%m%d_%H%M -d"-10 min"`
	local begin_time=`echo "${curr_time}" | awk '{
		min = sprintf("%02d", int(substr($1, 12, 2) / 10) * 10)
		print substr($1, 1, 11)""min
	}'`
	local curr_date=`echo "${curr_time}" | awk '{
		print substr($1, 1, 8)
	}'`
	local curr_hour=`echo "${curr_time}" | awk '{
		print substr($1, 10, 2)
	}'`

	local files=${LOCAL_TOPIC_DATA_PATH}/${topic}/${topic}_*_${begin_time}
	cat ${files} >${LOCAL_TMP_PATH}/${topic}.${begin_time}
		
	# no data, need warning
	if [ ! -s ${LOCAL_TMP_PATH}/${topic}.${begin_time} ]
	then
		${WARNING_BIN} "${PRODUCT_LINE} : no data : ${topic}_${begin_time}" "${SMS_USER[$FATAL]}"
	else
		upload_to_hdfs ${LOCAL_TMP_PATH}/${topic}.${begin_time} \
			${HDFS_TOPIC_DATA_PATH}/${topic}/${curr_date}/${curr_hour} ${topic}.${begin_time}
	fi

	rm -f ${LOCAL_TMP_PATH}/${topic}.${begin_time}
	rm -f ${files}

	return 0
}

function upload_to_hdfs()
{
	local data_file=$1
	local hdfs_dir=$2
	local hdfs_file=$3

	${HADOOP_MKDIR} ${hdfs_dir}
	${HADOOP_APPEND} ${data_file} ${hdfs_dir}/${hdfs_file}

        return 0
}

if [ $# -eq 0 ]
then
	echo "${BASH_SOURCE[0]} <topic>" 2>&1
	exit 1
fi

check_data $1
