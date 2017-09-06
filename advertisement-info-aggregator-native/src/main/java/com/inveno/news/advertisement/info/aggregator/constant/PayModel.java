package com.inveno.news.advertisement.info.aggregator.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dory on 2016/10/11.
 */
public class PayModel {
    public static class PayModelValue {

        public static final PayModelValue CPC = new PayModelValue(1);

        public static final PayModelValue CPM = new PayModelValue(2);

        private int value = 0;

        private PayModelValue(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        @Override
        public boolean equals(Object obj) {
            boolean ret = false;

            if (obj instanceof PayModelValue) {
                ret = (value == ((PayModelValue)obj).value);
            } else if (obj instanceof Integer) {
                ret = (value == (Integer)obj);
            }

            return ret;
        }
    }

    public static class PayModelValueDesc {

        public static final PayModelValueDesc CPC = new PayModelValueDesc("cpc");

        public static final PayModelValueDesc CPM = new PayModelValueDesc("cpm");

        private String value = null;

        private PayModelValueDesc(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public static Map<Integer, String> PayModel_value_desc = new HashMap<Integer, String>();

    static {
        PayModel_value_desc.put(PayModelValue.CPC.getValue(), PayModelValueDesc.CPC.toString());
        PayModel_value_desc.put(PayModelValue.CPM.getValue(), PayModelValueDesc.CPM.toString());
    }

    public static String getPayModel(String PayModel_str) {
        int PayModel = PayModelValue.CPC.getValue();
        try {
            PayModel = Integer.valueOf(PayModel_str);
        } catch (Exception e){
        }
        return getPayModel(PayModel);
    }

    public static String getPayModel(int PayModel) {
        String ret = PayModel_value_desc.get(PayModel);
        if (ret == null) {
            ret = PayModelValueDesc.CPC.toString();
        }
        return ret;
    }
}
