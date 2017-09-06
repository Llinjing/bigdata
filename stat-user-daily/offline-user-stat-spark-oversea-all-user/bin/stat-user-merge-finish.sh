#!/bin/bash

function prepare()
{
        source ../conf/project.conf
        source ../conf/hadoop.conf
        source ../conf/mysql.conf
        source /etc/profile

        mkdir -p ${LOCAL_DATA_PATH}
        mkdir -p ${LOCAL_TMP_PATH}

	    ${HADOOP_RMR} ${MR_STAT_USER_MERGE_OUTPUT_PATH_NOTICIAS}
        ${HADOOP_RMR} ${MR_STAT_USER_MERGE_OUTPUT_PATH_OTHER}
        ${HADOOP_RMR} ${MR_STAT_USER_MERGE_SCENARIOCHANNEL_OUTPUT_PATH}
        ${HADOOP_MKDIR} ${S3_DAILY_USER_PATH}

        return 0
}

function stat_daily_user_by_noticias()
{
/usr/lib/spark/bin/spark-submit --master yarn-cluster \
        --name ${APP_NAME} \
        --num-executors 30 \
        --executor-memory 4G \
        --executor-cores 1 \
        --driver-memory 3G \
        --queue offline \
        --conf "spark.hadoop.mapreduce.input.fileinputformat.split.minsize=268435456" \
        --conf "spark.reducer.maxSizeInFlight=24m" \
        --conf "spark.shuffle.copier.threads=5" \
        --conf "spark.yarn.executor.memoryOverhead=4096" \
        --conf "spark.yarn.driver.memoryOverhead=3072" \
        --conf "spark.storage.memoryFraction=0.5" \
        --conf "spark.driver.extraJavaOptions=-XX:+UseParNewGC -XX:+UseConcMarkSweepGC -Xmn512M -Xms3072M -Xmx3072M -XX:MaxPermSize=512M -XX:+PrintGCDetails -XX:+PrintGCTimeStamps" \
        --conf "spark.executor.extraJavaOptions=-XX:+UseParNewGC -XX:+UseConcMarkSweepGC -Xss6M" \
        --jars ${LOCAL_LIB_PATH}/fastjson-1.2.7.jar \
        --class com.inveno.news.base.stat.MergeUserStatInfo ${LOCAL_LIB_PATH}/offline-user-stat-spark-oversea-all-user-0.0.1.jar \
        ${MR_STAT_USER_REQUEST_INPUT_PATH} ${MR_STAT_USER_IMPRESSION_INPUT_PATH} ${MR_STAT_USER_CLICK_INPUT_PATH} ${MR_STAT_USER_DWELLTIME_INPUT_PATH} ${MR_STAT_USER_LISTPAGE_DWELLTIME_INPUT_PATH} ${MR_STAT_USER_MERGE_OUTPUT_PATH_NOTICIAS}

    return 0
}

