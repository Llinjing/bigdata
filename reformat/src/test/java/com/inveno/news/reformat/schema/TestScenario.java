package com.inveno.news.reformat.schema;

import com.inveno.news.reformat.schema.news.Scenario;

public class TestScenario {

	public static void main(String [] args) {
		Scenario scenario = new Scenario();
		scenario.setPosition_type("aa");
		scenario.setPosition("1");
		scenario.setChannel("2");
		System.out.println(scenario.toString());
		
		System.out.println(Scenario.parseScenario("hotoday", "0x01011e"));
	}
	
}
