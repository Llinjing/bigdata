package com.inveno.news.reformat.convert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.inveno.news.reformat.convert.AbstractConverter;


public class TestAbstractConverter extends AbstractConverter {

	@Override
	public Map<String, List<String>> convert(String str) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		List<String> list = new ArrayList<String>();
		list.add(str);
		map.put("test", list);
		return map;
	}

	@Override
	public Object clone() {
		return new TestAbstractConverter();
	}
	
	@Override
	public boolean init() {
		return true;
	}
	
	@Override
	public void uninit() {
		
	}
	
	public static void main(String [] args) {
		TestAbstractConverter converter = new TestAbstractConverter();
		Map<String, List<String>> ret = converter.convert("test, abcd");
		for (String key : ret.keySet()) {
			System.out.println(key);
			for (String s : ret.get(key)) {
				System.out.println(s);
			}
		}
	}

}
