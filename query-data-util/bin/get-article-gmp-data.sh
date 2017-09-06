#!/bin/bash

function prepare()
{
    cd "$( dirname "${BASH_SOURCE[0]}" )"
    source ../conf/project.conf

    if [ -z "${JAVA_HOME}" ]
    then
        echo "JAVA_HOME is not set" >&2
        exit 1
    fi
}

function get_article_gmp()
{
    ${REDIS_BIN} -c -h ${REDIS_SERVER} -p ${REDIS_PORT} HGETALL ${REDIS_KEY}-0 > ${LOCAL_TMP_PATH}/article-gmp.list
    for ((i=1; i<${REDIS_KEY_PARTITIONS}; i++))
    do
        ${REDIS_BIN} -c -h ${REDIS_SERVER} -p ${REDIS_PORT} HGETALL ${REDIS_KEY}-${i} >> ${LOCAL_TMP_PATH}/article-gmp.list
    done

    cat ${LOCAL_TMP_PATH}/article-gmp.list | python ${LOCAL_BIN_PATH}/parse-article-gmp-json.py  > ${LOCAL_TMP_PATH}/article-gmp-parse.list

    cat ${LOCAL_TMP_PATH}/article-gmp-parse.list | awk 'BEGIN{
        FS="\t";
        OFS="\t"
        OFMT="%.8f"
    }{
        article_id = $1;
        for(i=2; i<=NF; i++)
        {
            if($i)
            {
                split($i, tmp_arr, ",")
                if(tmp_arr[1] > 0 && tmp_arr[1]=="noticias")
                {
                    print article_id, tmp_arr[1], tmp_arr[2]
                }
            }
        }
    }END{
    }' > ${LOCAL_DATA_PATH}/article-gmp.list
}


prepare
get_article_gmp
