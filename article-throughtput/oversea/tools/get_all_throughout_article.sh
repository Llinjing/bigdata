#!/bin/bash

function get_data ()
{
        i=-1
        last_date="0000"
        while(($i<23)) ; do
                i=`expr $i + 1`
                for j in `seq 0 5` ; do
                        if [ $i -lt 10 ] ; then
                                date="0"$i$j"0"
                        else
                                date=$i$j"0"
                        fi
                        path="/inveno-projects/article-throughput-stat/data/article-throughput/history/${1}/"$date"/part*B"
                        if [ $date != "0000" ] ; then
                                echo $path
                                hadoop fs -cat $path >> article_throughput_${1}.txt
                        fi
                        last_date=$date
                done
        done

        hadoop fs -cat "/inveno-projects/article-throughput-stat/data/article-throughput/history/${2}/0000/part*B" >> article_throughput_${1}.txt
                
        return 0
}

function parse_result ()
{
        filelist=`ls ./`
        for file in $filelist
        do
                grep coolpad $file | awk 'BEGIN{OFS="\t"}{print "'$file'", $0}END{}'  ##> ../througthput_coolpad_0806
        done
        return 0

}



get_data $1 $2
#parse_result
