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
	
	local min=`echo "${curr_time}" | awk '{
                min = sprintf("%02d", int(substr($1, 12, 2) / 10) * 10)
                print min
        }'`

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

	local files=${LOCAL_TOPIC_DATA_PATH}/${topic}/${topic}_*_${begin_time}
	if [ $min -eq 0 ]
	then
	
		cat ${files} >${LOCAL_TMP_PATH}/${topic}.${begin_time}
		
		# no data, need warning
		if [ ! -s ${LOCAL_TMP_PATH}/${topic}.${begin_time} ]
		then
			${WARNING_BIN} "${PRODUCT_LINE} : no data : ${topic}_${begin_time}" "${SMS_USER[$FATAL]}"
		else
			upload_to_hdfs ${LOCAL_TMP_PATH}/${topic}.${begin_time} \
				${HDFS_TOPIC_DATA_PATH}/${topic}/${curr_date}/${curr_hour} ${topic}.${begin_time}

            ##业务需要下掉1小时高展服务
			#article_high_impression_1hour ${LOCAL_TMP_PATH}/${topic}.${begin_time} ${begin_time}
		fi
	fi

	rm -f ${LOCAL_TMP_PATH}/${topic}.${begin_time}
	rm -f ${files}

	return 0
}


function article_high_impression_1hour()
{
	local article_feedback_1hour_file=$1
	local revise_time=$2

	local revise_min=`echo "${revise_time}" | awk '{ print substr($1, length($1) - 1, 2); }'`

	## each hour
	if [ "${revise_min}" == "00" ]
	then
		:
	else
		return 0
	fi

	mkdir -p product_configid
	mkdir -p product
	:>${LOCAL_DATA_PATH}/high-impression-article.list

	## config
	mysql -h${MYSQL_HOST[0]} -P${MYSQL_PORT[0]} -u${MYSQL_USER[0]} -p${MYSQL_PWD[0]} -N \
		<${LOCAL_SQL_PATH}/get_high_impression_config-1hour.sql >${LOCAL_CONF_PATH}/high-impression.conf

	cat ${article_feedback_1hour_file} | python ./parse-article-feedback.py | awk 'BEGIN{
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
			print timestamp[key], key, click[key], impression[key], dwelltime[key] >"product_configid/"arr[2]"#"arr[3]"#"arr[4]"#"arr[5]"#"arr[6]
			print timestamp[key], key, click[key], impression[key], dwelltime[key] >"product/"arr[2]"#"arr[4]"#"arr[5]"#"arr[6]
		}
	}'

	# configid=all
	for file in `ls product`
	do
		local product_id=`echo $file | awk '{split($1, arr, "#"); print arr[1]}'`
		local language=`echo $file | awk '{split($1, arr, "#"); print arr[2]}'`
		local content_type=`echo $file | awk '{split($1, arr, "#"); print arr[3]}'`
		local scenario_channel=`echo $file | awk '{split($1, arr, "#"); print arr[4]}'`
		local percent=`get_percent_all $product_id $language $content_type $scenario_channel ${LOCAL_CONF_PATH}/high-impression.conf`
		local total_impression=`cat product/$file | awk '{sum += $9}END{print sum}'`
		local break_impression=`echo $total_impression $percent | awk '{print int($1 * ($2 + 0))}'`
		local min_impression=`get_min_impression_all $product_id $language $content_type $scenario_channel ${LOCAL_CONF_PATH}/high-impression.conf`

		if [ $break_impression -le 0 ]
		then
			continue
		fi

		sort -t $'\t' -k9,9nr product/$file | awk 'BEGIN{
			FS = "\t"
			OFS = "\t"
		}{
			sum += $9
			if (sum <= '${break_impression}')
			{
				if ($9 > '${min_impression}')
				{
					$4 = "-99"
					print $0"\t1"
				}
			}
			else
			{
				exit
			}
		}' >>${LOCAL_DATA_PATH}/high-impression-article.list
	done

	## product + configid
	for file in `ls product_configid`
	do
		local product_id=`echo $file | awk '{split($1, arr, "#"); print arr[1]}'`
		local config_id=`echo $file | awk '{split($1, arr, "#"); print arr[2]}'`
		local language=`echo $file | awk '{split($1, arr, "#"); print arr[3]}'`
		local content_type=`echo $file | awk '{split($1, arr, "#"); print arr[4]}'`
		local scenario_channel=`echo $file | awk '{split($1, arr, "#"); print arr[5]}'`
		local percent=`get_percent $product_id $config_id $language $content_type $scenario_channel ${LOCAL_CONF_PATH}/high-impression.conf`
		local total_impression=`cat product_configid/$file | awk '{sum += $9}END{print sum}'`
		local break_impression=`echo $total_impression $percent | awk '{print int($1 * ($2 + 0))}'`
		local min_impression=`get_min_impression $product_id $config_id $language $content_type $scenario_channel ${LOCAL_CONF_PATH}/high-impression.conf`

		if [ $break_impression -le 0 ]
		then
			continue
		fi

		sort -t $'\t' -k9,9nr product_configid/$file | awk 'BEGIN{
			FS = "\t"
			OFS = "\t"
		}{
			sum += $9
			if (sum <= '${break_impression}')
			{
				if ($9 > '${min_impression}')
				{
					print $0"\t1"
				}
			}
			else
			{
				exit
			}
		}' >>${LOCAL_DATA_PATH}/high-impression-article.list
	done

	## write
	if [ -s ${LOCAL_DATA_PATH}/high-impression-article.list ]
	then
		write_high_impression_article ${LOCAL_DATA_PATH}/high-impression-article.list
	else
		${WARNING_BIN} "no high impression article by 1hour: ${topic}_${begin_time}" "${SMS_USER[$FATAL]}"
	fi

	rm -rf product_configid product

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

function write_high_impression_article()
{
	local data_file=$1
	
	local TASK_NAME=write-high-impression-article

	$JAVA_HOME/bin/java \
        	-Djava.ext.dirs=${LOCAL_LIB_PATH} \
	        -Dlog4j.configuration=file:${LOCAL_CONF_PATH}/log4j-${TASK_NAME}.properties \
        	com.inveno.news.sparkstreaming.data.monitor.bz.HighImpressionArticleWriter \
		${data_file} ${LOCAL_CONF_PATH}/mysql.properties \
        	1>${LOCAL_LOG_PATH}/${TASK_NAME}.err 2>&1
	
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
		if ($1 == "'${product_id}'" && $2 == "'${config_id}'" && $4 == "'${language}'" && $6 == "'${content_type}'" $7 == "'${scenario_channel}'")
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

if [ $# -eq 0 ]
then
	echo "${BASH_SOURCE[0]} <topic>" 2>&1
	exit 1
fi

check_data $1
