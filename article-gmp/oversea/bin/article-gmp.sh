#!/bin/bash
### nohup sh -x article-gmp.sh online 1>../log/article-gmp.err 2>&1 & 

source ../conf/project.conf

function online()
{
	APP_NAME=${APP_NAME[0]}
	hadoop fs -rm -r ${CHECKPOINT_HDFS_PATH}/*

    app_id=`yarn application -list | grep "${APP_NAME}" | awk '{print $1}'`
    if [ -n "${app_id}" ]
    then
        yarn application -kill ${app_id}
    fi

	spark-submit --master yarn-cluster \
        	--name ${APP_NAME} \
	    	--queue realtime \
	        --executor-memory 2G \
        	--executor-cores 1 \
	        --num-executors 10 \
	        --driver-memory 3G \
	        --conf "spark.ui.port=45045" \
            --jars ${LOCAL_LIB_PATH}/fastjson-1.2.7.jar,${LOCAL_LIB_PATH}/jedis-2.8.0.jar,${LOCAL_LIB_PATH}/commons-pool2-2.4.2.jar,${LOCAL_LIB_PATH}/commons-pool-1.5.4.jar,${LOCAL_LIB_PATH}/kafka-clients-0.8.2.2.jar,${LOCAL_LIB_PATH}/kafka_2.10-0.8.2.2.jar,${LOCAL_LIB_PATH}/java-utils-0.1.3.jar,${LOCAL_LIB_PATH}/scala-utils-0.0.1.jar,${LOCAL_LIB_PATH}/spark-streaming-kafka_2.10-1.5.0.jar,${LOCAL_LIB_PATH}/metrics-core-2.2.0.jar,${LOCAL_LIB_PATH}/article-gmp-common-0.0.1.jar \
        	--class com.inveno.article.gmp.ArticleGmp ${LOCAL_LIB_PATH}/article-gmp-oversea-0.0.1.jar
}

function online_test()
{
    if [ ${zookeeper_test_node_path} ]
    then
        ${ZOOKEEPER_BIN} -server ${zookeeper_ips} rmr ${zookeeper_test_node_path}
    fi
    hadoop fs -rm -r s3://bigdata-east/user/haifeng.huang/spark/checkpoint/*
    hadoop fs -rm -r s3://bigdata-east/user/haifeng.huang/spark/data/*
    APP_NAME=article-gmp-huanghaifeng

    spark-submit --master yarn-cluster \
        --name ${APP_NAME} \
        --queue realtime \
        --executor-memory 1G \
        --executor-cores 1 \
        --num-executors 3 \
        --driver-memory 1G \
        --conf "spark.ui.port=45044" \
        --jars ${LOCAL_LIB_PATH}/fastjson-1.2.7.jar,${LOCAL_LIB_PATH}/jedis-2.8.0.jar,${LOCAL_LIB_PATH}/commons-pool2-2.4.2.jar,${LOCAL_LIB_PATH}/commons-pool-1.5.4.jar,${LOCAL_LIB_PATH}/kafka-clients-0.8.2.2.jar,${LOCAL_LIB_PATH}/kafka_2.10-0.8.2.2.jar,${LOCAL_LIB_PATH}/java-utils-0.1.3.jar,${LOCAL_LIB_PATH}/scala-utils-0.0.1.jar,${LOCAL_LIB_PATH}/spark-streaming-kafka_2.10-1.5.0.jar,${LOCAL_LIB_PATH}/metrics-core-2.2.0.jar,${LOCAL_LIB_PATH}/article-gmp-common-0.0.1.jar \
        --class com.inveno.article.gmp.ArticleGmp ${LOCAL_LIB_PATH}/article-gmp-oversea-test-0.0.1.jar
}

type=$1
if [ -z ${type} ] ; then
    echo "please input parameter, (online or test)"
elif [ ${type} == "online" ] ; then
    echo "online"
    online
elif [ ${type} == "test" ] ; then
    echo "test"
    online_test
elif [ ${type} == "online_delete_zk" ] ; then
    echo "online_delete_zk"
    if [ ${zookeeper_online_node_path} ]
    then
        ${ZOOKEEPER_BIN} -server ${zookeeper_ips} rmr ${zookeeper_online_node_path}
    fi
    online
else
    echo "please input right parameter, (online or test)"
fi

### nohup sh -x article-gmp.sh online 1>../log/article-gmp.err 2>&1 & 
