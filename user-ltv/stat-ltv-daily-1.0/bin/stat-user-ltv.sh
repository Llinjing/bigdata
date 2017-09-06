#!/bin/bash
echo $1
date=$1
if [ "$date" ]; then
        echo "$date"
else
        date=`date -d yesterday +%Y%m%d`
        echo "$date"
fi
function prepare()
{
        source ../conf/project.conf
        source ../conf/hadoop.conf
        source ../conf/mysql.conf
	export HADOOP_HOME=/usr/lib/hadoop/
        mkdir -p ${LOCAL_DATA_PATH}/user_ltv/${date}/
        mkdir -p ${LOCAL_TMP_PATH}

        return 0
}

function exec_hive()
{
	/usr/lib/hive/bin/hive -e "set mapred.job.queue.name=offline;select product_id,app_ver,language,promotion,news_configid,protocol,content_type,'all',uid,count(*) as num from ( 
                                                select get_json_object(json_string,'$.uid') as uid,get_json_object(json_string,'$.product_id') as product_id,\
                                                       get_json_object(json_string,'$.upack.news_configid') as news_configid,get_json_object(json_string,'$.article_impression_extra.content_type') as content_type,\
                                                       get_json_object(json_string,'$.protocol') as protocol,get_json_object(json_string,'$.language') as language,get_json_object(json_string,'$.app_ver') as app_ver,\
                                                       get_json_object(json_string,'$.promotion') as promotion, get_json_object(json_string,'$.article_impression_extra.content_id') as content_id,get_json_object(json_string,'$.event_id') as event_id \
                                                from impression_reformat where date='${date}' and get_json_object(json_string,'$.scenario.desc') != '' and get_json_object(json_string,'$.scenario.desc') != 'push' group by \
                                                        get_json_object(json_string,'$.uid'),get_json_object(json_string,'$.product_id'),get_json_object(json_string,'$.upack.news_configid'),\
                                                        get_json_object(json_string,'$.article_impression_extra.content_type'),get_json_object(json_string,'$.protocol'),get_json_object(json_string,'$.language'),\
                                                        get_json_object(json_string,'$.app_ver'),get_json_object(json_string,'$.promotion'), get_json_object(json_string,'$.article_impression_extra.content_id'),\
                                                        get_json_object(json_string,'$.event_id') \
                                                ) t \
                                        group by product_id,app_ver,language,promotion,news_configid,protocol,content_type,uid" > ${LOCAL_DATA_PATH}/user_ltv/${date}/user_impression_nopush_ltv.list

	/usr/lib/hive/bin/hive -e "set mapred.job.queue.name=offline;select product_id,app_ver,language,promotion,news_configid,protocol,content_type,'all',uid,count(*) as num from ( 
                                                select get_json_object(json_string,'$.uid') as uid,get_json_object(json_string,'$.product_id') as product_id,\
                                                       get_json_object(json_string,'$.upack.news_configid') as news_configid,get_json_object(json_string,'$.article_click_extra.content_type') as content_type,\
                                                       get_json_object(json_string,'$.protocol') as protocol,get_json_object(json_string,'$.language') as language,get_json_object(json_string,'$.app_ver') as app_ver,\
                                                       get_json_object(json_string,'$.promotion') as promotion, get_json_object(json_string,'$.article_click_extra.content_id') as content_id,get_json_object(json_string,'$.event_id') as event_id \
                                                from click_reformat where date='${date}' and get_json_object(json_string,'$.scenario.desc') = 'push' group by \
                                                        get_json_object(json_string,'$.uid'),get_json_object(json_string,'$.product_id'),get_json_object(json_string,'$.upack.news_configid'),\
                                                        get_json_object(json_string,'$.article_click_extra.content_type'),get_json_object(json_string,'$.protocol'),get_json_object(json_string,'$.language'),\
                                                        get_json_object(json_string,'$.app_ver'),get_json_object(json_string,'$.promotion'), get_json_object(json_string,'$.article_click_extra.content_id'),\
                                                        get_json_object(json_string,'$.event_id') \
                                                ) t \
                                        group by product_id,app_ver,language,promotion,news_configid,protocol,content_type,uid" > ${LOCAL_DATA_PATH}/user_ltv/${date}/user_impression_pushclick_ltv.list
	/usr/lib/hive/bin/hive -e "set mapred.job.queue.name=offline;select product_id,app_ver,language,promotion,news_configid,protocol,content_type,scenario,uid,count(*) as num from ( 
                                                select get_json_object(json_string,'$.uid') as uid,get_json_object(json_string,'$.scenario.desc') as scenario,get_json_object(json_string,'$.product_id') as product_id,\
                                                       get_json_object(json_string,'$.upack.news_configid') as news_configid,get_json_object(json_string,'$.article_click_extra.content_type') as content_type,\
                                                       get_json_object(json_string,'$.protocol') as protocol,get_json_object(json_string,'$.language') as language,get_json_object(json_string,'$.app_ver') as app_ver,\
                                                       get_json_object(json_string,'$.promotion') as promotion, get_json_object(json_string,'$.article_click_extra.content_id') as content_id,get_json_object(json_string,'$.event_id') as event_id \
                                                from click_reformat where date='${date}' group by \
                                                        get_json_object(json_string,'$.uid'),get_json_object(json_string,'$.scenario.desc'),get_json_object(json_string,'$.product_id'),get_json_object(json_string,'$.upack.news_configid'),\
                                                        get_json_object(json_string,'$.article_click_extra.content_type'),get_json_object(json_string,'$.protocol'),get_json_object(json_string,'$.language'),\
                                                        get_json_object(json_string,'$.app_ver'),get_json_object(json_string,'$.promotion'), get_json_object(json_string,'$.article_click_extra.content_id'),\
                                                        get_json_object(json_string,'$.event_id') \
                                                ) t group by product_id,app_ver,language,promotion,news_configid,protocol,content_type,scenario,uid;" > ${LOCAL_DATA_PATH}/user_ltv/${date}/user_click_ltv.list

        /usr/lib/hive/bin/hive -e "set mapred.job.queue.name=offline;select product_id,app_ver,language,promotion,news_configid,protocol,content_type,scenario,uid,count(*) as num from ( 
                                                select get_json_object(json_string,'$.uid') as uid,get_json_object(json_string,'$.scenario.desc') as scenario,get_json_object(json_string,'$.product_id') as product_id,\
                                                       get_json_object(json_string,'$.upack.news_configid') as news_configid,get_json_object(json_string,'$.article_impression_extra.content_type') as content_type,\
                                                       get_json_object(json_string,'$.protocol') as protocol,get_json_object(json_string,'$.language') as language,get_json_object(json_string,'$.app_ver') as app_ver,\
                                                       get_json_object(json_string,'$.promotion') as promotion, get_json_object(json_string,'$.article_impression_extra.content_id') as content_id,get_json_object(json_string,'$.event_id') as event_id \
                                                from impression_reformat where date='${date}' group by \
                                                        get_json_object(json_string,'$.uid'),get_json_object(json_string,'$.scenario.desc'),get_json_object(json_string,'$.product_id'),get_json_object(json_string,'$.upack.news_configid'),\
                                                        get_json_object(json_string,'$.article_impression_extra.content_type'),get_json_object(json_string,'$.protocol'),get_json_object(json_string,'$.language'),\
                                                        get_json_object(json_string,'$.app_ver'),get_json_object(json_string,'$.promotion'), get_json_object(json_string,'$.article_impression_extra.content_id'),\
                                                        get_json_object(json_string,'$.event_id') \
                                                ) t group by product_id,app_ver,language,promotion,news_configid,protocol,content_type,scenario,uid" > ${LOCAL_DATA_PATH}/user_ltv/${date}/user_impression_ltv.list
 	/usr/lib/hive/bin/hive -e "set mapred.job.queue.name=offline;select product_id,app_ver,language,promotion,news_configid,protocol,content_type,scenario,uid,count(*) as num from ( 
                                                select get_json_object(json_string,'$.uid') as uid,get_json_object(json_string,'$.scenario.desc') as scenario,get_json_object(json_string,'$.product_id') as product_id,\
                                                       get_json_object(json_string,'$.upack.news_configid') as news_configid,get_json_object(json_string,'$.article_impression_extra.content_type') as content_type,\
                                                       get_json_object(json_string,'$.protocol') as protocol,get_json_object(json_string,'$.language') as language,get_json_object(json_string,'$.app_ver') as app_ver,\
                                                       get_json_object(json_string,'$.promotion') as promotion, get_json_object(json_string,'$.article_impression_extra.content_id') as content_id,get_json_object(json_string,'$.event_id') as event_id \
                                                from request_reformat where date='${date}' group by \
                                                        get_json_object(json_string,'$.uid'),get_json_object(json_string,'$.scenario.desc'),get_json_object(json_string,'$.product_id'),get_json_object(json_string,'$.upack.news_configid'),\
                                                        get_json_object(json_string,'$.article_impression_extra.content_type'),get_json_object(json_string,'$.protocol'),get_json_object(json_string,'$.language'),\
                                                        get_json_object(json_string,'$.app_ver'),get_json_object(json_string,'$.promotion'), get_json_object(json_string,'$.article_impression_extra.content_id'),\
                                                        get_json_object(json_string,'$.event_id') \
                                                ) t \
                                        group by product_id,app_ver,language,promotion,news_configid,protocol,content_type,scenario,uid" > ${LOCAL_DATA_PATH}/user_ltv/${date}/user_request_ltv.list
 	/usr/lib/hive/bin/hive -e "set mapred.job.queue.name=offline;select product_id,app_ver,language,promotion,news_configid,protocol,content_type,scenario,uid,sum(dwelltime) as num from ( 
                                                select get_json_object(json_string,'$.uid') as uid,get_json_object(json_string,'$.scenario.desc') as scenario,get_json_object(json_string,'$.product_id') as product_id,\
                                                       get_json_object(json_string,'$.upack.news_configid') as news_configid,get_json_object(json_string,'$.article_dwelltime_extra.content_type') as content_type,\
                                                       get_json_object(json_string,'$.protocol') as protocol,get_json_object(json_string,'$.language') as language,get_json_object(json_string,'$.app_ver') as app_ver,\
                                                       get_json_object(json_string,'$.promotion') as promotion, get_json_object(json_string,'$.article_dwelltime_extra.content_id') as content_id,get_json_object(json_string,'$.event_id') as event_id, \
						       get_json_object(json_string,'$.article_dwelltime_extra.dwelltime') as dwelltime \
                                                from dwelltime_reformat where date='${date}' group by \
                                                        get_json_object(json_string,'$.uid'),get_json_object(json_string,'$.scenario.desc'),get_json_object(json_string,'$.product_id'),get_json_object(json_string,'$.upack.news_configid'),\
                                                        get_json_object(json_string,'$.article_dwelltime_extra.content_type'),get_json_object(json_string,'$.protocol'),get_json_object(json_string,'$.language'),\
                                                        get_json_object(json_string,'$.app_ver'),get_json_object(json_string,'$.promotion'), get_json_object(json_string,'$.article_dwelltime_extra.content_id'),\
                                                        get_json_object(json_string,'$.event_id'),get_json_object(json_string,'$.article_dwelltime_extra.dwelltime') \
                                                ) t \
                                        group by product_id,app_ver,language,promotion,news_configid,protocol,content_type,scenario,uid" > ${LOCAL_DATA_PATH}/user_ltv/${date}/user_dwelltime_ltv.list
}

