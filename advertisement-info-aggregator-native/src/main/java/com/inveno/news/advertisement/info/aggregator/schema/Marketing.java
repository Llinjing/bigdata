package com.inveno.news.advertisement.info.aggregator.schema;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Created by dory on 2016/10/15.
 */
public class Marketing {
    private String marketing_id = null;
    private String daily_count_limit = null;
    private String pay_model = null;
    private String position_type = null;
    private String target_size = null;
    private String unit_price = null;

    public String getMarketing_id() {
        return marketing_id;
    }

    public void setMarketing_id(String marketing_id) {
        this.marketing_id = marketing_id;
    }

    public String getDaily_count_limit() {
        return daily_count_limit;
    }

    public void setDaily_count_limit(String daily_count_limit) {
        this.daily_count_limit = daily_count_limit;
    }

    public String getPay_model() {
        return pay_model;
    }

    public void setPay_model(String pay_model) {
        this.pay_model = pay_model;
    }

    public String getPosition_type() {
        return position_type;
    }

    public void setPosition_type(String position_type) {
        this.position_type = position_type;
    }

    public String getTarget_size() {
        return target_size;
    }

    public void setTarget_size(String target_size) {
        this.target_size = target_size;
    }

    public String getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(String unit_price) {
        this.unit_price = unit_price;
    }

    public String toString(){
        return JSON.toJSONString(this);
    }
}
