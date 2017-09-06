#!/bin/bash

source ../conf/project.conf
source ../conf/warning.conf
source /etc/profile

tmp=1

if [ $# -eq 1 ]
then
	tmp=0
	echo "Uage: app_name"
fi

for((i=${tmp};i<${APP_NUM};i=i+2))
do
	echo ${APP_NAME[$i]}
	app_id=`yarn application -list | grep "${APP_NAME[$i]}" | awk '{print $1}'`
	if [ ! -z "${app_id}" ]
	then
		rm -rf ${LOCAL_TMP_PATH}/${APP_NAME[$i]}_index.html
		url=`yarn applicationattempt -list ${app_id} | grep -v cluster | grep "http" | awk '{print $4}'`
		wget -c "${url}" -O ${LOCAL_TMP_PATH}/${APP_NAME[$i]}_index.html
		msg=`cat ${LOCAL_TMP_PATH}/${APP_NAME[$i]}_index.html | grep "RUNNING"`
		if [ ! -z "${msg}" ]
		then
			${WARNING_BIN} "${PRODUCT_LINE}:${APP_NAME[$i]} is time out, please check ~!" "${SMS_USER[$FATAL]}"
		fi
	fi
done

