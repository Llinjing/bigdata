#!/bin/bash

function prepare()
{
    source ../conf/project.conf
    source ../conf/hadoop.conf
    source ../conf/mysql.conf

    mkdir -p ${LOCAL_DATA_PATH}
    mkdir -p ${LOCAL_TMP_PATH}

	${HADOOP_RMR} ${MR_OUTPUT_PATH}/stat-source-merge
    ${HADOOP_RMR} ${MR_STAT_ARTICLE_PARSE_OUTPUT_PATH}

    return 0
}

function mysql_select()
{
    echo "USE db_mta;" >${LOCAL_TMP_PATH}/select-source.sql
    echo "set names utf8;select source_name from t_source where is_dashboard_media_pv_display=1;" >>${LOCAL_TMP_PATH}/select-source.sql
    mysql -h${MYSQL_HOST[0]} -P${MYSQL_PORT[0]} -u${MYSQL_USER[0]} -p${MYSQL_PWD[0]} -N<${LOCAL_TMP_PATH}/select-source.sql >${LOCAL_DATA_PATH}/paid-media.list

    ${HADOOP_RMR} ${MR_OUTPUT_PATH}/paid-media/*

    ${HADOOP_PUT} ${LOCAL_DATA_PATH}/paid-media.list ${MR_OUTPUT_PATH}/paid-media

    return 0
}

function start_spark()
{
	/usr/lib/spark/bin/spark-submit --master yarn-cluster \
        --name ${APP_NAME} \
        --executor-memory 2G \
        --executor-cores 1 \
        --num-executors 30 \
        --driver-memory 2G \
        --queue offline \
        --conf "spark.hadoop.mapreduce.input.fileinputformat.split.minsize=268435456" \
        --conf "spark.reducer.maxSizeInFlight=24m" \
        --conf "spark.shuffle.copier.threads=5" \
        --conf "spark.yarn.executor.memoryOverhead=2048" \
        --conf "spark.yarn.driver.memoryOverhead=2048" \
        --conf "spark.storage.memoryFraction=0.5" \
        --conf "spark.driver.extraJavaOptions=-XX:+UseParNewGC -XX:+UseConcMarkSweepGC -Xmn512M -Xms2048M -Xmx2048M -XX:MaxPermSize=512M -XX:+PrintGCDetails -XX:+PrintGCTimeStamps" \
        --conf "spark.executor.extraJavaOptions=-XX:+UseParNewGC -XX:+UseConcMarkSweepGC -Xss6M" \
        --jars ${LOCAL_LIB_PATH}/fastjson-1.2.7.jar \
        --class com.inveno.news.stat.MergeUserStatInfo ${LOCAL_LIB_PATH}/offline-source-stat-spark-oversea-0.0.1.jar \
        ${MR_PAID_MEDIA_PATH} ${MR_STAT_ARTICLE_PARSE_INPUT_PATH} ${MR_STAT_ARTICLE_PARSE_OUTPUT_PATH} ${MR_STAT_USER_REQUEST_INPUT_PATH} ${MR_STAT_USER_IMPRESSION_INPUT_PATH} ${MR_STAT_USER_CLICK_INPUT_PATH} ${MR_STAT_USER_DWELLTIME_INPUT_PATH} ${MR_OUTPUT_PATH}/stat-source-merge

	return 0
}

function stat_merge()
{
    ${HADOOP_CAT} ${MR_OUTPUT_PATH}/stat-source-merge/part-* >${LOCAL_DATA_PATH}/stat-user-merge.list
    monitor_file=${LOCAL_DATA_PATH}/stat-user-merge.list
    if [ ! -s $monitor_file ]
    then
        echo ${YESTERDAY}, "overseas-us-east paid media job failed" >> ${LOCAL_LOG_PATH}/monitor.log
        ${WARNING_BIN} "overseas-us-east paid media job failed" "${PHONENUMS}"
    else
        echo ${YESTERDAY},"overseas-us-east paid media job success" >> ${LOCAL_LOG_PATH}/monitor.log
    fi
    cat ${LOCAL_DATA_PATH}/stat-user-merge.list | python ${LOCAL_BIN_PATH}/stat-source-merge-parse.py >${LOCAL_DATA_PATH}/stat-user-merge-parse.list

    echo "USE db_mta;" >${LOCAL_TMP_PATH}/select-article-source.sql
    echo "select source,count(content_id) from t_content_2017_05 where discovery_time>='${YESTERDAY_FORMAT} 00:00:00' and discovery_time<'${TODAY_FORMAT} 00:00:00' group by source;" >>${LOCAL_TMP_PATH}/select-article-source.sql
    mysql -h${MYSQL_HOST[0]} -P${MYSQL_PORT[0]} -u${MYSQL_USER[0]} -p${MYSQL_PWD[0]} -N<${LOCAL_TMP_PATH}/select-article-source.sql >${LOCAL_DATA_PATH}/stat-source-article.list

        awk 'BEGIN{
            FS = "\t"
            OFS = "\t"
        }{
            if(ARGIND==1) {
                dict[$1] = $2
            } else if(ARGIND==2) {
                if ($1 in dict) {
                    print $1, $2, $3, $4, $5, $6, ($7+0), ($8+0), ($9+0), ($10+0), ($11+0), ($12+0), ($13+0), dict[$1]
                } else {
                    print $1, $2, $3, $4, $5, $6, ($7+0), ($8+0), ($9+0), ($10+0), ($11+0), ($12+0), ($13+0), 0
                }
            }
        }' ${LOCAL_DATA_PATH}/stat-source-article.list ${LOCAL_DATA_PATH}/stat-user-merge-parse.list >${LOCAL_DATA_PATH}/stat-user-merge-tmp.list

        echo "USE dashboard;" >${LOCAL_TMP_PATH}/query-daily-article.sql
        echo "set names utf8;select 'all', 'all', sum(request), sum(impression) from t_daily_article_operate_${YESTERDAY} where user_type='all' union all select app, 'all', sum(request), sum(impression) from t_daily_article_operate_${YESTERDAY} where user_type='all' group by app union all select 'all', language, sum(request), sum(impression) from t_daily_article_operate_${YESTERDAY} where user_type='all' group by language union all select app, language, sum(request), sum(impression) from t_daily_article_operate_${YESTERDAY} where user_type='all' group by app, language;" >>${LOCAL_TMP_PATH}/query-daily-article.sql
        
        mysql -h${MYSQL_HOST[2]} -P${MYSQL_PORT[2]} -u${MYSQL_USER[2]} -p${MYSQL_PWD[2]} -N<${LOCAL_TMP_PATH}/query-daily-article.sql >${LOCAL_DATA_PATH}/stat-query-daily-article.list

        awk 'BEGIN{
            FS = "\t"
            OFS = "\t"
        }{
            if (ARGIND ==1) {
                dict[$1"\t"$2] = $3"\t"$4
            } else if (ARGIND == 2) {
                if ($2"\t"$3 in dict) {
                    print $1, $2, $3, $4, $5, $6, ($7+0), ($8+0), ($9+0), ($10+0), ($11+0), ($12+0), ($13+0), ($14+0), dict[$2"\t"$3]
                } else {
                    print $1, $2, $3, $4, $5, $6, ($7+0), ($8+0), ($9+0), ($10+0), ($11+0), ($12+0), ($13+0), ($14+0), 0, 0
                }
            }
        }' ${LOCAL_DATA_PATH}/stat-query-daily-article.list \
           ${LOCAL_DATA_PATH}/stat-user-merge-tmp.list  >${LOCAL_DATA_PATH}/stat-user-result-tmp.list

        awk 'BEGIN{
                FS = "\t"
                OFS = "\t"
        }{
                timestamp = sprintf("%s-%s-%s 00:00:00", 
                        substr("'${YESTERDAY}'", 1, 4),
                        substr("'${YESTERDAY}'", 5, 2),
                        substr("'${YESTERDAY}'", 7, 2))
                print "0", timestamp, $1, "all", $2, $3, $4, $5, $6, ($7+0), ($8+0), ($9+0), ($10+0), ($11+0), ($12+0), ($13+0), ($14+0), ($15+0), ($16+0)
        }END{
        }' ${LOCAL_DATA_PATH}/stat-user-result-tmp.list >${LOCAL_DATA_PATH}/stat-user-result.list

        echo "USE dashboard;" >${LOCAL_TMP_PATH}/load-daily-user.sql
        echo "LOAD DATA LOCAL INFILE '${LOCAL_DATA_PATH}/stat-user-result.list' INTO TABLE \
                t_daily_source FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n' IGNORE 0 LINES;" \
                >>${LOCAL_TMP_PATH}/load-daily-user.sql

        mysql -h${MYSQL_HOST[2]} -P${MYSQL_PORT[2]} -u${MYSQL_USER[2]} -p${MYSQL_PWD[2]} \
                <${LOCAL_TMP_PATH}/load-daily-user.sql

        return 0
}

prepare

mysql_select

start_spark

stat_merge
