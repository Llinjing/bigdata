#!/bin/sh

# author: huanghaifeng

function prepare()
{
	source ../conf/project.conf
	source ../conf/mysql.conf
	source ../conf/warning.conf
}

function get_data()
{
	export HADOOP_HOME=/usr/lib/hadoop
	export PATH=/usr/local/sbin:/sbin:/bin:/usr/sbin:/usr/bin:/usr/lib/hadoop/bin:/usr/lib/hive//bin:/usr/lib/spark/bin:/usr/lib/hadoop-yarn/bin:/usr/local/zookeeper/bin:/root/bin
	local date=`date +%Y%m%d`
	echo ${date}
	local time=`date +%s`
	time=$[time-5*60]
	time=${time:0:8}
	echo ${time}
	hadoop fs -cat ${IMPRESSION_HDFS_PATH}/impression-${time}00000/part* > ${LOCAL_DATA_PATH}/impression.list
	hadoop fs -cat ${ARTICLE_GMP_HDFS_PATH}/article-gmp-${time}00000/part* > ${LOCAL_DATA_PATH}/article-gmp.list

        local curr_time=`date +%Y%m%d%H%M -d"-10 min"`  # max latency 20 minutes
        local curr_date=`echo "${curr_time}" | awk '{
                print substr($1, 1, 8)
        }'`
        local curr_hour=`echo "${curr_time}" | awk '{
                print substr($1, 9, 2)
        }'`
        local curr_min=`echo "${curr_time}" | awk '{
                print sprintf("%02d", int(substr($1, 11, 2) / 10) * 10)
        }'`
        local impression_path=/inveno-data/offline/format-data/topic/impression-reformat/${curr_date}/${curr_hour}/*.${curr_date}_${curr_hour}${curr_min}
	echo ${impression_path}
	hadoop fs -cat ${impression_path} > ${LOCAL_DATA_PATH}/impression-reformat.list
	cat ${LOCAL_DATA_PATH}/impression-reformat.list | python ${LOCAL_BIN_PATH}/monitor-impression-parse.py 1>>${LOCAL_DATA_PATH}/hotoday_article_language.list 2>${LOCAL_LOG_PATH}/hotoday_article_language.log
	sort ${LOCAL_DATA_PATH}/hotoday_article_language.list | uniq | sort -o ${LOCAL_DATA_PATH}/hotoday_article_language.list
}

function merge_product_id_language()
{
	awk 'BEGIN{
		FS = "\t"
		OFS = "\t"
	}{
		if(ARGIND == 1)
		{
			article_language["("$1] = $2
		}
		else if(ARGIND == 2)
                {
                        n = split($0, arr, "###")
                        article_id = arr[1]
                        if (article_id in article_language)
                        {
                                n = split($0, arr_new, ",")
                                print arr_new[1]"-"article_language[article_id]","arr_new[2]","arr_new[3]","arr_new[4]","arr_new[5], "impression"
                        }
                        else
                        {
                               print $0, "impression"
                        }
                }
                else if(ARGIND == 3)
                {
                        n = split($0, arr, "###")
                        article_id = arr[1]
                        if (article_id in article_language)
                        {
                                n = split($0, arr_new, ",")
                                print arr_new[1]"-"article_language[article_id]","arr_new[2]","arr_new[3]","arr_new[4], "article_gmp"
                        }
                        else
                        {
                                print $0, "article_gmp"
                        }
                }
	}END{
	}' ${LOCAL_DATA_PATH}/hotoday_article_language.list ${LOCAL_DATA_PATH}/impression.list ${LOCAL_DATA_PATH}/article-gmp.list > ${LOCAL_DATA_PATH}/article-gmp-monitor-data.tmp
	
        awk 'BEGIN{
                FS = "\t"
                OFS = "\t"
        }{
                if(ARGIND == 1)
                {
                        article_black_list["("$1]
                }
                else if(ARGIND == 2)
                {
                        n = split($1, arr, "###")
                        if (arr[1] in article_black_list)
                        {
                                count ++
                        }
                        else
                        {
                                print $0
                        }
                }
        }END{

        }' /data1/apps/article-gmp-language/data/article-black-list.list ${LOCAL_DATA_PATH}/article-gmp-monitor-data.tmp > ${LOCAL_DATA_PATH}/article-gmp-monitor-data.tmp.tmp
	mv ${LOCAL_DATA_PATH}/article-gmp-monitor-data.tmp.tmp ${LOCAL_DATA_PATH}/article-gmp-monitor-data.tmp

	#cp ${LOCAL_DATA_PATH}/impression.list ${LOCAL_DATA_PATH}/impression.list_backup
	#cp ${LOCAL_DATA_PATH}/article-gmp.list ${LOCAL_DATA_PATH}/article-gmp.list_backup
	cat ${LOCAL_DATA_PATH}/article-gmp-monitor-data.tmp | grep "impression" | awk '{print $1}' > ${LOCAL_DATA_PATH}/impression.list
	cat ${LOCAL_DATA_PATH}/article-gmp-monitor-data.tmp | grep "article_gmp" | awk '{print $1}' > ${LOCAL_DATA_PATH}/article-gmp.list
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
        redis-cli -c -h 172.31.30.87 -p 6300 hlen ${REDIS_KEY} > ${LOCAL_DATA_PATH}/redis-gmp-key-length.list
	local redis_gmp_key_length=`cat ${LOCAL_DATA_PATH}/redis-gmp-key-length.list`
	if [ -z "${redis_gmp_key_length}" ] ; then 
 	   	## baojing
        	${WARNING_BIN} "${PRODUCT_LINE}:article_gmp_spark_streaming_monitor_redis-gmp-key-length_is_null" ${SMS_USER[$FATAL]}
	        redis_gmp_key_length=0
	fi
	echo ${redis_gmp_key_length} 

	## min article id
        local time=`date "+%Y-%m-%d %H:%M:00" -d"-2 day +8 hour"`
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
	fi
 	echo ${min_article_id}

        ## mysql available publish article amount
        ## select COUNT(*) from t_content where firm_app like '%"app":"coolpad"%' and content_id>=37416882;
        
        local count_sql="select COUNT(*) from db_mta.t_content where firm_app like '%\"app\":\"hotoday\"%' and discovery_time>=\""${time}"\" and state=1 and language=\"hindi\""
        echo ${count_sql}
        #mysql -h${MYSQL_HOST[3]} -P${MYSQL_PORT[3]} -u${MYSQL_USER[3]} -p${MYSQL_PWD[3]} -e "${count_sql}" > ${LOCAL_DATA_PATH}/mysql_article_count.list
        #local hotoday_hindi_mysql_article_count=`sed -n '2,1p' ${LOCAL_DATA_PATH}/mysql_article_count.list`
        #echo ${hotoday_hindi_article_count}
        
        local count_sql="select COUNT(*) from db_mta.t_content where firm_app like '%\"app\":\"hotoday\"%' and discovery_time>=\""${time}"\" and state=1 and language=\"english\""
        echo ${count_sql}
        #mysql -h${MYSQL_HOST[3]} -P${MYSQL_PORT[3]} -u${MYSQL_USER[3]} -p${MYSQL_PWD[3]} -e "${count_sql}" > ${LOCAL_DATA_PATH}/mysql_article_count.list
        #local hotoday_english_mysql_article_count=`sed -n '2,1p' ${LOCAL_DATA_PATH}/mysql_article_count.list`
        #echo ${hotoday_english_mysql_article_count}
        
        local count_sql="select COUNT(*) from db_mta.t_content where firm_app like '%\"app\":\"mata\"%' and discovery_time>=\""${time}"\" and state=1"
        echo ${count_sql}
        #mysql -h${MYSQL_HOST[3]} -P${MYSQL_PORT[3]} -u${MYSQL_USER[3]} -p${MYSQL_PWD[3]} -e "${count_sql}" > ${LOCAL_DATA_PATH}/mysql_article_count.list
        #local mata_mysql_article_count=`sed -n '2,1p' ${LOCAL_DATA_PATH}/mysql_article_count.list`
        #echo ${mata_mysql_article_count}

        count_sql="select COUNT(*) from db_mta.t_content where firm_app like '%\"app\":\"noticias\"%' and discovery_time>=\""${time}"\" and state=1"
        echo ${count_sql}
        #mysql -h${MYSQL_HOST[3]} -P${MYSQL_PORT[3]} -u${MYSQL_USER[3]} -p${MYSQL_PWD[3]} -e "${count_sql}" > ${LOCAL_DATA_PATH}/mysql_article_count.list
        #local noticias_mysql_article_count=`sed -n '2,1p' ${LOCAL_DATA_PATH}/mysql_article_count.list`
        #echo ${noticias_mysql_article_count}
        
        count_sql="select COUNT(*) from db_mta.t_content where firm_app like '%\"app\":\"noticiasboom\"%' and discovery_time>=\""${time}"\" and state=1"
        echo ${count_sql}
        #mysql -h${MYSQL_HOST[3]} -P${MYSQL_PORT[3]} -u${MYSQL_USER[3]} -p${MYSQL_PWD[3]} -e "${count_sql}" > ${LOCAL_DATA_PATH}/mysql_article_count.list
        #local noticiasboom_mysql_article_count=`sed -n '2,1p' ${LOCAL_DATA_PATH}/mysql_article_count.list`
        #echo ${noticiasboom_mysql_article_count}

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
        	                if (gmp >= 0.02)
                	        {
                        	        article_gmp_ge_2_percent[product_id]++
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
				if (key=="hotoday-hindi")
                                {
                                        print monitor_gmp[key"-spark-streaming"], "'${hotoday_hindi_article_count}'"+0, article_count[key]+0, '${redis_gmp_key_length}'+0, article_gmp_ge_8_percent[key]+0, article_gmp_ge_4_percent[key]+0, article_gmp_ge_2_percent[key]+0, article_gmp_max[key]+0.0, (article_count[key]==0 ? 0.0 : article_gmp_sum[key]/article_count[key])
                                }
				else if (key=="hotoday-english")
                                {
                                        print monitor_gmp[key"-spark-streaming"], "'${hotoday_english_article_count}'"+0, article_count[key]+0, '${redis_gmp_key_length}'+0, article_gmp_ge_8_percent[key]+0, article_gmp_ge_4_percent[key]+0, article_gmp_ge_2_percent[key]+0, article_gmp_max[key]+0.0, (article_count[key]==0 ? 0.0 : article_gmp_sum[key]/article_count[key])
                                }
				else if (key=="mata")
                                {
                                        print monitor_gmp[key"-spark-streaming"], "'${mata_mysql_article_count}'"+0, article_count[key]+0, '${redis_gmp_key_length}'+0, article_gmp_ge_8_percent[key]+0, article_gmp_ge_4_percent[key]+0, article_gmp_ge_2_percent[key]+0, article_gmp_max[key]+0.0, (article_count[key]==0 ? 0.0 : article_gmp_sum[key]/article_count[key])
                                }
				else if (key=="noticias")
				{
					print monitor_gmp[key"-spark-streaming"], "'${noticias_mysql_article_count}'"+0, article_count[key]+0, '${redis_gmp_key_length}'+0, article_gmp_ge_8_percent[key]+0, article_gmp_ge_4_percent[key]+0, article_gmp_ge_2_percent[key]+0, article_gmp_max[key]+0.0, (article_count[key]==0 ? 0.0 : article_gmp_sum[key]/article_count[key])
				}
				else if (key=="noticiasboom")
				{
					print monitor_gmp[key"-spark-streaming"], "'${noticiasboom_mysql_article_count}'"+0, article_count[key]+0, '${redis_gmp_key_length}'+0, article_gmp_ge_8_percent[key]+0, article_gmp_ge_4_percent[key]+0, article_gmp_ge_2_percent[key]+0, article_gmp_max[key]+0.0, (article_count[key]==0 ? 0.0 : article_gmp_sum[key]/article_count[key])
				}
				else
				{
					print monitor_gmp[key"-spark-streaming"], "0", article_count[key]+0, '${redis_gmp_key_length}'+0, article_gmp_ge_8_percent[key]+0, article_gmp_ge_4_percent[key]+0, article_gmp_ge_2_percent[key]+0, article_gmp_max[key]+0.0, (article_count[key]==0 ? 0.0 : article_gmp_sum[key]/article_count[key])
				}
                        }
                }

        }' ${LOCAL_DATA_PATH}/article-gmp-parse.list ${LOCAL_DATA_PATH}/monitor-gmp-result.tmp > ${LOCAL_DATA_PATH}/monitor-gmp-result.list

}
 
function backup()
{
	local time=`date +%Y%m%d%H%M%S`
	cp ${LOCAL_DATA_PATH}/monitor-gmp-result.list ${LOCAL_DATA_PATH}/backup/monitor-gmp-result.list.${time}

        local curr_time=`date +%Y%m%d%H%M -d"-10 min"`  # max latency 20 minutes
        local curr_date=`echo "${curr_time}" | awk '{
                print substr($1, 1, 8)
        }'`
        local curr_hour=`echo "${curr_time}" | awk '{
                print substr($1, 9, 2)
        }'`
        local curr_min=`echo "${curr_time}" | awk '{
                print sprintf("%02d", int(substr($1, 11, 2) / 10) * 10)
        }'`
        echo ${curr_date}${curr_hour}${curr_min}
	local path=${LOCAL_DATA_PATH}/article-gmp/${curr_date}${curr_hour}${curr_min}
	mkdir -p ${path}
	cp ${LOCAL_DATA_PATH}/article-gmp-parse.list ${path}/article-gmp.list 

	sh -x ./get_gmp_variance.sh
}

prepare
get_data
merge_product_id_language
parse_data
get_sum
calculate
parse_gmp
calculate_more
backup
