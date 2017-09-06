package com.inveno.news.advertisement.info.aggregator;

import com.inveno.news.advertisement.info.aggregator.constant.Device;
import com.inveno.news.advertisement.info.aggregator.constant.Network;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dory on 2016/10/11.
 */
public class test_schema {
    /*public static void set_material() {
        MaterialSchema mater_schema = new MaterialSchema();
        mater_schema.setTitle("test");
        mater_schema.setDesc("just test");
        //mater_schema.setMain_img("aa");
        mater_schema.setIcon("bb");
        mater_schema.setCreate_time("2016-07-08");
        mater_schema.setUpdate_time("2016-07-09");
        System.out.println(mater_schema.toString());
    }*/
    public static void main(String[] args)
    {
       // set_material();
        System.out.println(Network.getNetwork(2));
        System.out.println(Device.getDevice(2));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        System.out.println(df.format(new Date()));
        System.out.print(df.format(System.currentTimeMillis() - 1000*60*10) );

        String str_test= ",";
        String test = str_test.substring(0,str_test.lastIndexOf(","));
        System.out.println("the test is " + test);


    }

}
