#!/bin/bash

function prepare()
{
        source ../conf/project.conf
        source ../conf/hadoop.conf
        source ../conf/mysql.conf
        source ../conf/decay.conf
	source ../conf/warning.conf

        return 0
}

function add_one_partition()
{
	curr_date=$1
	curr_time=$2
	hive -e "alter table article_gmp add partition (date='${curr_date}', min='${curr_time}') location '/inveno-projects/offline/article-gmp/data/article-gmp/history/${curr_date}/${curr_time}/'"
	return 0
}

function add_one_day_partitions()
{
        base_path=${MR_DATA_PATH}/article-gmp/history/$1/

	for ((i=0; i<144; i+=1))
	do
	    tmp=`date +%H%M -d"-$[i*10] min"`
	    time_min[i]=${tmp:0:3}"0"
	done
	
	sql="\"alter table article_gmp add"
	for data in ${time_min[@]}  
	do  
		sql+=" partition (date='$1', min='${data}') location '/inveno-projects/offline/article-gmp/data/article-gmp/history/$1/${data}/'"
	done
	sql+="\""
	echo ${sql}
	hive -e ${sql}
}

function add_some_hours_partitions()
{
        base_path=${MR_DATA_PATH}/article-gmp/history/$1/

        for ((i=0; i<50; i+=1))
        do
            tmp=`date +%H%M -d"-$[i*10+20] min"`
	    if [ tmp > "0830" ] ; then
	    	time_min[i]=${tmp:0:3}"0"
	    fi
	    echo ${tmp}
        done

        sql="hive -e \"alter table article_gmp add"
        for data in ${time_min[@]}
        do
                sql+=" partition (date='$1', min='${data}') location '/inveno-projects/offline/article-gmp/data/article-gmp/history/$1/${data}/'"
        done
        sql+="\""
        echo ${sql} > hive.sh
        ##hive -e ${sql}
	##sh -x ./hive.sh
}

prepare
#add_one_partition 20160921 0810
#add_one_day_partitions 20160921
add_some_hours_partitions 20160922





