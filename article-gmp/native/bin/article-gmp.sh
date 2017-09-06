#!/bin/bash
### nohup sh -x article-gmp.sh online 1>../log/article-gmp.err 2>&1 & 

source ../conf/project.conf

function online()
{
	APP_NAME=${APP_NAME[0]}
	hadoop fs -rm -r ${CHECKPOINT_HDFS_PATH}/*
	#hadoop fs -rm -r ${DATA_HDFS_PATH}/*

	spark-submit --master yarn-cluster \
        	--name ${APP_NAME} \
	        --executor-memory 3G \
        	--executor-cores 1 \
	        --num-executors 10 \
	        --driver-memory 3G \
	        --conf "spark.ui.port=46043" \
	        --jars ${LOCAL_LIB_PATH}/fastjson-1.2.7.jar,${LOCAL_LIB_PATH}/jedis-2.8.0.jar,${LOCAL_LIB_PATH}/commons-pool2-2.4.2.jar,${LOCAL_LIB_PATH}/commons-pool-1.5.4.jar,${LOCAL_LIB_PATH}/kafka-clients-0.9.0.0.jar,${LOCAL_LIB_PATH}/kafka_2.10-0.9.0.0.jar,${LOCAL_LIB_PATH}/java-utils-0.1.3.jar,${LOCAL_LIB_PATH}/scala-utils-0.0.1.jar \
        	--class com.inveno.article.gmp.ArticleGmp ${LOCAL_LIB_PATH}/article-gmp-0.0.1.jar
}

function online_test()
{
	${ZOOKEEPER_BIN} -server ${zookeeper_ips} rmr ${zookeeper_test_node_path}
        hadoop fs -rm -r /test/huanghaifeng/spark/checkpoint/*
        hadoop fs -rm -r /test/huanghaifeng/spark/data/*
        APP_NAME=article-gmp-huanghaifeng

        spark-submit --master yarn-cluster \
                --name ${APP_NAME} \
                --executor-memory 1G \
                --executor-cores 1 \
                --num-executors 3 \
                --driver-memory 1G \
                --conf "spark.ui.port=46044" \
                --jars ${LOCAL_LIB_PATH}/fastjson-1.2.7.jar,${LOCAL_LIB_PATH}/jedis-2.8.0.jar,${LOCAL_LIB_PATH}/commons-pool2-2.4.2.jar,${LOCAL_LIB_PATH}/commons-pool-1.5.4.jar,${LOCAL_LIB_PATH}/kafka-clients-0.9.0.0.jar,${LOCAL_LIB_PATH}/kafka_2.10-0.9.0.0.jar,${LOCAL_LIB_PATH}/java-utils-0.1.3.jar,${LOCAL_LIB_PATH}/scala-utils-0.0.1.jar \
                --class com.inveno.article.gmp.ArticleGmp ${LOCAL_LIB_PATH}/article-gmp-0.0.1-test.jar
}

type=$1
delete_online_offset=$2

if [ ${delete_online_offset} ] ; then
    if [ ${delete_online_offset} == "confirm_delete_online_offset" ] ; then
        echo "delete online zk offset"
        ${ZOOKEEPER_BIN} -server ${zookeeper_ips} rmr ${zookeeper_online_node_path}
    fi
fi

if [ -z ${type} ] ; then
    echo "please input parameter, (online or test)"
elif [ ${type} == "online" ] ; then
    echo "online"
    online
elif [ ${type} == "test" ] ; then
    echo "test"
    online_test
else
    echo "please input right parameter, (online or test)"
fi

### nohup sh -x article-gmp.sh online 1>../log/article-gmp.err 2>&1 & 
