#!/bin/bash

function write_user_gmp()
{
	local data_file=$1
	
	source ../conf/project.conf

	mkdir -p ${LOCAL_DATA_PATH}
	mkdir -p ${LOCAL_LOG_PATH}

	$JAVA_HOME/bin/java \
		-Djava.ext.dirs=${LOCAL_LIB_PATH} \
		-Dlog4j.configuration=file:${LOCAL_CONF_PATH}/log4j-write-to-redis.properties \
		com.inveno.news.article_gmp.ArticleGMPRedisClusterWriter ${LOCAL_CONF_PATH}/write-to-redis.properties \
		${data_file} 1>${LOCAL_LOG_PATH}/write-to-redis.err 2>&1

	return 0
}


write_user_gmp $1
