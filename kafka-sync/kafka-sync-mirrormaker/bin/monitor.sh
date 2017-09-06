#!/bin/bash

function monitor()
{
    source ../conf/project.conf

    for((i=0;i<${TOPIC_NUM};++i))
    do
        local pid=""

        read -r pid <${PID_FILE[$i]}
        if [ -n "$pid" ]
        then
            local exist_pid=`ps -ef | grep "$pid" | grep -v grep | awk '{
                if ($2 == "'${pid}'")
                {
                    print $2
                }
            }'`

            if [ -z "$exist_pid" ]
            then
                :
                ## warning
            fi
        fi
    done

    return 0
}

monitor
