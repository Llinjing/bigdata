#!/bin/bash

function prepare()
{
	chmod +x *
	source ./project.conf 
	return 0
}


## output: content_id<\t>product_id<\t>language<\t>impression<\t>update_date<\t>first_date

function mark_data()
{
	local input_file=${mapreduce_map_input_file}

	if [[ "${input_file}" =~ "10min" ]]
	then
		python ./parse-impression.py | awk 'BEGIN{
			FS = "\t"
			OFS = "\t"
		}{
			## content_id<\t>product_id<\t>language<\t>impression
			count[$1"\t"$2"\t"$3] += $4
		}END{
			for (key in count)
			{
				print key, count[key], "impression"
			}
		}'
        elif [[ "${input_file}" =~ "article-total-feedback" ]] 
        then		
                awk 'BEGIN{
                       	FS = "\t"
                 	OFS = "\t"
	        }{
			## not NF
        	        if ($5 >= "'${VALID_DATE}'")
                        {
                       	        print $1, $2, $3, $4, $5, $6, "article-total-feedback"
                  	}
               	}'
	fi
}


prepare

mark_data

