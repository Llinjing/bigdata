#!/bin/bash

source ../conf/project.conf
source /etc/profile

APP_NAME=advertise-user-10min-feedback
CHECKPOINT_DIR=/user/${USER}/sparkstreaming-data/${APP_NAME}

IS_FIRST_TIME=1
if [ ${IS_FIRST_TIME} -eq 1 ]
then
	hadoop fs -rm -r ${CHECKPOINT_DIR}
fi

spark-submit --master yarn-cluster \
	--name ${APP_NAME} \
	--executor-memory 2G \
	--executor-cores 1 \
	--num-executors 5  \
	--driver-memory 3G \
	--conf spark.yarn.executor.memoryOverhead=1024 \
	--conf spark.yarn.driver.memoryOverhead=1024 \
	--conf "spark.ui.port=45041" \
	--conf "spark.streaming.receiver.maxRate=20000" \
	--conf "spark.storage.memoryFraction=0.5" \
	--conf "spark.ui.showConsoleProgress=false" \
	--conf "spark.driver.extraJavaOptions=-XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:MaxPermSize=512M -XX:+PrintGCDetails -verbose:gc -XX:+PrintGCTimeStamps" \
	--conf "spark.streaming.backpressure.enabled=true" \
	--conf "spark.locality.wait=1s" \
	--conf "spark.streaming.blockInterval=1000ms" \
	--jars ${LOCAL_LIB_PATH}/fastjson-1.2.7.jar,${LOCAL_LIB_PATH}/jedis-2.8.0.jar,${LOCAL_LIB_PATH}/commons-pool2-2.4.2.jar,${LOCAL_LIB_PATH}/commons-pool-1.5.4.jar,${LOCAL_LIB_PATH}/kafka-clients-0.9.0.0.jar,${LOCAL_LIB_PATH}/kafka_2.11-0.9.0.0.jar,${LOCAL_LIB_PATH}/java-utils-0.0.8.jar \
	--class com.inveno.advertise.nearline.feedback.AdvertiseUserFeedback ${LOCAL_LIB_PATH}/ad-nearline-feedback.jar \
	${APP_NAME} 10 1 \
	${CHECKPOINT_DIR}

