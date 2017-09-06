#!/bin/sh

# author: huanghaifeng

function prepare()
{
	source ../conf/project.conf
	source ../conf/mysql.conf
	source ../conf/warning.conf
}

function get_article_ids_from_redis()
{
    ${REDIS_BIN} -c -h ${REDIS_SERVER} -p ${REDIS_PORT} HGETALL ${REDIS_KEY}-0 > ${LOCAL_DATA_PATH}/monitor-valid-article-gmp.list
    for ((i=1; i<${REDIS_KEY_PARTITIONS}; i++)) 
    do  
        ${REDIS_BIN} -c -h ${REDIS_SERVER} -p ${REDIS_PORT} HGETALL ${REDIS_KEY}-${i} >> ${LOCAL_DATA_PATH}/monitor-valid-article-gmp.list
    done

    cat ${LOCAL_DATA_PATH}/monitor-valid-article-gmp.list  | python ${LOCAL_BIN_PATH}/data-monitor-valid-gmp-parse.py  > ${LOCAL_DATA_PATH}/monitor-valid-article-gmp-parse.tmp

    cat ${LOCAL_DATA_PATH}/monitor-valid-article-gmp-parse.tmp | awk 'BEGIN{
        FS="\t";
        OFS="\t"
        OFMT="%.8f"
    }{
        article_id = $1;
        for(i=2; i<=NF; i++)
        {
            if($i)
            {
                split($i, tmp_arr, ",")
                if(tmp_arr[1] > 0)
                {
                    print article_id, tmp_arr[1], tmp_arr[2]
                }
            }
        }
    }END{
    }' > ${LOCAL_DATA_PATH}/monitor-valid-article-gmp-parse.list
}

function get_data_info_from_cluster()
{
	export HADOOP_HOME=/usr/lib/hadoop
	export PATH=/usr/local/sbin:/sbin:/bin:/usr/sbin:/usr/bin:/usr/lib/hadoop/bin:/usr/lib/hive//bin:/usr/lib/spark/bin:/usr/lib/hadoop-yarn/bin:/usr/local/zookeeper/bin:/root/bin

    local curr_time=`date +%Y%m%d%H%M -d"-10 min"`  
    local curr_date=`echo "${curr_time}" | awk '{
            print substr($1, 1, 8)
    }'`
    local curr_hour=`echo "${curr_time}" | awk '{
            print substr($1, 9, 2)
    }'`
    local curr_min=`echo "${curr_time}" | awk '{
            print sprintf("%02d", int(substr($1, 11, 2) / 10) * 10)
    }'`
    local path=s3://bigdata-east/inveno-data/format-data/topic/request-reformat/${curr_date}/${curr_hour}/request-reformat.${curr_date}_${curr_hour}${curr_min}*
	echo ${path}
	hadoop fs -cat ${path} > ${LOCAL_DATA_PATH}/monitor-impression-reformat.list
	cat ${LOCAL_DATA_PATH}/monitor-impression-reformat.list | python ${LOCAL_BIN_PATH}/data-monitor-impression-reformat-parse.py 1>>${LOCAL_DATA_PATH}/monitor-article-info.list 2>${LOCAL_LOG_PATH}/monitor-article-info.err
    sort ${LOCAL_DATA_PATH}/monitor-article-info.list | uniq | sort -o ${LOCAL_DATA_PATH}/monitor-article-info.list

    path=s3://bigdata-east/inveno-data/format-data/content-signal/toHDFS/${curr_date}/${curr_hour}/*${curr_hour}${curr_min}*
    echo ${path}
    hadoop fs -cat ${path} > ${LOCAL_DATA_PATH}/monitor-content-signal.list
    cat ${LOCAL_DATA_PATH}/monitor-content-signal.list | python ${LOCAL_BIN_PATH}/data-monitor-content-signal-parse.py 1>>${LOCAL_DATA_PATH}/monitor-article-info-signal.list 2>${LOCAL_LOG_PATH}/monitor-article-info-signal.err
    sort ${LOCAL_DATA_PATH}/monitor-article-info-signal.list | uniq | sort -o ${LOCAL_DATA_PATH}/monitor-article-info-signal.list
}

function merge_gmp_and_info()
{
    awk 'BEGIN{
        FS = "\t"
        OFS = "\t"
        OFMT="%.8f"
    }{
        if(ARGIND == 1)
        {
            article_gmp[$1"###"$2] = $3
        }
        else if(ARGIND == 2)
        {
            article_info[$1] = $2"###"$3
        }
        else if(ARGIND == 3)
        {
            article_category_map[$1] = $2
        }
        else if(ARGIND == 4)
        {
            article_info_signal[$1] = $2"###"$3"###"$4"###"$5"###"$6"###"article_category_map[$7]
        }
    }END{
        for(key in article_gmp)
        {
            split(key, tmp_arr, "###")
            article_id = tmp_arr[1]
            product_id = tmp_arr[2]

            if(article_id in article_info)
            {
                result =  product_id"###"article_info[article_id]
            }
            else
            {
                result =  product_id"###unknown###unknown"
            }

            if(article_id in article_info_signal)
            {
                result =  result"###"article_info_signal[article_id]
            }
            else
            {
                result =  result"###unknown###unknown###unknown###unknown###unknown###unknown"
            }

            print result, article_gmp[key]
        }
    }' ${LOCAL_DATA_PATH}/monitor-valid-article-gmp-parse.list ${LOCAL_DATA_PATH}/monitor-article-info.list ${LOCAL_DATA_PATH}/monitor-article-category-map.list ${LOCAL_DATA_PATH}/monitor-article-info-signal.list > ${LOCAL_DATA_PATH}/monitor-article-gmp-result.tmp
}

