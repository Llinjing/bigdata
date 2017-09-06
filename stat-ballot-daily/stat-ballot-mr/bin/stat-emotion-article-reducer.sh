#!/bin/bash

function prepare()
{
	chmod +x *
	return 0
}

function stat()
{
	awk 'BEGIN{
		FS = "\t"
		OFS = "\t"
	}{
        all[$1] 
		bore_num[$1]+=$2
		like_num[$1]+=$3
		angry_num[$1]+=$4
		sad_num[$1]+=$5
	}END{
		for (key in all)
		{
			print key, bore_num[key], like_num[key], angry_num[key], sad_num[key]
		}
	}'  

	return 0
}


prepare

stat
