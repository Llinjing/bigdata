package com.inveno.news.advertisement.info.aggregator.schema;

import com.alibaba.fastjson.JSON;

/**
 * Created by dory on 2016/10/15.
 */
public class OperationStrategy {
    private  String operation_strategy_id = null;
    private String sort_weight = null;

    public String getOperation_strategy_id() {
        return operation_strategy_id;
    }

    public void setOperation_strategy_id(String operation_strategy_id) {
        this.operation_strategy_id = operation_strategy_id;
    }

    public String getSort_weight() {
        return sort_weight;
    }

    public void setSort_weight(String sort_weight) {
        this.sort_weight = sort_weight;
    }

    public String toString(){
        return JSON.toJSONString(this);
    }
}
