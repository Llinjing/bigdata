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
        line_str = line_str.strip()
        try:
            line_json = json.loads(line_str)
            for key in line_json:
                for product_id in key:
                    print product_id +','+ str(key[product_id]['ctr']) +'\t',
            print ''
        except:
            print line_str+'\t',
            continue
