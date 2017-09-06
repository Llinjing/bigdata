#!/bin/bash

function prepare()
{
	chmod +x *
	source ./decay.conf
	return 0
}

function mark_data()
{
	local input_file=${mapreduce_map_input_file}

	if [[ "${input_file}" =~ "article-click-impression" ]]
	then
		awk 'BEGIN{
			FS = "\t"
			OFS = "\t"
		}{
			print $0, "article-click-impression"
		}'
	elif [[ "${input_file}" =~ "latest-article-gmp" ]]
	then
		awk 'BEGIN{
			FS = "\t"
			OFS = "\t"
		}{
			# filter part-A: article_id<\t>product_id
			if (NF>3 && $6>"'${VALID_DATE}'"){
				print $0, "latest-article-gmp"
			}
		}'
	fi

	return 0
}


prepare

mark_data

