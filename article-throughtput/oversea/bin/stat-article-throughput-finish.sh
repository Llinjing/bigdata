#!/bin/bash

function prepare()
{
    source ../conf/project.conf
    source ../conf/hadoop.conf
    source ../conf/mysql.conf
    source ../conf/warning.conf

    for file in `ls ${LOCAL_RUNNING_TASK_POOL_PATH}`
    do
            cat ${LOCAL_RUNNING_TASK_POOL_PATH}/${file} > ${LOCAL_DATA_PATH}/running-task.list
    done
    touch ${LOCAL_DATA_PATH}/running-task.list	

    return 0
}

function mv_last_feedback_file()
{
	local task_file=`head -1 ${LOCAL_DATA_PATH}/running-task.list`
    local task_file_length=`echo ${#task_file}`
    local curr_date=`echo ${task_file:task_file_length-15:8}`
	local curr_time=`echo ${task_file:task_file_length-6:4}`
    echo ${curr_date}
	echo ${curr_time}
	
	local history_path=${MR_DATA_PATH}/article-throughput/history/${curr_date}/${curr_time}
	${HADOOP_MKDIR}  ${history_path}
    ${HADOOP_RMFILE} ${history_path}/*
	${HADOOP_MV} ${MR_DATA_PATH}/article-total-feedback/* ${history_path}/
	if [ $? -eq 0 ]
	then
        ${HADOOP_RMFILE} ${MR_DATA_PATH}/article-total-feedback/*
		${HADOOP_MV} ${MR_STAT_ARTICLE_THROUGHPUT_OUTPUT_PATH}/* ${MR_DATA_PATH}/article-total-feedback/
        if [ $? -ne 0 ]
        then
            ${HADOOP_MV} ${MR_STAT_ARTICLE_THROUGHPUT_OUTPUT_PATH}/* ${MR_DATA_PATH}/article-total-feedback/  
            ${WARNING_BIN} "${PRODUCT_LINE}-stat-article-throught-mv-data-failed"  "${SMS_USER[$INFO]}"
        fi
    else
        ${WARNING_BIN} "${PRODUCT_LINE}-stat-article-throught-mv-data-to-history-failed,-try-again"  "${SMS_USER[$INFO]}"
        ${HADOOP_MV} ${MR_DATA_PATH}/article-total-feedback/* ${history_path}/
        if [ $? -eq 0 ]
        then
            ${HADOOP_RMFILE} ${MR_DATA_PATH}/article-total-feedback/*
            ${HADOOP_MV} ${MR_STAT_ARTICLE_THROUGHPUT_OUTPUT_PATH}/* ${MR_DATA_PATH}/article-total-feedback/
            if [ $? -ne 0 ]
            then
                ${HADOOP_MV} ${MR_STAT_ARTICLE_THROUGHPUT_OUTPUT_PATH}/* ${MR_DATA_PATH}/article-total-feedback/
                ${WARNING_BIN} "${PRODUCT_LINE}-stat-article-throught-mv-data-failed"  "${SMS_USER[$INFO]}"
            fi
        fi
	fi

	return 0
}

function get_article_thoughtput()
{
	${HADOOP_CAT} ${MR_DATA_PATH}/article-total-feedback/part-*B > ${LOCAL_DATA_PATH}/article-thoughtput.list

	local task_file=`head -1 ${LOCAL_DATA_PATH}/running-task.list` 
	local task_file_length=`echo ${#task_file}`
	local timestamp=`echo ${task_file:task_file_length-15:8}``echo ${task_file:task_file_length-6:4}`
	echo ${timestamp}

	awk 'BEGIN{
		FS=OFS="\t"
	}{
		# product_id<\t>language<\t>content_type<\t>platform<\t>type<\t>article_id
        dict[$2"\t"$3"\t"$4"\t"$5"\t"$7"\t"$1]
        dict[$2"\t"$3"\t""all""\t"$5"\t"$7"\t"$1]
        
        dict[$2"\t""all""\t"$4"\t"$5"\t"$7"\t"$1]
        dict[$2"\t""all""\t""all""\t"$5"\t"$7"\t"$1]
        
        dict["all""\t"$3"\t"$4"\t"$5"\t"$7"\t"$1]
        dict["all""\t"$3"\t""all""\t"$5"\t"$7"\t"$1]
        
        dict["all""\t""all""\t"$4"\t"$5"\t"$7"\t"$1]
        dict["all""\t""all""\t""all""\t"$5"\t"$7"\t"$1]
    }END{
		for (key in dict)
		{
			n = split(key, arr, "\t")
			product_ids[arr[1]"\t"arr[2]"\t"arr[3]"\t"arr[4]"\t"arr[5]]++
		}

        timestamp_day = sprintf("%s-%s-%s 00:00:00",
                substr("'${timestamp}'", 1, 4),
                substr("'${timestamp}'", 5, 2),
                substr("'${timestamp}'", 7, 2))

        timestamp_hour = sprintf("%s-%s-%s %s:00:00",
                substr("'${timestamp}'", 1, 4),
                substr("'${timestamp}'", 5, 2),
                substr("'${timestamp}'", 7, 2),
                substr("'${timestamp}'", 9, 2))

        timestamp_min = sprintf("%s-%s-%s %s:%s:00",
                substr("'${timestamp}'", 1, 4),
                substr("'${timestamp}'", 5, 2),
                substr("'${timestamp}'", 7, 2),
                substr("'${timestamp}'", 9, 2),
                substr("'${timestamp}'", 11, 2))

        print "id", "timestamp_day", "timestamp_hour", "timestamp_min", "product_id", "language", "content_type", "platform", "type", "article_count"
        for (key in product_ids)
        {
                print "0", timestamp_day, timestamp_hour, timestamp_min, key, product_ids[key]
        }
    }' ${LOCAL_DATA_PATH}/article-thoughtput.list > ${LOCAL_DATA_PATH}/article-thoughtput-mysql.list
}

function insert_into_mysql()
{
    	echo "USE dashboard;" > ${LOCAL_DATA_PATH}/load-article-throughput.sql
        echo "LOAD DATA LOCAL INFILE '${LOCAL_DATA_PATH}/article-thoughtput-mysql.list' INTO TABLE \
		t_article_throughput_stat FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n' IGNORE 1 LINES;" \
                >> ${LOCAL_DATA_PATH}/load-article-throughput.sql

        mysql -h${MYSQL_HOST[2]} -P${MYSQL_PORT[2]} -u${MYSQL_USER[2]} -p${MYSQL_PWD[2]} \
                <${LOCAL_DATA_PATH}/load-article-throughput.sql

}

prepare
mv_last_feedback_file
get_article_thoughtput
insert_into_mysql