function stat_merge()
{
	awk 'BEGIN{
                FS = "\t"
                OFS = "\t"
        }{
                ## from xiaozhi -1      inveno  all     chinese all     7
                ##  to product_id, news_configid, content_type, language, scenario
                key = $1"\t"$2"\t"$3"\t"$4"\t"$5"\t"$6"\t"$7"\t"$8"\t"$9
                all[key]

                if (ARGIND == 1)
                {
                        impression_all[key] += $10
                }
                else if (ARGIND == 2)
                {
                        impression_all[key] += $10
                }
                else if (ARGIND == 3)
                {
                        impression[key] += $10
                }
                else if (ARGIND == 4)
                {
                        click[key] += $10
                }
                else if (ARGIND == 5)
                {
                        request[key] += $10
                }
                else if (ARGIND == 6)
                {
                        dwelltime[key] += $10
                }
        }END{
		timestamp = sprintf("%s-%s-%s 00:00:00", 
                        substr("'${date}'", 1, 4),
                        substr("'${date}'", 5, 2),
                        substr("'${date}'", 7, 2))

                print "id", "timestamp", "product_id", "app_ver", "language", "promotion", "config_id", "protocol", "content_type", "scenario","uid",
                        "total_request", 
                        "total_click",  
                        "total_dwelltime", 
			"total_impression",
			"total_impression_all"
                
                for (key in all)
                {
                        print "0", timestamp, key,
				(request[key] + 0),
                                (click[key] + 0),
                                (dwelltime[key] + 0),
				(impression[key] + 0),
				(impression_all[key] + 0)
                }
        }' ${LOCAL_DATA_PATH}/user_ltv/${date}/user_impression_nopush_ltv.list ${LOCAL_DATA_PATH}/user_ltv/${date}/user_impression_pushclick_ltv.list ${LOCAL_DATA_PATH}/user_ltv/${date}/user_impression_ltv.list ${LOCAL_DATA_PATH}/user_ltv/${date}/user_click_ltv.list \
	   ${LOCAL_DATA_PATH}/user_ltv/${date}/user_request_ltv.list ${LOCAL_DATA_PATH}/user_ltv/${date}/user_dwelltime_ltv.list >${LOCAL_DATA_PATH}/user_ltv/${date}/user_ltv_result.list

	cat ${LOCAL_DATA_PATH}/user_ltv/${date}/user_ltv_result.list | python stat-user-ltv-mysql.py 1>../data/user_ltv/${date}/python_mysql.err 2>&1
}

prepare

exec_hive
stat_merge
