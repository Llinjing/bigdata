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
                --executor-memory 2G \
                --executor-cores 1 \
                --num-executors 10 \
                --driver-memory 2G \
                --conf "spark.ui.port=46043" \
                --jars ${LOCAL_LIB_PATH}/fastjson-1.2.7.jar,${LOCAL_LIB_PATH}/jedis-2.7.3.jar,${LOCAL_LIB_PATH}/commons-pool2-2.4.2.jar,${LOCAL_LIB_PATH}/commons-pool-1.5.4.jar,${LOCAL_LIB_PATH}/kafka-clients-0.9.0.0.jar,${LOCAL_LIB_PATH}/kafka_2.10-0.9.0.0.jar,${LOCAL_LIB_PATH}/java-utils-0.0.8.jar,${LOCAL_LIB_PATH}/scala-utils-0.0.1.jar \
                --class com.inveno.article.gmp.ArticleGmp ${LOCAL_LIB_PATH}/article-gmp-0.0.1.jar
}

function online_test()
{
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
                --jars ${LOCAL_LIB_PATH}/fastjson-1.2.7.jar,${LOCAL_LIB_PATH}/jedis-2.7.3.jar,${LOCAL_LIB_PATH}/commons-pool2-2.4.2.jar,${LOCAL_LIB_PATH}/commons-pool-1.5.4.jar,${LOCAL_LIB_PATH}/kafka-clients-0.9.0.0.jar,${LOCAL_LIB_PATH}/kafka_2.10-0.9.0.0.jar,${LOCAL_LIB_PATH}/java-utils-0.0.8.jar,${LOCAL_LIB_PATH}/scala-utils-0.0.1.jar \
                --class com.inveno.article.gmp.ArticleGmp ${LOCAL_LIB_PATH}/article-gmp-0.0.1-test.jar
}

type=$1
if [ -z ${type} ] ; then
    echo "please input parameter, (online or test)"
elif [ ${type} == "online" ] ; then
    echo "online"
    online
elif [ ${type} == "test" ] ; then
    echo "test"

## nohup sh -x article-gmp.sh online 1>../log/article-gmp.err 2>&1 &
### nohup sh -x article-gmp.sh test1>../log/article-gmp.err 2>&1 &
