package com.inveno.news.advertisement.info.aggregator.schema;

import com.alibaba.fastjson.JSON;

/**
 * Created by dory on 2016/10/15.
 */
public class Ad {
    private String ad_id = null;
    private String ad_source = null;
    private String advertiser_id = null;
    private String advertiser_name = null;
    private String industry = null;
    private String type = null;

    public String getAd_id() {
        return ad_id;
    }

    public void setAd_id(String ad_id) {
        this.ad_id = ad_id;
    }

    public String getAd_source() {
        return ad_source;
    }

    public void setAd_source(String ad_source) {
        this.ad_source = ad_source;
    }

    public String getAdvertiser_id() {
        return advertiser_id;
    }

    public void setAdvertiser_id(String advertiser_id) {
        this.advertiser_id = advertiser_id;
    }

    public String getAdvertiser_name() {
        return advertiser_name;
    }

    public void setAdvertiser_name(String advertiser_name) {
        this.advertiser_name = advertiser_name;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String toString(){
        return JSON.toJSONString(this);
    }
}
