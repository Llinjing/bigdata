#!/bin/bash

source ../conf/project.conf
source ../conf/warning.conf

for((i=0;i<${APP_NUM};++i))
do
	app_id=`yarn application -list | grep "${APP_NAME[$i]}" | awk '{print $1}'`
	if [ -z "${app_id}" ] 
	then
		${WARNING_BIN} "${PRODUCT_LINE}: spark streaming ${APP_NAME[$i]} is not running" "${SMS_USER[$FATAL]}"
	fi
done
