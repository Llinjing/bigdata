#!/bin/bash

function prepare()
{
	source ../conf/project.conf
	source ../conf/hadoop.conf

	${HADOOP_MKDIR} ${MR_DATA_PATH}/latest-article-gmp
	${HADOOP_TOUCH} ${MR_DATA_PATH}/latest-article-gmp/_empty

	return 0
}


prepare

