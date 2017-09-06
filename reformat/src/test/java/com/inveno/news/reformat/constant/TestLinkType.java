package com.inveno.news.reformat.constant;

public class TestLinkType {
	
	public static void main(String [] args) {
		System.out.println(LinkType.getLinkType(LinkType.LinkTypeValue.UNKNOWN.getValue()));
		System.out.println(LinkType.getLinkType(LinkType.LinkTypeValue.DETAIL_PAGE_VEDIO.getValue()));
		System.out.println(LinkType.getLinkType(LinkType.LinkTypeValue.LISTPAGE_VEDIO.getValue()));
		System.out.println(LinkType.getLinkType(LinkType.LinkTypeValue.SPECIAL_TOPIC.getValue()));
		System.out.println(LinkType.getLinkType(LinkType.LinkTypeValue.NATIVE.getValue()));
		System.out.println(LinkType.getLinkType(LinkType.LinkTypeValue.SMS.getValue()));
	}

}
