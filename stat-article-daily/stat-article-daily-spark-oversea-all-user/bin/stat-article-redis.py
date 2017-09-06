#!/usr/bin/env python
#coding:utf-8

from rediscluster import StrictRedisCluster
import sys
import datetime

def redis_cluster():
    redis_nodes =  [{'host':'10.10.100.100','port':6300},
                    {'host':'10.10.100.101','port':6300},
                    {'host':'10.10.100.102','port':6300},
                    {'host':'10.10.100.103','port':6301},
                    {'host':'10.10.100.104','port':6301},
                    {'host':'10.10.100.105','port':6301}
                   ]
    try:
        redisconn = StrictRedisCluster(startup_nodes=redis_nodes)
    except Exception,e:
        print "Connect Error!"
        sys.exit(1)

    today = datetime.date.today()
    yesterday = today - datetime.timedelta(days=1)
    str_time = str(yesterday).replace("-","")
    redisconn.set(str_time+'_article_feedback_sm', '1')
    redisconn.expire(str_time+'_article_feedback_sm', 604800)
    print "write redis sucee"

redis_cluster()
