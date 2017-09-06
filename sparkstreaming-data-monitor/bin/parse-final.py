#!/usr/bin/env python
# coding=utf-8

'''
@author: panhongan
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
                content_id = line_json['article_impression_extra']['content_id']
                uid = line_json['uid']
                log_time = line_json['log_time']
                log_type = line_json['log_type']
                product_id = line_json['product_id']
                try:
                    app_ver = line_json['revised_app_ver']
                except:
                    app_ver = line_json['app_ver'] 
                print content_id + "\t" + uid + "\t" + log_time + "\t" + log_type + "\t" + app_ver + "\t" + product_id
	except:
		continue

