#!/bin/bash

function prepare()
{
        source ../conf/project.conf
        source ../conf/hadoop.conf
        source ../conf/mysql.conf
        source ../conf/decay.conf
	source ../conf/warning.conf

        for file in `ls ${LOCAL_RUNNING_TASK_POOL_PATH}`
        do
                cat ${LOCAL_RUNNING_TASK_POOL_PATH}/${file} > ${LOCAL_DATA_PATH}/running-task.list
        done
	touch ${LOCAL_DATA_PATH}/running-task.list	

        return 0
}

function mv_last_gmp_file()
{
	local task_file=`head -1 ${LOCAL_DATA_PATH}/running-task.list`
        local task_file_length=`echo ${#task_file}`
        local curr_date=`echo ${task_file:task_file_length-13:8}`
	local curr_time=`echo ${task_file:task_file_length-4:4}`
        echo ${curr_date}
	echo ${curr_time}
	
	local history_path=${MR_DATA_PATH}/article-gmp/history/${curr_date}
	${HADOOP_MKDIR} ${history_path}
	${HADOOP_MV} ${MR_DATA_PATH}/latest-article-gmp ${history_path}/${curr_time}
	if [ $? -eq 0 ]
	then
		${HADOOP_MV} ${MR_UPDATE_ARTICLE_GMP_OUTPUT_PATH} ${MR_DATA_PATH}/latest-article-gmp
		#local hive_sql="\"alter table article_gmp add partition (date='${curr_date}', min='${curr_time}') location '${history_path}/${curr_time}/'\""
                #export HADOOP_HOME="/usr/lib/hadoop/"
                #/usr/lib/hive/bin/hive -e ${hive_sql}
		local hive_sql="/usr/lib/hive/bin/hive -e \"set mapred.job.queue.name=realtime; alter table article_gmp add partition (date='${curr_date}', min='${curr_time}') location '${history_path}/${curr_time}/'\""
		echo "export HADOOP_HOME=\"/usr/lib/hadoop/\"" > load_hive_data.sh
		echo ${hive_sql} >> load_hive_data.sh
		sh -x ./load_hive_data.sh 
	fi
	return 0
}

function get_gmp_article_count()
{
	${HADOOP_CAT} ${MR_DATA_PATH}/latest-article-gmp/part-*-A > ${LOCAL_DATA_PATH}/gmp-article-count.list

	local task_file=`head -1 ${LOCAL_DATA_PATH}/running-task.list` 
	local task_file_length=`echo ${#task_file}`
	local timestamp=`echo ${task_file:task_file_length-13:8}``echo ${task_file:task_file_length-4:4}`
	echo ${timestamp}

	awk 'BEGIN{
		FS=OFS="\t"
	}{
		# product_id<\t>article
		dict[$2"\t"$1]
	}END{
		for (key in dict)
		{
			n = split(key, arr, "\t")
			product_ids[arr[1]]++
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

                print "id", "timestamp_day", "timestamp_hour", "timestamp_min", "product_id", "article_count"
                for (key in product_ids)
                {
                        print "0", timestamp_day, timestamp_hour, timestamp_min, key, product_ids[key]
                }
        }' ${LOCAL_DATA_PATH}/gmp-article-count.list > ${LOCAL_DATA_PATH}/gmp-article-count-result.list

	return 0
}

function insert_into_mysql()
{
	echo "USE dashboard;" >${LOCAL_DATA_PATH}/load-minutes-throughput.sql
        echo "LOAD DATA LOCAL INFILE '${LOCAL_DATA_PATH}/gmp-article-count-result.list' INTO TABLE \
		t_minute_throughput FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n' IGNORE 1 LINES;" \
                >>${LOCAL_DATA_PATH}/load-minutes-throughput.sql

        mysql -h${MYSQL_HOST[2]} -P${MYSQL_PORT[2]} -u${MYSQL_USER[2]} -p${MYSQL_PWD[2]} \
                <${LOCAL_DATA_PATH}/load-minutes-throughput.sql

	return 0
}