function stat_merge_base_by_noticias()
{
        ${HADOOP_CAT} ${MR_STAT_USER_MERGE_OUTPUT_PATH_NOTICIAS}/part-* >${LOCAL_DATA_PATH}/stat-user-merge.list

        monitor_file=${LOCAL_DATA_PATH}/stat-user-merge.list
        if [ ! -s $monitor_file ]
        then
            echo ${YESTERDAY}, "overseas-us-east stat daily user by all job failed" >> ${LOCAL_LOG_PATH}/monitor.log
            ${WARNING_BIN} "overseas-us-east stat daily user by all job failed" "${PHONENUMS}"
        else
            echo ${YESTERDAY},"overseas-us-east stat daily user by all job success" >> ${LOCAL_LOG_PATH}/monitor.log
        fi

        cat ${LOCAL_DATA_PATH}/stat-user-merge.list | python ${LOCAL_BIN_PATH}/stat-user-merge-parse.py >${LOCAL_DATA_PATH}/stat-user-merge-parse.list

        awk 'BEGIN{
                FS = "\t"
                OFS = "\t"
        }{
                print '${YESTERDAY}'"0000", $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, ($14+0), ($15+0), ($16+0), ($17+0), ($18+0), ($19+0), ($20+0), ($21+0), ($22+0), ($23+0), $1, "all"
        }END{
        }' ${LOCAL_DATA_PATH}/stat-user-merge-parse.list \
        >${LOCAL_DATA_PATH}/t-daily-user-all-user.list

        awk 'BEGIN{
                FS = "\t"
                OFS = "\t"
        }{
                timestamp = sprintf("%s-%s-%s 00:00:00", 
                        substr("'${YESTERDAY}'", 1, 4),
                        substr("'${YESTERDAY}'", 5, 2),
                        substr("'${YESTERDAY}'", 7, 2))
                print "0", timestamp, $1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, ($14+0), ($15+0), ($16+0), ($17+0), ($18+0), ($19+0), ($20+0), ($21+0), ($22+0), ($23+0), "all"
        }END{
        }' ${LOCAL_DATA_PATH}/stat-user-merge-parse.list \
        >${LOCAL_DATA_PATH}/stat-user-result.list

        echo "USE dashboard;" >${LOCAL_TMP_PATH}/load-daily-user.sql
        echo "LOAD DATA LOCAL INFILE '${LOCAL_DATA_PATH}/stat-user-result.list' INTO TABLE \
                t_daily_user FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n' IGNORE 0 LINES;" \
                >>${LOCAL_TMP_PATH}/load-daily-user.sql

        mysql -h${MYSQL_HOST[2]} -P${MYSQL_PORT[2]} -u${MYSQL_USER[2]} -p${MYSQL_PWD[2]} \
                <${LOCAL_TMP_PATH}/load-daily-user.sql

        ${HADOOP_PUT} ${LOCAL_DATA_PATH}/t-daily-user-all-user.list ${S3_DAILY_USER_PATH}
        return 0
}

function stat_daily_user_by_other()
{
/usr/lib/spark/bin/spark-submit --master yarn-cluster \
        --name ${APP_NAME} \
        --num-executors 30 \
        --executor-memory 4G \
        --executor-cores 1 \
        --driver-memory 3G \
        --queue offline \
        --conf "spark.hadoop.mapreduce.input.fileinputformat.split.minsize=268435456" \
        --conf "spark.reducer.maxSizeInFlight=24m" \
        --conf "spark.shuffle.copier.threads=5" \
        --conf "spark.yarn.executor.memoryOverhead=4096" \
        --conf "spark.yarn.driver.memoryOverhead=3072" \
        --conf "spark.storage.memoryFraction=0.5" \
        --conf "spark.driver.extraJavaOptions=-XX:+UseParNewGC -XX:+UseConcMarkSweepGC -Xmn512M -Xms3072M -Xmx3072M -XX:MaxPermSize=512M -XX:+PrintGCDetails -XX:+PrintGCTimeStamps" \
        --conf "spark.executor.extraJavaOptions=-XX:+UseParNewGC -XX:+UseConcMarkSweepGC -Xss6M" \
        --jars ${LOCAL_LIB_PATH}/fastjson-1.2.7.jar \
        --class com.inveno.news.other.stat.MergeUserStatInfo ${LOCAL_LIB_PATH}/offline-user-stat-spark-oversea-all-user-0.0.1.jar \
        ${MR_STAT_USER_REQUEST_INPUT_PATH} ${MR_STAT_USER_IMPRESSION_INPUT_PATH} ${MR_STAT_USER_CLICK_INPUT_PATH} ${MR_STAT_USER_DWELLTIME_INPUT_PATH} ${MR_STAT_USER_LISTPAGE_DWELLTIME_INPUT_PATH} ${MR_STAT_USER_MERGE_OUTPUT_PATH_OTHER}

	return 0
}

