package com.inveno.news.advertisement.info.aggregator.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dory on 2016/10/12.
 */
public class TargetSystem {
    public static class TargetSystemValue{
        public static final TargetSystemValue UNKNOWN = new TargetSystemValue(-1);

        public static final TargetSystemValue PMS = new TargetSystemValue(0);

        public static final TargetSystemValue MALACCA = new TargetSystemValue(1);

        private int value = 0;
        public TargetSystemValue(int value){
            this.value = value;
        }
        public int getValue(){
            return value;
        }

    }

    public static class TargetSystemDesc{

        public static final TargetSystemDesc UNKNOWN = new TargetSystemDesc("unknown");

        public static final TargetSystemDesc PMS = new TargetSystemDesc("pms");

        public static final TargetSystemDesc MALACCA = new TargetSystemDesc("malacca");

        private String value = null;
        public TargetSystemDesc(String value){
            this.value = value;
        }
        public String toString(){
            return value;
        }

    }
    public static Map<Integer, String> targetsystem_value_desc = new HashMap<Integer, String>();

    static {
        targetsystem_value_desc.put(TargetSystemValue.UNKNOWN.getValue(), TargetSystemDesc.UNKNOWN.toString());
        targetsystem_value_desc.put(TargetSystemValue.PMS.getValue(), TargetSystemDesc.PMS.toString());
        targetsystem_value_desc.put(TargetSystemValue.MALACCA.getValue(), TargetSystemDesc.MALACCA.toString());
    }

    public static String getTargetSystem(String targetsystem_str){
        int targetsystem = TargetSystemValue.UNKNOWN.getValue();
        try{
            targetsystem = Integer.valueOf(targetsystem_str);
        }catch (Exception e){

        }
        return getTargetSystem(targetsystem);
    }
    public static String getTargetSystem(int targetsystem){
        String ret = targetsystem_value_desc.get(targetsystem);
        if(ret == null){
            ret = TargetSystemDesc.UNKNOWN.toString();
        }
        return  ret;
    }
}
