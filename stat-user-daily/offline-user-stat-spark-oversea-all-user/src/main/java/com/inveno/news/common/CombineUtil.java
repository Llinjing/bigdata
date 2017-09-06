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

			if (count == 0 || count == 1 || count == 6 || count == 9 || count == 12) {
				//platform product_id protocal scenario_channel biz_configid
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

			if (count == 0 || count == 1 || count == 6 || count == 9 || count == 10 || count == 12) {
				//platform product_id protocal scenario_channel scenario biz_configid
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
	
	public static void combineOther(String[] arr, String[] help_arr, int count,
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
			combineOther(arr, help_arr, count + 1, len, list);

			if (count == 0 || count == 1 || count == 2 || count == 5 || count == 6 || count == 9 || count == 12) {
				//platform product_id app_ver news_configid protocal scenario_channel biz_configid
			} else {
				if (count == 7 && arr[count].startsWith("advertisement")) {
					help_arr[count] = "advertisement_all";
					combineOther(arr, help_arr, count + 1, len, list);
				}

				help_arr[count] = "all";
				combineOther(arr, help_arr, count + 1, len, list);
			}
		}
	}
	
	public static void combineImpressionOther(String[] arr, String[] help_arr, int count,
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
			combineImpressionOther(arr, help_arr, count + 1, len, list);

			if (count == 0 || count == 1 || count == 2 || count == 5 || count == 6 || count == 9 || count == 10 || count == 12) {
				//platform product_id app_ver news_configid protocal scenario_channel scenario biz_configid
			} else {
				if (count == 7 && arr[count].startsWith("advertisement")) {
					help_arr[count] = "advertisement_all";
					combineImpressionOther(arr, help_arr, count + 1, len, list);
				}

				help_arr[count] = "all";
				combineImpressionOther(arr, help_arr, count + 1, len, list);
			}
		}
	}
	
	public static void combineSC(String[] arr, String[] help_arr, int count,
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
			combineSC(arr, help_arr, count + 1, len, list);

			if (count == 0 || count == 1 || count == 6 || count == 9 || count == 11 || count == 12) {
				//platform product_id protocal scenario_channel ad_configid biz_configid
			} else {
				if (count == 7 && arr[count].startsWith("advertisement")) {
					help_arr[count] = "advertisement_all";
					combineSC(arr, help_arr, count + 1, len, list);
				}

				help_arr[count] = "all";
				combineSC(arr, help_arr, count + 1, len, list);
			}
		}
	}
	
	public static void combineImpressionSC(String[] arr, String[] help_arr, int count,
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
			combineImpressionSC(arr, help_arr, count + 1, len, list);

			if (count == 0 || count == 1 || count == 6 || count == 9 || count == 10 || count == 11 || count == 12) {
				//platform product_id protocal scenario_channel scenario ad_configid biz_configid
			} else {
				if (count == 7 && arr[count].startsWith("advertisement")) {
					help_arr[count] = "advertisement_all";
					combineImpressionSC(arr, help_arr, count + 1, len, list);
				}

				help_arr[count] = "all";
				combineImpressionSC(arr, help_arr, count + 1, len, list);
			}
		}
	}

}
