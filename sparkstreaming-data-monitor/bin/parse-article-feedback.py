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
		timestamp = line_json['timestamp']
                content_id = line_json['content_id']
                product_id = line_json['product_id']
                config_id = line_json['config_id']
                click = line_json['click']
                impression = line_json['impression']
                if impression == 0:
                    impression = line_json['request']
                dwelltime = line_json['dwelltime']
                language = line_json['language']
                content_type = line_json['content_type']
		strategy = line_json['strategy']
		scenario_channel = line_json['scenario_channel']
                
                if content_type == "advertisement_third_party" or content_type == "advertisement_hard":
                    continue

		if strategy != "push" and strategy !="relative_recommendation" and strategy !="force_insert":
                    print timestamp + "\t" + content_id + "\t" + product_id + "\t" + config_id + "\t" + language + "\t" + str(click) + "\t" + str(impression) + "\t" + str(dwelltime) + "\t" + content_type + "\t" + scenario_channel
	except:
		continue
