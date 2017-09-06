package com.inveno.news.advertisement.info.aggregator.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dory on 2016/10/11.
 */
public class Gender {
    public static class GenderValue{
        public static final GenderValue UNKNOWN = new GenderValue(-1);

        public static final GenderValue ALL = new GenderValue(0);

        public static final GenderValue MALE = new GenderValue(1);

        public static final GenderValue FEMAEL = new GenderValue(2);

        private int value = 0;
        public GenderValue(int value){
            this.value = value;
        }

        public int getValue(){
            return value;
        }

    }

    public static class GenderDesc{

        public static final GenderDesc UNKOWN = new GenderDesc("unknown");

        public static final GenderDesc ALL = new GenderDesc("all");

        public static final GenderDesc MALE = new GenderDesc("male");

        public static final GenderDesc FEMALE = new GenderDesc("female");

        String value = null;
        public GenderDesc(String value){
            this.value = value;
        }
        public String toString(){
            return value;
        }
    }

    public static Map<Integer,String> gender_value_desc = new HashMap<Integer,String>();
    static {
        gender_value_desc.put(GenderValue.UNKNOWN.getValue(),GenderDesc.UNKOWN.toString());
        gender_value_desc.put(GenderValue.ALL.getValue(), GenderDesc.ALL.toString());
        gender_value_desc.put(GenderValue.MALE.getValue(), GenderDesc.MALE.toString());
        gender_value_desc.put(GenderValue.FEMAEL.getValue(), GenderDesc.FEMALE.toString());
    }

    public static String getGender(String gender_str){
        int gender = GenderValue.UNKNOWN.getValue();
        try{
            gender = Integer.valueOf(gender_str);
        }catch (Exception e)
        {

        }
        return getGender(gender);
    }

    public static String getGender(int gender){
        String ret = gender_value_desc.get(gender);
        if(ret == null){
            ret = GenderDesc.UNKOWN.toString();
        }
        return ret;
    }


}