function stat_merge_base_by_other()
{
        ${HADOOP_CAT} ${MR_STAT_USER_MERGE_OUTPUT_PATH_OTHER}/part-* >${LOCAL_DATA_PATH}/stat-user-merge.list

        monitor_file=${LOCAL_DATA_PATH}/stat-user-merge.list
        if [ ! -s $monitor_file ]
        then
            echo ${YESTERDAY}, "overseas-us-east stat daily user by all job failed" >> ${LOCAL_LOG_PATH}/monitor.log
            ${WARNING_BIN} "overseas-us-east stat daily user by all job failed" "${PHONENUMS}"
        else
            echo ${YESTERDAY},"overseas-us-east stat daily user by all job success" >> ${LOCAL_LOG_PATH}/monitor.log
        fi

    	cat ${LOCAL_DATA_PATH}/stat-user-merge.list | python ${LOCAL_BIN_PATH}/stat-user-merge-parse.py >${LOCAL_DATA_PATH}/stat-user-merge-parse.list

        awk 'BEGIN{
                FS = "\t"
                OFS = "\t"
        }{
                print '${YESTERDAY}'"0000", $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, ($14+0), ($15+0), ($16+0), ($17+0), ($18+0), ($19+0), ($20+0), ($21+0), ($22+0), ($23+0), $1, "all"
        }END{
        }' ${LOCAL_DATA_PATH}/stat-user-merge-parse.list \
        >${LOCAL_DATA_PATH}/t-daily-user-all-user-other.list

        ${HADOOP_PUT} ${LOCAL_DATA_PATH}/t-daily-user-all-user-other.list ${S3_DAILY_USER_PATH}

        awk 'BEGIN{
                FS = "\t"
                OFS = "\t"
        }{
                timestamp = sprintf("%s-%s-%s 00:00:00", 
                        substr("'${YESTERDAY}'", 1, 4),
                        substr("'${YESTERDAY}'", 5, 2),
                        substr("'${YESTERDAY}'", 7, 2))
                print "0", timestamp, $1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, ($14+0), ($15+0), ($16+0), ($17+0), ($18+0), ($19+0), ($20+0), ($21+0), ($22+0), ($23+0), "all"
        }END{
        }' ${LOCAL_DATA_PATH}/stat-user-merge-parse.list \
        >${LOCAL_DATA_PATH}/stat-user-result.list

        echo "USE dashboard;" >${LOCAL_TMP_PATH}/load-daily-user.sql
        echo "LOAD DATA LOCAL INFILE '${LOCAL_DATA_PATH}/stat-user-result.list' INTO TABLE \
                t_daily_user FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n' IGNORE 0 LINES;" \
                >>${LOCAL_TMP_PATH}/load-daily-user.sql

        mysql -h${MYSQL_HOST[2]} -P${MYSQL_PORT[2]} -u${MYSQL_USER[2]} -p${MYSQL_PWD[2]} \
                <${LOCAL_TMP_PATH}/load-daily-user.sql

        return 0
}

function stat_daily_user_scenariochannel()
{
/usr/lib/spark/bin/spark-submit --master yarn-cluster \
        --name ${APP_NAME} \
        --num-executors 30 \
        --executor-memory 4G \
        --executor-cores 1 \
        --driver-memory 3G \
        --queue offline \
        --conf "spark.hadoop.mapreduce.input.fileinputformat.split.minsize=268435456" \
        --conf "spark.reducer.maxSizeInFlight=24m" \
        --conf "spark.shuffle.copier.threads=5" \
        --conf "spark.yarn.executor.memoryOverhead=4096" \
        --conf "spark.yarn.driver.memoryOverhead=3072" \
        --conf "spark.storage.memoryFraction=0.5" \
        --conf "spark.shuffle.memoryFraction=0.5" \
        --conf "spark.driver.extraJavaOptions=-XX:+UseParNewGC -XX:+UseConcMarkSweepGC -Xmn512M -Xms3072M -Xmx3072M -XX:MaxPermSize=512M -XX:+PrintGCDetails -XX:+PrintGCTimeStamps" \
        --conf "spark.executor.extraJavaOptions=-XX:+UseParNewGC -XX:+UseConcMarkSweepGC -Xss16M" \
        --jars ${LOCAL_LIB_PATH}/fastjson-1.2.7.jar \
        --class com.inveno.news.scenariochannel.stat.MergeUserStatInfo ${LOCAL_LIB_PATH}/offline-user-stat-spark-oversea-all-user-0.0.1.jar \
        ${MR_STAT_USER_REQUEST_INPUT_PATH} ${MR_STAT_USER_IMPRESSION_INPUT_PATH} ${MR_STAT_USER_CLICK_INPUT_PATH} ${MR_STAT_USER_DWELLTIME_INPUT_PATH} ${MR_STAT_USER_LISTPAGE_DWELLTIME_INPUT_PATH} ${MR_STAT_USER_MERGE_SCENARIOCHANNEL_OUTPUT_PATH}

	return 0
}

