#!/usr/bin/env python
#-*-coding:utf8-*-

'''
@author: huanghaifeng
'''
import sys
import string

try:
    import simplejson as json
except:
    import json

if __name__ == "__main__":
    reload(sys)
    sys.setdefaultencoding('utf-8')
    for line_str in sys.stdin.readlines():
        try:
            line_str = line_str.strip()
            line_arr = line_str.split('\t')
            tmp_dict = {}
            tmp_dict['timestamp'] = line_arr[0]
            tmp_dict['product_id'] = line_arr[1]
            tmp_dict['article_count'] = line_arr[2]
            tmp_dict['gmp_sum'] = line_arr[3]
            tmp_dict['max_gmp'] = line_arr[4]
            tmp_dict['ge_8_percent_article_count'] = line_arr[5]
            tmp_dict['ge_4_percent_article_count'] = line_arr[6]
            tmp_dict['ge_2_percent_article_count'] = line_arr[7]
            tmp_dict['language'] = line_arr[8]
            tmp_dict['content_type'] = line_arr[9]
            tmp_dict['publisher'] = line_arr[10]
            tmp_dict['source'] = line_arr[11]
            tmp_dict['sourceType'] = line_arr[12]
            tmp_dict['body_images_count'] = line_arr[13]
            tmp_dict['rate'] = line_arr[14]
            tmp_dict['type'] = line_arr[15]
            tmp_dict['category'] = line_arr[16]
            print json.dumps(tmp_dict)
        except:
            #print line_str
            continue
