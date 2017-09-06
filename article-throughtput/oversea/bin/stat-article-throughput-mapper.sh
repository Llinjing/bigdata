#!/bin/bash

function prepare()
{
	chmod +x *
	source ./project.conf 
	return 0
}

## output: content_id<\t>product_id<\t>language<\t>content_type<\t>platform<\t>stat
function mark_data()
{
	local input_file=${mapreduce_map_input_file}

	if [[ "${input_file}" =~ "impression" ]]
	then
		python ./parse-impression.py | awk 'BEGIN{
			FS = "\t"
			OFS = "\t"
		}{
			## content_id<\t>product_id<\t>language<\t>content_type<\t>platform
			count[$1"\t"$2"\t"$3"\t"$4"\t"$5] += 1
		}END{
			for (key in count)
			{
				print key, count[key], "impression"
			}
		}'
    elif [[ "${input_file}" =~ "request" ]]
    then
        python ./parse-request.py | awk 'BEGIN{
            FS = "\t"
            OFS = "\t"
        }{
            ## content_id<\t>product_id<\t>language<\t>content_type<\t>platform
            count[$1"\t"$2"\t"$3"\t"$4"\t"$5] += 1
        }END{
            for (key in count)
            {
                print key, count[key], "request"
            }
        }'
    elif [[ "${input_file}" =~ "article-total-feedback" ]] 
    then		
        awk 'BEGIN{
          	FS = "\t"
           	OFS = "\t"
        }{
			## not NF
   	        if ($8 >= "'${VALID_DATE}'")
            {
       	        print $0, "article-total-feedback"
           	}
       	}'
	fi
}

prepare

mark_data

