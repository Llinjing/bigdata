#!/bin/bash

function prepare()
{
        source ../conf/project.conf
        source ../conf/hadoop.conf
        source ../conf/mysql.conf
        source /etc/profile

        mkdir -p ${LOCAL_DATA_PATH}
        mkdir -p ${LOCAL_TMP_PATH}

        ${HADOOP_RMR} ${STAT_AD_HOUR_OUTPUT_PATH}/stat-ad-hour

        return 0
}

function stat_ad_hour()
{
/usr/lib/spark/bin/spark-submit --master yarn-cluster \
        --name ${APP_NAME} \
        --executor-memory 1G \
        --executor-cores 1 \
        --num-executors 10 \
        --driver-memory 1G \
        --queue offline \
        --conf "spark.hadoop.mapreduce.input.fileinputformat.split.minsize=268435456" \
        --conf "spark.reducer.maxSizeInFlight=24m" \
        --conf "spark.shuffle.copier.threads=5" \
        --conf "spark.yarn.executor.memoryOverhead=1024" \
        --conf "spark.yarn.driver.memoryOverhead=1024" \
        --conf "spark.storage.memoryFraction=0.5" \
        --conf "spark.driver.extraJavaOptions=-XX:+UseParNewGC -XX:+UseConcMarkSweepGC -Xmn512M -Xms1024M -Xmx1024M -XX:MaxPermSize=512M -XX:+PrintGCDetails -XX:+PrintGCTimeStamps" \
        --conf "spark.executor.extraJavaOptions=-XX:+UseParNewGC -XX:+UseConcMarkSweepGC -Xss6M" \
        --jars ${LOCAL_LIB_PATH}/fastjson-1.2.7.jar \
        --class com.inveno.advertisement.stat.MergeAdvertisementStat ${LOCAL_LIB_PATH}/stat-advertisement-daily-0.0.1.jar \
        ${STAT_AD_HOUR_EXTEND_EVENT_INPUT_PATH} ${STAT_AD_HOUR_IMPRESSION_INPUT_PATH} ${STAT_AD_HOUR_CLICK_INPUT_PATH} ${STAT_AD_HOUR_OUTPUT_PATH}/stat-ad-hour
        return 0
}

function stat_ad_merge()
{
        ${HADOOP_CAT} ${STAT_AD_HOUR_OUTPUT_PATH}/stat-ad-hour/part-* >${LOCAL_DATA_PATH}/stat-ad-hour-tmp.list

        monitor_file=${LOCAL_DATA_PATH}/stat-ad-hour-tmp.list
        if [ ! -s $monitor_file ]
        then
            echo ${YESTERDAY}, "overseas-us-east stat ad hour by all job failed" >> ${LOCAL_LOG_PATH}/monitor.log
            ${WARNING_BIN} "overseas-us-east stat ad hour by all job failed" "${PHONENUMS}"
        else
            echo ${YESTERDAY},"overseas-us-east stat ad hour by all job success" >> ${LOCAL_LOG_PATH}/monitor.log
        fi

        cat ${LOCAL_DATA_PATH}/stat-ad-hour-tmp.list | python ${LOCAL_BIN_PATH}/stat-ad-parse.py >${LOCAL_DATA_PATH}/stat-ad-hour.list
        
        awk 'BEGIN{
                FS = "\t"
                OFS = "\t"
        }{
                print "0", "'"${TODAY_FORMAT}"'"" 00:00:00", "'"${TODAY_FORMAT}"'"" ""'"${HOUR}"'"":00:00", $0
        }END{
        }' ${LOCAL_DATA_PATH}/stat-ad-hour.list \
        >${LOCAL_DATA_PATH}/stat-ad-hour-result.list

        echo "USE dashboard;" >${LOCAL_TMP_PATH}/load-ad-hour.sql
        echo "LOAD DATA LOCAL INFILE '${LOCAL_DATA_PATH}/stat-ad-hour-result.list' INTO TABLE \
                t_daily_advertisement_stat FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n' IGNORE 0 LINES;" \
                >>${LOCAL_TMP_PATH}/load-ad-hour.sql

        mysql -h${MYSQL_HOST[2]} -P${MYSQL_PORT[2]} -u${MYSQL_USER[2]} -p${MYSQL_PWD[2]} \
                <${LOCAL_TMP_PATH}/load-ad-hour.sql

        return 0
}

prepare

stat_ad_hour

stat_ad_merge
