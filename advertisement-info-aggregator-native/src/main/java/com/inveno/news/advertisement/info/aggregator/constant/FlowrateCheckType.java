package com.inveno.news.advertisement.info.aggregator.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dory on 2016/10/11.
 */
public class FlowrateCheckType {
    public static class FlowrateCheckTypeValue {

        public static final FlowrateCheckTypeValue ALL = new FlowrateCheckTypeValue(0);

        public static final FlowrateCheckTypeValue SELECTIVE_DELIVERY = new FlowrateCheckTypeValue(1);

        public static final FlowrateCheckTypeValue SELECTIVE_EXCLUSION = new FlowrateCheckTypeValue(2);

        private int value = 0;

        private FlowrateCheckTypeValue(int value) {
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

            if (obj instanceof FlowrateCheckTypeValue) {
                ret = (value == ((FlowrateCheckTypeValue)obj).value);
            } else if (obj instanceof Integer) {
                ret = (value == (Integer)obj);
            }

            return ret;
        }
    }

    public static class FlowrateCheckTypeValueDesc {

        public static final FlowrateCheckTypeValueDesc ALL = new FlowrateCheckTypeValueDesc("all");

        public static final FlowrateCheckTypeValueDesc SELECTIVE_DELIVERY = new FlowrateCheckTypeValueDesc("selective_delivery");

        public static final FlowrateCheckTypeValueDesc SELECTIVE_EXCLUSION = new FlowrateCheckTypeValueDesc("selective_exclusion");

        private String value = null;

        private FlowrateCheckTypeValueDesc(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public static Map<Integer, String> FlowrateCheckType_value_desc = new HashMap<Integer, String>();

    static {
        FlowrateCheckType_value_desc.put(FlowrateCheckTypeValue.ALL.getValue(), FlowrateCheckTypeValueDesc.ALL.toString());
        FlowrateCheckType_value_desc.put(FlowrateCheckTypeValue.SELECTIVE_DELIVERY.getValue(), FlowrateCheckTypeValueDesc.SELECTIVE_DELIVERY.toString());
        FlowrateCheckType_value_desc.put(FlowrateCheckTypeValue.SELECTIVE_EXCLUSION.getValue(), FlowrateCheckTypeValueDesc.SELECTIVE_EXCLUSION.toString());
    }

    public static String getFlowrateCheckType(String FlowrateCheckType_str) {
        int FlowrateCheckType = FlowrateCheckTypeValue.ALL.getValue();
        try {
            FlowrateCheckType = Integer.valueOf(FlowrateCheckType_str);
        } catch (Exception e){
        }
        return getFlowrateCheckType(FlowrateCheckType);
    }

    public static String getFlowrateCheckType(int FlowrateCheckType) {
        String ret = FlowrateCheckType_value_desc.get(FlowrateCheckType);
        if (ret == null) {
            ret = FlowrateCheckTypeValueDesc.ALL.toString();
        }
        return ret;
    }
}
