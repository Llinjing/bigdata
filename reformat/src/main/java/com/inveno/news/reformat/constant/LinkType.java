package com.inveno.news.reformat.constant;

import java.util.HashMap;
import java.util.Map;


public class LinkType {
	
	public static class LinkTypeValue {
	
		public static final LinkTypeValue UNKNOWN = new LinkTypeValue(-1);
		
		public static final LinkTypeValue WEBVIEW = new LinkTypeValue(0x01);
		
		public static final LinkTypeValue NATIVE = new LinkTypeValue(0x02);
		
		public static final LinkTypeValue DOUBLE_CLICK = new LinkTypeValue(0x04);
		
		public static final LinkTypeValue BROWSER = new LinkTypeValue(0x08);
		
		public static final LinkTypeValue SPECIAL_TOPIC = new LinkTypeValue(0x10);
		
		public static final LinkTypeValue NO_ACTION = new LinkTypeValue(0x20);
		
		public static final LinkTypeValue LISTPAGE_VEDIO = new LinkTypeValue(0x40);
		
		public static final LinkTypeValue DETAIL_PAGE_VEDIO = new LinkTypeValue(0x80);
		
		public static final LinkTypeValue ORIGINAL_LINK = new LinkTypeValue(0x100);
		
		public static final LinkTypeValue APP_DOWNLOAD_INSTALL_ACTIVATE = new LinkTypeValue(0x200);
		
		public static final LinkTypeValue PHONE_CALL = new LinkTypeValue(0x400);
		
		public static final LinkTypeValue SMS = new LinkTypeValue(0x800);
		
		public static final LinkTypeValue SMART_VIEW = new LinkTypeValue(0x1000);
		
		public static final LinkTypeValue H5_NATIVE = new LinkTypeValue(0x2000);
		
		private int value = -1;
		
		private LinkTypeValue(int value) {
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
			
			if (obj instanceof LinkTypeValue) {
				ret = (value == ((LinkTypeValue)obj).value);
			} else if (obj instanceof Integer) {
				ret = (value == (Integer)obj);
			}
			
			return ret;
		}
	}
	
	private static class LinkTypeValueDesc {
		
		public static final LinkTypeValueDesc UNKNOWN = new LinkTypeValueDesc("unknown");
		
		public static final LinkTypeValueDesc WEBVIEW = new LinkTypeValueDesc("webview");
		
		public static final LinkTypeValueDesc NATIVE = new LinkTypeValueDesc("native");
		
		public static final LinkTypeValueDesc DOUBLE_CLICK = new LinkTypeValueDesc("double_click");
		
		public static final LinkTypeValueDesc BROWSER = new LinkTypeValueDesc("browser");
		
		public static final LinkTypeValueDesc SPECIAL_TOPIC = new LinkTypeValueDesc("special_topic");
		
		public static final LinkTypeValueDesc NO_ACTION = new LinkTypeValueDesc("no_action");
		
		public static final LinkTypeValueDesc LISTPAGE_VEDIO = new LinkTypeValueDesc("listpage_vedio");
		
		public static final LinkTypeValueDesc DETAIL_PAGE_VEDIO = new LinkTypeValueDesc("detail_page_vedio");
		
		public static final LinkTypeValueDesc ORIGINAL_LINK = new LinkTypeValueDesc("original_link");
		
		public static final LinkTypeValueDesc APP_DOWNLOAD_INSTALL_ACTIVATE = new LinkTypeValueDesc("app_download_install_activate");
		
		public static final LinkTypeValueDesc PHONE_CALL = new LinkTypeValueDesc("phone_call");
		
		public static final LinkTypeValueDesc SMS = new LinkTypeValueDesc("sms");
		
		public static final LinkTypeValueDesc SMART_VIEW = new LinkTypeValueDesc("smart_view");
		
		public static final LinkTypeValueDesc H5_NATIVE = new LinkTypeValueDesc("h5_native");
		
		private String value = null;
		
		private LinkTypeValueDesc(String value) {
			this.value = value;
		}
		
		@Override
		public String toString() {
			return value;
		}
		
	}
	
	public static Map<Integer, String> link_type_desc = new HashMap<Integer, String>();
	
	static {
		link_type_desc.put(LinkTypeValue.UNKNOWN.getValue(), LinkTypeValueDesc.UNKNOWN.toString());
		link_type_desc.put(LinkTypeValue.WEBVIEW.getValue(), LinkTypeValueDesc.WEBVIEW.toString());
		link_type_desc.put(LinkTypeValue.NATIVE.getValue(), LinkTypeValueDesc.NATIVE.toString());
		link_type_desc.put(LinkTypeValue.DOUBLE_CLICK.getValue(), LinkTypeValueDesc.DOUBLE_CLICK.toString());
		link_type_desc.put(LinkTypeValue.BROWSER.getValue(), LinkTypeValueDesc.BROWSER.toString());
		link_type_desc.put(LinkTypeValue.SPECIAL_TOPIC.getValue(), LinkTypeValueDesc.SPECIAL_TOPIC.toString());
		link_type_desc.put(LinkTypeValue.NO_ACTION.getValue(), LinkTypeValueDesc.NO_ACTION.toString());
		link_type_desc.put(LinkTypeValue.LISTPAGE_VEDIO.getValue(), LinkTypeValueDesc.LISTPAGE_VEDIO.toString());
		link_type_desc.put(LinkTypeValue.DETAIL_PAGE_VEDIO.getValue(), LinkTypeValueDesc.DETAIL_PAGE_VEDIO.toString());
		link_type_desc.put(LinkTypeValue.ORIGINAL_LINK.getValue(), LinkTypeValueDesc.ORIGINAL_LINK.toString());
		link_type_desc.put(LinkTypeValue.APP_DOWNLOAD_INSTALL_ACTIVATE.getValue(), LinkTypeValueDesc.APP_DOWNLOAD_INSTALL_ACTIVATE.toString());
		link_type_desc.put(LinkTypeValue.PHONE_CALL.getValue(), LinkTypeValueDesc.PHONE_CALL.toString());
		link_type_desc.put(LinkTypeValue.SMS.getValue(), LinkTypeValueDesc.SMS.toString());
		link_type_desc.put(LinkTypeValue.SMART_VIEW.getValue(), LinkTypeValueDesc.SMART_VIEW.toString());
		link_type_desc.put(LinkTypeValue.H5_NATIVE.getValue(), LinkTypeValueDesc.H5_NATIVE.toString());
	}
	
	public static String getLinkType(String link_type_hex) {
		int link_type = LinkTypeValue.UNKNOWN.getValue();
		try {
			link_type = Integer.valueOf(link_type_hex.substring(2)).intValue();
		} catch (Exception e) {
		}
		return getLinkType(link_type);
	}
	
	public static String getLinkType(int link_type) {
		String ret = link_type_desc.get(link_type);
		if (ret == null) {
			ret = LinkTypeValueDesc.UNKNOWN.toString();
		}
		return ret;
	}

}
