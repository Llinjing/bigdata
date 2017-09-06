package com.inveno.news.common;

import java.util.ArrayList;
import java.util.List;

import static com.inveno.news.common.Constant.*;

/**
 * Created by dory on 2016/11/25.
 */
public class CombineUtil {
    public static void combine(String[] arr, String[] help_arr, int count,
                               int len, List<String> list) {
        if (count >= len) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < len - 1; ++i) {
                sb.append(help_arr[i]).append(KEY_TAG());
            }
            sb.append(help_arr[len - 1]);
            list.add(sb.toString());
        } else {
            help_arr[count] = arr[count];
            combine(arr, help_arr, count + 1, len, list);

            if (count == 0 ) {
                // do nothing
            } else {
                help_arr[count] = "all";
                combine(arr, help_arr, count + 1, len, list);
            }
        }
    }

    /*public static void getDimention(String[] arr, String str_replac, int len,List<String> list){
        int num = (int)Math.pow(2,len);
        for(int i = 0; i < num; )
        {
            StringBuffer sb = new StringBuffer();
            for(int j = 0; j < len -1; j++)
            {
                sb.append(arr[j]);
            }
        }
    }*/

    /*public static void main(String[] args) {
        String[] arr={"coolpad", "abc", "11", "11", "news", "11","33"};
        List<String> list = new ArrayList<String>();
        String[] help_arr = new String[arr.length];
        int icount=0;
        int ilen = arr.length - 1;
        combine(arr, help_arr, icount, ilen, list);
        int icnt=0;
        for(String str: list){
            System.out.println(str);
            icnt++;
        }
        System.out.println("icnt is " + icnt);

        //System.out.println((int)Math.pow(2,5));
    }*/
}