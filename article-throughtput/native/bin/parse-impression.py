#!/usr/bin/env python
# coding=utf-8

'''
@author: huanghaifeng
'''

import sys
import string

reload(sys)
sys.setdefaultencoding('utf-8')

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
            content_type = line_json['content_type']
            language = line_json['language']
	    request = line_json['request']
	    impression = line_json['impression']

            if content_type == "news" and content_id!='': 
		if product_id=="ali":
		    if impression>0:
		        print content_id +"\t"+ product_id +"\t"+ language +"\t"+ (str)(impression)
		elif request>0:
		    print content_id +"\t"+ product_id +"\t"+ language +"\t"+ (str)(request)
        except:
            continue
        
