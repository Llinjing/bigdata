package com.inveno.news.reformat.constant;

import java.util.HashMap;
import java.util.Map;

public class ContentType {
	
	public static class ContentTypeValue {
		
		public static final ContentTypeValue UNKNOWN = new ContentTypeValue(-1);
		
		public static final ContentTypeValue NEWS = new ContentTypeValue(0x01);
		
		public static final ContentTypeValue SHORT_VEDIO = new ContentTypeValue(0x02);
		
		public static final ContentTypeValue LONG_VEDIO = new ContentTypeValue(0x04);

		public static final ContentTypeValue TOPIC = new ContentTypeValue(0x08);
		
		public static final ContentTypeValue SPECIAL_ISSUE = new ContentTypeValue(0x10);
		
		public static final ContentTypeValue GIF = new ContentTypeValue(0x20);
		
		public static final ContentTypeValue BANNER = new ContentTypeValue(0x40);
		
		public static final ContentTypeValue BIG_IMAGE = new ContentTypeValue(0x80);
		
		public static final ContentTypeValue IMAGE_SET = new ContentTypeValue(0x100);
		
		public static final ContentTypeValue ADVERTISEMENT_HARD = new ContentTypeValue(0x200);
		
		public static final ContentTypeValue ADVERTISEMENT_SOFT = new ContentTypeValue(0x400);
		
		public static final ContentTypeValue ADVERTISEMENT_THIRD_PARTY = new ContentTypeValue(0x800);
		
		public static final ContentTypeValue AUDIO = new ContentTypeValue(0x1000);
		
		public static final ContentTypeValue COMMENT_BACK = new ContentTypeValue(0x2000);
		
		private int content_type = -1;
		
		private ContentTypeValue(int content_type) {
			this.content_type = content_type;
		}
		
		public int getValue() {
			return content_type;
		}
		
		@Override
		public String toString() {
			return String.valueOf(content_type);
		}
		
		public boolean equals(Object obj) {
			boolean ret = false;
			
			if (obj instanceof ContentTypeValue) {
				ret = (content_type == ((ContentTypeValue)obj).content_type);
			} else if (obj instanceof Integer) {
				ret = (content_type == ((Integer)obj));
			}
			
			return ret;
		}
		
	}
	
	public static class ContentTypeValueDesc {
		
		public static final ContentTypeValueDesc UNKNOWN = new ContentTypeValueDesc("unknown");
		
		public static final ContentTypeValueDesc NEWS = new ContentTypeValueDesc("news");
		
		public static final ContentTypeValueDesc SHORT_VEDIO = new ContentTypeValueDesc("short_video");
		
		public static final ContentTypeValueDesc LONG_VEDIO = new ContentTypeValueDesc("long_video");

		public static final ContentTypeValueDesc TOPIC = new ContentTypeValueDesc("topic");
		
		public static final ContentTypeValueDesc SPECIAL_ISSUE = new ContentTypeValueDesc("special_issue");
		
		public static final ContentTypeValueDesc GIF = new ContentTypeValueDesc("gif");
		
		public static final ContentTypeValueDesc BANNER = new ContentTypeValueDesc("banner");
		
		public static final ContentTypeValueDesc BIG_IMAGE = new ContentTypeValueDesc("big_image");
		
		public static final ContentTypeValueDesc IMAGE_SET = new ContentTypeValueDesc("image_set");
		
		public static final ContentTypeValueDesc ADVERTISEMENT = new ContentTypeValueDesc("advertisement");
		
		public static final ContentTypeValueDesc ADVERTISEMENT_HARD = new ContentTypeValueDesc("advertisement_hard");
		
		public static final ContentTypeValueDesc ADVERTISEMENT_SOFT = new ContentTypeValueDesc("advertisement_soft");
		
		public static final ContentTypeValueDesc ADVERTISEMENT_THIRD_PARTY = new ContentTypeValueDesc("advertisement_third_party");
		
		public static final ContentTypeValueDesc AUDIO = new ContentTypeValueDesc("audio");
		
		public static final ContentTypeValueDesc COMMENT_BACK = new ContentTypeValueDesc("comment_back");
		
		private String content_type = null;
		
		private ContentTypeValueDesc(String type) {
			this.content_type = type;
		}
		
		@Override
		public String toString() {
			return content_type;
		}
		
	}
	
	public static Map<Integer, String> content_type_desc = new HashMap<Integer, String>();
	
	static {
		content_type_desc.put(ContentTypeValue.UNKNOWN.getValue(), ContentTypeValueDesc.UNKNOWN.toString());
		content_type_desc.put(ContentTypeValue.NEWS.getValue(), ContentTypeValueDesc.NEWS.toString());
		content_type_desc.put(ContentTypeValue.UNKNOWN.getValue(), ContentTypeValueDesc.UNKNOWN.toString());
		content_type_desc.put(ContentTypeValue.SHORT_VEDIO.getValue(), ContentTypeValueDesc.SHORT_VEDIO.toString());
		content_type_desc.put(ContentTypeValue.LONG_VEDIO.getValue(), ContentTypeValueDesc.LONG_VEDIO.toString());
		content_type_desc.put(ContentTypeValue.TOPIC.getValue(), ContentTypeValueDesc.TOPIC.toString());
		content_type_desc.put(ContentTypeValue.SPECIAL_ISSUE.getValue(), ContentTypeValueDesc.SPECIAL_ISSUE.toString());
		content_type_desc.put(ContentTypeValue.GIF.getValue(), ContentTypeValueDesc.GIF.toString());
		content_type_desc.put(ContentTypeValue.BANNER.getValue(), ContentTypeValueDesc.BANNER.toString());
		content_type_desc.put(ContentTypeValue.BIG_IMAGE.getValue(), ContentTypeValueDesc.BIG_IMAGE.toString());
		content_type_desc.put(ContentTypeValue.IMAGE_SET.getValue(), ContentTypeValueDesc.IMAGE_SET.toString());
		content_type_desc.put(ContentTypeValue.ADVERTISEMENT_HARD.getValue(), ContentTypeValueDesc.ADVERTISEMENT.toString());
		content_type_desc.put(ContentTypeValue.ADVERTISEMENT_SOFT.getValue(), ContentTypeValueDesc.ADVERTISEMENT.toString());
		content_type_desc.put(ContentTypeValue.ADVERTISEMENT_THIRD_PARTY.getValue(), ContentTypeValueDesc.ADVERTISEMENT.toString());
		content_type_desc.put(ContentTypeValue.AUDIO.getValue(), ContentTypeValueDesc.AUDIO.toString());
		content_type_desc.put(ContentTypeValue.COMMENT_BACK.getValue(), ContentTypeValueDesc.COMMENT_BACK.toString());
	}
	
	public static String getContentType(String content_type_hex) {
		int content_type = ContentTypeValue.UNKNOWN.getValue();
		try {
			content_type = Integer.valueOf(content_type_hex.substring(2), 16).intValue();
		} catch (Exception e) {
		}
		return getContentType(content_type);
	}
	
	public static String getContentType(int content_type) {
		String ret = content_type_desc.get(content_type);
		if (ret == null) {
			ret = ContentTypeValueDesc.UNKNOWN.toString();
		}
		return ret;
	}
}
