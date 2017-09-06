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
			#print a[key], "intersect"
			delete b[key]
		}
		else
		{
			#print a[key], "only_gmp"
            ""
		}
		delete a[key]
	}
	
	for(key in b)
	{
		print key, "only_throughput"
	}
}' article_gmp_id_noticias_short_video_all_impression.list article_throughput_20170323_noticias_short_video_all_impression.list
