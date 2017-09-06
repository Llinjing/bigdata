#!/bin/bash

function check_data()
{
	local topic=$1

	source ../conf/hadoop.conf
	source ../conf/project.conf
	source ../conf/warning.conf
	source ../conf/mysql.conf

	mkdir -p ${LOCAL_TMP_PATH}
	mkdir -p ${LOCAL_TOPIC_DATA_PATH}

	local curr_time=`date +%Y%m%d_%H%M -d"-10 min"`
	local begin_time=`echo "${curr_time}" | awk '{
		min = sprintf("%02d", int(substr($1, 12, 2) / 10) * 10)
		print substr($1, 1, 11)""min
	}'`
	local curr_date=`echo "${curr_time}" | awk '{
		print substr($1, 1, 8)
	}'`
	local curr_hour=`echo "${curr_time}" | awk '{
		print substr($1, 10, 2)
	}'`

	local files=${LOCAL_TOPIC_DATA_PATH}/${topic}/*_${begin_time}
	cat ${files} >${LOCAL_TMP_PATH}/${topic}.${begin_time}
		
	#no data, need warning
	if [ ! -s ${LOCAL_TMP_PATH}/${topic}.${begin_time} ]
	then
		${WARNING_BIN} "${PRODUCT_LINE} : no data : ${topic}_${begin_time}" "${SMS_USER[$FATAL]}"
	else
		upload_to_hdfs ${LOCAL_TMP_PATH}/${topic}.${begin_time} \
			${HDFS_TOPIC_DATA_PATH}/${topic}/${curr_date}/${curr_hour} ${topic}.${begin_time}

		article_high_impression_10min ${LOCAL_TMP_PATH}/${topic}.${begin_time}

		article_feedback_stat_10min ${LOCAL_TMP_PATH}/${topic}.${begin_time}

		write_article_feedback_10min ${LOCAL_TMP_PATH}/${topic}.${begin_time}
	fi

	rm -f ${LOCAL_TMP_PATH}/${topic}.${begin_time}
	rm -f ${files}

	return 0
}

function upload_to_hdfs()
{
	local data_file=$1
	local hdfs_dir=$2
	local hdfs_file=$3

	${HADOOP_MKDIR} ${hdfs_dir}
	${HADOOP_APPEND} ${data_file} ${hdfs_dir}/${hdfs_file}

	return 0
}

function article_high_impression_10min()
{
        local article_feedback_10min_file=$1

    	mkdir -p product_configid-10min
        mkdir -p product-10min
        :>${LOCAL_DATA_PATH}/high-impression-article-10min.list

        ## config
        mysql -h${MYSQL_HOST[0]} -P${MYSQL_PORT[0]} -u${MYSQL_USER[0]} -p${MYSQL_PWD[0]} -N \
                <${LOCAL_SQL_PATH}/get_high_impression_config-10min.sql >${LOCAL_CONF_PATH}/high-impression-10min.conf

	    cat ${article_feedback_10min_file} | python ./parse-article-feedback.py | awk 'BEGIN{
                FS = "\t"
                OFS = "\t"
        }{
                key = $2"\t"$3"\t"$4"\t"$5"\t"$9"\t"$10
                timestamp[key] = $1
                click[key] += $6
                impression[key] += $7
                dwelltime[key] += $8
        }END{
                for (key in timestamp)
                {
                        split(key, arr, "\t")
                        print timestamp[key], key, click[key], impression[key], dwelltime[key] >"product_configid-10min/"arr[2]"#"arr[3]"#"arr[4]"#"arr[5]"#"arr[6]
                        print timestamp[key], key, click[key], impression[key], dwelltime[key] >"product-10min/"arr[2]"#"arr[4]"#"arr[5]"#"arr[6]
                }
        }'

        for file in `ls product-10min`
        do
                local product_id=`echo $file | awk '{split($1, arr, "#"); print arr[1]}'`
                local language=`echo $file | awk '{split($1, arr, "#"); print arr[2]}'`
		        local content_type=`echo $file | awk '{split($1, arr, "#"); print arr[3]}'`
		        local scenario_channel=`echo $file | awk '{split($1, arr, "#"); print arr[4]}'`
                local percent=`get_percent_all $product_id $language $content_type $scenario_channel ${LOCAL_CONF_PATH}/high-impression-10min.conf`
                local total_impression=`cat product-10min/$file | awk '{sum += $9}END{print sum}'`
                local break_impression=`echo $total_impression $percent | awk '{print int($1 * ($2 + 0))}'`
		        local min_impression=`get_min_impression_all $product_id $language $content_type $scenario_channel ${LOCAL_CONF_PATH}/high-impression-10min.conf`

                if [ $break_impression -le 0 ]
                then
                        continue
                fi

                sort -t $'\t' -k9,9nr product-10min/$file | awk 'BEGIN{
                        FS = "\t"
                        OFS = "\t"
                }{
                        sum += $9
                        if (sum <= '${break_impression}')
                        {
				$4 = "-99"
				dedup_click[$1"\t"$2"\t"$3"\t"$4"\t"$5"\t"$6"\t"$7] += $8
				dedup_impression[$1"\t"$2"\t"$3"\t"$4"\t"$5"\t"$6"\t"$7] += $9
				dedup_dwelltime[$1"\t"$2"\t"$3"\t"$4"\t"$5"\t"$6"\t"$7] += $10
                        }
                        else
                        {
                                exit
                        }
                }END{
			for (key in dedup_impression) {
				if (dedup_impression[key] > '${min_impression}') {
					print key, dedup_click[key], dedup_impression[key], dedup_dwelltime[key], 2
				}
			}
		}' >>${LOCAL_DATA_PATH}/high-impression-article-10min.list
        done

	for file in `ls product_configid-10min`
        do
                local product_id=`echo $file | awk '{split($1, arr, "#"); print arr[1]}'`
                local config_id=`echo $file | awk '{split($1, arr, "#"); print arr[2]}'`
                local language=`echo $file | awk '{split($1, arr, "#"); print arr[3]}'`
		        local content_type=`echo $file | awk '{split($1, arr, "#"); print arr[4]}'`
		        local scenario_channel=`echo $file | awk '{split($1, arr, "#"); print arr[5]}'`
                local percent=`get_percent $product_id $config_id $language $content_type $scenario_channel ${LOCAL_CONF_PATH}/high-impression-10min.conf`
                local total_impression=`cat product_configid-10min/$file | awk '{sum += $9}END{print sum}'`
                local break_impression=`echo $total_impression $percent | awk '{print int($1 * ($2 + 0))}'`
		        local min_impression=`get_min_impression $product_id $config_id $language $content_type $scenario_channel ${LOCAL_CONF_PATH}/high-impression-10min.conf`

                if [ $break_impression -le 0 ]
                then
                        continue
                fi

                sort -t $'\t' -k9,9nr product_configid-10min/$file | awk 'BEGIN{
                        FS = "\t"
                        OFS = "\t"
                }{
                        sum += $9
                        if (sum <= '${break_impression}')
                        {
                                if ($9 > '${min_impression}')
                                {
                                        print $0"\t2"
                                }
                        }
                        else
                        {
                                exit
                        }
                }' >>${LOCAL_DATA_PATH}/high-impression-article-10min.list
        done

	## write
        if [ -s ${LOCAL_DATA_PATH}/high-impression-article-10min.list ]
        then
                write_high_impression_article ${LOCAL_DATA_PATH}/high-impression-article-10min.list
        else
                ${WARNING_BIN} "no high impression article by 10min: ${topic}_${begin_time}" "${SMS_USER[$FATAL]}"
        fi

    	rm -rf product_configid-10min product-10min

        return 0
}

function get_percent()
{
        local product_id=$1
        local config_id=$2
        local language=$3
	    local content_type=$4
	    local scenario_channel=$5
        local config_file=$6

        awk 'BEGIN{
                OFS = "\t"
                proportion = 0
        }{
                if ($1 == "'${product_id}'" && $2 == "'${config_id}'" && $4 == "'${language}'" && $6 == "'${content_type}'" && $7 == "'${scenario_channel}'")
                {
                        proportion = $3 / 100
                }
        }END{
                print proportion
        }' ${config_file}

        return 0
}

function get_min_impression()
{
	    local product_id=$1
        local config_id=$2
        local language=$3
	    local content_type=$4
	    local scenario_channel=$5
        local config_file=$6

        awk 'BEGIN{
                FS = "\t"
                impression = 0
        }{
                if ($1 == "'${product_id}'" && $2 == "'${config_id}'" && $4 == "'${language}'" && $6 == "'${content_type}'" && $7 == "'${scenario_channel}'")
                {
                        impression = $5
                }
        }END{
                print impression
        }' ${config_file}
}

function get_percent_all()
{
        local product_id=$1
        local language=$2
	    local content_type=$3
	    local scenario_channel=$4
        local config_file=$5

        awk 'BEGIN{
                FS = "\t"
                proportion = 0
        }{
                if ($1 == "'${product_id}'" && $2 == "-99" && $4 == "'${language}'" && $6 == "'${content_type}'" && $7 == "'${scenario_channel}'")
                {
                        proportion = $3 / 100
                }
        }END{
                print proportion
        }' ${config_file}
}

function get_min_impression_all()
{
	    local product_id=$1
        local language=$2
	    local content_type=$3
	    local scenario_channel=$4
	    local config_file=$5

        awk 'BEGIN{
                FS = "\t"
                impression = 0
        }{
                if ($1 == "'${product_id}'" && $2 == "-99" && $4 == "'${language}'" && $6 == "'${content_type}'" && $7 == "'${scenario_channel}'")
                {
                        impression = $5
                }
        }END{
                print impression
        }' ${config_file}
}

function write_high_impression_article()
{
        local data_file=$1
        
        local TASK_NAME=write-high-impression-article-10min

        $JAVA_HOME/bin/java \
                -Djava.ext.dirs=${LOCAL_LIB_PATH} \
                -Dlog4j.configuration=file:${LOCAL_CONF_PATH}/log4j-${TASK_NAME}.properties \
                com.inveno.news.sparkstreaming.data.monitor.bz.HighImpressionArticleWriter \
                ${data_file} ${LOCAL_CONF_PATH}/mysql.properties \
                1>${LOCAL_LOG_PATH}/${TASK_NAME}.err 2>&1
        
        return 0
}

function article_feedback_stat_10min()
{
	    local article_feedback_10min_file=$1

        mkdir -p product_language-10min
        :>${LOCAL_DATA_PATH}/article-feedback-impression-10min.list

        ## config
        mysql -h${MYSQL_HOST[0]} -P${MYSQL_PORT[0]} -u${MYSQL_USER[0]} -p${MYSQL_PWD[0]} -N \
                <${LOCAL_SQL_PATH}/get_top_article_config-10min.sql >${LOCAL_CONF_PATH}/article-feedback-impression-10min.conf.tmp

	    cat ${LOCAL_CONF_PATH}/article-feedback-impression-10min.conf.tmp | awk 'BEGIN{FS="##";OFS="\t"}{print $1, $2, $3}' >${LOCAL_CONF_PATH}/article-feedback-impression-10min.conf
	    rm -rf ${LOCAL_CONF_PATH}/article-feedback-impression-10min.conf.tmp

        cat ${article_feedback_10min_file} | python ./parse-article-feedback-10min.py | awk 'BEGIN{
                FS = "\t"
                OFS = "\t"
        }{
                key = $2"\t"$3"\t"$5
                timestamp[key] = $1
                click[key] += $6
                impression[key] += $7
                dwelltime[key] += $8
		        request[key] += $9
		        datetime[key] = $10
        }END{
                for (key in timestamp)
                {
                        split(key, arr, "\t")
                        print datetime[key], timestamp[key], key, request[key], impression[key], click[key], dwelltime[key] >"product_language-10min/"arr[2]"#"arr[3]
                }
        }'

	for file in `ls product_language-10min`
        do
                local product_id=`echo $file | awk '{split($1, arr, "#"); print arr[1]}'`
                local language=`echo $file | awk '{split($1, arr, "#"); print arr[2]}'`
                local top_num=`get_top_num_all $product_id $language ${LOCAL_CONF_PATH}/article-feedback-impression-10min.conf`

		        sort -t $'\t' -k7,7nr product_language-10min/$file | awk 'BEGIN{
                        FS = "\t"
                        OFS = "\t"
                }{
			print $0
		}' >${LOCAL_DATA_PATH}/article-feedback-impression-10min.list.tmp

		cat ${LOCAL_DATA_PATH}/article-feedback-impression-10min.list.tmp | head -n ${top_num} >>${LOCAL_DATA_PATH}/article-feedback-impression-10min.list
		rm -rf ${LOCAL_DATA_PATH}/article-feedback-impression-10min.list.tmp
        done

	## write
        if [ -s ${LOCAL_DATA_PATH}/article-feedback-impression-10min.list ]
        then
                write_article_feedback_stat_10min ${LOCAL_DATA_PATH}/article-feedback-impression-10min.list
        else
                ${WARNING_BIN} "no top article 10min: ${topic}_${begin_time}" "${SMS_USER[$FATAL]}"
        fi

	    rm -rf product_language-10min
	
	return 0
}

function get_top_num_all()
{
    	local product_id=$1
        local language=$2
        local config_file=$3

        awk 'BEGIN{
                FS = "\t"
                top_num = 0
        }{
                if ($1 == "'${product_id}'" && $2 == "'${language}'")
                {
                        top_num = $3
                }
        }END{
                print top_num
        }' ${config_file}
}

function write_article_feedback_stat_10min()
{
	local data_file=$1

    local TASK_NAME=write-top-article-impression-10min

	cat ${data_file} | awk 'BEGIN{FS=OFS="\t"}{print "0", $1, $2"00", $3, $4, $5}' >${LOCAL_DATA_PATH}/article-feedback-impression-10min-t_top_article_10min.list
	cat ${data_file} | awk 'BEGIN{FS=OFS="\t"}{print "0", $1, $3, $4, $5, $6, $7, $8, $9}' >${LOCAL_DATA_PATH}/article-feedback-impression-10min-t_top_article_stat_daily.list

	echo "USE db_mta;" >${LOCAL_SQL_PATH}/load-top-article-10min.sql
	echo "LOAD DATA LOCAL INFILE '${LOCAL_DATA_PATH}/article-feedback-impression-10min-t_top_article_10min.list' INTO TABLE t_top_article_10min FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n' IGNORE 0 LINES;" >>${LOCAL_SQL_PATH}/load-top-article-10min.sql
	echo "LOAD DATA LOCAL INFILE '${LOCAL_DATA_PATH}/article-feedback-impression-10min-t_top_article_stat_daily.list' INTO TABLE t_top_article_stat_daily FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n' IGNORE 0 LINES;" >>${LOCAL_SQL_PATH}/load-top-article-10min.sql

	mysql -h${MYSQL_HOST[0]} -P${MYSQL_PORT[0]} -u${MYSQL_USER[0]} -p${MYSQL_PWD[0]} <${LOCAL_SQL_PATH}/load-top-article-10min.sql

	return 0
}

function write_article_feedback_10min()
{
        local data_file=$1

        local TASK_NAME=write-article-feedback-10min

	    cat ${data_file} | python ./article-feedback-10min-parse.py | awk 'BEGIN{
		    FS = "\t"
		    OFS = "\t"
	    }{
		    dict_click[$2]+=$5
		    dict_impression[$2]+=$6
		    dict_dwelltime[$2]+=$7
	    } END {
		for (key in dict_click) {
			if (dict_click[key] > 0 || dict_impression[key] > 10) {
				print key, dict_click[key], dict_impression[key], dict_dwelltime[key]
			}
		}
	}' >${LOCAL_DATA_PATH}/10min_article_feedback_tmp.list

	cat ${LOCAL_DATA_PATH}/10min_article_feedback_tmp.list | python article-feedback-10min-query.py >${LOCAL_DATA_PATH}/10min_article_data_query.json

	curl -XPOST 10.10.10.100:9200/cms_content-*/t_content_feedback/_search?_source=content_id,impression_sum,click_sum,dwelltime_sum,ctr_sum?pretty=true --data-binary @${LOCAL_DATA_PATH}/10min_article_data_query.json >${LOCAL_DATA_PATH}/10min_article_feedback_query.list

	cat ${LOCAL_DATA_PATH}/10min_article_feedback_query.list | python article-feedback-10min-process.py >${LOCAL_DATA_PATH}/10min_article_feedback_sum.list

	awk 'BEGIN{
		FS=OFS="\t"
	}{
		if (ARGIND == 1) {
			sum_click[$1] += $2
			sum_impression[$1] += $3
			sum_dwelltime[$1] += $4
		} else if (ARGIND == 2) {
			if ($1 in sum_click) {
				print $1, $2, $3, $4, ($3>0 ? $2/$3 : 0), (sum_click[$1]+$2), (sum_impression[$1]+$3), (sum_dwelltime[$1]+$4), ((sum_impression[$1]+$3)>0 ? (sum_click[$1]+$2)/(sum_impression[$1]+$3) : 0)
			} else {
				print $1, $2, $3, $4, ($3>0 ? $2/$3 : 0), $2, $3, $4, ($3>0 ? $2/$3 : 0)
			}
		}
	}END{
	}' ${LOCAL_DATA_PATH}/10min_article_feedback_sum.list \
		${LOCAL_DATA_PATH}/10min_article_feedback_tmp.list \
		>${LOCAL_DATA_PATH}/10min_article_feedback_update.list

	cat ${LOCAL_DATA_PATH}/10min_article_feedback_update.list | python article-feedback-10min-update.py >${LOCAL_DATA_PATH}/10min_article_data_update.json

	curl -XPOST 10.10.10.100:9200/cms_content-2017.03/t_content_feedback/_bulk?pretty --data-binary @${LOCAL_DATA_PATH}/10min_article_data_update.json 2>/dev/null

	return 0
}


if [ $# -eq 0 ]
then
	echo "${BASH_SOURCE[0]} <topic>" 2>&1
	exit 1
fi

check_data $1
