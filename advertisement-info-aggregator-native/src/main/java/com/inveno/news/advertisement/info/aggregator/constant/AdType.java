package com.inveno.news.advertisement.info.aggregator.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dory on 2016/10/12.
 */
public class AdType {
    public static class AdTypeValue{
        public static final AdTypeValue UNKNOWN = new AdTypeValue(-1);

        public static final AdTypeValue LINK_PROMOTION = new AdTypeValue(1);

        public static final AdTypeValue APP_PROMOTION = new AdTypeValue(2);

        public static final AdTypeValue UNUSED = new AdTypeValue(3);

        public static final AdTypeValue TEL_PROMOTION = new AdTypeValue(4);

        public static final AdTypeValue VIDEO_PROMOTION = new AdTypeValue(5);

        public static final AdTypeValue SOFT_ARTICLE_PROMOTION = new AdTypeValue(6);

        private int value = -1;
        public AdTypeValue(int value){
            this.value = value;
        }
        public int getValue(){
            return value;
        }

    }

    public static class AdTypeDesc{
        // 1 link_promotion 2 app_promotion 3 unused 4 tel_promotion 5 video_promotion 6 soft_article_promotion
        public static final AdTypeDesc UNKNOWN = new AdTypeDesc("unknown");

        public static final AdTypeDesc LINK_PROMOTION = new AdTypeDesc("link_promotion");

        public static final AdTypeDesc APP_PROMOTION = new AdTypeDesc("app_promotion");

        public static final AdTypeDesc UNUSED = new AdTypeDesc("unused");

        public static final AdTypeDesc TEL_PROMOTION = new AdTypeDesc("tel_promotion");

        public static final AdTypeDesc VIDEO_PROMOTION = new AdTypeDesc("video_promotion");

        public static final AdTypeDesc SOFT_ARTICLE_PROMOTION = new AdTypeDesc("soft_article_promotion");

        private String value = null;
        public AdTypeDesc(String value){
            this.value = value;
        }
        public String toString(){
            return value;
        }
    }
    public static Map<Integer, String> adtype_value_desc = new HashMap<Integer, String>();

    static {
        adtype_value_desc.put(AdTypeValue.UNKNOWN.getValue(), AdTypeDesc.UNKNOWN.toString());
        adtype_value_desc.put(AdTypeValue.LINK_PROMOTION.getValue(), AdTypeDesc.LINK_PROMOTION.toString());
        adtype_value_desc.put(AdTypeValue.APP_PROMOTION.getValue(), AdTypeDesc.APP_PROMOTION.toString());
        adtype_value_desc.put(AdTypeValue.UNUSED.getValue(), AdTypeDesc.UNUSED.toString());
        adtype_value_desc.put(AdTypeValue.TEL_PROMOTION.getValue(), AdTypeDesc.TEL_PROMOTION.toString());
        adtype_value_desc.put(AdTypeValue.VIDEO_PROMOTION.getValue(), AdTypeDesc.VIDEO_PROMOTION.toString());
        adtype_value_desc.put(AdTypeValue.SOFT_ARTICLE_PROMOTION.getValue(), AdTypeDesc.SOFT_ARTICLE_PROMOTION.toString());
    }

    public static String getAdType(String adtype_str){
        int adtype = AdTypeValue.UNKNOWN.getValue();
        try{
            adtype = Integer.valueOf(adtype_str);
        }catch (Exception e){}
        return getAdType(adtype);
    }

    public static String getAdType(int adtype){
        String ret = adtype_value_desc.get(adtype);
        if(ret == null){
            ret = AdTypeDesc.UNKNOWN.toString();
        }
        return ret;
    }
}
