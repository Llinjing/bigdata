package com.inveno.news.common;

import java.util.List;

import static com.inveno.news.common.Constant.*;

/**
 * Created by gaofeilu on 2017/3/8.
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

            if (count == 0 || count == 1 ) {
                // do nothing
            } else {
                help_arr[count] = "all";
                combine(arr, help_arr, count + 1, len, list);
            }
        }
    }
}
