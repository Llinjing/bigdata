package com.inveno.news.advertisement.info.aggregator.schema;

import com.alibaba.fastjson.JSON;

/**
 * Created by dory on 2016/10/11.
 */
public class Delivery {
    private String delivery_id = null;
    private Ad ad = null;
    private Marketing marketing = null;
    private String material_id = null;
    private OperationStrategy operation_strategy = null;

    public String getDelivery_id() {
        return delivery_id;
    }

    public void setDelivery_id(String delivery_id) {
        this.delivery_id = delivery_id;
    }

    public Ad getAd() {
        return ad;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
    }

    public Marketing getMarketing() {
        return marketing;
    }

    public void setMarketing(Marketing marketing) {
        this.marketing = marketing;
    }

    public String getMaterial_id() {
        return material_id;
    }

    public void setMaterial_id(String material_id) {
        this.material_id = material_id;
    }

    public OperationStrategy getOperation_strategy() {
        return operation_strategy;
    }

    public void setOperation_strategy(OperationStrategy operation_strategy) {
        this.operation_strategy = operation_strategy;
    }

    public String toString(){
        return JSON.toJSONString(this);
    }
}
