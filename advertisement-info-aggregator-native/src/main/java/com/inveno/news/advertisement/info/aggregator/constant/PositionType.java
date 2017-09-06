package com.inveno.news.advertisement.info.aggregator.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dory on 2016/10/11.
 */
public class PositionType {
    public static class PositionTypeValue {

        public static final PositionTypeValue BANNER = new PositionTypeValue(1);

        public static final PositionTypeValue SPLASH_SCREEN = new PositionTypeValue(2);

        public static final PositionTypeValue INTERSTITIAL_SCREEN = new PositionTypeValue(3);

        public static final PositionTypeValue FEEDS = new PositionTypeValue(4);

        public static final PositionTypeValue TEXT_LINK = new PositionTypeValue(5);

        private int value = 0;

        private PositionTypeValue(int value) {
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

            if (obj instanceof PositionTypeValue) {
                ret = (value == ((PositionTypeValue)obj).value);
            } else if (obj instanceof Integer) {
                ret = (value == (Integer)obj);
            }

            return ret;
        }
    }

    public static class PositionTypeValueDesc {

        public static final PositionTypeValueDesc BANNER = new PositionTypeValueDesc("banner");

        public static final PositionTypeValueDesc SPLASH_SCREEN = new PositionTypeValueDesc("splash_screen");

        public static final PositionTypeValueDesc INTERSTITIAL_SCREEN = new PositionTypeValueDesc("interstitial_screen");

        public static final PositionTypeValueDesc FEEDS = new PositionTypeValueDesc("feeds");

        public static final PositionTypeValueDesc TEXT_LINK = new PositionTypeValueDesc("text_link");

        private String value = null;

        private PositionTypeValueDesc(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public static Map<Integer, String> PositionType_value_desc = new HashMap<Integer, String>();

    static {
        PositionType_value_desc.put(PositionTypeValue.BANNER.getValue(), PositionTypeValueDesc.BANNER.toString());
        PositionType_value_desc.put(PositionTypeValue.SPLASH_SCREEN.getValue(), PositionTypeValueDesc.SPLASH_SCREEN.toString());
        PositionType_value_desc.put(PositionTypeValue.INTERSTITIAL_SCREEN.getValue(), PositionTypeValueDesc.INTERSTITIAL_SCREEN.toString());
        PositionType_value_desc.put(PositionTypeValue.FEEDS.getValue(), PositionTypeValueDesc.FEEDS.toString());
        PositionType_value_desc.put(PositionTypeValue.TEXT_LINK.getValue(), PositionTypeValueDesc.TEXT_LINK.toString());
    }

    public static String getPositionType(String PositionType_str) {
        int PositionType = PositionTypeValue.BANNER.getValue();
        try {
            PositionType = Integer.valueOf(PositionType_str);
        } catch (Exception e){
        }
        return getPositionType(PositionType);
    }

    public static String getPositionType(int PositionType) {
        String ret = PositionType_value_desc.get(PositionType);
        if (ret == null) {
            ret = PositionTypeValueDesc.BANNER.toString();
        }
        return ret;
    }
}
