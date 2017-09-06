#!/bin/bash

function prepare()
{
        source ../conf/project.conf
        source ../conf/hadoop.conf
        source ../conf/mysql.conf
        source /etc/profile

        mkdir -p ${LOCAL_DATA_PATH}
        mkdir -p ${LOCAL_TMP_PATH}

        APP_NAME="emotion-article"
        timestamp=${YESTERDAY:0:4}'-'${YESTERDAY:4:2}'-'${YESTERDAY:6:2}" 00:00:00"
        return 0
}


function get_and_upload_fresh()
{
        mysql_deal="mysql -h${MYSQL_HOST[3]} -u${MYSQL_USER[3]} -p${MYSQL_PWD[3]}"
        ${mysql_deal} -N -e "select distinct uid from zhizi_user.daily_fresh_user where epoch_day= floor(unix_timestamp()/(24*3600)) -1" > ${LOCAL_DATA_PATH}/fresh_user.list

        ${HADOOP_MKDIR}  ${MR_STAT_FRESH_USER_OUTPUT_PATH}
        ${HADOOP_PUT}  ${LOCAL_DATA_PATH}/fresh_user.list ${MR_STAT_FRESH_USER_OUTPUT_PATH}

}


function start_article_spark()
{
        aws  s3  sync . ${MR_STAT_EMOTION_ARTICLE_OUTPUT_PATH}  --delete
        ${HADOOP_RMR} ${MR_STAT_EMOTION_ARTICLE_OUTPUT_PATH}

        spark-submit --master yarn-cluster \
        --name ${APP_NAME} \
        --executor-memory 2G \
        --executor-cores 1 \
        --num-executors 10 \
        --driver-memory 4G \
        --queue offline \
        --conf spark.hadoop.mapreduce.input.fileinputformat.split.minsize=268435456 \
        --conf spark.yarn.executor.memoryOverhead=2048 \
        --conf spark.yarn.driver.memoryOverhead=2048 \
        --conf "spark.speculation=true" \
        --conf "spark.speculation.interval=100" \
        --conf "spark.speculation.quantile=0.75" \
        --conf "spark.speculation.multiplier=1.5" \
        --conf "spark.ui.port=45044" \
        --conf "spark.storage.memoryFraction=0.7" \
        --conf "spark.ui.showConsoleProgress=false" \
        --conf "spark.executor.extraJavaOptions=-Xss2M -XX:+PrintGCDetails -XX:+PrintGCTimeStamps" \
        --conf "spark.locality.wait=1s" \
        --jars ${LOCAL_LIB_PATH}/fastjson-1.2.7.jar \
        --class com.inveno.news.stat.StatBallotArticle ${LOCAL_LIB_PATH}/stat-ballot-daily-1.0-SNAPSHOT.jar \
        ${MR_STAT_EMOTION_ARTICLE_INPUT_PATH} ${MR_STAT_PROFILE_USER_INPUT_PATH} ${MR_STAT_FRESH_USER_OUTPUT_PATH} ${MR_STAT_EMOTION_ARTICLE_OUTPUT_PATH}
        return 0
}




function stat_article_result()
{
    ${HADOOP_CAT} ${MR_STAT_EMOTION_ARTICLE_OUTPUT_PATH}/part* > ${LOCAL_DATA_PATH}/tmp_article_result.list
    cat ${LOCAL_DATA_PATH}/tmp_article_result.list|python stat-result-parse.py > ${LOCAL_DATA_PATH}/tmp_article_result.list2 
    awk 'BEGIN{
        FS="\t"
        OFS="\t"
    }{
        print 0, "'"${timestamp}"'", $0
    }END{}'  ${LOCAL_DATA_PATH}/tmp_article_result.list2 > ${LOCAL_DATA_PATH}/article_result_finish.list
         
}

function insert_article_sql()
{
    echo "USE dashboard;" >${LOCAL_TMP_PATH}/load-daily-emotion-article-stat.sql
    echo "delete from t_daily_ballot_article_test where timestamp='${timestamp}';" >>${LOCAL_TMP_PATH}/load-daily-emotion-article-stat.sql
    echo "LOAD DATA LOCAL INFILE '${LOCAL_DATA_PATH}/article_result_finish.list' INTO TABLE \
                t_daily_ballot_article_test FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n';" \
                >>${LOCAL_TMP_PATH}/load-daily-emotion-article-stat.sql

        mysql -h${MYSQL_HOST[2]} -P${MYSQL_PORT[2]} -u${MYSQL_USER[2]} -p${MYSQL_PWD[2]} \
                <${LOCAL_TMP_PATH}/load-daily-emotion-article-stat.sql  
}

echo "----------emontion-article begin------------------"
prepare

get_and_upload_fresh

start_article_spark

stat_article_result

insert_article_sql

echo "----------emontion-article end------------------"