function write_article_gmp_to_redis()
{
	${HADOOP_CAT} ${MR_DATA_PATH}/latest-article-gmp/part-000[0-9][0-9] >${LOCAL_DATA_PATH}/article-gmp.list
	touch ${LOCAL_DATA_PATH}/article-gmp.list

        local time=`date "+%Y-%m-%d %H:%M:00" -d"-3 day -10 min"`
        ## SELECT min(content_id) FROM db_mta.t_content WHERE discovery_time>="2016-08-08 09:43:40";
        local sql="select min(content_id) from db_mta.t_content where discovery_time>=\""${time}"\""
        echo ${time}
        echo ${sql}
        mysql -h${MYSQL_HOST[3]} -P${MYSQL_PORT[3]} -u${MYSQL_USER[3]} -p${MYSQL_PWD[3]} -e "${sql}" > ${LOCAL_DATA_PATH}/min_3days_article_id.list
        local min_article_id=`sed -n '2,1p' ${LOCAL_DATA_PATH}/min_3days_article_id.list`
        if [ -z "${min_article_id}" ] ; then
                ## baojing
                ${WARNING_BIN} "overseas:article_gmp_monitor_min_3days_article_id_is_null" ${SMS_USER[$FATAL]}
                min_article_id=0
        fi
        echo ${min_article_id} 

	cat ${LOCAL_DATA_PATH}/article-gmp.list | awk 'BEGIN{
                FS = "\t"
                OFS = "\t"
        }{
                ## article_id<\t>product_id<\t>click<\t>impression<\t>ctr<\t>timestamp<\t>total_impression
		if ($1 >= '${min_article_id}')
		{
                	print $0
	                print $1, "-1", $3, $4, $5, $6, $7
		}
        }END{
        }' > ${LOCAL_DATA_PATH}/article-gmp-add-all.list
        touch ${LOCAL_DATA_PATH}/article-gmp-add-all.list

        cat ${LOCAL_DATA_PATH}/article-gmp-add-all.list | awk 'BEGIN{
                FS = "\t"
                OFS = "\t"
		OFMT = "%.8f"
        }{
                dict[$1"\t"$2]
                click[$1"\t"$2] += $3
                impression[$1"\t"$2] += $4
                total_impression[$1"\t"$2] += $7
        }END{
                for (key in dict)
                {
                        #print key, click[key], impression[key], total_impression[key]
                        if( click[key]<impression[key] && impression[key]>0 && total_impression[key]>='${IMPRESSION_THRESHOLD}')
                        {
                                print key, click[key], impression[key], click[key]/impression[key]
                        }
                }
        }' > ${LOCAL_DATA_PATH}/valid-article-gmp.list
        touch ${LOCAL_DATA_PATH}/valid-article-gmp.list

	sh -x ${LOCAL_BIN_PATH}/write-to-redis-cluster.sh ${LOCAL_DATA_PATH}/valid-article-gmp.list
	sh -x ${LOCAL_BIN_PATH}/write-to-kafka.sh > ${LOCAL_LOG_PATH}/write-to-kafka.log
	return 0
}

function data_monitor()
{
        ## KEEP HISTORY DATA
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
        local monitor_time=${curr_date}${curr_hour}${curr_min}
        monitor_time=`date +%Y%m%d%H%M%S`
        cp ${LOCAL_DATA_PATH}/article-gmp-monitor.list ${LOCAL_DATA_PATH}/monitor-data/article-gmp-monitor.list_${monitor_time}

        local time=`date "+%Y-%m-%d %H:%M:00" -d"-2 day -10 min"`
        ## SELECT min(content_id) FROM db_mta.t_content WHERE discovery_time>="2016-08-08 09:43:40";
        local sql="select min(content_id) from db_mta.t_content where discovery_time>=\""${time}"\""
        echo ${time}
        echo ${sql}
        mysql -h${MYSQL_HOST[3]} -P${MYSQL_PORT[3]} -u${MYSQL_USER[3]} -p${MYSQL_PWD[3]} -e "${sql}" > ${LOCAL_DATA_PATH}/min_article_id.list
        local min_article_id=`sed -n '2,1p' ${LOCAL_DATA_PATH}/min_article_id.list`
        if [ -z "${min_article_id}" ] ; then
                ## baojing
                ${WARNING_BIN} "overseas:article_gmp_monitor_min_article_id_is_null" ${SMS_USER[$FATAL]}
                min_article_id=0
        fi
        echo ${min_article_id}  
        awk 'BEGIN{
                FS = "\t"
                OFS = "\t"
		OFMT = "%.8f"
        }{
                if($1 >= '${min_article_id}')
                {
                        all_article[$1"\t"$2]
                        if ($7 >= '${IMPRESSION_THRESHOLD}')
                        {
				if ($5 >= 0)
				{
					valid_all_article[$1"\t"$2]
					sum_gmp[$2] += $5
				}
                                ## valid_all_article[$1"\t"$2]
				## sum_gmp[$2] += $5

                                if ($5 >= 0.08)
                                {
                                        all_article_ge_008[$1"\t"$2]
                                }
                                if ($5 >= 0.04)
                                {
                                        all_article_ge_004[$1"\t"$2]
                                }
				if ($5 >= 0.02)
				{
                                        all_article_ge_002[$1"\t"$2]
                                }
                                if ($5 > max_gmp[$2])
                                {
                                        max_gmp[$2] = $5
                                }
                        }
                }
        }END{
                for (key in all_article)
                {
                        n = split(key, key_arr, "\t")
                        id = key_arr[2]
                        product_id[id]++
                }

                for (key in valid_all_article)
                {
                        n = split(key, key_arr, "\t")
                        id = key_arr[2]
                        product_id_valid[id]++
                }

                for (key in all_article_ge_008)
                {
                        n = split(key, key_arr, "\t")
                        id = key_arr[2]
                        product_id_ge_008[id]++
                }

                for (key in all_article_ge_004)
                {
                        n = split(key, key_arr, "\t")
                        id = key_arr[2]
                        product_id_ge_004[id]++
                }
 
                for (key in all_article_ge_002)
                {
                        n = split(key, key_arr, "\t")
                        id = key_arr[2]
                        product_id_ge_002[id]++
                }

                for (key in product_id)
                {
                        if (key != "")
                        {
                                print key, product_id[key]+0, product_id_valid[key]+0, product_id_ge_008[key]+0, product_id_ge_004[key]+0, max_gmp[key]+0, (product_id_valid[key]==0 ? 0 :sum_gmp[key]/product_id_valid[key]), product_id_ge_002[key]+0
                        }
                }

        }' ${LOCAL_DATA_PATH}/article-gmp.list > ${LOCAL_DATA_PATH}/article-gmp-monitor.list

}

