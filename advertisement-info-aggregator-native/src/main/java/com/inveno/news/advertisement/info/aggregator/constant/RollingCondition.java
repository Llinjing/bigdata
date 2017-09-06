package com.inveno.news.advertisement.info.aggregator.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dory on 2016/10/11.
 */
public class RollingCondition {
    public static class RollingConditionValue {

        public static final RollingConditionValue UNROLLING = new RollingConditionValue(0);

        public static final RollingConditionValue ROLLING = new RollingConditionValue(1);

        private int value = 0;

        private RollingConditionValue(int value) {
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

            if (obj instanceof RollingConditionValue) {
                ret = (value == ((RollingConditionValue)obj).value);
            } else if (obj instanceof Integer) {
                ret = (value == (Integer)obj);
            }

            return ret;
        }
    }

    public static class RollingConditionValueDesc {

        public static final RollingConditionValueDesc UNROLLING = new RollingConditionValueDesc("unrolling");

        public static final RollingConditionValueDesc ROLLING = new RollingConditionValueDesc("rolling");

        private String value = null;

        private RollingConditionValueDesc(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public static Map<Integer, String> RollingCondition_value_desc = new HashMap<Integer, String>();

    static {
        RollingCondition_value_desc.put(RollingConditionValue.UNROLLING.getValue(), RollingConditionValueDesc.UNROLLING.toString());
        RollingCondition_value_desc.put(RollingConditionValue.ROLLING.getValue(), RollingConditionValueDesc.ROLLING.toString());
    }

    public static String getRollingCondition(String RollingCondition_str) {
        int RollingCondition = RollingConditionValue.UNROLLING.getValue();
        try {
            RollingCondition = Integer.valueOf(RollingCondition_str);
        } catch (Exception e){
        }
        return getRollingCondition(RollingCondition);
    }

    public static String getRollingCondition(int RollingCondition) {
        String ret = RollingCondition_value_desc.get(RollingCondition);
        if (ret == null) {
            ret = RollingConditionValueDesc.UNROLLING.toString();
        }
        return ret;
    }
}
