#!/usr/bash

awk 'BEGIN{
	FS=OFS="\t"
}{
	old_data=$2

        min = sprintf("%s-%s-%s %s:%s:00",
      		substr(old_data, 1, 4),
                substr(old_data, 5, 2),
                substr(old_data, 7, 2),
                substr(old_data, 9, 2),
                substr(old_data, 11, 2))

        hour = sprintf("%s-%s-%s %s:00:00",
                substr(old_data, 1, 4),
                substr(old_data, 5, 2),
                substr(old_data, 7, 2),
                substr(old_data, 9, 2))
 
        day = sprintf("%s-%s-%s 00:00:00",
               substr(old_data, 1, 4),
               substr(old_data, 5, 2),
               substr(old_data, 7, 2))

	print "0", day, hour, min, $3, $4 
}END{
}'