function data_monitor_language()
{
        #${HADOOP_CAT} ${MR_DATA_PATH}/latest-article-gmp/part-000[0-9][0-9] >${LOCAL_DATA_PATH}/article-gmp.list
        touch ${LOCAL_DATA_PATH}/article-gmp.list

        local time=`date "+%Y-%m-%d %H:%M:00" -d"-2 day -10 min"`
        ## SELECT min(content_id) FROM db_mta.t_content WHERE discovery_time>="2016-08-08 09:43:40";
        local sql="select min(content_id) from db_mta.t_content where discovery_time>=\""${time}"\""
        echo ${time}
        echo ${sql}
        mysql -h${MYSQL_HOST[3]} -P${MYSQL_PORT[3]} -u${MYSQL_USER[3]} -p${MYSQL_PWD[3]} -e "${sql}" > ${LOCAL_DATA_PATH}/min_article_id.list
        local min_article_id=`sed -n '2,1p' ${LOCAL_DATA_PATH}/min_article_id.list`
        if [ -z "${min_article_id}" ] ; then
                ## baojing
                ${WARNING_BIN} "overseas:article_gmp_monitor_min_article_id_is_null" ${SMS_USER[$FATAL]}
                min_article_id=0
        fi
        echo ${min_article_id}  
        awk 'BEGIN{
                FS = "\t"
                OFS = "\t"
		OFMT = "%.8f"
        }{
                if($1 >= '${min_article_id}')
                {
			key1 = $1"\t"$2"\t"$8
			key2 = $2"-"$8
                        all_article[key1]
                        if ($7 >= '${IMPRESSION_THRESHOLD}')
                        {
                                if ($5 >= 0)
                                {
                                        valid_all_article[key1]
                                        sum_gmp[key2] += $5
                                }

                                if ($5 >= 0.08)
                                {
                                        all_article_ge_008[key1]
                                }
	
				if ($5 >= 0.04)
                                {
                                        all_article_ge_004[key1]
                                }

                                if ($5 >= 0.02)
                                {
                                        all_article_ge_002[key1]
                                }

                                if ($5 > max_gmp[key2])
                                {
                                        max_gmp[key2] = $5
                                }
                        }
                }
        }END{
                for (key in all_article)
                {
                        n = split(key, key_arr, "\t")
                        id = key_arr[2]"-"key_arr[3]
                        product_id[id]++
                }

                for (key in valid_all_article)
                {
                        n = split(key, key_arr, "\t")
                        id = key_arr[2]"-"key_arr[3]
                        product_id_valid[id]++
                }

                for (key in all_article_ge_008)
                {
                        n = split(key, key_arr, "\t")
                        id = key_arr[2]"-"key_arr[3]
                        product_id_ge_008[id]++
                }

                for (key in all_article_ge_004)
                {
                        n = split(key, key_arr, "\t")
                        id = key_arr[2]"-"key_arr[3]
                        product_id_ge_004[id]++
                }
 
                for (key in all_article_ge_002)
                {
                        n = split(key, key_arr, "\t")
                        id = key_arr[2]"-"key_arr[3]
                        product_id_ge_002[id]++
                }

                for (key in product_id)
                {
                        if (key != "")
                        {
				print key, product_id[key]+0, product_id_valid[key]+0, product_id_ge_008[key]+0, product_id_ge_004[key]+0, max_gmp[key]+0, (product_id_valid[key]==0 ? 0 :sum_gmp[key]/product_id_valid[key]), product_id_ge_002[key]+0
                        }
                }

        }' ${LOCAL_DATA_PATH}/article-gmp.list > ${LOCAL_DATA_PATH}/article-gmp-monitor-language.list

	cat ${LOCAL_DATA_PATH}/article-gmp-monitor-language.list | python monitor-data-parse.py > /data/metric/article-gmp/base.list
}

