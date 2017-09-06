package com.inveno.bigdata.sync.convert;

import com.github.panhongan.util.control.Lifecycleable;

public abstract class AbstractConverter implements Lifecycleable {
	
	public abstract void convert(String str);
	
	public abstract Object clone();
	
}
