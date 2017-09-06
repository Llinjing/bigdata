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
	}END{
		for (key in all)
		{
			print key
		}
	}'  

	return 0
}


prepare

stat
