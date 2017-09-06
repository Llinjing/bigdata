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
    json_str = '{"size":10000,"filter":{"bool":{"should":[{"terms":{"content_id":['
    index = 0
    for line_str in sys.stdin.readlines():
    	line_str = line_str.strip()
    	arr_str = line_str.split("\t")
	if index != 0:
		json_str += ','
    	json_str += '"' + arr_str[0] + '"'
	index += 1
    json_str += ']}}]}}}'
    print json_str
