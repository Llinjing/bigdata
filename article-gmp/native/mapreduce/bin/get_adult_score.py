#!/usr/bin/env python
# coding=utf-8

'''
@author: huanghaifeng
'''

import sys
import string
import urllib2

reload(sys)
sys.setdefaultencoding('utf-8')

try:
    import simplejson as json
except:
    import json

if __name__ == "__main__":
    try:
        content_id = sys.argv[1]
        req = urllib2.Request('http://192.168.1.239:9200/tchannel/tchannel/_search?q=content_id:%s&_source=adult_score_v1'%(content_id))
        response = urllib2.urlopen(req)
        the_page = response.read()
        #print the_page
        line_json = json.loads(the_page)
        print (str)(line_json['hits']['hits'][0]['_source']['adult_score_v1'])
    except:
        print '-1'

def get_adult_score():
    try:
        content_id = sys.argv[1]
        req = urllib2.Request('http://192.168.1.239:9200/tchannel/tchannel/_search?q=content_id:%s&_source=adult_score_v1'%(content_id))
        response = urllib2.urlopen(req)
        the_page = response.read()
        #print the_page
        line_json = json.loads(the_page)
        return (str)(line_json['hits']['hits'][0]['_source']['adult_score_v1'])
    except:
        return '-1'



##"ttp://192.168.1.239:9200/tchannel/tchannel/_search?q=content_id:34363459&_source=adult_score_v1"
##"http://121.201.57.43:9200/tchannel/tchannel/_search?q=content_id:34363459&_source=adult_score_v1"



