package com.inveno.news.reformat.constant;

import java.util.HashMap;
import java.util.Map;

public class ScenarioType {
	
	public static class PositionType {
		
		public static class PositionTypeValue {
			
			public static final PositionTypeValue UNKNOWN = new PositionTypeValue(-1);
			
			public static final PositionTypeValue WATERFALL = new PositionTypeValue(0x00);
			
			public static final PositionTypeValue LONG_LISTPAGE = new PositionTypeValue(0x01);
			
			public static final PositionTypeValue SHORT_LISTPAGE = new PositionTypeValue(0x02);
			
			public static final PositionTypeValue PUSH = new PositionTypeValue(0x03);
			
			public static final PositionTypeValue SPECIAL_ISSUE_PAGE = new PositionTypeValue(0x04);
			
			public static final PositionTypeValue RELEVANT_RECOMMENTDATION = new PositionTypeValue(0x05);
			
			public static final PositionTypeValue SINGLE_NEWS = new PositionTypeValue(0x06);
			
			public static final PositionTypeValue OTHER = new PositionTypeValue(0x07);
			
			private int value = -1;
			
			private PositionTypeValue(int value) {
				this.value = value;
			}
			
			public int getValue() {
				return value;
			}
			
			@Override
			public String toString() {
				return String.valueOf(value);
			}
			
			@Override
			public boolean equals(Object obj) {
				boolean ret = false;
				
				if (obj instanceof EventID) {
					ret = (value == ((PositionTypeValue)obj).value);
				} else if (obj instanceof Integer) {
					ret = (value == (Integer)obj);
				}
				
				return ret;
			}
			
		} // end class PositionTypeValue
		
		public static class PositionTypeValueDesc {
			
			public static final PositionTypeValueDesc UNKNOWN = new PositionTypeValueDesc("unknown");
			
			public static final PositionTypeValueDesc WATERFALL= new PositionTypeValueDesc("waterfall");
			
			public static final PositionTypeValueDesc LONG_LISTPAGE = new PositionTypeValueDesc("long_listpage");
			
			public static final PositionTypeValueDesc SHORT_LISTPAGE = new PositionTypeValueDesc("short_listpage");
			
			public static final PositionTypeValueDesc PUSH = new PositionTypeValueDesc("push");
			
			public static final PositionTypeValueDesc SPECIAL_ISSUE_PAGE = new PositionTypeValueDesc("special_issue_page");
			
			public static final PositionTypeValueDesc RELEVANT_RECOMMENTDATION = new PositionTypeValueDesc("relevant_recommendation");
			
			public static final PositionTypeValueDesc SINGLE_NEWS = new PositionTypeValueDesc("single_news");
			
			public static final PositionTypeValueDesc OTHER = new PositionTypeValueDesc("other");
			
			private String desc = null;
			
			private PositionTypeValueDesc(String desc) {
				this.desc = desc;
			}
			
			@Override
			public String toString() {
				return desc;
			}
			
			@Override
			public boolean equals(Object obj) {
				boolean ret = false;
				
				if (obj instanceof EventID) {
					ret = desc.contentEquals(((PositionTypeValueDesc)obj).desc);
				} else if (obj instanceof String) {
					ret = desc.contentEquals((String)obj);
				}
				
				return ret;
			}
			
		} // end class PositionTypeValueDesc
	
		private static Map<Integer, String> position_type_desc = new HashMap<Integer, String>();
		
		static {
			position_type_desc.put(PositionTypeValue.WATERFALL.getValue(), PositionTypeValueDesc.WATERFALL.toString());
			position_type_desc.put(PositionTypeValue.LONG_LISTPAGE.getValue(), PositionTypeValueDesc.LONG_LISTPAGE.toString());
			position_type_desc.put(PositionTypeValue.SHORT_LISTPAGE.getValue(), PositionTypeValueDesc.SHORT_LISTPAGE.toString());
			position_type_desc.put(PositionTypeValue.PUSH.getValue(), PositionTypeValueDesc.PUSH.toString());
			position_type_desc.put(PositionTypeValue.SPECIAL_ISSUE_PAGE.getValue(), PositionTypeValueDesc.SPECIAL_ISSUE_PAGE.toString());
			position_type_desc.put(PositionTypeValue.RELEVANT_RECOMMENTDATION.getValue(), PositionTypeValueDesc.RELEVANT_RECOMMENTDATION.toString());
			position_type_desc.put(PositionTypeValue.SINGLE_NEWS.getValue(), PositionTypeValueDesc.SINGLE_NEWS.toString());
			position_type_desc.put(PositionTypeValue.OTHER.getValue(), PositionTypeValueDesc.OTHER.toString());
		}
		
