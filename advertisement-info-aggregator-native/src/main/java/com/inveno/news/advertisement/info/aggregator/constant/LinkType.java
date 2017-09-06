package com.inveno.news.advertisement.info.aggregator.constant;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by dory on 2016/10/12.
 */
public class LinkType {
    public static class LinkTypeValue{
        //0: all 1 embedded_browser 2 external_browser
        public static final LinkTypeValue UNKNOWN = new LinkTypeValue(-1);

        public static final LinkTypeValue ALL = new LinkTypeValue(0);

        public static final LinkTypeValue EMBEDDED_BROWSER = new LinkTypeValue(1);

        public static final LinkTypeValue EXTERNAL_BROWSER = new LinkTypeValue(2);

        private int value = -1;
        public LinkTypeValue(int value){
            this.value = value;
        }

        public int getValue(){
            return value;
        }
    }

    public static class LinkTypeDesc{
        public static final LinkTypeDesc UNKNOWN = new LinkTypeDesc("unknown");

        public static final LinkTypeDesc ALL = new LinkTypeDesc("all");

        public static final LinkTypeDesc EMBEDDED_BROWSER = new LinkTypeDesc("embedded_browser");

        public static final LinkTypeDesc EXTERNAL_BROWSER = new LinkTypeDesc("external_browser");

        private String value = null;
        public LinkTypeDesc(String value){
            this.value = value;
        }
        public String toString(){
            return value;
        }

    }
    public static Map<Integer, String> link_type_value_desc = new HashMap<Integer, String>();
    static {
        link_type_value_desc.put(LinkTypeValue.UNKNOWN.getValue(), LinkTypeDesc.UNKNOWN.toString());
        link_type_value_desc.put(LinkTypeValue.ALL.getValue(), LinkTypeDesc.ALL.toString());
        link_type_value_desc.put(LinkTypeValue.EMBEDDED_BROWSER.getValue(), LinkTypeDesc.EMBEDDED_BROWSER.toString());
        link_type_value_desc.put(LinkTypeValue.EXTERNAL_BROWSER.getValue(), LinkTypeDesc.EXTERNAL_BROWSER.toString());
    }

    public static String getLinkType(String link_type_str){
        int link_type = LinkTypeValue.UNKNOWN.getValue();
        try{
            link_type = Integer.valueOf(link_type_str);
        }catch (Exception e){}
        return getLinkType(link_type);
    }
    public static String getLinkType(int link_type){
        String ret = link_type_value_desc.get(link_type);
        if(ret == null)
        {
            ret = LinkTypeDesc.UNKNOWN.toString();
        }
        return ret;
    }

}
