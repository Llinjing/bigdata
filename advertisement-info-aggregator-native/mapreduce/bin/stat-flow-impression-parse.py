#!/usr/bin/env python
# coding=utf-8

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
        line_json = ''
        try:
            line_json = json.loads(line_str)
        except:
            sys.stderr.write("invalid data, failed get json data: " + line_str + "\n")
            continue
                       
        try:
            #scenario = line_json['scenario']
            scenario = line_json['ad_impression_extra']['delivery']['ad_scenario']['channel_desc']
        except:
            scenario = 'unknown' 

        try:
            ad_source = line_json['ad_impression_extra']['delivery']['ad']['ad_source']
        except:
            ad_source = 'unknown' 

        try:
            #channel_id = line_json['scenario']['channel_id']
            channel_id = line_json['ad_impression_extra']['delivery']['ad_scenario']['channel_id']
        except:
            channel_id = 'unknown'

        try:
            product_id = line_json['product_id']
        except:
            product_id = 'unknown'

        try:
            app_ver = line_json['app_ver']
        except:
            app_ver = 'unknown'

        try:
            position_id = line_json['ad_impression_extra']['delivery']['marketing']['position_id']
        except:
            position_id = 'unknown'

        try:
            position_type = line_json['ad_impression_extra']['delivery']['marketing']['position_type']
        except:
            position_type = 'unknown'

        try:
            target_size = line_json['ad_impression_extra']['delivery']['marketing']['target_size']
        except:
            target_size = 'unknown'

        try:
            industry = line_json['ad_impression_extra']['delivery']['ad']['industry']
        except:
            industry = 'unknown'

        try:
            advertisement_type = line_json['ad_impression_extra']['delivery']['ad']['type']
        except:
            advertisement_type = 'unknown'

        try:
            news_configid = line_json['upack']['news_configid']
        except:
            news_configid = 'unknown'

        try:
            biz_configid = line_json['upack']['biz_configid']
        except:
            biz_configid = 'unknown'

        try:
            ad_configid = line_json['upack']['ad_configid']
        except:
            ad_configid = 'unknown'

        try:
            advertisement_id = line_json['ad_impression_extra']['delivery']['delivery_id']
        except:
            advertisement_id = 'unknown'

        try:
            unit_price = line_json['ad_impression_extra']['delivery']['marketing']['unit_price']
        except:
            unit_price = '0'

        try:
            pay_model = line_json['ad_impression_extra']['delivery']['marketing']['pay_model']
        except:
            pay_model = 'unknown'

        print advertisement_id+'###'+scenario+'###'+ad_source+'###'+channel_id+'###'+product_id+'###'+app_ver+'###'+position_id+'###'+position_type+'###'+target_size+'###'+industry+'###'+advertisement_type+'###'+news_configid+'###'+biz_configid+'###'+ad_configid+'###'+unit_price+'###'+pay_model
