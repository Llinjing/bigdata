#!/bin/bash

source ../conf/project.conf
source /etc/profile

APP_NAME=article-feedback-1hour-json
#CHECKPOINT_DIR=/user/${USER}/sparkstreaming-data/${APP_NAME}

if [ $# -eq 1 ]
then
	echo "Note : remove the zookeeper nodes  "
	sh -x ${LOCAL_BIN_PATH}/zk-clean.sh ${APP_NAME}
fi

IS_FIRST_TIME=1

if [ ${IS_FIRST_TIME} -eq 1 ]
then
	hadoop fs -rm -r ${CHECKPOINT_DIR}
fi

spark-submit --master yarn-cluster \
	--name ${APP_NAME} \
	--queue realtime \
	--executor-memory 4G \
	--executor-cores 2 \
	--num-executors 5 \
	--driver-memory 4G \
	--conf spark.yarn.executor.memoryOverhead=2048 \
	--conf spark.yarn.driver.memoryOverhead=4096 \
	--conf "spark.ui.port=45044" \
	--conf "spark.streaming.receiver.maxRate=20000" \
	--conf "spark.storage.memoryFraction=0.5" \
	--conf "spark.ui.showConsoleProgress=false" \
	--conf "spark.driver.extraJavaOptions=-XX:+UseParNewGC -Xms4096m -Xmx4096m -XX:+UseConcMarkSweepGC -XX:MaxPermSize=512M -XX:+PrintGCDetails -verbose:gc -XX:+PrintGCTimeStamps" \
	--conf "spark.streaming.backpressure.enabled=true" \
	--conf "spark.locality.wait=1s" \
	--conf "spark.streaming.blockInterval=1000ms" \
	--jars ${LOCAL_LIB_PATH}/fastjson-1.2.7.jar,${LOCAL_LIB_PATH}/jedis-2.8.0.jar,${LOCAL_LIB_PATH}/commons-pool2-2.4.2.jar,${LOCAL_LIB_PATH}/commons-pool-1.5.4.jar,${LOCAL_LIB_PATH}/kafka-clients-0.9.0.0.jar,${LOCAL_LIB_PATH}/kafka_2.10-0.9.0.0.jar,${LOCAL_LIB_PATH}/spark-streaming-kafka_2.10-1.5.0.jar,${LOCAL_LIB_PATH}/java-utils-0.0.8.jar,${LOCAL_LIB_PATH}/metrics-core-2.2.0.jar \
	--class com.inveno.news.nearline.feedback.ArticleFeedback ${LOCAL_LIB_PATH}/nearline-feedback_2.10-1.0.jar \
	${APP_NAME} 60 1 \
	${APP_NAME}
	#${CHECKPOINT_DIR}


