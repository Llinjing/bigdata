#!/bin/bash

timestamp=`date +%Y%m%d%H%M`

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
        #monitor_time=`date +%Y%m%d%H%M%S`
##        mv ${LOCAL_DATA_PATH}/article-gmp-monitor-variance.list ${LOCAL_DATA_PATH}/monitor-data/article-gmp-monitor-variance.list_${monitor_time}
        return 0
}

function get_path()
{
	base_path=${LOCAL_DATA_PATH}"/article-gmp/"
	for ((i=2; i<14; i+=1))  
	do  
	    path_tmp=`date +%Y%m%d%H%M -d"-$[i*10] min"`
	    gmp_path_after[$[i-2]]=${base_path}${path_tmp:0:11}"0/article-gmp.list"
	done

	article_gmp_path=${base_path}${path_tmp:0:11}"0/article-gmp.list"

        for ((i=14; i<20; i+=1))
        do
            path_tmp=`date +%Y%m%d%H%M -d"-$[i*10] min"`
            gmp_path_before[$[i-14]]=${base_path}${path_tmp:0:11}"0/article-gmp.list"
        done

        for data in ${gmp_path_after[@]}
        do
            touch ${data}  
        done
        touch ${article_gmp_path}    
	for data in ${gmp_path_before[@]}
	do
	    touch ${data}
	done
}

function get_high_gmp_article()
{
        awk 'BEGIN{
                FS = "\t"
                OFS = "\t"
        }{
		key = $1"\t"$2
                if(ARGIND == 1)
                {
                        high_gmp_article[key]
                }
                else if(ARGIND == 2)
                {
                        gmp = $3
                        if(key in high_gmp_article)
                        {
                                print $0, "alive"
                        }
                        else
                        {
                                if(gmp >= 0.02)
                                {
                                        print key, gmp, "new_high_gmp_article"
                                }
                        }
                }
        }END{
        }' ${LOCAL_DATA_PATH}/high-gmp-article-variance.list $1 > ${LOCAL_DATA_PATH}/high-gmp-article-variance.list_all

        grep "new_high_gmp_article" ${LOCAL_DATA_PATH}/high-gmp-article-variance.list_all | awk 'BEGIN{
                FS = "\t"
		OFS = "\t"
        }{
                print $0, '${timestamp}'
        }END{}' > ${LOCAL_DATA_PATH}/high-gmp-article-variance.list_new

	cat ${LOCAL_DATA_PATH}/high-gmp-article-variance.list_new >> ${LOCAL_DATA_PATH}/high-gmp-article-variance.list
}

