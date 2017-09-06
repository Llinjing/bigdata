#!/usr/bin/env python
# coding=utf-8

'''
@author: gaofeilu
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
    #begin_time = sys.argv[1]
    #end_time = sys.argv[2]
    for line_str in sys.stdin.readlines():
        line_str = line_str.strip()
        try:
                line_json = json.loads(line_str)
                product_id = line_json['product_id']
                language = line_json['language']
                content_id = str(line_json['content_id'])
                bore_num = str(line_json['ballot']['Bored'])
                like_num = str(line_json['ballot']['Like'])
                angry_num = str(line_json['ballot']['Angry'])
                sad_num = str(line_json['ballot']['Sad'])
                uid = line_json['uid']
                #if event_time < begin_time or event_time > end_time:
                #        continue
        except:
                sys.stderr.write("invalid data, failed get json data: " + line_str + "\n")
                continue

        if product_id!='' and language!='' and content_id!='':
                print uid+"###"+product_id+'###'+language+'###'+content_id+'\t'+bore_num+'\t'+like_num+'\t'+angry_num+'\t'+sad_num
        else:
                sys.stderr.write("invalid data, key is null: " + line_str + "\n")
