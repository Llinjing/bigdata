#!/bin/bash

source ../conf/project.conf
source ../conf/warning.conf

for((i=0;i<${APP_NUM};++i))
do
	app_id=`yarn application -list | grep "${APP_NAME[$i]}" | awk '{print $1}'`
	if [ ! -z "${app_id}" ]
	then
		url=`yarn applicationattempt -list ${app_id} | grep "http" | grep -v cluster | awk '{print $4}'`
		echo ${url}

		rm ${LOCAL_DATA_PATH}/${APP_NAME[$i]}-application-index.html
		wget -c "${url}" -O ${LOCAL_DATA_PATH}/${APP_NAME[$i]}-application-index.html
		msg=`grep "RUNNING" ${LOCAL_DATA_PATH}/${APP_NAME[$i]}-application-index.html`
		if [ ! -z "${msg}" ]
		then
			${WARNING_BIN} "${PRODUCT_LINE}:${APP_NAME[$i]} is running, but not finish current tasts, maybe blocked, please check~" "${SMS_USER[$ERROR]}"
		else
			## check data
			minute=`date +%M`
			time_interval=${minute:1:1}
			sh -x ${LOCAL_BIN_PATH}/job-exceptions-monitor.sh ${time_interval} "1"${time_interval} 1>../log/job-exceptions-monitor.log 2>&1
		fi
	fi
done
