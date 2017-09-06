package com.inveno.news.reformat.constant;

import java.util.HashMap;
import java.util.Map;

public class Display {

	public static class DisplayValue {

		public static final DisplayValue UNKNOWN = new DisplayValue("-1");

		public static final DisplayValue NO_PHOTO = new DisplayValue("0x00000001");

		public static final DisplayValue ONE_PHOTO = new DisplayValue("0x00000002");

		public static final DisplayValue THREE_PHOTOS = new DisplayValue("0x00000004");

		public static final DisplayValue BANNER = new DisplayValue("0x00000008");

		public static final DisplayValue MANY_PHOTOS = new DisplayValue("0x00000010");

		public static final DisplayValue WATERFALL = new DisplayValue("0x00000020");

		public static final DisplayValue PHOTO_SET = new DisplayValue("0x00000040");

		public static final DisplayValue BANNER_CROUSEL = new DisplayValue("0x00000080");

		public static final DisplayValue BIG_PHOTO = new DisplayValue("0x00000400");

		public static final DisplayValue GIF = new DisplayValue("0x00000800");

		public static final DisplayValue VEDIO = new DisplayValue("0x00001000");
		
		public static final DisplayValue OPENNING = new DisplayValue("0x00002000");
		
		public static final DisplayValue PIC_TEXT_BTN = new DisplayValue("0x00004000");
		
		public static final DisplayValue INTERSTITIAL = new DisplayValue("0x00008000");

		private String value = "-1";

		private DisplayValue(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return String.valueOf(value);
		}

		@Override
		public boolean equals(Object obj) {
			boolean ret = false;

			if (obj instanceof DisplayValue) {
				ret = (value == ((DisplayValue) obj).value);
			} else if (obj instanceof String) {
				ret = (value == (String) obj);
			}

			return ret;
		}
	}

	private static class DisplayValueDesc {

		public static final DisplayValueDesc UNKNOWN = new DisplayValueDesc("unknown");

		public static final DisplayValueDesc NO_PHOTO = new DisplayValueDesc("no_photo");

		public static final DisplayValueDesc ONE_PHOTO = new DisplayValueDesc("one_photo");

		public static final DisplayValueDesc THREE_PHOTOS = new DisplayValueDesc("three_photos");

		public static final DisplayValueDesc BANNER = new DisplayValueDesc("banner");

		public static final DisplayValueDesc MANY_PHOTOS = new DisplayValueDesc("many_photos");

		public static final DisplayValueDesc WATERFALL = new DisplayValueDesc("waterfall");

		public static final DisplayValueDesc PHOTO_SET = new DisplayValueDesc("photo_set");

		public static final DisplayValueDesc BANNER_CROUSEL = new DisplayValueDesc("banner_crousel");

		public static final DisplayValueDesc BIG_PHOTO = new DisplayValueDesc("big_photo");

		public static final DisplayValueDesc GIF = new DisplayValueDesc("gif");

		public static final DisplayValueDesc VEDIO = new DisplayValueDesc("vedio");
		
		public static final DisplayValueDesc OPENNING = new DisplayValueDesc("openning");
		
		public static final DisplayValueDesc PIC_TEXT_BTN = new DisplayValueDesc("pic_text_btn");
		
		public static final DisplayValueDesc INTERSTITIAL = new DisplayValueDesc("interstitial");

		private String value = null;

		private DisplayValueDesc(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}

	}

	public static Map<String, String> display_desc = new HashMap<String, String>();

	static {
		display_desc.put(DisplayValue.UNKNOWN.getValue(), DisplayValueDesc.UNKNOWN.toString());
		display_desc.put(DisplayValue.NO_PHOTO.getValue(), DisplayValueDesc.NO_PHOTO.toString());
		display_desc.put(DisplayValue.ONE_PHOTO.getValue(), DisplayValueDesc.ONE_PHOTO.toString());
		display_desc.put(DisplayValue.THREE_PHOTOS.getValue(), DisplayValueDesc.THREE_PHOTOS.toString());
		display_desc.put(DisplayValue.BANNER.getValue(), DisplayValueDesc.BANNER.toString());
		display_desc.put(DisplayValue.MANY_PHOTOS.getValue(), DisplayValueDesc.MANY_PHOTOS.toString());
		display_desc.put(DisplayValue.WATERFALL.getValue(), DisplayValueDesc.WATERFALL.toString());
		display_desc.put(DisplayValue.PHOTO_SET.getValue(), DisplayValueDesc.PHOTO_SET.toString());
		display_desc.put(DisplayValue.BANNER_CROUSEL.getValue(), DisplayValueDesc.BANNER_CROUSEL.toString());
		display_desc.put(DisplayValue.BIG_PHOTO.getValue(), DisplayValueDesc.BIG_PHOTO.toString());
		display_desc.put(DisplayValue.GIF.getValue(), DisplayValueDesc.GIF.toString());
		display_desc.put(DisplayValue.VEDIO.getValue(), DisplayValueDesc.VEDIO.toString());
		display_desc.put(DisplayValue.OPENNING.getValue(), DisplayValueDesc.OPENNING.toString());
		display_desc.put(DisplayValue.PIC_TEXT_BTN.getValue(), DisplayValueDesc.PIC_TEXT_BTN.toString());
		display_desc.put(DisplayValue.INTERSTITIAL.getValue(), DisplayValueDesc.INTERSTITIAL.toString());
	}

	public static String getDisplay(String display) {
		String ret = display_desc.get(display);
		if (ret == null) {
			ret = DisplayValueDesc.UNKNOWN.toString();
		}
		return ret;
	}

}
