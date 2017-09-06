package com.inveno.news.common;

import java.util.List;

public class CombineUtil {

	public static void combine(String[] arr, String[] help_arr, int count,
			int len, List<String> list) {	
		if (count >= len) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < len - 1; ++i) {
				sb.append(help_arr[i]).append(Constant.KEY_TAG());
			}
			sb.append(help_arr[len - 1]);
			list.add(sb.toString());
		} else {
			help_arr[count] = arr[count];
			combine(arr, help_arr, count + 1, len, list);

			if (count == 0 || count == 1 || count == 6) {
				//platform product_id protocal
			} else {
				if (count == 7 && arr[count].startsWith("advertisement")) {
					help_arr[count] = "advertisement_all";
					combine(arr, help_arr, count + 1, len, list);
				}

				help_arr[count] = "all";
				combine(arr, help_arr, count + 1, len, list);
			}
		}
	}
	
	public static void combineImpression(String[] arr, String[] help_arr, int count,
			int len, List<String> list) {
		if (count >= len) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < len - 1; ++i) {
				sb.append(help_arr[i]).append(Constant.KEY_TAG());
			}
			sb.append(help_arr[len - 1]);
			list.add(sb.toString());
		} else {
			help_arr[count] = arr[count];
			combineImpression(arr, help_arr, count + 1, len, list);

			if (count == 0 || count == 1 || count == 6 || count == 10) {
				//platform product_id protocal scenario
			} else {
				if (count == 7 && arr[count].startsWith("advertisement")) {
					help_arr[count] = "advertisement_all";
					combineImpression(arr, help_arr, count + 1, len, list);
				}

				help_arr[count] = "all";
				combineImpression(arr, help_arr, count + 1, len, list);
			}
		}
	}

}
