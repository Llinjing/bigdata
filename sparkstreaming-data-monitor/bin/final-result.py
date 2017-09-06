#!/usr/bin/env python
# coding=utf-8

'''
@author: panhongan
'''

import sys
import string
import time

try:
    import simplejson as json
except:
    import json

if __name__ == "__main__":
    monitor_time = time.strftime("%Y-%m-%d %H:%M",time.localtime(time.time()))

    for line_str in sys.stdin.readlines():
        line_str = line_str.strip()
	try:
                arr = line_str.split("\t")
                if len(arr) == 6:
                    dict = {}
                    dict["content_id"] = arr[0]
                    dict["uid"] = arr[1]
                    dict["log_time"] = arr[2]
                    dict["log_type"] = arr[3]
                    dict['app_ver'] = arr[4]
                    dict['product_id'] = arr[5]
                    dict["monitor_time"] = monitor_time

                    print json.dumps(dict, separators=(',', ':'))
	except:
		continue


