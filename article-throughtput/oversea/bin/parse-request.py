#!/usr/bin/env python
# coding=utf-8

'''
@author: haifeng.huang
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
            content_id = line_json['article_request_extra']['content_id']
            content_type = line_json['article_request_extra']['content_type']
            language = line_json['app_lan']
            platform = line_json['platform']

            if content_type!='' and content_id!='' and language!='' and product_id!='' and platform!='' and ('advertisement' not in content_type): 
                print content_id +"\t"+ product_id +"\t"+ language +"\t"+ content_type +"\t"+ platform
                print content_id +"\t"+ product_id +"\t"+ language +"\t"+ content_type +"\t"+ 'all'
        except:
            continue
        
