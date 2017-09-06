#!/bin/bash
function prepare()
{
        source ../conf/project.conf
        source ../conf/hadoop.conf
        source ../conf/mysql.conf

        mkdir -p ${LOCAL_DATA_PATH}
        mkdir -p ${LOCAL_TMP_PATH}
	APP_NAME=STAT-USER-LTV

	timestamp=${BEF_THIRTYDAY:0:4}"-"${BEF_THIRTYDAY:4:2}"-"${BEF_THIRTYDAY:6:2}" 00:00:00"
        return 0
}

#        --conf "spark.driver.extraJavaOptions=-XX:+UseParNewGC -Xss8M -Xmn512M -Xms3072M -Xmx3072M -XX:+UseConcMarkSweepGC -XX:MaxPermSize=512M -XX:+PrintGCDetails -verbose:gc -XX:+PrintGCTimeStamps" \
function stat_spark()
{
        ${HADOOP_RMR} ${MR_STAT_LTV_RESULT_PATH} 
/usr/lib/spark/bin/spark-submit --master yarn-cluster \
        --name ${APP_NAME} \
        --executor-memory 2G \
        --executor-cores 1 \
        --num-executors 15 \
        --driver-memory 2G \
        --queue offline \
        --conf spark.yarn.executor.memoryOverhead=2048 \
        --conf "spark.speculation=true" \
        --conf "spark.speculation.interval=100" \
        --conf "spark.speculation.quantile=0.75" \
        --conf "spark.speculation.multiplier=1.5" \
	--conf spark.yarn.driver.memoryOverhead=2048 \
        --conf "spark.ui.port=45044" \
        --conf "spark.storage.memoryFraction=0.7" \
        --conf "spark.ui.showConsoleProgress=false" \
	--conf "spark.executor.extraJavaOptions=-Xss2M -XX:+PrintGCDetails -XX:+PrintGCTimeStamps" \
        --conf "spark.locality.wait=1s" \
        --jars ${LOCAL_LIB_PATH}/fastjson-1.2.7.jar \
        --class com.inveno.news.stat.StatUserLTV ${LOCAL_LIB_PATH}/stat-ltv-daily-1.0-SNAPSHOT.jar ${MR_STAT_CURR_UPDATE_ACCU_LTV_PATH} ${MR_STAT_NEWUSER_INPUT_PATH} ${MR_STAT_IMP_INPUT_PATH} ${MR_STAT_CLICK_INPUT_PATH} ${MR_STAT_LISTPAGE_DWELLTIME_INPUT_PATH} ${MR_STAT_DETAIL_DWELLTIME_INPUT_PATH} ${MR_STAT_LTV_RESULT_PATH} 

        return 0
}
        
function get_result_data()
{
        ${HADOOP_CAT} ${MR_STAT_LTV_RESULT_PATH}/* > ${LOCAL_DATA_PATH}/ltv-result-tmp.list
        cat ${LOCAL_DATA_PATH}/ltv-result-tmp.list|python stat-result-parse.py > ${LOCAL_DATA_PATH}/ltv-parse.list
        awk 'BEGIN{
                FS="\t"
                OFS="\t"
        }{
                print 0,"'"${timestamp}"'", $0
        }END{
                
        }' ${LOCAL_DATA_PATH}/ltv-parse.list \
        > ${LOCAL_DATA_PATH}/ltv-result.list

}

function insert_sql()
{
	
	echo "USE dashboard;" >${LOCAL_TMP_PATH}/load-user-ltv-new.sql
	echo "delete from t_user_ltv_new_test where timestamp='${timestamp}';" >> ${LOCAL_TMP_PATH}/load-user-ltv-new.sql 
        echo "LOAD DATA LOCAL INFILE '${LOCAL_DATA_PATH}/ltv-result.list' INTO TABLE \
                t_user_ltv_new_test FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n';" \
                >>${LOCAL_TMP_PATH}/load-user-ltv-new.sql

        mysql -h${MYSQL_HOST[2]} -P${MYSQL_PORT[2]} -u${MYSQL_USER[2]} -p${MYSQL_PWD[2]} \
                <${LOCAL_TMP_PATH}/load-user-ltv-new.sql
}

prepare

stat_spark

get_result_data

#insert_sql

