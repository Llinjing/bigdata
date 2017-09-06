#!/bin/bash
function prepare()
{
        source ../conf/project.conf
        source ../conf/hadoop.conf
        source ../conf/mysql.conf

        mkdir -p ${LOCAL_DATA_PATH}
        mkdir -p ${LOCAL_TMP_PATH}
	APP_NAME=LTV-UpdateAccumulation

        ${HADOOP_RMR} ${MR_STAT_CURR_UPDATE_ACCU_LTV_PATH}

        return 0
}

#        --conf "spark.driver.extraJavaOptions=-XX:+UseParNewGC -Xss8M -Xmn512M -Xms3072M -Xmx3072M -XX:+UseConcMarkSweepGC -XX:MaxPermSize=512M -XX:+PrintGCDetails -verbose:gc -XX:+PrintGCTimeStamps" \
function stat_spark()
{
/usr/lib/spark/bin/spark-submit --master yarn-cluster \
        --name ${APP_NAME} \
        --executor-memory 2G \
        --executor-cores 1 \
        --num-executors 20 \
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
        --class com.inveno.news.stat.UpdateAccumulation ${LOCAL_LIB_PATH}/stat-ltv-daily-1.0-SNAPSHOT.jar ${MR_STAT_BEF_UPDATE_ACCU_LTV_PATH} ${MR_STAT_IMP_INPUT_PATH} ${MR_STAT_CLICK_INPUT_PATH} ${MR_STAT_LISTPAGE_DWELLTIME_INPUT_PATH} ${MR_STAT_DETAIL_DWELLTIME_INPUT_PATH} ${MR_STAT_CURR_UPDATE_ACCU_LTV_PATH}

        return 0
}
        
function get_result_data
{
        ${HADOOP_CAT} ${MR_STAT_USER_EXTEND_OUTPUT_PATH}/* > ${LOCAL_DATA_PATH}/user-extend-tmp.list
        cat ${LOCAL_DATA_PATH}/user-extend-tmp.list|python stat-result-parse.py > ${LOCAL_DATA_PATH}/user-extend-result-tmp.list
        awk 'BEGIN{
                FS="\t"
                OFS="\t"
        }{
                timestamp = sprintf("%s-%s-%s 00:00:00", 
                        substr("'${YESTERDAY}'", 1, 4),
                        substr("'${YESTERDAY}'", 5, 2),
                        substr("'${YESTERDAY}'", 7, 2))
                print 0,timestamp, $0
        }END{
                
        }' ${LOCAL_DATA_PATH}/user-extend-result-tmp.list \
        > ${LOCAL_DATA_PATH}/user-extend-result.list

}



prepare

stat_spark

