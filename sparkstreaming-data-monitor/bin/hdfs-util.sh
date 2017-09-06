#!/bin/bash

function delete_by_date()
{
	local hdfs_dir=$1
	local date_format=$2
	local keeper_days=$3

	local expired_date=`date +${date_format} -d"-${keeper_days} day"`
	
	hadoop fs -ls ${hdfs_dir} | awk '{if(NF == 8) print $NF}' | 
	while read -r hdfs_sub_dir
	do
		local date_tag=`echo ${hdfs_sub_dir} | awk '{n = split($1, arr, "/"); print arr[n]}'`
		if [[ ${date_tag} < ${expired_date} ]]
		then
			hadoop fs -rm -r ${hdfs_sub_dir}
		fi
	done
	
	return 0
}
