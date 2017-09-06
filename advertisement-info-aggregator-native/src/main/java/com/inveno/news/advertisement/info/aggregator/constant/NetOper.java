package com.inveno.news.advertisement.info.aggregator.constant;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by dory on 2016/10/11.
 */
public class NetOper {
    public static class NetOperValue{
        public static final NetOperValue UNKNOWN = new NetOperValue(-1);

        public static final NetOperValue ALL = new NetOperValue(0);

        public static final NetOperValue CMCC = new NetOperValue(1);
        
        public static final NetOperValue CUCC = new NetOperValue(2);
        
        public static final NetOperValue CTCC = new NetOperValue(3);
        private int value = -1;
        public NetOperValue(int value){
            this.value = value;
        }
        public int getValue(){
            return value;
        }

    }

    public static class NetOperDesc{

        public static final NetOperDesc UNKNOWN = new NetOperDesc("unknown");

        public static final NetOperDesc ALL = new NetOperDesc("all");

        public static final NetOperDesc CMCC = new NetOperDesc("cmcc");

        public static final NetOperDesc CUCC = new NetOperDesc("cucc");

        public static final NetOperDesc CTCC = new NetOperDesc("ctcc");

        String value = null;
        public NetOperDesc(String value){
            this.value = value;
        }
        public String toString(){
            return value;
        }
    }
    public static Map<Integer, String> netoper_value_desc = new HashMap<Integer, String>();
    static {
        netoper_value_desc.put(NetOperValue.UNKNOWN.getValue(), NetOperDesc.UNKNOWN.toString());
        netoper_value_desc.put(NetOperValue.ALL.getValue(), NetOperDesc.ALL.toString());
        netoper_value_desc.put(NetOperValue.CMCC.getValue(), NetOperDesc.CMCC.toString());
        netoper_value_desc.put(NetOperValue.CUCC.getValue(), NetOperDesc.CUCC.toString());
        netoper_value_desc.put(NetOperValue.CTCC.getValue(), NetOperDesc.CUCC.toString());
    }

    public static String getNetOper(String netoper_str){
        int netoper = NetOperValue.UNKNOWN.getValue();
        try{
          netoper = Integer.valueOf(netoper_str);
        }catch (Exception e)
        {}
        return getNetOper(netoper);
    }
    public static String getNetOper(int netoper){
        String ret = netoper_value_desc.get(netoper);
        if(ret == null){
            ret = NetOperDesc.UNKNOWN.toString();
        }
        return ret;
    }
}
