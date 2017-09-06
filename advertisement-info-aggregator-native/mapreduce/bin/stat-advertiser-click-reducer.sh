#!/bin/bash

function prepare()
{
    chmod +x *
    return 0
}

function stat()
{
    mkdir -p tmp

    awk 'BEGIN{
        FS = "\t"
        OFS = "\t"
    }{
        ## advertisement_id, count[advertisement_id]
        print $2 >"tmp/"$1
    }END{
    }'

    for file in `ls tmp`
    do
        awk 'BEGIN{
            FS = "\t"
            OFS = "\t"
            total = 0
        }{
            total += $1
        }END{
            prefix = "'${file}'"
            print prefix, total
        }' tmp/${file}
    done

    rm -rf tmp
    return 0
}

prepare
stat
