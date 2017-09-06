package com.inveno.news.common;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.inveno.news.common.Constant.*;

/**
 * Created by admin on 2017/3/23.
 */
public class CombineUtil {
   /* public static void combine(String[] arr, String[] help_arr, int count,
                               int len, ArrayList<Map<String, Long>> list, Long num) {
        Map<String, Long> tmp_map = new HashMap<>();
        if (count >= len) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < len - 1; ++i) {
                sb.append(help_arr[i]).append(KEY_TAG());
            }
            sb.append(help_arr[len - 1]);
            tmp_map.put(sb.toString(), num);
            list.add(tmp_map);
        } else {
            help_arr[count] = arr[count];
            combine(arr, help_arr, count + 1, len, list, num);

            if (count == 1 ) {
                // do nothing
            } else {
                help_arr[count] = "all";
                combine(arr, help_arr, count + 1, len, list, num);
            }
        }
    }*/

    public static void combine(String[] arr, String[] help_arr, int count,
                               int len, List<String> list) {
        Map<String, Long> tmp_map = new HashMap<>();
        if (count >= len) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < len - 1 ; ++i) {
                sb.append(help_arr[i]).append(KEY_TAG());
            }
            sb.append(help_arr[len - 1]);
            list.add(sb.toString());
        } else {
            help_arr[count] = arr[count];
            combine(arr, help_arr, count + 1, len, list);

            if (count == 1 ) {
                // do nothing
            } else {
                help_arr[count] = "all";
                combine(arr, help_arr, count + 1, len, list);
            }
        }
    }


}
