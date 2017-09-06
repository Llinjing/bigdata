#!/bin/shell

# author: huanghaifeng

function prepare()
{
	source ../conf/project.conf
	source ../conf/mysql.conf
	source ../conf/warning.conf
}

function get_data()
{
	#hadoop fs -ls ${IMPRESSION_HDFS_PATH}/${TODAY}/
	#hadoop fs -ls ${ARTICLE_GMP_HDFS_PATH}/${TODAY}/
	local date=`date +%Y%m%d`
	echo ${date}
	local time=`date +%s`
	time=$[time-8*60]
	time=${time:0:8}
	echo ${time}
	hadoop fs -cat ${REQUEST_HDFS_PATH}/request-${time}00000/part* > ${LOCAL_DATA_PATH}/impression.list
	hadoop fs -cat ${IMPRESSION_HDFS_PATH}/impression-${time}00000/part* >> ${LOCAL_DATA_PATH}/impression.list
	hadoop fs -cat ${ARTICLE_GMP_HDFS_PATH}/article-gmp-${time}00000/part* > ${LOCAL_DATA_PATH}/article-gmp.list
}

function parse_data()
{
	touch ${LOCAL_DATA_PATH}/impression.list
	touch ${LOCAL_DATA_PATH}/article-gmp.list

	awk 'BEGIN{
        	FS=","
	        OFS="\t"
       		OFMT="%.8f"
	}{
        	if (ARGIND == 1)
	        {
        	        article_impression[$1] = $4
	        }
      		else if (ARGIND == 2)
	        {
        	        if($1 in article_impression)
                	{
                        	article_gmp[$1] = $4
	                }
       		}
	}END{
        	for (key in article_impression)
	        {
        	        split(key, arr, "###")
                	article_id = substr(arr[1], 2, length(arr[1]))
	                gmp = substr(article_gmp[key], 1, length(article_gmp[key])-2)
        	        print arr[2], article_id, article_impression[key], gmp+0.0
       		}
	}' ${LOCAL_DATA_PATH}/impression.list ${LOCAL_DATA_PATH}/article-gmp.list | sort -nr -k4 > ${LOCAL_DATA_PATH}/monitor-gmp-sort.tmp
}

function get_sum()
{
	touch ${LOCAL_DATA_PATH}/monitor-gmp-sort.tmp
	cat ${LOCAL_DATA_PATH}/monitor-gmp-sort.tmp | awk 'BEGIN{
		FS = "\t"
		OFS = "\t"
		OFMT="%.8f"
	}{
		total_impression[$1] += $3
		total_weigthed_gmp[$1] += $3*$4
	}END{
		for(key in total_impression)
		{
			print key, total_impression[key], total_weigthed_gmp[key]+0.0
		}
	}' > ${LOCAL_DATA_PATH}/monitor-gmp-sum.tmp
}

function calculate()
{
	awk 'BEGIN{
		FS = "\t"
		OFS = "\t"
		OFMT="%.8f"
	}{
		if (FILENAME == ARGV[1])
		{
			product_id_weigthed_gmp[$1] = $3/$2
			impression_threshold_ge_02[$1] = 0.2*$2
			impression_threshold_ge_04[$1] = 0.4*$2
		}else if (FILENAME == ARGV[2])
		{
			product_id_total_impression[$1] += $3
			if(product_id_total_impression[$1] < impression_threshold_ge_02[$1])
			{
				product_id_total_article_count_ge_02[$1] += 1	
				product_id_total_article_count_ge_04[$1] += 1			
				product_id_min_gmp_ge_02[$1] = $4
			}
			else if(product_id_total_impression[$1] < impression_threshold_ge_04[$1])
			{
				product_id_total_article_count_ge_04[$1] += 1		
				product_id_min_gmp_ge_04[$1] = $4
			}
		}
	}END{
		for(key in impression_threshold_ge_02)
		{
			print key"-spark-streaming", product_id_weigthed_gmp[key]+0.0, 
			product_id_total_article_count_ge_02[key]+0, product_id_min_gmp_ge_02[key]+0.0, 
			product_id_total_article_count_ge_04[key]+0, product_id_min_gmp_ge_04[key]+0.0
		}
	}' ${LOCAL_DATA_PATH}/monitor-gmp-sum.tmp ${LOCAL_DATA_PATH}/monitor-gmp-sort.tmp > ${LOCAL_DATA_PATH}/monitor-gmp-result.tmp
}

function parse_gmp()
{
	touch ${LOCAL_DATA_PATH}/article-gmp.list

	awk 'BEGIN{
        	FS=","
	        OFS="\t"
        	OFMT="%.8f"
	}{      
		article_gmp[$1] = $NF
	}END{
        	for (key in article_gmp)
	        {
        	        split(key, arr, "###")
                	article_id = substr(arr[1], 2, length(arr[1]))
			product_id = arr[2]
	                gmp = substr(article_gmp[key], 1, length(article_gmp[key])-2)
			if (product_id != "-1")
			{
	        	        print product_id, article_id, gmp+0.0
			}
            	}
	}' ${LOCAL_DATA_PATH}/article-gmp.list > ${LOCAL_DATA_PATH}/article-gmp-parse.list
}

function parse_impression()
{
        touch ${LOCAL_DATA_PATH}/impression.list

        awk 'BEGIN{
                FS=","
                OFS="\t"
        }{      
                article_impression[$1]
        }END{
                for (key in article_impression)
                {
                        split(key, arr, "###")
                        article_id = substr(arr[1], 2, length(arr[1]))
                        product_id = arr[2]
                        if (product_id != "-1")
                        {
                                print product_id, article_id
                        }
                }
        }' ${LOCAL_DATA_PATH}/impression.list > ${LOCAL_DATA_PATH}/impression-parse.list
}

