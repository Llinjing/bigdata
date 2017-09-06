#!/bin/bash
### nohup sh -x article-emotion-gmp.sh online 1>../log/article-emotion-gmp.err 2>&1 & 

source ../conf/project.conf
export HADOOP_HOME=/usr/lib/hadoop
export PATH=/usr/lib/jvm/jre-1.8.0-openjdk.x86_64/bin:/usr/local/bin:/bin:/usr/bin:/usr/local/sbin:/usr/sbin:/sbin:/usr/local/go/bin/:/Data/coding/golang/bin/:/usr/lib/spark//bin:/home/haifeng.huang/.local/bin

function online()
{
	APP_NAME=${APP_NAME[0]}
	hadoop fs -rm -r ${CLUSTER_CHECKPOINT_PATH}/*
	#hadoop fs -rm -r ${CLUSTER_DATA_PATH}/*

    app_id=`yarn application -list | grep "${APP_NAME[0]}" | awk '{print $1}'`
    if [ -n "${app_id}" ]
    then
        yarn application -kill ${app_id}
    fi

	spark-submit --master yarn-cluster \
        	--name ${APP_NAME} \
	    	--queue realtime \
	        --executor-memory 2G \
        	--executor-cores 1 \
	        --num-executors 6 \
	        --driver-memory 3G \
	        --conf "spark.ui.port=45045" \
            --jars ${LOCAL_LIB_PATH}/fastjson-1.2.7.jar,${LOCAL_LIB_PATH}/jedis-2.8.0.jar,${LOCAL_LIB_PATH}/commons-pool2-2.4.2.jar,${LOCAL_LIB_PATH}/commons-pool-1.5.4.jar,${LOCAL_LIB_PATH}/kafka-clients-0.8.2.2.jar,${LOCAL_LIB_PATH}/kafka_2.10-0.8.2.2.jar,${LOCAL_LIB_PATH}/java-utils-0.1.3.jar,${LOCAL_LIB_PATH}/scala-utils-0.0.1.jar,${LOCAL_LIB_PATH}/spark-streaming-kafka_2.10-1.5.0.jar,${LOCAL_LIB_PATH}/metrics-core-2.2.0.jar \
        	--class com.inveno.article.emotion.gmp.ArticleEmotionGmp ${LOCAL_LIB_PATH}/article-emotion-gmp-oversea-0.0.1.jar
}

function online_test()
{
    sh -x ./delete_zookeeeper.sh
    hadoop fs -rm -r s3://bigdata-east/user/haifeng.huang/spark/checkpoint/*
    hadoop fs -rm -r s3://bigdata-east/user/haifeng.huang/spark/data/*
    APP_NAME=article-emotion-gmp-huanghaifeng

    spark-submit --master yarn-cluster \
                 --name ${APP_NAME} \
	             --queue realtime \
                 --executor-memory 1G \
                 --executor-cores 1 \
                 --num-executors 3 \
                 --driver-memory 1G \
                 --conf "spark.ui.port=46044" \
                 --jars ${LOCAL_LIB_PATH}/fastjson-1.2.7.jar,${LOCAL_LIB_PATH}/jedis-2.8.0.jar,${LOCAL_LIB_PATH}/commons-pool2-2.4.2.jar,${LOCAL_LIB_PATH}/commons-pool-1.5.4.jar,${LOCAL_LIB_PATH}/kafka-clients-0.8.2.2.jar,${LOCAL_LIB_PATH}/kafka_2.10-0.8.2.2.jar,${LOCAL_LIB_PATH}/java-utils-0.1.3.jar,${LOCAL_LIB_PATH}/scala-utils-0.0.1.jar,${LOCAL_LIB_PATH}/spark-streaming-kafka_2.10-1.5.0.jar,${LOCAL_LIB_PATH}/metrics-core-2.2.0.jar \
                 --class com.inveno.article.emotion.gmp.ArticleEmotionGmp ${LOCAL_LIB_PATH}/article-emotion-gmp-oversea-0.0.1-test.jar
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
else
    echo "please input right parameter, (online or test)"
fi

### nohup sh -x article-emotion-gmp.sh online 1>../log/article-emotion-gmp.err 2>&1 & 
