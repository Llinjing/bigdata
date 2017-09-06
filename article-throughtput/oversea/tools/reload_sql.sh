#!/bin/bash
########################
###	<usage> sh -x reload_sql.sh 140240
########################

function prepare()
{
        source ../conf/project.conf
        source ../conf/hadoop.conf
        source ../conf/mysql.conf
        return 0
}

function insert_into_mysql()
{
	if [ -z $1 ] ; then
		echo "<usage> sh -x reload_sql.sh 140240"
		exit
	fi
	echo "USE dashboard;" >${LOCAL_DATA_PATH}/reload-article-throughput.sql
        echo "LOAD DATA LOCAL INFILE '${LOCAL_DATA_PATH}/article-thoughtput-mysql.list_201609${1}' INTO TABLE \
		t_article_throughput FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n' IGNORE 1 LINES;" \
                >>${LOCAL_DATA_PATH}/reload-article-throughput.sql

#        mysql -h${MYSQL_HOST[2]} -P${MYSQL_PORT[2]} -u${MYSQL_USER[2]} -p${MYSQL_PWD[2]} \
#                <${LOCAL_DATA_PATH}/reload-article-throughput.sql

}

prepare
insert_into_mysql $1
