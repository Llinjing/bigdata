package com.inveno.news.advertisement.info.aggregator.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dory on 2016/10/11.
 */
public class Device {
    public static class DeviceValue {
        public static final DeviceValue UNKNOWN = new DeviceValue(-1);

        public static final DeviceValue ALL = new DeviceValue(0);

        public static final DeviceValue MOBILE_DEVICE = new DeviceValue(1);

        public static final DeviceValue PC_DEVICE = new DeviceValue(2);

        public static final DeviceValue TV_DEVICE = new DeviceValue(3);


        private int value = -1;

        public DeviceValue(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }

    public static class DeviceDesc {

        public static final DeviceDesc UNKNOWN = new DeviceDesc("unknown");

        public static final DeviceDesc ALL = new DeviceDesc("all");

        public static final DeviceDesc MOBILE_DEVICE = new DeviceDesc("mobile_device");

        public static final DeviceDesc PC_DEVICE = new DeviceDesc("pc_device");

        public static final DeviceDesc TV_DEVICE = new DeviceDesc("tv_device");

        private String value = null;

        public DeviceDesc(String value) {
            this.value = value;
        }

        public String toString() {
            return value;
        }
    }

    public static Map<Integer, String> device_value_desc = new HashMap<Integer, String>();

    static {
        device_value_desc.put(DeviceValue.UNKNOWN.getValue(), DeviceDesc.UNKNOWN.toString());
        device_value_desc.put(DeviceValue.ALL.getValue(), DeviceDesc.ALL.toString());
        device_value_desc.put(DeviceValue.MOBILE_DEVICE.getValue(), DeviceDesc.MOBILE_DEVICE.toString());
        device_value_desc.put(DeviceValue.PC_DEVICE.getValue(), DeviceDesc.PC_DEVICE.toString());
        device_value_desc.put(DeviceValue.TV_DEVICE.getValue(), DeviceDesc.TV_DEVICE.toString());
    }

    public static String getDevice(String device_str){
        int device = DeviceValue.UNKNOWN.getValue();
        try{
            device = Integer.valueOf(device_str);
        }catch (Exception e){

        }
        return getDevice(device);
    }

    public static String getDevice(int device){
        String ret = device_value_desc.get(device);
        if(ret == null){
            ret = DeviceDesc.UNKNOWN.toString();
        }
        return ret;
    }
}
