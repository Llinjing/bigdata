#!/bin/bash

function prepare()
{
	chmod +x *
	return 0
}

function output()
{
	yesterday=`date +%Y-%m-%d" 00:00:00" -d"-1 day"`
        begin_time=`date -d "${yesterday}" +%s`
        today=`date +%Y-%m-%d" 00:00:00"`
        end_time=`date -d "${today}" +%s`
	#python ./stat-user-click-parse.py ${begin_time} ${end_time} 2>/dev/null | awk 'BEGIN{

	python ./stat-four-emotion-parse.py 2>/dev/null 
	return 0
}

prepare
output

