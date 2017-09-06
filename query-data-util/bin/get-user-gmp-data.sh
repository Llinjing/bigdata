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

function get_user_gmp()
{
    hadoop fs -cat ${CLUSTER_USER_GMP_PATH} > ${LOCAL_DATA_PATH}/user-gmp.list
}

prepare
get_user_gmp

