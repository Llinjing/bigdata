package com.inveno.news.reformat.convert;

import java.util.List;
import java.util.Map;

import com.github.panhongan.util.control.Lifecycleable;

public abstract class AbstractConverter implements Lifecycleable {
	
	public abstract Map<String, List<String>> convert(String str);
	
	public abstract Object clone();
	
}
