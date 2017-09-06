#!/bin/bash

source ../conf/project.conf
source ../conf/warning.conf
export HADOOP_HOME=/usr/lib/hadoop
export PATH=/usr/lib/jvm/jre-1.8.0-openjdk.x86_64/bin:/usr/local/bin:/bin:/usr/bin:/usr/local/sbin:/usr/sbin:/sbin:/usr/local/go/bin/:/Data/coding/golang/bin/:/usr/lib/spark//bin:/home/haifeng.huang/.local/bin

for((i=0;i<${APP_NUM};++i))
do
	app_id=`yarn application -list | grep "${APP_NAME[$i]}" | awk '{print $1}'`
	if [ -z "${app_id}" ] 
	then
		${WARNING_BIN} "${PRODUCT_LINE}: spark streaming ${APP_NAME[$i]} is not running, will restart" "${SMS_USER[$FATAL]}"
        nohup sh -x article-emotion-gmp.sh online 1>../log/article-emotion-gmp.err 2>&1 &
	fi
done
