#!/usr/bin/env python
# coding=utf-8

'''
@author: zhangrujing
'''

import sys
import string
import time

try:
    import simplejson as json
except:
    import json
    
if __name__ == "__main__":
    reload(sys)
    sys.setdefaultencoding('utf-8')
    event_time = time.strftime("%Y%m%d%H%M%S", time.localtime())
    for line_str in sys.stdin.readlines():
    	line_str = line_str.strip()
    	arr_str = line_str.split("\t")
	print '{"update":{"_id":"' + arr_str[0] + '"}}' + "\n" + '{"doc":{"click_10min":' + arr_str[1] + ', "impression_10min":' + arr_str[2] + ', "dwelltime_10min":' + arr_str[3] + ', "ctr_10min":' + arr_str[4] + ', "click_sum":' + arr_str[5] + ', "impression_sum":' + arr_str[6] + ', "dwelltime_sum":' + arr_str[7] + ', "ctr_sum":' + arr_str[8] + ', "event_time":' + event_time + '}}'
