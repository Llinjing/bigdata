#!/bin/bash

source ../conf/project.conf
source ../conf/warning.conf

for((i=0;i<${APP_NUM};++i))
do
	pid=`ps -ef | grep ${APP_NAME[$i]} | grep -v "grep" | awk '{print $2}'`
	if [ -z "${pid}" ]
	then
		${WARNING_BIN} "${PRODUCT_LINE} : ${APP_NAME[$i]} data exporter service is not running" ${SMS_USER[0]}
	fi
done