function save_gmp_data_local()
{
        local task_file=`head -1 ${LOCAL_DATA_PATH}/running-task.list`
        local task_file_length=`echo ${#task_file}`
        local timestamp=`echo ${task_file:task_file_length-13:8}``echo ${task_file:task_file_length-4:4}`
        echo ${timestamp}
        local path=${LOCAL_DATA_PATH}/article-gmp/${timestamp}/
        mkdir -p ${path}

        ${HADOOP_CAT} ${MR_DATA_PATH}/latest-article-gmp/part-*-A > ${path}/article-throughtput.list
        ${HADOOP_CAT} ${MR_DATA_PATH}/latest-article-gmp/part-00[0-9][0-9][0-9] > ${path}/article-gmp.list

}

function get_high_gmp_article()
{

        local min_article_id=`sed -n '2,1p' ${LOCAL_DATA_PATH}/min_article_id.list`
        if [ -z "${min_article_id}" ] ; then
                ## baojing
                ${WARNING_BIN} "overseas:article_gmp_monitor_min_article_id_is_null" ${SMS_USER[$FATAL]}
                min_article_id=0
        fi
        echo ${min_article_id}

	awk 'BEGIN{
		FS = "\t"
		OFS = "\t"
	}{
		if(ARGIND == 1)
		{
			high_gmp_article_7_percent[$1]
		}
		else if(ARGIND == 2)
		{
			high_gmp_article_8_percent[$1]
		}
		else if(ARGIND == 3)
		{
                        key = $2"-"$8
                        dict[key]
                        total_request = $7
                        gmp = $5
		
			if($1 in high_gmp_article_7_percent)
			{
				print $0, "alive-7"
			}
			else 
			{
				if('${min_article_id}'<$1 && total_request > 200)
				{
					if(gmp >= 0.07)
					{
						high_gmp_ge_07[key]++
						print $1, key, total_request, gmp, "new_high_gmp_article_7"
					}
				}
			}

			if($1 in high_gmp_article_8_percent)
                        {
                                print $0, "alive-8"
                        }
                        else
                        {
                                if('${min_article_id}'<$1 && total_request > 200)
                                {
                                        if(gmp >= 0.08)
                                        {
                                                high_gmp_ge_08[key]++
                                                print $1, key, total_request, gmp, "new_high_gmp_article_8"
                                        }
                                }
                        }
		}
	}END{
		for(key in dict)
		{
			print key, high_gmp_ge_08[key]+0, high_gmp_ge_07[key]+0, "high_gmp_article_count"
		}
	}' ${LOCAL_DATA_PATH}/high-gmp-article-7-percent.list ${LOCAL_DATA_PATH}/high-gmp-article-8-percent.list ${LOCAL_DATA_PATH}/article-gmp.list > ${LOCAL_DATA_PATH}/high-gmp-article-analysis.list 

	grep "new_high_gmp_article_7" ${LOCAL_DATA_PATH}/high-gmp-article-analysis.list | awk 'BEGIN{
		FS = "\t"
	}{
		print $0
	}END{}' >> ${LOCAL_DATA_PATH}/high-gmp-article-7-percent.list

        grep "new_high_gmp_article_8" ${LOCAL_DATA_PATH}/high-gmp-article-analysis.list | awk 'BEGIN{
                FS = "\t"
        }{
                print $0
        }END{}' >> ${LOCAL_DATA_PATH}/high-gmp-article-8-percent.list

        grep "high_gmp_article_count" ${LOCAL_DATA_PATH}/high-gmp-article-analysis.list | awk 'BEGIN{
                FS = "\t"
		OFS = "\t"
        }{
               	print $1, $2, $3
        }END{}' > ${LOCAL_DATA_PATH}/article-gmp-monitor-high-gmp.list
	cat ${LOCAL_DATA_PATH}/article-gmp-monitor-high-gmp.list | python monitor-high-gmp-parse.py > /data/metric/article-gmp/high-gmp.list
}


prepare

mv_last_gmp_file
write_article_gmp_to_redis
data_monitor
data_monitor_language
save_gmp_data_local
get_high_gmp_article

## no need open
#get_gmp_article_count
#insert_into_mysql


