#!/usr/bin/env python
# coding=utf-8

'''
@author: huanghaifeng
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
    timestamp = time.strftime('%Y%m%d',time.localtime(time.time()))
    for line_str in sys.stdin.readlines():
        line_str = line_str.strip()
	try:
		article = {}
		line_json = json.loads(line_str)
		content_id = line_json['content_id']
		source = line_json['source']
		sourceType = line_json['sourceType']
		publisher = line_json['publisher']
                body_images_count = line_json['body_images_count']
                rate = line_json['rate']
                category = json.loads(line_json['categories'])['v8'].keys()[0]
	except:
		sys.stderr.write("invalid data, " + line_str + "\n")
		continue
	
        if content_id!='' and publisher!='' and source!='' and sourceType!='' and body_images_count!='' and rate!='':
		print content_id +'\t'+ publisher +'\t'+ source +'\t'+ sourceType +'\t'+ body_images_count +'\t'+ rate +'\t'+ str(category) +'\t'+ timestamp
        else:
		sys.stderr.write("invalid data, value is null")
