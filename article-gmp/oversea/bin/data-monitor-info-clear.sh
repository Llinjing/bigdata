#!/bin/sh

# author: huanghaifeng

function prepare()
{
	source ../conf/project.conf
}

function clear_article_info_info()
{
    awk 'BEGIN{
        FS = "\t"
        OFS = "\t"
    }{
        if($NF > '${VALID_ARTICLE_INFO_DATE}')
        {
            print $0
        }
    }END{
    }' ${LOCAL_DATA_PATH}/monitor-article-info.list > ${LOCAL_DATA_PATH}/monitor-article-info.list.tmp
    
    if [ -s ${LOCAL_DATA_PATH}/monitor-article-info.list.tmp ] ; then
        mv ${LOCAL_DATA_PATH}/monitor-article-info.list.tmp ${LOCAL_DATA_PATH}/monitor-article-info.list
    fi
}

function clear_article_info_signal_info()
{
    awk 'BEGIN{
        FS = "\t"
        OFS = "\t"
    }{
        if($NF > '${VALID_ARTICLE_INFO_DATE}')
        {
            print $0
        }
    }END{
    }' ${LOCAL_DATA_PATH}/monitor-article-info-signal.list > ${LOCAL_DATA_PATH}/monitor-article-info-signal.list.tmp

    if [ -s ${LOCAL_DATA_PATH}/monitor-article-info-signal.list.tmp ] ; then
        mv ${LOCAL_DATA_PATH}/monitor-article-info-signal.list.tmp ${LOCAL_DATA_PATH}/monitor-article-info-signal.list
    fi
}

prepare
clear_article_info_info
clear_article_info_signal_info