		public static String getPositionTypeDesc(int position_type) {
			String str = position_type_desc.get(position_type);
			if (str == null) {
				str = PositionTypeValueDesc.UNKNOWN.toString();
			}
			return str;
		}
	
	} // end class PositionType
	
	
	////////// Position /////////
	public static class Position {
		
		private static final String UNKNOWN = "unknown";
		
		private static Map<String, String> product_position_desc = new HashMap<String, String>();
	
		static {
			// hotoday
			product_position_desc.put(ProductID.HOTODAY.toString() + "0x0101", "long_listpage");
			product_position_desc.put(ProductID.HOTODAY.toString() + "0x0102", "poll_list");
			product_position_desc.put(ProductID.HOTODAY.toString() + "0x0103", "subscription_list");
			product_position_desc.put(ProductID.HOTODAY.toString() + "0x0104", "subscription_source_list");
			product_position_desc.put(ProductID.HOTODAY.toString() + "0x0105", "personal_centre");
			product_position_desc.put(ProductID.HOTODAY.toString() + "0x0106", "favourites_page");
			product_position_desc.put(ProductID.HOTODAY.toString() + "0x0107", "search_page");
			product_position_desc.put(ProductID.HOTODAY.toString() + "0x0308", "push");
			product_position_desc.put(ProductID.HOTODAY.toString() + "0x0409", "special_issue");
			product_position_desc.put(ProductID.HOTODAY.toString() + "0x050c", "relevant_recommendation");
			product_position_desc.put(ProductID.HOTODAY.toString() + "0x070d", "detail_textbottom");
			product_position_desc.put(ProductID.HOTODAY.toString() + "0x010e", "self_media");
			product_position_desc.put(ProductID.HOTODAY.toString() + "0x070f", "detailopen_facebookshare");
			product_position_desc.put(ProductID.HOTODAY.toString() + "0x0110", "video_tab");
			product_position_desc.put(ProductID.HOTODAY.toString() + "0x0711", "open_screen_ad");
			product_position_desc.put(ProductID.HOTODAY.toString() + "0x0712", "quickread");
			product_position_desc.put(ProductID.HOTODAY.toString() + "0x0713", "lockscreen");
			product_position_desc.put(ProductID.HOTODAY.toString() + "0x0714", "insert_screen");
			product_position_desc.put(ProductID.HOTODAY.toString() + "0x0715", "floating_window");
			
			// mata
			product_position_desc.put(ProductID.MATA.toString() + "0x0101", "long_listpage");
			product_position_desc.put(ProductID.MATA.toString() + "0x0102", "poll_list");
			product_position_desc.put(ProductID.MATA.toString() + "0x0103", "subscription_list");
			product_position_desc.put(ProductID.MATA.toString() + "0x0104", "subscription_source_list");
			product_position_desc.put(ProductID.MATA.toString() + "0x0105", "personal_centre");
			product_position_desc.put(ProductID.MATA.toString() + "0x0106", "favourites_page");
			product_position_desc.put(ProductID.MATA.toString() + "0x0107", "search_page");
			product_position_desc.put(ProductID.MATA.toString() + "0x0308", "push");
			product_position_desc.put(ProductID.MATA.toString() + "0x0409", "special_issue");
			product_position_desc.put(ProductID.MATA.toString() + "0x050c", "relevant_recommendation");
			product_position_desc.put(ProductID.MATA.toString() + "0x070d", "detail_textbottom");
			product_position_desc.put(ProductID.MATA.toString() + "0x010e", "self_media");
			product_position_desc.put(ProductID.MATA.toString() + "0x070f", "detailopen_facebookshare");
			product_position_desc.put(ProductID.MATA.toString() + "0x0110", "video_tab");
			product_position_desc.put(ProductID.MATA.toString() + "0x0711", "open_screen_ad");
			product_position_desc.put(ProductID.MATA.toString() + "0x0712", "quickread");
			product_position_desc.put(ProductID.MATA.toString() + "0x0713", "lockscreen");
			product_position_desc.put(ProductID.MATA.toString() + "0x0714", "insert_screen");
			product_position_desc.put(ProductID.MATA.toString() + "0x0715", "floating_window");
			
			// noticias
			product_position_desc.put(ProductID.NOTICIAS.toString() + "0x0101", "long_listpage");
			product_position_desc.put(ProductID.NOTICIAS.toString() + "0x0102", "poll_list");
			product_position_desc.put(ProductID.NOTICIAS.toString() + "0x0103", "subscription_list");
			product_position_desc.put(ProductID.NOTICIAS.toString() + "0x0104", "subscription_source_list");
			product_position_desc.put(ProductID.NOTICIAS.toString() + "0x0105", "personal_centre");
			product_position_desc.put(ProductID.NOTICIAS.toString() + "0x0106", "favourites_page");
			product_position_desc.put(ProductID.NOTICIAS.toString() + "0x0107", "search_page");
			product_position_desc.put(ProductID.NOTICIAS.toString() + "0x0308", "push");
			product_position_desc.put(ProductID.NOTICIAS.toString() + "0x0409", "special_issue");
			product_position_desc.put(ProductID.NOTICIAS.toString() + "0x050c", "relevant_recommendation");
			product_position_desc.put(ProductID.NOTICIAS.toString() + "0x070d", "detail_textbottom");
			product_position_desc.put(ProductID.NOTICIAS.toString() + "0x010e", "self_media");
			product_position_desc.put(ProductID.NOTICIAS.toString() + "0x070f", "detailopen_facebookshare");
			product_position_desc.put(ProductID.NOTICIAS.toString() + "0x0110", "video_tab");
			product_position_desc.put(ProductID.NOTICIAS.toString() + "0x0711", "open_screen_ad");
			product_position_desc.put(ProductID.NOTICIAS.toString() + "0x0712", "quickread");
			product_position_desc.put(ProductID.NOTICIAS.toString() + "0x0713", "lockscreen");
			product_position_desc.put(ProductID.NOTICIAS.toString() + "0x0714", "insert_screen");
			product_position_desc.put(ProductID.NOTICIAS.toString() + "0x0715", "floating_window");
			
			// noticiasboom
			product_position_desc.put(ProductID.NOTICIAS_BOOM.toString() + "0x0101", "long_listpage");
			product_position_desc.put(ProductID.NOTICIAS_BOOM.toString() + "0x0102", "poll_list");
			product_position_desc.put(ProductID.NOTICIAS_BOOM.toString() + "0x0103", "subscription_list");
			product_position_desc.put(ProductID.NOTICIAS_BOOM.toString() + "0x0104", "subscription_source_list");
			product_position_desc.put(ProductID.NOTICIAS_BOOM.toString() + "0x0105", "personal_centre");
			product_position_desc.put(ProductID.NOTICIAS_BOOM.toString() + "0x0106", "favourites_page");
			product_position_desc.put(ProductID.NOTICIAS_BOOM.toString() + "0x0107", "search_page");
			product_position_desc.put(ProductID.NOTICIAS_BOOM.toString() + "0x0308", "push");
			product_position_desc.put(ProductID.NOTICIAS_BOOM.toString() + "0x0409", "special_issue");
			product_position_desc.put(ProductID.NOTICIAS_BOOM.toString() + "0x050c", "relevant_recommendation");
			product_position_desc.put(ProductID.NOTICIAS_BOOM.toString() + "0x070d", "detail_textbottom");
			product_position_desc.put(ProductID.NOTICIAS_BOOM.toString() + "0x010e", "self_media");
			product_position_desc.put(ProductID.NOTICIAS_BOOM.toString() + "0x070f", "detailopen_facebookshare");
			product_position_desc.put(ProductID.NOTICIAS_BOOM.toString() + "0x0110", "video_tab");
			product_position_desc.put(ProductID.NOTICIAS_BOOM.toString() + "0x0711", "open_screen_ad");
			product_position_desc.put(ProductID.NOTICIAS_BOOM.toString() + "0x0712", "quickread");
			product_position_desc.put(ProductID.NOTICIAS_BOOM.toString() + "0x0713", "lockscreen");
			product_position_desc.put(ProductID.NOTICIAS_BOOM.toString() + "0x0714", "insert_screen");
			product_position_desc.put(ProductID.NOTICIAS_BOOM.toString() + "0x0715", "floating_window");
			
			// noticiasboomchile
			product_position_desc.put(ProductID.NOTICIAS_BOOM_CHILE.toString() + "0x0101", "long_listpage");
			product_position_desc.put(ProductID.NOTICIAS_BOOM_CHILE.toString() + "0x0102", "poll_list");
			product_position_desc.put(ProductID.NOTICIAS_BOOM_CHILE.toString() + "0x0103", "subscription_list");
			product_position_desc.put(ProductID.NOTICIAS_BOOM_CHILE.toString() + "0x0104", "subscription_source_list");
			product_position_desc.put(ProductID.NOTICIAS_BOOM_CHILE.toString() + "0x0105", "personal_centre");
			product_position_desc.put(ProductID.NOTICIAS_BOOM_CHILE.toString() + "0x0106", "favourites_page");
			product_position_desc.put(ProductID.NOTICIAS_BOOM_CHILE.toString() + "0x0107", "search_page");
			product_position_desc.put(ProductID.NOTICIAS_BOOM_CHILE.toString() + "0x0308", "push");
			product_position_desc.put(ProductID.NOTICIAS_BOOM_CHILE.toString() + "0x0409", "special_issue");
			product_position_desc.put(ProductID.NOTICIAS_BOOM_CHILE.toString() + "0x050c", "relevant_recommendation");
			product_position_desc.put(ProductID.NOTICIAS_BOOM_CHILE.toString() + "0x070d", "detail_textbottom");
			product_position_desc.put(ProductID.NOTICIAS_BOOM_CHILE.toString() + "0x010e", "self_media");
			product_position_desc.put(ProductID.NOTICIAS_BOOM_CHILE.toString() + "0x070f", "detailopen_facebookshare");
			product_position_desc.put(ProductID.NOTICIAS_BOOM_CHILE.toString() + "0x0110", "video_tab");
			product_position_desc.put(ProductID.NOTICIAS_BOOM_CHILE.toString() + "0x0711", "open_screen_ad");
			product_position_desc.put(ProductID.NOTICIAS_BOOM_CHILE.toString() + "0x0712", "quickread");
			product_position_desc.put(ProductID.NOTICIAS_BOOM_CHILE.toString() + "0x0713", "lockscreen");
			product_position_desc.put(ProductID.NOTICIAS_BOOM_CHILE.toString() + "0x0714", "insert_screen");
			product_position_desc.put(ProductID.NOTICIAS_BOOM_CHILE.toString() + "0x0715", "floating_window");
			
			// noticiasboomcolombia
			product_position_desc.put(ProductID.NOTICIAS_BOOM_COLOMBIA.toString() + "0x0101", "long_listpage");
			product_position_desc.put(ProductID.NOTICIAS_BOOM_COLOMBIA.toString() + "0x0102", "poll_list");
			product_position_desc.put(ProductID.NOTICIAS_BOOM_COLOMBIA.toString() + "0x0103", "subscription_list");
			product_position_desc.put(ProductID.NOTICIAS_BOOM_COLOMBIA.toString() + "0x0104", "subscription_source_list");
			product_position_desc.put(ProductID.NOTICIAS_BOOM_COLOMBIA.toString() + "0x0105", "personal_centre");
			product_position_desc.put(ProductID.NOTICIAS_BOOM_COLOMBIA.toString() + "0x0106", "favourites_page");
			product_position_desc.put(ProductID.NOTICIAS_BOOM_COLOMBIA.toString() + "0x0107", "search_page");
			product_position_desc.put(ProductID.NOTICIAS_BOOM_COLOMBIA.toString() + "0x0308", "push");
			product_position_desc.put(ProductID.NOTICIAS_BOOM_COLOMBIA.toString() + "0x0409", "special_issue");
			product_position_desc.put(ProductID.NOTICIAS_BOOM_COLOMBIA.toString() + "0x050c", "relevant_recommendation");
			product_position_desc.put(ProductID.NOTICIAS_BOOM_COLOMBIA.toString() + "0x070d", "detail_textbottom");
			product_position_desc.put(ProductID.NOTICIAS_BOOM_COLOMBIA.toString() + "0x010e", "self_media");
			product_position_desc.put(ProductID.NOTICIAS_BOOM_COLOMBIA.toString() + "0x070f", "detailopen_facebookshare");
			product_position_desc.put(ProductID.NOTICIAS_BOOM_COLOMBIA.toString() + "0x0110", "video_tab");
			product_position_desc.put(ProductID.NOTICIAS_BOOM_COLOMBIA.toString() + "0x0711", "open_screen_ad");
			product_position_desc.put(ProductID.NOTICIAS_BOOM_COLOMBIA.toString() + "0x0712", "quickread");
			product_position_desc.put(ProductID.NOTICIAS_BOOM_COLOMBIA.toString() + "0x0713", "lockscreen");
			product_position_desc.put(ProductID.NOTICIAS_BOOM_COLOMBIA.toString() + "0x0714", "insert_screen");
			product_position_desc.put(ProductID.NOTICIAS_BOOM_COLOMBIA.toString() + "0x0715", "floating_window");
			
			// ali
			product_position_desc.put(ProductID.ALI.toString() + "0x0101", "ali_left_first_screen");
			product_position_desc.put(ProductID.ALI.toString() + "0x0104", "ali_left_first_screen_h5");
			product_position_desc.put(ProductID.ALI.toString() + "0x0105", "ali_cloud_news");
			product_position_desc.put(ProductID.ALI.toString() + "0x0108", "ali_cn_h5");
			product_position_desc.put(ProductID.ALI.toString() + "0x0203", "ali_hot_recomm");
			product_position_desc.put(ProductID.ALI.toString() + "0x0207", "ali_cn_hot_recomm");
			product_position_desc.put(ProductID.ALI.toString() + "0x0502", "ali_relevant_recomm");
			product_position_desc.put(ProductID.ALI.toString() + "0x0506", "ali_cn_relevant_recomm");
			
			// nubiabrowser
			product_position_desc.put(ProductID.NUBIABROWSER.toString() + "0x0101", "long_listpage");
			product_position_desc.put(ProductID.NUBIABROWSER.toString() + "0x0104", "h5");
			product_position_desc.put(ProductID.NUBIABROWSER.toString() + "0x0203", "excellent_recommendation");
			product_position_desc.put(ProductID.NUBIABROWSER.toString() + "0x0502", "relevant_recommendation");
			
			// zuimeitianqi(new)
			product_position_desc.put(ProductID.ZUIMEITIANQI_NEW.toString() + "0x0101", "long_listpage");
			product_position_desc.put(ProductID.ZUIMEITIANQI_NEW.toString() + "0x0502", "relevant_recommendation");
			
			// 魔秀锁屏
			product_position_desc.put(ProductID.MOXIU_SUOPING_NEW.toString() + "0x0101", "long_listpage");
			product_position_desc.put(ProductID.MOXIU_SUOPING_NEW.toString() + "0x0104", "h5");
			product_position_desc.put(ProductID.MOXIU_SUOPING_NEW.toString() + "0x0203", "excellent_recommendation");
			product_position_desc.put(ProductID.MOXIU_SUOPING_NEW.toString() + "0x0502", "relevant_recommendation");
			
			// 魔秀搜索
			product_position_desc.put(ProductID.MOXIU_SEARCH.toString() + "0x0101", "long_listpage");
			product_position_desc.put(ProductID.MOXIU_SEARCH.toString() + "0x0104", "h5");
			product_position_desc.put(ProductID.MOXIU_SEARCH.toString() + "0x0203", "excellent_recommendation");
			product_position_desc.put(ProductID.MOXIU_SEARCH.toString() + "0x0502", "relevant_recommendation");
			
			// 魔秀桌面（新）
			product_position_desc.put(ProductID.MOXIU_ZHUOMIAN_NEW.toString() + "0x0101", "long_listpage");
			product_position_desc.put(ProductID.MOXIU_ZHUOMIAN_NEW.toString() + "0x0104", "h5");
			product_position_desc.put(ProductID.MOXIU_ZHUOMIAN_NEW.toString() + "0x0203", "excellent_recommendation");
			product_position_desc.put(ProductID.MOXIU_ZHUOMIAN_NEW.toString() + "0x0502", "relevant_recommendation");
		}
		
