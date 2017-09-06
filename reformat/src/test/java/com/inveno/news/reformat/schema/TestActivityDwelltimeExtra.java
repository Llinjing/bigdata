package com.inveno.news.reformat.schema;

import com.inveno.news.reformat.schema.news.ActivityDwelltimeExtra;

public class TestActivityDwelltimeExtra {

	public static void main(String [] args) {
		ActivityDwelltimeExtra extra = new ActivityDwelltimeExtra();
		extra.setDwelltime("90");
		extra.setPage_name("com.abc.def");
		System.out.println(extra.toString());
	}
}
