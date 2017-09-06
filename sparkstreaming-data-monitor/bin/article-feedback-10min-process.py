#!/usr/bin/env python
# coding=utf-8

'''
@author: zhangrujing
'''

import sys
import string

try:
    import simplejson as json
except:
    import json

if __name__ == "__main__":
    reload(sys)
    sys.setdefaultencoding('utf-8')

    string_str = "";	
    for line_str in sys.stdin.readlines():
    	line_str = line_str.strip()
	string_str+=line_str

    #print string_str
    
    line_json=json.loads(string_str)
    arr_json=line_json['hits']['hits']

    for arr in arr_json:
	arr_str = arr['_source']
	content_id = arr_str['content_id']
	click = "0"
	if 'click_sum' in arr_str:
		click = str(arr_str['click_sum'])
	impression = "0"
	if 'impression_sum' in arr_str:
		impression = str(arr_str['impression_sum'])
	dwelltime = "0"
	if 'dwelltime_sum' in arr_str:
		dwelltime = str(arr_str['dwelltime_sum'])
	print content_id + "\t" + click + "\t" + impression + "\t" + dwelltime 
