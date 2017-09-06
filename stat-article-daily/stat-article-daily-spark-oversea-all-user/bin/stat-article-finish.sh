#!/bin/bash

function prepare()
{
	source ../conf/project.conf
	source ../conf/hadoop.conf
	source ../conf/mysql.conf

	mkdir -p ${LOCAL_DATA_PATH}
    ${HADOOP_RMR} ${MR_WORK_PATH}/stat-article-info
    ${HADOOP_RMR} ${MR_WORK_PATH}/stat-article-feedback

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
        --jars ../lib/fastjson-1.2.7.jar \
        --class com.inveno.news.stat.MergeArticleStat ${LOCAL_LIB_PATH}/stat-article-daily-spark-oversea-all-user-0.0.1.jar \
        ${MR_STAT_ARTICLE_PARSE_INPUT_PATH} ${MR_WORK_PATH}/stat-article-info ${MR_STAT_ARTICLE_REQUEST_INPUT_PATH} ${MR_STAT_ARTICLE_IMPRESSION_INPUT_PATH} ${MR_STAT_ARTICLE_CLICK_INPUT_PATH} ${MR_STAT_ARTICLE_DWELLTIME_INPUT_PATH} ${MR_WORK_PATH}/stat-article-feedback

    return 0
}

function stat_article_finish()
{
        ${HADOOP_CAT} ${MR_WORK_PATH}/stat-article-info/* >${LOCAL_DATA_PATH}/stat-article-info-tmp.list
        ${HADOOP_CAT} ${MR_WORK_PATH}/stat-article-feedback/* >${LOCAL_DATA_PATH}/stat-article-feedback-tmp.list
        less ${LOCAL_DATA_PATH}/stat-article-info-tmp.list | python ${LOCAL_BIN_PATH}/stat-article-info-parse.py 1>${LOCAL_DATA_PATH}/stat-article-info.list
        less ${LOCAL_DATA_PATH}/stat-article-feedback-tmp.list | python ${LOCAL_BIN_PATH}/stat-article-feedback-parse.py 1>${LOCAL_DATA_PATH}/stat-article-feedback.list

	    timestamp=${YESTERDAY:0:4}'-'${YESTERDAY:4:2}'-'${YESTERDAY:6:2}" 00:00:00"

	    awk 'BEGIN{
                FS = "\t"
                OFS = "#\t#"
        }{
                if(ARGIND==1) {
			        info[$1]
                } else if(ARGIND==2) {
                    if ($14 in info) {
				        print 0, "'"${timestamp}"'", $14, $1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $15, $16, $17, $18, "all"
                    }
                }
        }' ${LOCAL_DATA_PATH}/stat-article-info.list \
		${LOCAL_DATA_PATH}/stat-article-feedback.list >${LOCAL_DATA_PATH}/stat-article-finish.list

        table_name_fix=${YESTERDAY:0:4}${YESTERDAY:4:2}${YESTERDAY:6:2}
        echo "USE dashboard;" >${LOCAL_TMP_PATH}/load-daily-article.sql
        echo "LOAD DATA LOCAL INFILE '${LOCAL_DATA_PATH}/stat-article-finish.list' INTO TABLE \
                t_daily_article_${table_name_fix} FIELDS TERMINATED BY '#\t#' LINES TERMINATED BY '\n' IGNORE 0 LINES;" \
                >> ${LOCAL_TMP_PATH}/load-daily-article.sql
        mysql -h${MYSQL_HOST[2]} -P${MYSQL_PORT[2]} -u${MYSQL_USER[2]} -p${MYSQL_PWD[2]}< ${LOCAL_TMP_PATH}/load-daily-article.sql

    	return 0
}

function stat_article_operate_finish()
{
	    awk 'BEGIN{
                FS = "\t"
                OFS = "\t"
        }{
		key = $14"\t"$1"\t"$2"\t"$4"\t"$5"\t"$6"\t"$8
                sum_request[key] += $15
                sum_impression[key] += $16
                sum_click[key] += $17
                sum_dwelltime[key] += $18
        }END{
                for (key in sum_request)
                {
                        print key, sum_request[key], sum_impression[key], sum_click[key], sum_dwelltime[key]
                }
        }' ${LOCAL_DATA_PATH}/stat-article-feedback.list >${LOCAL_DATA_PATH}/stat-article-feedback-operate.list

        touch ${LOCAL_DATA_PATH}/stat-article-feedback-operate.list
	    touch ${LOCAL_DATA_PATH}/stat-article-category-info-tmp.list

        mysql -h${MYSQL_HOST[1]} -P${MYSQL_PORT[1]} -u${MYSQL_USER[1]} -p${MYSQL_PWD[1]} -N<${LOCAL_TMP_PATH}/load-category.sql >${LOCAL_DATA_PATH}/stat-article-category.list

	    timestamp=${YESTERDAY:0:4}'-'${YESTERDAY:4:2}'-'${YESTERDAY:6:2}" 00:00:00"
        awk 'BEGIN{
                FS = "\t"
                OFS = "#\t#"
        }{
                if(ARGIND==1){
			        info[$1] = $2"#\t#"$3"#\t#"$4"#\t#"$5"#\t#"$6"#\t#"$7"#\t#"$8"#\t#"$9"#\t#"$10"#\t#"$11"#\t#"$12"#\t#""unknown""#\t#"$14
		        } else if(ARGIND==2){
                        if($1 in info){
                                print 0, "'"${timestamp}"'", $1, $2, $3, $4, $5, $6, $7, info[$1], $8, $9, $10, $11, "all"
                        }
                }
        }' ${LOCAL_DATA_PATH}/stat-article-info.list ${LOCAL_DATA_PATH}/stat-article-feedback-operate.list >${LOCAL_DATA_PATH}/stat-article-finish-operate.list

        table_name_fix=${YESTERDAY:0:4}${YESTERDAY:4:2}${YESTERDAY:6:2}

        echo "USE dashboard;" >${LOCAL_TMP_PATH}/load-daily-article-operate.sql
        echo "LOAD DATA LOCAL INFILE '${LOCAL_DATA_PATH}/stat-article-finish-operate.list' INTO TABLE \
                t_daily_article_operate_${table_name_fix} FIELDS TERMINATED BY '#\t#' LINES TERMINATED BY '\n' IGNORE 0 LINES;" \
                >> ${LOCAL_TMP_PATH}/load-daily-article-operate.sql

        mysql -h${MYSQL_HOST[2]} -P${MYSQL_PORT[2]} -u${MYSQL_USER[2]} -p${MYSQL_PWD[2]} \
                < ${LOCAL_TMP_PATH}/load-daily-article-operate.sql

        return 0
}

prepare

start_spark

stat_article_finish

stat_article_operate_finish