function get_monitor_result()
{
    touch ${LOCAL_DATA_PATH}/monitor-article-gmp-result.tmp
    local curr_day=`date +%Y%m%d -d"-0 min"`
    local curr_hour_min=`date +%H%M -d"-0 min"`
    curr_hour_min=`echo "${curr_hour_min}" | awk '{
           print substr($1, 1, 3)
    }'`
    local curr_time=${curr_day}${curr_hour_min}"0"
    awk 'BEGIN{
        FS = "\t"
        OFS = "\t"
        OFMT="%.8f"
    }{
        key = $1
        gmp = $2
        article_count[key] ++
        article_gmp_sum[key] += gmp
        if(gmp > article_max_gmp[key])
        {
            article_max_gmp[key] = gmp
        }
        if(gmp >= 0.02)
        {
            article_gmp_ge_2_percent[key] ++
        }
        if(gmp >= 0.04)
        {
            article_gmp_ge_4_percent[key] ++
        }
        if(gmp >= 0.08)
        {
            article_gmp_ge_8_percent[key] ++
        }
    }END{
        ##print "id", "timestamp", "product_id", "article_count", "gmp_sum", "max_gmp", "ge_8_percent_article_count", "ge_4_percent_article_count",  "ge_2_percent_article_count", "language", "content_type", "publisher", "source", "sourceType", "body_images_count", "rate", "type", "category"
        for(key in article_count)
        {
            split(key, tmp_arr, "###")
            print "'${curr_time}'", tmp_arr[1], article_count[key]+0, article_gmp_sum[key]+0.0, article_max_gmp[key]+0.0, article_gmp_ge_8_percent[key]+0, article_gmp_ge_4_percent[key]+0, article_gmp_ge_2_percent[key]+0, tmp_arr[2], tmp_arr[3], tmp_arr[4], tmp_arr[5], tmp_arr[6], tmp_arr[7], tmp_arr[8], "impression", tmp_arr[9]
        }
    }' ${LOCAL_DATA_PATH}/monitor-article-gmp-result.tmp > ${LOCAL_DATA_PATH}/monitor-article-gmp-result.list
}

function insert_into_kafka()
{
    cat ${LOCAL_DATA_PATH}/monitor-article-gmp-result.list | grep noticias | python ${LOCAL_BIN_PATH}/data-monitor-result-json.py  1>${LOCAL_DATA_PATH}/monitor-article-gmp-result-json.list
    cat ${LOCAL_DATA_PATH}/monitor-article-gmp-result-json.list | ${KAFKA_CONSOLE_PRODUCER} --broker-list ${BROKER_LIST} -topic ${ARTICLE_GMP_MONITOR_TOPIC}
    #cat ${LOCAL_DATA_PATH}/monitor-article-gmp-result-json.list | ${KAFKA_CONSOLE_PRODUCER} --broker-list ${BROKER_LIST} -topic ${ARTICLE_GMP_MONITOR_TEST_TOPIC}
    return 0
}

function request_gmp_monitor()
{
    sh -x ${LOCAL_BIN_PATH}/data-monitor-request.sh 1>${LOCAL_LOG_PATH}/data-monitor-request.log 2>&1
    return 0
}

function singapore_impression_gmp_monitor()
{
    sh -x ${LOCAL_BIN_PATH}/data-monitor-singapore-impression.sh 1>${LOCAL_LOG_PATH}/data-monitor-singapore-impression.log 2>&1
    return 0
}

function insert_into_mysql()
{
    echo "USE dashboard;" > ${LOCAL_DATA_PATH}/monitor-article-gmp-result.sql
    echo "LOAD DATA LOCAL INFILE '${LOCAL_DATA_PATH}/monitor-article-gmp-result.list' INTO TABLE \
        t_article_gmp_monitor FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n' IGNORE 1 LINES;" \
            >>${LOCAL_DATA_PATH}/monitor-article-gmp-result.sql
    mysql -h${MYSQL_HOST[0]} -P${MYSQL_PORT[0]} -u${MYSQL_USER[0]} -p${MYSQL_PWD[0]} < ${LOCAL_DATA_PATH}/monitor-article-gmp-result.sql
}

prepare
get_article_ids_from_redis
get_data_info_from_cluster
merge_gmp_and_info
get_monitor_result
insert_into_kafka

request_gmp_monitor
singapore_impression_gmp_monitor