function calculate_more()
{
	## redis key length
        redis-cli -c -h 192.168.1.82 -p 9379 hlen ${REDIS_KEY} > ${LOCAL_DATA_PATH}/redis-gmp-key-length.list
	local redis_gmp_key_length=`cat ${LOCAL_DATA_PATH}/redis-gmp-key-length.list`
	if [ -z "${redis_gmp_key_length}" ] ; then 
 	   	## baojing
        	${WARNING_BIN} "${PRODUCT_LINE}:article_gmp_spark_streaming_monitor_redis-gmp-key-length_is_null" ${SMS_USER[$FATAL]}
	        redis_gmp_key_length=0
	fi
	echo ${redis_gmp_key_length} 

        local time=`date "+%Y-%m-%d %H:%M:00" -d"-2 day -30 min"`
	## mysql min article_id
        ## SELECT min(content_id) FROM db_mta.t_content WHERE discovery_time>="2016-08-08 09:43:40";
        local sql="select min(content_id) from db_mta.t_content where discovery_time>=\""${time}"\""
        echo ${sql}
        mysql -h${MYSQL_HOST[3]} -P${MYSQL_PORT[3]} -u${MYSQL_USER[3]} -p${MYSQL_PWD[3]} -e "${sql}" > ${LOCAL_DATA_PATH}/min_article_id.list
        local min_article_id=`sed -n '2,1p' ${LOCAL_DATA_PATH}/min_article_id.list`
        if [ -z "${min_article_id}" ] ; then
                ## baojing
                ${WARNING_BIN} "${PRODUCT_LINE}:article_gmp_spark_streaming_monitor_min_article_id_is_null" ${SMS_USER[$FATAL]}
                min_article_id=0
	else 
	        ## mysql available publish article amount
	        ## select COUNT(*) from t_content where firm_app like '%"app":"coolpad"%' and content_id>=37416882;
	        local count_sql="select COUNT(*) from db_mta.t_content where firm_app like '%\"app\":\"coolpad\"%' and content_id>='${min_article_id}'"
	        echo ${count_sql}
	        mysql -h${MYSQL_HOST[3]} -P${MYSQL_PORT[3]} -u${MYSQL_USER[3]} -p${MYSQL_PWD[3]} -e "${count_sql}" > ${LOCAL_DATA_PATH}/mysql_article_count.list
	        local mysql_article_count=`sed -n '2,1p' ${LOCAL_DATA_PATH}/mysql_article_count.list`
	        if [ -z "${mysql_article_count}" ] ; then
	                ## baojing
        	        ${WARNING_BIN} "${PRODUCT_LINE}:article_gmp_spark_streaming_monitor_mysql_available_article_count_is_null" ${SMS_USER[$FATAL]}
                	mysql_article_count=0
	        fi
	        echo ${mysql_article_count}  		
        fi
        echo ${min_article_id}  

	touch ${LOCAL_DATA_PATH}/article-gmp-parse.list
	touch ${LOCAL_DATA_PATH}/monitor-gmp-result.tmp
        awk 'BEGIN{
                FS = "\t"
                OFS = "\t"
		OFMT="%.8f"
        }{
		if (ARGIND == 1)
		{
	                if($2 >= '${min_article_id}')
        	        {
				gmp = $3
				product_id = $1
	                        if (gmp >= 0.08)
        	                {
                	                article_gmp_ge_8_percent[product_id]++
	                        }
        	                if (gmp >= 0.04)
                	        {
                        	        article_gmp_ge_4_percent[product_id]++
	                        }
        	                if (gmp >= 0.2)
                	        {
                        	        article_gmp_ge_20_percent[product_id]++
	                        }
	
				article_count[product_id] += 1
                	        article_gmp_sum[$1] += gmp
	                        if (gmp > article_gmp_max[product_id])
        	                {
                	                article_gmp_max[product_id] = gmp
                        	}
                	}
		}
		else if (ARGIND == 2)
		{
			monitor_gmp[$1] = $0
		}
        }END{
                for (key in article_count)
                {
                        if (key"-spark-streaming" in monitor_gmp)
                        {
                                print monitor_gmp[key"-spark-streaming"], (key=="coolpad" ? "'${mysql_article_count}'": 0), article_count[key]+0, '${redis_gmp_key_length}'+0, article_gmp_ge_8_percent[key]+0, article_gmp_ge_4_percent[key]+0, article_gmp_ge_20_percent[key]+0, article_gmp_max[key]+0.0, (article_count[key]==0 ? 0.0 : article_gmp_sum[key]/article_count[key])
                        }
                }

        }' ${LOCAL_DATA_PATH}/article-gmp-parse.list ${LOCAL_DATA_PATH}/monitor-gmp-result.tmp > ${LOCAL_DATA_PATH}/monitor-gmp-result.list

}
 
function backup()
{
	local time=`date +%Y%m%d%H%M%S`
	cp ${LOCAL_LOG_PATH}/data-monitor.log ${LOCAL_LOG_PATH}/backup/data-monitor.log.${time}
}

prepare
get_data
parse_data
get_sum
calculate
parse_gmp
calculate_more
backup


## parse
## awk 'BEGIN{FS=",";OFS="\t"}{print substr($1,2,8), substr($4,0,10)}END{}'

