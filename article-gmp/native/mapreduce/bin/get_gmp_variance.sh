#!/bin/bash

function prepare()
{
        source ../conf/project.conf
        ## KEEP HISTORY DATA
        local curr_time=`date +%Y%m%d%H%M -d"-10 min"`
        local curr_date=`echo "${curr_time}" | awk '{
               print substr($1, 1, 8)
        }'`
        local curr_hour=`echo "${curr_time}" | awk '{
               print substr($1, 9, 2)
        }'`
        local curr_min=`echo "${curr_time}" | awk '{
               print sprintf("%02d", int(substr($1, 11, 2) / 10) * 10)
        }'`
        local monitor_time=${curr_date}${curr_hour}${curr_min}
        mv ${LOCAL_DATA_PATH}/article-gmp-monitor-variance.list ${LOCAL_DATA_PATH}/monitor-data/article-gmp-monitor-variance.list_${monitor_time}
        return 0
}

function get_path()
{
	base_path=${LOCAL_DATA_PATH}"/article-gmp/"
	for ((i=2; i<14; i+=1))  
	do  
	    path_tmp=`date +%Y%m%d%H%M -d"-$[i*10] min"`
	    gmp_path[$[i-2]]=${base_path}${path_tmp:0:11}"0/article-gmp.list"
	done

	throughtput_path=${base_path}${path_tmp:0:11}"0/article-throughtput.list"
}

function get_variance()
{
	get_path	

#        for data in ${gmp_path[@]}
#        do
#            echo ${data}  
#        done
#        echo ${throughtput_path}	

	awk 'BEGIN{
		FS = "\t"
		OFS = "\t"
		OFMT = "%.8f"
	}{
		if (ARGIND == 1)
		{
			throughput[$1]
		}
		else if (ARGIND == 2)
		{
			gmp_0[$1] = $5
			gmp_info[$1] = $2
		}
		else if (ARGIND == 3)
                {
			gmp_1[$1] = $5
                }
                else if (ARGIND == 4)
                {
                        gmp_2[$1] = $5
                }
                else if (ARGIND == 5)
                {
                        gmp_3[$1] = $5
                }
                else if (ARGIND == 6)
                {
                        gmp_4[$1] = $5
                }
                else if (ARGIND == 7)
                {
                        gmp_5[$1] = $5
                }
                else if (ARGIND == 8)
                {
                        gmp_6[$1] = $5
                }
                else if (ARGIND == 9)
                {
                        gmp_7[$1] = $5
                }
                else if (ARGIND == 10)
                {
                        gmp_8[$1] = $5
                }
                else if (ARGIND == 11)
                {
                        gmp_9[$1] = $5
                }
                else if (ARGIND == 12)
                {
                        gmp_10[$1] = $5
                }
                else if (ARGIND == 13)
                {
                        gmp_11[$1] = $5
                }
	}END{
		time_length = 12.0
		for(key in throughput)
		{
			dict[0]=gmp_0[key]
			dict[1]=gmp_1[key]
			dict[2]=gmp_2[key]
			dict[3]=gmp_3[key]
			dict[4]=gmp_4[key]
			dict[5]=gmp_5[key]
			dict[6]=gmp_6[key]
			dict[7]=gmp_7[key]
			dict[8]=gmp_8[key]
			dict[9]=gmp_9[key]
			dict[10]=gmp_10[key]
			dict[11]=gmp_11[key]
			sum = 0.0 
			variance = 0.0

			for(key1 in dict)
			{
				sum += dict[key1]
			}
			average = sum/time_length

			for(key2 in dict)
			{
				variance += (dict[key2]-average)*(dict[key2]-average)
			}
			variance = variance/time_length

			###print key, gmp_info[key], gmp_0[key], gmp_1[key], gmp_2[key], gmp_3[key], gmp_4[key], gmp_5[key], gmp_6[key], gmp_7[key], gmp_8[key], gmp_9[key], gmp_10[key], gmp_11[key], sum, average, variance
			print gmp_info[key], variance
		}
	}' ${throughtput_path} ${gmp_path[0]} ${gmp_path[1]} ${gmp_path[2]} ${gmp_path[3]} ${gmp_path[4]} ${gmp_path[5]} ${gmp_path[6]} ${gmp_path[7]} ${gmp_path[8]} ${gmp_path[9]} ${gmp_path[10]} ${gmp_path[11]} > ${LOCAL_DATA_PATH}"/article-gmp-variance.list"

}

function analysis()
{
	touch ${LOCAL_DATA_PATH}"/article-gmp-variance.list"
	cat ${LOCAL_DATA_PATH}"/article-gmp-variance.list" | awk 'BEGIN{
		FS = "\t"
	}{
		dict[$1]
		if($2 < 0.00001)
		{
			variance_ge_0_lt_1[$1]++
		}
		else if($2 < 0.00003)
		{
			variance_ge_1_lt_3[$1]++
		}
                else if($2 < 0.00005)
                {
			variance_ge_3_lt_5[$1]++
                }
                else if($2 < 0.00007)
                {
			variance_ge_5_lt_7[$1]++
                }
                else if($2 < 0.00009)
                {
			variance_ge_7_lt_9[$1]++
                }
                else
                {
			variance_ge_9[$1]++
                }
	}END{
		for(key in dict)
		{
			print key, variance_ge_0_lt_1[key]+0, variance_ge_1_lt_3[key]+0, variance_ge_3_lt_5[key]+0, variance_ge_5_lt_7[key]+0, variance_ge_7_lt_9[key]+0, variance_ge_9[key]+0
		}
	}' > ${LOCAL_DATA_PATH}"/article-gmp-monitor-variance.list"
}

function delete_old_data()
{
	old_path=${LOCAL_DATA_PATH}"/article-gmp/"`date +%Y%m%d -d"-1 day"`"[0-2][0-9][0-5]0"
	echo ${old_path}
	rm -rf ${old_path}
}


prepare
get_variance
analysis
delete_old_data
