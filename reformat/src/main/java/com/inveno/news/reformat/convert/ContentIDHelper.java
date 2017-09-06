package com.inveno.news.reformat.convert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.inveno.news.reformat.constant.AdSource;
import com.inveno.news.reformat.constant.AdSource.AdSourceValueDesc;
import com.inveno.news.reformat.constant.ContentType;

public class ContentIDHelper {
	
	public static final String TAOBAO_AD_ID = "1153200000000000000";
	
	public static final String BAIDU_AD_ID = "1153760000000000000";

	public static final String DUJIN_AD_ID = "1154040000000000000";
	
	public static final String JX360_AD_ID = "1154180000000000000";
	
	public static final String SOUGOU_AD_ID = "1154320000000000000";
	
	public static final String YIYUN_AD_ID = "1154460000000000000";
	
	public static final String YUNOS_AD_ID = "1154610000000000000";
	
	public static final String KDXF_AD_ID = "1154750000000000000";
	
	private static List<Integer> article_id_lengths = new ArrayList<Integer>();
	
	private static Map<String, String> ad_source_map = new HashMap<String, String>();
	
	static {
		article_id_lengths.add(7);
		article_id_lengths.add(8);
		article_id_lengths.add(10);
		
		ad_source_map.put("00001", AdSourceValueDesc.SELF_AD.toString());
		ad_source_map.put("00101", AdSourceValueDesc.SELF_AD.toString());
		ad_source_map.put("00010", AdSourceValueDesc.TAOBAO_AD.toString());
		ad_source_map.put("00110", AdSourceValueDesc.BAIDU_AD.toString());
		ad_source_map.put("01000", AdSourceValueDesc.DUJIN_AD.toString());
		ad_source_map.put("01001", AdSourceValueDesc.JX360_AD.toString());
		ad_source_map.put("01010", AdSourceValueDesc.SOUGOU_AD.toString());
		ad_source_map.put("01011", AdSourceValueDesc.YIYUN_AD.toString());
		ad_source_map.put("01100", AdSourceValueDesc.YUNOS_AD.toString());
		ad_source_map.put("01101", AdSourceValueDesc.KDXF_AD.toString());
	}
	
	public static String getContentType(String content_id) {
		ContentType.ContentTypeValue type = ContentType.ContentTypeValue.UNKNOWN;
		
		if (content_id != null) {
			int len = content_id.length();
			if (article_id_lengths.contains(len)) {
				type = ContentType.ContentTypeValue.NEWS;
			} else {
				if (len == 19 && content_id.startsWith("115306")) {
					type = ContentType.ContentTypeValue.ADVERTISEMENT_HARD;
				} else if (len == 19 && content_id.startsWith("115362")) {
					type = ContentType.ContentTypeValue.ADVERTISEMENT_SOFT;
				} else {
					type = ContentType.ContentTypeValue.ADVERTISEMENT_THIRD_PARTY;
				}
			}
		}
		
		return ContentType.getContentType(type.getValue());
	}
	
	public static String getAdSource(String content_id) {
		String ad_source = null;
		
		try {
			String binary_str = Long.toBinaryString(Long.valueOf(content_id));
			int delt = Long.SIZE - binary_str.length();
			for (int i = 0; i < delt; ++i) {
				binary_str = "0" + binary_str;
			}
			ad_source = ad_source_map.get(binary_str.substring(12, 17));
		} catch (Exception e) {	
		}
		
		if (ad_source == null) {
			ad_source = AdSource.AdSourceValueDesc.UNKNOWN.toString();
		}
		
		return ad_source;
	}

	public static String getUnitAdId(String ad_source, String content_id) {
		if (ad_source == null || AdSource.AdSourceValueDesc.UNKNOWN.equals(ad_source) || AdSource.AdSourceValueDesc.SELF_AD.equals(ad_source)) {
			return content_id;
		}
		
		String ad_id = "0000000000000000000";
		try {
			if (AdSource.AdSourceValueDesc.BAIDU_AD.equals(ad_source)) {
				ad_id = ContentIDHelper.BAIDU_AD_ID;
			} else if (AdSource.AdSourceValueDesc.TAOBAO_AD.equals(ad_source)) {
				ad_id = ContentIDHelper.TAOBAO_AD_ID;
			}else if (AdSource.AdSourceValueDesc.DUJIN_AD.equals(ad_source)) {
				ad_id = ContentIDHelper.DUJIN_AD_ID;
			}else if (AdSource.AdSourceValueDesc.JX360_AD.equals(ad_source)) {
				ad_id = ContentIDHelper.JX360_AD_ID;
			}else if (AdSource.AdSourceValueDesc.SOUGOU_AD.equals(ad_source)) {
				ad_id = ContentIDHelper.SOUGOU_AD_ID;
			}else if (AdSource.AdSourceValueDesc.YIYUN_AD.equals(ad_source)) {
				ad_id = ContentIDHelper.YIYUN_AD_ID;
			}else if (AdSource.AdSourceValueDesc.YUNOS_AD.equals(ad_source)) {
				ad_id = ContentIDHelper.YUNOS_AD_ID;
			}else if (AdSource.AdSourceValueDesc.KDXF_AD.equals(ad_source)) {
				ad_id = ContentIDHelper.KDXF_AD_ID;
			}	
		} catch (Exception e) {	
		}
		
		return ad_id;
	}
}
