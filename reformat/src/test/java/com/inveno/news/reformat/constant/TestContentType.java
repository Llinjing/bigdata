package com.inveno.news.reformat.constant;

public class TestContentType {
	
	public static void main(String [] args) {
		System.out.println(ContentType.getContentType(ContentType.ContentTypeValue.UNKNOWN.getValue()));
		System.out.println(ContentType.getContentType(ContentType.ContentTypeValue.ADVERTISEMENT_HARD.getValue()));
		System.out.println(ContentType.getContentType(ContentType.ContentTypeValue.ADVERTISEMENT_SOFT.getValue()));
		System.out.println(ContentType.getContentType(ContentType.ContentTypeValue.NEWS.getValue()));
		System.out.println(ContentType.getContentType(ContentType.ContentTypeValue.SHORT_VEDIO.getValue()));
	}

}
