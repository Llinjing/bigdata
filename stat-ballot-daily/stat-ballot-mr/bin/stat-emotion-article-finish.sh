#!/bin/bash
begin_time=`date -d "${yesterday}" +%s`
today=`date +%Y%m%d`
end_time=`date -d "${today}" +%s`
timestamp=${YESTERDAY:0:4}'-'${YESTERDAY:4:2}'-'${YESTERDAY:6:2}" 00:00:00"
function prepare()
{
    source ../conf/project.conf
    source ../conf/hadoop.conf
    source ../conf/mysql.conf
	chmod +x *

    mkdir -p ${LOCAL_TMP_PATH}
	source /etc/profile

    return 0
}


function stat_merge()
{
    ${HADOOP_CAT} ${MR_STAT_EMOTION_ARTICLE_OUTPUT_PATH}/* > ${LOCAL_DATA_PATH}/tmp_result.list  

    #uid,product_id, language, content_id, bore_num,like_num, angry_num, sad_num
	awk 'BEGIN{
                FS = "\t"
                OFS = "\t"
        }{
        if(ARGIND == 1)
        {
            profile[$1]
        }
        else if(ARGIND == 2)
        {
            fresh[$1]
        }
        else if(ARGIND == 3)
        {
            split($1,arr,"###")
            key=arr[2]"\t"arr[3]"\t"arr[4]
            key_all=key"\tall"
            all[key_all]
            result_bore[key_all]+=$2
            result_like[key_all]+=$3
            result_angry[key_all]+=$4
            result_sad[key_all]+=$5
            if(arr[1] in profile)
            {
                key_new=key"\tnew"
                all[key_new]
                result_bore[key_new]+=$2
                result_like[key_new]+=$3
                result_angry[key_new]+=$4
                result_sad[key_new]+=$5
            }else if(arr[1] in fresh)
            {
                key_fresh=key"\tfresh"
                all[key_fresh]
                result_bore[key_fresh]+=$2
                result_like[key_fresh]+=$3
                result_angry[key_fresh]+=$4
                result_sad[key_fresh]+=$5

            }
        }
    }END{
            for(key in all)
            {
                print 0, "'"${timestamp}"'", key, (result_bore[key] + 0),(result_like[key] + 0), (result_angry[key] + 0), (result_sad[key] + 0)
            }

    }' ${LOCAL_DATA_PATH}/profile_user.list ${LOCAL_DATA_PATH}/fresh_user.list ${LOCAL_DATA_PATH}/tmp_result.list \
     > ${LOCAL_DATA_PATH}/result_finish.list
	return 0

}

function insert_sql()
{
    echo "USE dashboard;" >${LOCAL_TMP_PATH}/load-daily-emotion-article-stat.sql
        echo "delete from t_daily_emotion_article_test where timestamp='${timestamp}';" >>${LOCAL_TMP_PATH}/load-daily-emotion-article-stat.sql
	echo "LOAD DATA LOCAL INFILE '${LOCAL_DATA_PATH}/result_finish.list' INTO TABLE \
                t_daily_emotion_article_test FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n';" \
                >>${LOCAL_TMP_PATH}/load-daily-emotion-article-stat.sql

        mysql -h${MYSQL_HOST[2]} -P${MYSQL_PORT[2]} -u${MYSQL_USER[2]} -p${MYSQL_PWD[2]} \
                <${LOCAL_TMP_PATH}/load-daily-emotion-article-stat.sql


}

prepare

#stat_merge

insert_sql
