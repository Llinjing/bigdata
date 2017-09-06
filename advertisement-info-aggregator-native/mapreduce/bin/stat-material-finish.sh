#!/bin/bash

function prepare()
{
    source ../conf/project.conf
    source ../conf/hadoop.conf
    source ../conf/mysql.conf

    return 0
}

function stat_merge()
{
#    ${HADOOP_CAT} ${MR_STAT_MATERIAL_CLICK_OUTPUT_PATH}/part-* | awk '{
#        print $0
#    }' > ${LOCAL_DATA_PATH}/material-click.list
#    touch ${LOCAL_DATA_PATH}/material-click.list
#
#    ${HADOOP_CAT} ${MR_STAT_MATERIAL_IMPRESSION_OUTPUT_PATH}/part-* | awk '{
#        print $0
#    }' > ${LOCAL_DATA_PATH}/material-impression.list
#    touch ${LOCAL_DATA_PATH}/material-impression.list

    awk 'BEGIN{
        FS="\t"
        OFS="\t"
        OFMT="%.8f"
    }{
        key = $1
        advertisement[key]
        if(ARGIND == 1)
        {
            click[key] += $2
        }
        else if(ARGIND == 2)
        {
            impression[key] += $2
        }
    }END{
        timestamp = sprintf("%s-%s-%s 00:00:00",
            substr("'${YESTERDAY}'", 1, 4),
            substr("'${YESTERDAY}'", 5, 2),
            substr("'${YESTERDAY}'", 7, 2))

        for(key in advertisement)
        {
            ###advertisement_id,advertiser_name,scenario,ad_source,channal_id,product_id,app_ver,position_id,position_type,target_size,industry,advertisement_type,news_configid,biz_configid,ad_configid,unit_price,pay_model
            split(key, arr, "###")
            ad_impression = impression[key]+0
            ad_click = click[key]+0
            unit_price = arr[16]
            pay_model = arr[17]
            if(pay_model == "cpc")
            {
                income = (unit_price*ad_click)+0.0
            }else if(pay_model == "cpm")
            {
                income = (unit_price*ad_impression/1000)+0.0
            }
            print 0, timestamp, arr[1], arr[2], arr[3], arr[4], arr[5], arr[6], arr[7], arr[8], arr[9], arr[10], arr[11], arr[12], arr[13], arr[14], arr[15], arr[16], arr[17], ad_impression, ad_click, income
        }

    }' ${LOCAL_DATA_PATH}/material-click.list ${LOCAL_DATA_PATH}/material-impression.list > ${LOCAL_DATA_PATH}/material-finish.list
}

function insert_mysql()
{
    echo "USE dashboard;" > ${LOCAL_TMP_PATH}/load-daily-ad-material.sql
    echo "LOAD DATA LOCAL INFILE '${LOCAL_DATA_PATH}/material-finish.list' INTO TABLE \
        t_daily_ad_material FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n' IGNORE 1 LINES;" \
        >> ${LOCAL_TMP_PATH}/load-daily-ad-material.sql

    mysql -h${MYSQL_HOST[2]} -P${MYSQL_PORT[2]} -u${MYSQL_USER[2]} -p${MYSQL_PWD[2]} < ${LOCAL_TMP_PATH}/load-daily-ad-material.sql
}

prepare
stat_merge
insert_mysql

