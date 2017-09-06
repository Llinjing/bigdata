#!/bin/bash

function prepare()
{
        source ../conf/project.conf
        source ../conf/hadoop.conf
        source ../conf/mysql.conf
        source /etc/profile

        mkdir -p ${LOCAL_DATA_PATH}
        mkdir -p ${LOCAL_TMP_PATH}

        APP_NAME="emotion-user"
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

#        --conf "spark.driver.extraJavaOptions=-XX:+UseParNewGC -Xss8M -Xmn512M -Xms2048M -Xmx2048M -XX:+UseConcMarkSweepGC -XX:MaxPermSize=512M -XX:+PrintGCDetails -verbose:gc -XX:+PrintGCTimeStamps" \

function start_user_spark()
{
        aws  s3  sync . ${MR_STAT_EMOTION_USER_OUTPUT_PATH}  --delete
        ${HADOOP_RMR} ${MR_STAT_EMOTION_USER_OUTPUT_PATH}

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
        --class com.inveno.news.stat.StatBallotUser ${LOCAL_LIB_PATH}/stat-ballot-daily-1.0-SNAPSHOT.jar \
        ${MR_STAT_EMOTION_USER_INPUT_PATH} ${MR_STAT_PROFILE_USER_INPUT_PATH} ${MR_STAT_FRESH_USER_OUTPUT_PATH} ${MR_STAT_EMOTION_USER_OUTPUT_PATH}
        return 0
}


function stat_user_result()
{
    ${HADOOP_CAT} ${MR_STAT_EMOTION_USER_OUTPUT_PATH}/part* > ${LOCAL_DATA_PATH}/tmp_user_result.list
    cat ${LOCAL_DATA_PATH}/tmp_user_result.list |\
    python stat-result-parse.py| \
    awk 'BEGIN{ 
        FS="\t"
        OFS="\t"
    }{
        key=$1"\t"$2"\t"$4     
        all[key]
        if($3 == "bore")
        {
            bore_user[key]+=$5
            bore_cnt[key]+=$6
        }
        if($3 == "like")
        {
            like_user[key]+=$5
            like_cnt[key]+=$6
        }
        if($3 == "angry")
        {
            angry_user[key]+=$5
            angry_cnt[key]+=$6
        }
        if($3 == "sad")
        {
           sad_user[key]+=$5 
           sad_cnt[key]+=$6 
        }
        if($3 == "ballot")
        {
            ballot_user[key]+=$5
            ballot_cnt[key]+=$6
        }

    }END{
       for(key in all) 
       {
            print 0, "'"${timestamp}"'", key, (bore_user[key]+0), (bore_cnt[key]+0), (like_user[key]+0), (like_cnt[key]+0), (angry_user[key]+0), (angry_cnt[key]+0), (sad_user[key]+0), (sad_cnt[key]+0), (ballot_user[key]+0), (ballot_cnt[key]+0)
       }
    
    }'  > ${LOCAL_DATA_PATH}/user_result_finish.list
    
}


function insert_user_sql()
{
    echo "USE dashboard;" >${LOCAL_TMP_PATH}/load-daily-emotion-user-stat.sql
    echo "delete from t_daily_ballot_user_test where timestamp='${timestamp}';" >>${LOCAL_TMP_PATH}/load-daily-emotion-user-stat.sql
    echo "LOAD DATA LOCAL INFILE '${LOCAL_DATA_PATH}/user_result_finish.list' INTO TABLE \
                t_daily_ballot_user_test FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n';" \
                >>${LOCAL_TMP_PATH}/load-daily-emotion-user-stat.sql

        mysql -h${MYSQL_HOST[2]} -P${MYSQL_PORT[2]} -u${MYSQL_USER[2]} -p${MYSQL_PWD[2]} \
                <${LOCAL_TMP_PATH}/load-daily-emotion-user-stat.sql  
}

echo "-----------emotion user begin-------------"

prepare

get_and_upload_fresh

start_user_spark

stat_user_result

insert_user_sql

echo "-----------emotion user end-------------"
