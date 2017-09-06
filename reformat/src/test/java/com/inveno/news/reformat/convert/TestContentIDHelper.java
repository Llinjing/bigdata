package com.inveno.news.reformat.convert;

public class TestContentIDHelper {

	public static void main(String[] args) {
		System.out.println(ContentIDHelper.getContentType("14770237")); // 8
		System.out.println(ContentIDHelper.getContentType("14770237a")); // 9
		System.out.println(ContentIDHelper.getContentType("1477023")); // 7
		System.out.println(ContentIDHelper.getContentType("1477023711")); // 10
		System.out.println(ContentIDHelper.getContentType("14770237111")); // 11

		// self ad
		System.out.println(ContentIDHelper.getAdSource("1153063137948295168"));
		System.out.println(ContentIDHelper.getAdSource("1153063137135788032"));
		System.out.println(ContentIDHelper.getAdSource("1153063137135435776"));
		System.out.println(ContentIDHelper.getAdSource("1153063137135337472"));
		System.out.println(ContentIDHelper.getAdSource("1153063137135140864"));
		System.out.println(ContentIDHelper.getAdSource("1153063137134804993"));
		System.out.println(ContentIDHelper.getAdSource("1153626087086465024"));
		System.out.println(ContentIDHelper.getAdSource("1153063137132707840"));
		System.out.println(ContentIDHelper.getAdSource("1153063137132576768"));
		System.out.println(ContentIDHelper.getAdSource("1153063137131823104"));

		// taobao_ad
		System.out.println(ContentIDHelper.getAdSource("1153202983322742386"));
		System.out.println(ContentIDHelper.getAdSource("1153202980992112551"));
		System.out.println(ContentIDHelper.getAdSource("1153202981707907053"));
		System.out.println(ContentIDHelper.getAdSource("1153202982619709385"));
		System.out.println(ContentIDHelper.getAdSource("1153202983727390714"));
		System.out.println(ContentIDHelper.getAdSource("1153202982788005886"));
		System.out.println(ContentIDHelper.getAdSource("1153202982232223551"));
		System.out.println(ContentIDHelper.getAdSource("1153202981590448126"));
		System.out.println(ContentIDHelper.getAdSource("1153202980078287358"));

		// baidu ad
		System.out.println(ContentIDHelper.getAdSource("1153765933293106934"));
		System.out.println(ContentIDHelper.getAdSource("1153765931327805191"));
		System.out.println(ContentIDHelper.getAdSource("1153765932892151083"));
		System.out.println(ContentIDHelper.getAdSource("1153765933259386844"));
		System.out.println(ContentIDHelper.getAdSource("1153765933798087651"));
		System.out.println(ContentIDHelper.getAdSource("1153765933696664047"));
		System.out.println(ContentIDHelper.getAdSource("1153765932405534718"));
		System.out.println(ContentIDHelper.getAdSource("1153765933561413627"));
	}

}