function stat_merge_scenariochannel()
{
	    ${HADOOP_CAT} ${MR_STAT_USER_MERGE_SCENARIOCHANNEL_OUTPUT_PATH}/part-* >${LOCAL_DATA_PATH}/stat-user-merge.list

        monitor_file=${LOCAL_DATA_PATH}/stat-user-merge.list
        if [ ! -s $monitor_file ]
        then
            echo ${YESTERDAY}, "overseas-us-east stat daily user by scenariochannel job failed" >> ${LOCAL_LOG_PATH}/monitor.log
            ${WARNING_BIN} "overseas-us-east stat daily user by scenariochannel job failed" "${PHONENUMS}"
        else
            echo ${YESTERDAY},"overseas-us-east stat daily user by scenariochannel job success" >> ${LOCAL_LOG_PATH}/monitor.log
        fi

	    cat ${LOCAL_DATA_PATH}/stat-user-merge.list | python ${LOCAL_BIN_PATH}/stat-user-merge-parse.py >${LOCAL_DATA_PATH}/stat-user-merge-parse.list

        awk 'BEGIN{
                FS = "\t"
                OFS = "\t"
        }{
                print '${YESTERDAY}'"0000", $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, ($14+0), ($15+0), ($16+0), ($17+0), ($18+0), ($19+0), ($20+0), ($21+0), ($22+0), ($23+0), $1, "all"
        }END{
        }' ${LOCAL_DATA_PATH}/stat-user-merge-parse.list \
        >${LOCAL_DATA_PATH}/t-daily-user-all-user-scenariochannel.list

        ${HADOOP_PUT} ${LOCAL_DATA_PATH}/t-daily-user-all-user-scenariochannel.list ${S3_DAILY_USER_PATH}

        awk 'BEGIN{
                FS = "\t"
                OFS = "\t"
        }{
                timestamp = sprintf("%s-%s-%s 00:00:00", 
                        substr("'${YESTERDAY}'", 1, 4),
                        substr("'${YESTERDAY}'", 5, 2),
                        substr("'${YESTERDAY}'", 7, 2))
                print "0", timestamp, $1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, ($14+0), ($15+0), ($16+0), ($17+0), ($18+0), ($19+0), ($20+0), ($21+0), ($22+0), ($23+0), "all"
        }END{
        }' ${LOCAL_DATA_PATH}/stat-user-merge-parse.list \
        >${LOCAL_DATA_PATH}/stat-user-result.list

        #echo "USE dashboard;" >${LOCAL_TMP_PATH}/load-daily-user.sql
        #echo "LOAD DATA LOCAL INFILE '${LOCAL_DATA_PATH}/stat-user-result.list' INTO TABLE \
        #        t_daily_user FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n' IGNORE 0 LINES;" \
        #        >>${LOCAL_TMP_PATH}/load-daily-user.sql

        #mysql -h${MYSQL_HOST[2]} -P${MYSQL_PORT[2]} -u${MYSQL_USER[2]} -p${MYSQL_PWD[2]} \
        #        <${LOCAL_TMP_PATH}/load-daily-user.sql

        return 0
}

function load_to_druid()
{
    sed -i '103s/^.*$/                "intervals":["'$YESTERDAY_FORMAT'\/'$TODAY_FORMAT'"]/' daily-user.json
    sed -i '110s/'$YESTERDAY_LAST'/'$YESTERDAY'/g' daily-user.json
    curl -X 'POST' -H 'Content-Type:application/json' -d @daily-user.json ${DRUID_URL}/druid/indexer/v1/task

    return 0
}

prepare

stat_daily_user_by_noticias

stat_merge_base_by_noticias

stat_daily_user_by_other

stat_merge_base_by_other

stat_daily_user_scenariochannel

stat_merge_scenariochannel

load_to_druid
