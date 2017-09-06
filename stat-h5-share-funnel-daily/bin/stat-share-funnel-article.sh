#!/bin/bash

function prepare()
{
        source ../conf/project.conf
        source ../conf/hadoop.conf
        source ../conf/mysql.conf
        source /etc/profile

        mkdir -p ${LOCAL_DATA_PATH}
        mkdir -p ${LOCAL_TMP_PATH}

        APP_NAME="h5-share-funnel-article"
        timestamp_day=${TODAY:0:4}'-'${TODAY:4:2}'-'${TODAY:6:2}" 00:00:00"
        timestamp_hour=${TODAY:0:4}'-'${TODAY:4:2}'-'${TODAY:6:2}" ${HOUR}:00:00"
        return 0
}


#        --conf "spark.driver.extraJavaOptions=-XX:+UseParNewGC -Xss8M -Xmn512M -Xms2048M -Xmx2048M -XX:+UseConcMarkSweepGC -XX:MaxPermSize=512M -XX:+PrintGCDetails -verbose:gc -XX:+PrintGCTimeStamps" \

function start_article_spark()
{
        aws  s3  sync . ${MR_STAT_H5_SHARE_ARTICLE_OUTPUT_PATH}  --delete
        ${HADOOP_RMR} ${MR_STAT_H5_SHARE_ARTICLE_OUTPUT_PATH}

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
        --class com.inveno.news.stat.StatShareFunnelArticle ${LOCAL_LIB_PATH}/stat-h5-share-funnel-daily-1.0-SNAPSHOT.jar \
        ${MR_STAT_H5_SHARE_ARTICLE_INPUT_PATH} ${MR_STAT_H5_SHARE_ARTICLE_OUTPUT_PATH}
        return 0
}


function stat_article_result()
{
    ${HADOOP_CAT} ${MR_STAT_H5_SHARE_ARTICLE_OUTPUT_PATH}/part* > ${LOCAL_DATA_PATH}/tmp_article_result.list
    cat ${LOCAL_DATA_PATH}/tmp_article_result.list |\
    python stat-result-parse.py| \
    awk 'BEGIN{ 
        FS="\t"
        OFS="\t"
    }{
        print 0, "'"${timestamp_day}"'", "'"${timestamp_hour}"'", $0 
    }END{
              
    }'> ${LOCAL_DATA_PATH}/article_result_finish.list
}


function insert_article_sql()
{
    echo "USE dashboard;" >${LOCAL_TMP_PATH}/load-daily-share-funnel-article.sql
    echo "delete from t_daily_h5_link_share_pv where timestamp_hour='${timestamp_hour}';" >>${LOCAL_TMP_PATH}/load-daily-share-funnel-article.sql
    echo "LOAD DATA LOCAL INFILE '${LOCAL_DATA_PATH}/article_result_finish.list' INTO TABLE \
                t_daily_h5_link_share_pv FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n';" \
                >>${LOCAL_TMP_PATH}/load-daily-share-funnel-article.sql

        mysql -h${MYSQL_HOST[4]} -P${MYSQL_PORT[4]} -u${MYSQL_USER[4]} -p${MYSQL_PWD[4]} \
                <${LOCAL_TMP_PATH}/load-daily-share-funnel-article.sql  
}




echo "-----------h5_share_article begin-------------"

prepare


start_article_spark

stat_article_result

insert_article_sql

echo "-----------h5_share_article end-------------"
