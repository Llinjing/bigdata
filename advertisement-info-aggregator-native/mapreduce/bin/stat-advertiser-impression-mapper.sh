#!/bin/bash

function prepare()
{
    chmod +x *
    return 0
}

function output()
{
    python ./stat-advertiser-impression-parse.py 2>/dev/null | awk 'BEGIN{
        FS = "\t"
        OFS = "\t"
    }{
        dedup[$1]++
    }END{
        for (key in dedup)
        {
            print key, dedup[key]
        }
    }'
    return 0
}

prepare
output
