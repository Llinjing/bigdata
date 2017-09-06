#!/bin/bash

source ../conf/project.conf
source ../conf/warning.conf
export HADOOP_HOME=/usr/lib/hadoop
export PATH=/usr/local/sbin:/sbin:/bin:/usr/sbin:/usr/bin:/usr/lib/hadoop/bin:/usr/lib/hive//bin:/usr/lib/spark/bin:/usr/lib/hadoop-yarn/bin:/usr/local/zookeeper/bin:/root/bin

for((i=0;i<${APP_NUM};++i))
do
	app_id=`yarn application -list | grep "${APP_NAME[$i]}" | awk '{print $1}'`
	if [ -z "${app_id}" ] 
	then
		${WARNING_BIN} "${PRODUCT_LINE}: spark streaming ${APP_NAME[$i]} is not running, will restart" "${SMS_USER[$ERROR]}"
		nohup sh -x article-gmp.sh online 1>../log/article-gmp.err 2>&1 & 
	fi
done
