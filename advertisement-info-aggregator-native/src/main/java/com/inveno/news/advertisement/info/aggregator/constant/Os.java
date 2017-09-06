package com.inveno.news.advertisement.info.aggregator.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dory on 2016/10/11.
 */
public class Os {

    public static class OsValue{
        public static final OsValue UNKNOWN = new OsValue(-1);

        public static final OsValue ALL = new OsValue(0);

        public static final OsValue ANDROID = new OsValue(1);

        public static final OsValue IOS = new OsValue(2);

        private int value = 0;
        public OsValue(int value){
            this.value = value;
        }
        public int getValue(){
            return value;
        }
    }

    public static class OsDesc{
        public static final OsDesc UNKNOWN = new OsDesc("unknown");

        public static final OsDesc ALL = new OsDesc("all");

        public static final OsDesc ANDROID = new OsDesc("android");

        public static final OsDesc IOS = new OsDesc("ios");


        private String value = null;
        public OsDesc(String value){
            this.value = value;
        }
        public String toString(){
            return value;
        }
    }

    public static Map<Integer, String> os_value_desc = new HashMap<Integer, String>();
    static {
        os_value_desc.put(OsValue.UNKNOWN.getValue(), OsDesc.UNKNOWN.toString());
        os_value_desc.put(OsValue.ALL.getValue(), OsDesc.ALL.toString());
        os_value_desc.put(OsValue.ANDROID.getValue(), OsDesc.ANDROID.toString());
        os_value_desc.put(OsValue.IOS.getValue(), OsDesc.IOS.toString());
    }

    public static String getOs(String os_str){
        int os = OsValue.UNKNOWN.getValue();
        try{
            os = Integer.valueOf(os_str);
        }catch (Exception e)
        {}
        return getOs(os);
    }
    public static String getOs(int os){
        String ret = os_value_desc.get(os);
        if(ret == null){
            ret = OsDesc.UNKNOWN.toString();
        }
        return ret;
    }
}
