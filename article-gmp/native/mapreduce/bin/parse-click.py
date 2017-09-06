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
            content_id = line_json['article_click_extra']['content_id']
            content_type= line_json['article_click_extra']['content_type']
	    uid = line_json['uid']
            #scenario = line_json['scenario']['desc']

            #if content_type=='news' and (scenario=='long_listpage' or scenario=='waterfall'):
            if content_type=='news':
                print content_id +'\t'+ product_id +'\t'+ uid
        except:
            continue
        
