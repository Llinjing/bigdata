package com.inveno.news.reformat.constant;

public class TestDisplay {
	
	public static void main(String [] args) {
		System.out.println(Display.getDisplay(Display.DisplayValue.UNKNOWN.getValue()));
		System.out.println(Display.getDisplay(Display.DisplayValue.BANNER.getValue()));
		System.out.println(Display.getDisplay(Display.DisplayValue.BIG_PHOTO.getValue()));
		System.out.println(Display.getDisplay(Display.DisplayValue.NO_PHOTO.getValue()));
		System.out.println(Display.getDisplay(Display.DisplayValue.THREE_PHOTOS.getValue()));
	}

}
