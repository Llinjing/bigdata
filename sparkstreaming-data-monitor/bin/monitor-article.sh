#!/bin/bash

function check_data()
{

	source ../conf/hadoop.conf
	source ../conf/project.conf
	source ../conf/warning.conf

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

    # HDFS_TOPIC_DATA_PATH
	# ${HADOOP_CAT} s3://bigdata-east/inveno-data/format-data/topic/impression-reformat/${curr_date}/${curr_hour}/*.${begin_time} >${LOCAL_TMP_PATH}/article.list
	${HADOOP_CAT} ${HDFS_TOPIC_DATA_PATH}/impression-reformat/${curr_date}/${curr_hour}/*.${begin_time}_* >${LOCAL_TMP_PATH}/article.list
	#${HADOOP_CAT} s3://bigdata-east/inveno-data/format-data/topic/request-reformat/${curr_date}/${curr_hour}/*.${begin_time} >>${LOCAL_TMP_PATH}/article.list
	${HADOOP_CAT} ${HDFS_TOPIC_DATA_PATH}/request-reformat/${curr_date}/${curr_hour}/*.${begin_time}_* >>${LOCAL_TMP_PATH}/article.list
	touch ${LOCAL_TMP_PATH}/article.list

	#no data, need warning
	if [ ! -s ${LOCAL_TMP_PATH}/article.list ]
	then
		${WARNING_BIN} "${PRODUCT_LINE} : article.list no data : ${topic}_${begin_time}" "${SMS_USER[$FATAL]}"
	else
		rm -rf articles
		mkdir -p articles

		#local match_date=`date +%Y-%m-%d -d"+8 hour -10 min"`  # 新加坡为UTC
		local match_date=`date +%Y-%m-%d -d"+0 hour -10 min"`

		cat ${LOCAL_TMP_PATH}/article.list | python ./parse-article-feedback-tmp.py >../tmp/parse.list
		cat ../tmp/parse.list | awk 'BEGIN{
			FS = "\t"
			OFS = "\t"
		}{
			if($3 ~ "'${match_date}'")
			{
				dedup[$1"\t"$2]
				dedup_article[$1]
			}
		}END{
			for (key in dedup)
			{
				split(key, arr, "\t")
				print arr[2] >"articles/"arr[1]
			}

			for (key in dedup_article)
			{
				print key >"'${LOCAL_TMP_PATH}'/article-dedup.list"
			}
		}'
		touch ${LOCAL_TMP_PATH}/article-dedup.list

		awk 'BEGIN{
			FS = "\t"
			OFS = "\t"
		}{
			if (ARGIND == 1)
			{
				black[$1] = $2
			}
			else
			{
				if ($1 in black)
				{
					print $1, black[$1]
				}
			}
		}' ${LOCAL_CONF_PATH}/blacklist.conf ${LOCAL_TMP_PATH}/article-dedup.list >${LOCAL_TMP_PATH}/hit-blacklist.list
		touch ${LOCAL_TMP_PATH}/hit-blacklist.list

		if [ -s ${LOCAL_TMP_PATH}/hit-blacklist.list ]
		then
			:>../tmp/hit-blacklist-src.list

			while read -r content_id publisher
			do
				grep -a "${content_id}" ../tmp/article.list | python ./parse-final.py >>../tmp/hit-blacklist-src.list
			done <${LOCAL_TMP_PATH}/hit-blacklist.list

			cat ../tmp/hit-blacklist-src.list | awk -F '\t' '{
				if ($4 == "report" && $1 < 1013829022)
				{
					next
				}
				print $0
			}' | python ./final-result.py >../tmp/warning-result.list
			sed -i "s/\"//g" ../tmp/warning-result.list

			while read -r warning_str
			do
				##${WARNING_BIN} "hit blacklist : $warning_str" "${SMS_USER[$FATAL]}"
				echo ""
			done <../tmp/warning-result.list
		fi
	fi

	return 0
}


check_data
