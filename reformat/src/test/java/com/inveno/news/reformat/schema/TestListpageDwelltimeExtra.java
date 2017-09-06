package com.inveno.news.reformat.schema;

import com.inveno.news.reformat.schema.news.ListpageDwelltimeExtra;

public class TestListpageDwelltimeExtra {
	
	public static void main(String [] args) {
		ListpageDwelltimeExtra extra = new ListpageDwelltimeExtra();
		extra.setDwelltime("123456");
		System.out.println(extra.toString());
	}

}
