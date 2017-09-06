#!/bin/bash

awk 'BEGIN{
	FS="\t"
	OFS="\t"
}{
	if(ARGIND == 1)
	{
		a[$1] 
	}
	else
	{
		b[$1]
	}	
}END{
	print length(a)
	print length(b)
	for(key in a)
	{
		if(key in b)
		{
			print key, "intersect"
		}
	}
}' article_throughput_hotoday_hindi_20161029_14908.txt article_throughput_hotoday_hindi_20161030_8982.txt
