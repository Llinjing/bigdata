#!/usr/bin/env python
# coding=utf-8

'''
@author: panhongan
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
            content_id = line_json['article_impression_extra']['content_id']
            content_type = line_json['article_impression_extra']['content_type']
	    language = line_json['app_lan']
            scenario = line_json['scenario']['desc']

            if content_id!='' and product_id!='' and language!='' and scenario!='':
                if content_type=='news' or content_type=='short_video':
                	if scenario=='long_listpage' or scenario=='waterfall':
		                print content_id +"\t"+ product_id +"\t"+ language
        except:
            continue
        
