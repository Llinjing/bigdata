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
    for line_str in sys.stdin.readlines():
        line_str = line_str.strip()
	try:
		line_json = json.loads(line_str)
		product_id = line_json['product_id']
                content_id = line_json['content_id']
                config_id = line_json['config_id']
		content_type = line_json['content_type']
                click = line_json['click']
                impression = line_json['impression']
                if impression == 0:
                    impression = line_json['request']
                dwelltime = line_json['dwelltime']
                if content_type == "news" or content_type == "short_video":
                    print product_id + "\t" + content_id + "\t" + config_id + "\t" + content_type + "\t" + str(click) + "\t" + str(impression) + "\t" + str(dwelltime)
	except:
		continue