		public static String getPositionDesc(String key) {
			String ret = product_position_desc.get(key);
			if (ret == null) {
				ret = UNKNOWN;
			}
			return ret;
		}
	}
	
	////////// Channel /////////
	public static class Channel {
		
		public static final String UNKNOWN = "unknown";
		
		private static Map<String, String> channel_desc = new HashMap<String, String>();
			
		static {
			channel_desc.put("0x00", "foryou");
			channel_desc.put("0x01", "hotspot");
			channel_desc.put("0x02", "politics");
			channel_desc.put("0x03", "national");
			channel_desc.put("0x04", "global");
			channel_desc.put("0x05", "entertainment");
			channel_desc.put("0x06", "sports_recreation");
			channel_desc.put("0x07", "military");
			channel_desc.put("0x08", "tech");
			channel_desc.put("0x09", "finance");
			channel_desc.put("0x0a", "headlines");
			channel_desc.put("0x0b", "video");
			channel_desc.put("0x0c", "cricket");
			channel_desc.put("0x0d", "sports");
			channel_desc.put("0x0e", "health");
			channel_desc.put("0x0f", "fashion");
			channel_desc.put("0x10", "lifestyle");
			channel_desc.put("0x11", "relationships");
			channel_desc.put("0x12", "photos");
			channel_desc.put("0x13", "humor");
			channel_desc.put("0x14", "education");
			channel_desc.put("0x15", "food");
			channel_desc.put("0x16", "career");
			channel_desc.put("0x17", "celebrity");
			channel_desc.put("0x18", "culture");
			channel_desc.put("0x19", "local");
			channel_desc.put("0x1a", "female");
			channel_desc.put("0x1b", "travel");
			channel_desc.put("0x1c", "reading");
			channel_desc.put("0x1d", "game");
			channel_desc.put("0x1e", "auto");
			channel_desc.put("0x1f", "business");
			channel_desc.put("0x20", "personal_comments");
			channel_desc.put("0x21", "personal_reading");
			channel_desc.put("0x22", "search");
			channel_desc.put("0x23", "read");
			channel_desc.put("0x24", "today_in_history");
			channel_desc.put("0x25", "beauty");
			channel_desc.put("0x26", "gif");			
			channel_desc.put("0x27", "constellation");
			channel_desc.put("0x28", "economy");
			channel_desc.put("0x29", "football");
			channel_desc.put("0x2a", "real_estate");
			channel_desc.put("0x2b", "self_media");
			channel_desc.put("0x2c", "society");
			channel_desc.put("0x2d", "local_city");
			channel_desc.put("0x2e", "meme");
			channel_desc.put("0x80", "subscribe");
			channel_desc.put("0x81", "inspiration");
			channel_desc.put("0x82", "latest");
			channel_desc.put("0x83", "trending");
			channel_desc.put("0x86", "us_election");
			channel_desc.put("0xfe", "test");
			channel_desc.put("0xff", UNKNOWN);
		}
		
		public static String getChannelDesc(String channel_value) {
			String ret = channel_desc.get(channel_value);
			if (ret == null) {
				ret = UNKNOWN;
			}
			return ret;
		}
	} // end class Channel
	
}
