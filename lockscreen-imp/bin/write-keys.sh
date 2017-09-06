#!/bin/bash

#############################
# $1 : key file
#############################

cd "$( dirname "${BASH_SOURCE[0]}" )"
source ../conf/project.conf

if [ -z "${JAVA_HOME}" ]
then
	echo "JAVA_HOME is not set" >&2
	exit 1
fi


acs_server=`grep -a "acs.server" ${LOCAL_CONF_PATH}/lockscreen-imp.properties | awk '{
    n = split($0, arr, "[=:]"); 
    sub("^[ \t]+", "", arr[2]); 
    print arr[2]
}'`

acs_port=`grep -a "acs.server" ${LOCAL_CONF_PATH}/lockscreen-imp.properties | awk '{
    n = split($0, arr, "[=:]"); 
    sub("[ \t]+$", "", arr[3]); 
    print arr[3]
}'`


mkdir -p ${LOCAL_LOG_PATH}

task_name=${TASK_NAME[$TASK_ID_WRITE_KEYS]}

JAVA_OPTS="-XX:PermSize=64M -XX:MaxPermSize=512m"

$JAVA_HOME/bin/java ${JAVA_OPTS} \
	-Djava.ext.dirs=${LOCAL_LIB_PATH} \
	-Dlog4j.configuration=file:${LOCAL_CONF_PATH}/log4j-${task_name}.properties \
	com.inveno.bigdata.lockscreen.tools.WriteKeys $1 ${acs_server} ${acs_port} \
	1>${LOCAL_LOG_PATH}/${task_name}.out 2>${LOCAL_LOG_PATH}/${task_name}.err

