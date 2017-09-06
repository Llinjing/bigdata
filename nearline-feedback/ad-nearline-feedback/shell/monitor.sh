#!/bin/bash

source ../conf/project.conf
source ../conf/warning.conf

for((i=0;i<${APP_NUM};++i))
do
	app_id=`yarn application -list | grep "${APP_NAME[$i]}" | awk '{print $1}'`
	if [ -z "${app_id}" ]
	then
		${WARNING_BIN} "${PRODUCT_LINE}: ${APP_NAME[$i]} is not running, service will restart !" "${SMS_USER[$FATAL]}"
		`nohup sh -x ${APP_SCRIPT[$i]}.sh 1> ../log/${APP_SCRIPT[$i]}.log 2>&1 &`
	fi
done





