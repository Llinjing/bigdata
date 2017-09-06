#!/bin/bash

function prepare()
{
	chmod +x *
	return 0
}


## output: content_id<\t>product_id<\t>language<\t>click<\t>impression

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
				print key, count[key], 0
			}
		}'
        elif [[ "${input_file}" =~ "request" ]]
        then
                python ./parse-request.py | awk 'BEGIN{
                        FS = "\t"
                        OFS = "\t"
                }{
                        count[$1"\t"$2"\t"$3]++
                }END{
                        for (key in count)
                        {
                                print key, 0, count[key]
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
				print key, 0, count[key]
			}
		}'
	fi
}


prepare

mark_data

