#!/bin/bash

source ../conf/project.conf

for((i=0;i<${TOPIC_NUM};++i))
do
	${CHECK_BIN[$i]} ${TOPIC[$i]} 
done
