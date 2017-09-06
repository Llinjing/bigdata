#!/bin/bash

function prepare()
{
	chmod +x *
	return 0
}


## output: content_id<\t>product_id<\t>uid<\t>click|request|impression

function mark_data()
{
	local input_file=${mapreduce_map_input_file}

	if [[ "${input_file}" =~ "click" ]]
	then
		python ./parse-click.py | awk 'BEGIN{
			FS = "\t"
			OFS = "\t"
		}{
			count[$1"\t"$2"\t"$3]++
		}END{
			for (key in count)
			{
				print key, "click"
			}
		}'
	elif [[ "${input_file}" =~ "impression" ]]
	then
		python ./parse-impression.py | awk 'BEGIN{
			FS = "\t"
			OFS = "\t"
		}{
			count[$1"\t"$2"\t"$3]++
		}END{
			for (key in count)
			{
				print key, "impression"
			}
		}'
        elif [[ "${input_file}" =~ "request" ]]
        then
                python ./parse-impression.py | awk 'BEGIN{
                        FS = "\t"
                        OFS = "\t"
                }{
                        count[$1"\t"$2"\t"$3]++
                }END{
                        for (key in count)
                        {
                                print key, "impression"
                        }
                }'
	fi
}


prepare

mark_data

