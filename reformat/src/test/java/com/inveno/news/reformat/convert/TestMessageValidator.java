package com.inveno.news.reformat.convert;

public class TestMessageValidator {
	
	public static void main(String [] args) {
		String [] apps = {"coolpad", "+1344", "ab-cd", "fadde-sdf_468", "ab:cd:ef", "#"};
		for (int i = 0; i < apps.length; ++i) {
			System.out.println(apps[i].matches(MessageValidator.APP_REGEX));
		}
		
		String [] uids = {"adadf", "+1344", "ab-cdd", "fadde-sdf_468", "ab:cd:ef:hg", "#445", "abc,", "7*9"};
		for (int i = 0; i < apps.length; ++i) {
			System.out.println(uids[i].matches(MessageValidator.UID_REGEX));
		}
		
		System.out.println(MessageValidator.validate("1919weowe", "emui", "123456", "abc"));
	}

}