function get_variance()
{
	get_path	
	get_high_gmp_article ${article_gmp_path}	

	awk 'BEGIN{
		FS = "\t"
		OFS = "\t"
		OFMT = "%.8f"
	}{
		article_key = $1"\t"$2
		product_id[article_key] = $1
		gmp = $3

		if (ARGIND == 1)
		{
			high_gmp_article[article_key]
		}
		else if (ARGIND == 2)
		{
			gmp_0[article_key] = gmp
		}
		else if (ARGIND == 3)
                {
			gmp_1[article_key] = gmp
                }
                else if (ARGIND == 4)
                {
                        gmp_2[article_key] = gmp
                }
                else if (ARGIND == 5)
                {
                        gmp_3[article_key] = gmp
                }
                else if (ARGIND == 6)
                {
                        gmp_4[article_key] = gmp
                }
                else if (ARGIND == 7)
                {
                        gmp_5[article_key] = gmp
                }
                else if (ARGIND == 8)
                {
                        gmp_6[article_key] = gmp
                }
                else if (ARGIND == 9)
                {
                        gmp_7[article_key] = gmp
                }
                else if (ARGIND == 10)
                {
                        gmp_8[article_key] = gmp
                }
                else if (ARGIND == 11)
                {
                        gmp_9[article_key] = gmp
                }
                else if (ARGIND == 12)
                {
                        gmp_10[article_key] = gmp
                }
                else if (ARGIND == 13)
                {
                        gmp_11[article_key] = gmp
                }
                else if (ARGIND == 14)
                {
                        gmp_101[article_key] = gmp
                }
                else if (ARGIND == 15)
                {
                        gmp_102[article_key] = gmp
                }
                else if (ARGIND == 16)
                {
                        gmp_103[article_key] = gmp
                }
                else if (ARGIND == 17)
                {
                        gmp_104[article_key] = gmp
                }
                else if (ARGIND == 18)
                {
                        gmp_105[article_key] = gmp
                }
                else if (ARGIND == 19)
                {
                        gmp_106[article_key] = gmp
                }
	}END{
		time_length = 18.0
		for(key in high_gmp_article)
		{
			dict[0]=gmp_0[key]+0
			dict[1]=gmp_1[key]+0
			dict[2]=gmp_2[key]+0
			dict[3]=gmp_3[key]+0
			dict[4]=gmp_4[key]+0
			dict[5]=gmp_5[key]+0
			dict[6]=gmp_6[key]+0
			dict[7]=gmp_7[key]+0
			dict[8]=gmp_8[key]+0
			dict[9]=gmp_9[key]+0
			dict[10]=gmp_10[key]+0
			dict[11]=gmp_11[key]+0

			dict[12]=gmp_101[key]+0
			dict[13]=gmp_102[key]+0
                        dict[14]=gmp_103[key]+0
                        dict[15]=gmp_104[key]+0
                        dict[16]=gmp_105[key]+0
                        dict[17]=gmp_106[key]+0

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

			##print key, gmp_info[key], gmp_0[key], gmp_1[key], gmp_2[key], gmp_3[key], gmp_4[key], gmp_5[key], gmp_6[key], gmp_7[key], gmp_8[key], gmp_9[key], gmp_10[key], gmp_11[key], gmp_101[key], gmp_102[key], gmp_103[key], gmp_104[key], gmp_105[key], gmp_106[key], sum, average, variance
			print product_id[key]"-spark-streaming", variance
		}
	}' ${LOCAL_DATA_PATH}/high-gmp-article-variance.list_new ${gmp_path_after[0]} ${gmp_path_after[1]} ${gmp_path_after[2]} ${gmp_path_after[3]} ${gmp_path_after[4]} ${gmp_path_after[5]} ${gmp_path_after[6]} ${gmp_path_after[7]} ${gmp_path_after[8]} ${gmp_path_after[9]} ${gmp_path_after[10]} ${gmp_path_after[11]} ${gmp_path_before[0]} ${gmp_path_before[1]} ${gmp_path_before[2]} ${gmp_path_before[3]} ${gmp_path_before[4]} ${gmp_path_before[5]} > ${LOCAL_DATA_PATH}"/article-gmp-monitor-variance.tmp"
}

function analysis()
{
	touch ${LOCAL_DATA_PATH}"/article-gmp-monitor-variance.tmp"
	cat ${LOCAL_DATA_PATH}"/article-gmp-monitor-variance.tmp" | awk 'BEGIN{
		FS = "\t"
		OFS = "\t"
	}{
		dict[$1]
		if($2 < 0.0001)
		{
			variance_ge_0_lt_1[$1]++
		}
		else if($2 < 0.0003)
		{
			variance_ge_1_lt_3[$1]++
		}
                else if($2 < 0.0005)
                {
			variance_ge_3_lt_5[$1]++
                }
                else if($2 < 0.0007)
                {
			variance_ge_5_lt_7[$1]++
                }
                else if($2 < 0.0009)
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
	old_path=${LOCAL_DATA_PATH}"/article-gmp/"`date +%Y%m%d -d"-2 day"`"[0-2][0-9][0-5]0"
	echo ${old_path}
	rm -rf ${old_path}
}


prepare
get_variance
analysis
delete_old_data
