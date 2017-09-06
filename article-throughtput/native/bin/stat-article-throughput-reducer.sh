#!/bin/bash

function prepare()
{
	chmod +x *
	source ./project.conf
	return 0
}

function merge()
{
	date_mark=`date +%Y%m%d%H%M%S`

        mkdir -p tmp

        awk 'BEGIN{
                FS = "\t"
                OFS = "\t"
        }{
                if ($NF == "impression")
                {
                        impression[$1"\t"$2"\t"$3] += $4
                }
                else if ($NF == "article-total-feedback")
                {
			## content_id<\t>product_id<\t>language<\t>impression<\t>date
                        print $0 >"./tmp/article-total-feedback.list"
                }
        }END{
                for (key in impression)
                {
			## content_id<\t>product_id<\t>language<\t>impression
                        print key, impression[key]+0 >"./tmp/impression.list"
                }
        }'

        touch ./tmp/impression.list
        touch ./tmp/article-total-feedback.list


        awk 'BEGIN{
                FS = "\t"
                OFS = "\t"
        }{
		key = $1"\t"$2"\t"$3
                if (ARGIND == 1)
                {
			#key = $1"\t"$2"\t"$3
                        impression[key] = $4
                }
                else
                {
			#key = $1"\t"$2"\t"$3
                        if (key in impression)
                        {
				## impression add
                                new_impression = $4 + impression[key]
                                print key, new_impression, "'${curr_date}'", $6, "#A"
				
				if ($4<'${IMPRESSION_THRESHOLD}' && new_impression>='${IMPRESSION_THRESHOLD}')
				{
					print key, $6, "#B"
				}
                                delete impression[key]
                        }
                        else 
                        {
                                print key, $4, $5, $6, "#A"
                        }
                }
        }END{
                for (key in impression)
                {
                        new_impression = (impression[key] + 0)
                        print key, new_impression, "'${curr_date}'", "'${date_mark}'","#A"
                        
			if (new_impression >= '${IMPRESSION_THRESHOLD}')
                        {
                        	print key, "'${date_mark}'", "#B"
                        }
                }
        }' ./tmp/impression.list ./tmp/article-total-feedback.list

        rm -rf tmp

        return 0
}
curr_date=$1

prepare

merge

