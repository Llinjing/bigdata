#!/bin/bash

function prepare()
{
        source ../conf/project.conf
        source ../conf/hadoop.conf
        source ../conf/mysql.conf

        mkdir -p ${LOCAL_DATA_PATH}
        mkdir -p ${LOCAL_TMP_PATH}

	${HADOOP_RMR} ${MR_OUTPUT_PATH}/stat-user-merge

        return 0
}

function start_spark()
{
	spark-submit --master yarn-cluster \
        --name ${APP_NAME} \
        --executor-memory 5G \
        --executor-cores 1 \
        --num-executors 30 \
        --driver-memory 3G \
	--conf spark.hadoop.mapreduce.input.fileinputformat.split.minsize=268435456 \
	--conf spark.reducer.maxSizeInFlight=24m \
	--conf spark.shuffle.copier.threads=5 \
        --conf spark.yarn.executor.memoryOverhead=2048 \
        --conf spark.yarn.driver.memoryOverhead=2048 \
        --conf "spark.ui.port=45044" \
        --conf "spark.storage.memoryFraction=0.5" \
        --conf "spark.ui.showConsoleProgress=false" \
        --conf "spark.driver.extraJavaOptions=-XX:+UseParNewGC -Xmn512M -Xms4096M -Xmx4096M -XX:+UseConcMarkSweepGC -XX:MaxPermSize=512M -XX:+PrintGCDetails -verbose:gc -XX:+PrintGCTimeStamps" \
        --jars ${LOCAL_LIB_PATH}/fastjson-1.2.7.jar \
        --class com.inveno.news.stat.MergeUserStatInfo ${LOCAL_LIB_PATH}/offline-source-stat-spark-native-0.0.1.jar \
        ${MR_PAID_MEDIA_PATH} ${MR_STAT_ARTICLE_INFO_INPUT_PATH} ${MR_STAT_USER_REQUEST_INPUT_PATH} ${MR_STAT_USER_IMPRESSION_INPUT_PATH} ${MR_STAT_USER_CLICK_INPUT_PATH} ${MR_OUTPUT_PATH}/stat-user-merge

	return 0
}

function stat_merge()
{
        ${HADOOP_CAT} ${MR_OUTPUT_PATH}/stat-user-merge/part-* >${LOCAL_DATA_PATH}/stat-user-merge.list

	cat ${LOCAL_DATA_PATH}/stat-user-merge.list | python ${LOCAL_BIN_PATH}/stat-user-merge-parse.py >${LOCAL_DATA_PATH}/stat-user-merge-parse.list

        awk 'BEGIN{
                FS = "\t"
                OFS = "\t"
        }{
                timestamp = sprintf("%s-%s-%s 00:00:00", 
                        substr("'${YESTERDAY}'", 1, 4),
                        substr("'${YESTERDAY}'", 5, 2),
                        substr("'${YESTERDAY}'", 7, 2))
                print "0", timestamp, $1, $2, $3, $4, $5, $6, $7, ($8+0), ($10+0), ($12+0), ($9+0), ($11+0), ($13+0)
        }END{
        }' ${LOCAL_DATA_PATH}/stat-user-merge-parse.list \
        >${LOCAL_DATA_PATH}/stat-user-result.list

        echo "USE dashboard;" >${LOCAL_TMP_PATH}/load-daily-user.sql
        echo "LOAD DATA LOCAL INFILE '${LOCAL_DATA_PATH}/stat-user-result.list' INTO TABLE \
                t_daily_source FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n' IGNORE 0 LINES;" \
                >>${LOCAL_TMP_PATH}/load-daily-user.sql

        mysql -h${MYSQL_HOST[2]} -P${MYSQL_PORT[2]} -u${MYSQL_USER[2]} -p${MYSQL_PWD[2]} \
                <${LOCAL_TMP_PATH}/load-daily-user.sql

        return 0
}

prepare

start_spark

stat_merge
