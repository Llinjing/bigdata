package com.inveno.bigdata.sync.convert;

import org.apache.commons.codec.binary.Base64;

public class TestBase64 {
	
	public static void main(String [] args) {
		Base64 base64 = new Base64();
		System.out.println(new String(base64.decode("eyJzdHJhdGVneSI6IjgiLCJjb250ZW50X3R5cGUiOiIweDAwMDAwMDAxIiwic291cmNlIjoi56m/5biu572RIiwiZGlzcGxheV90eXBlIjoiMHgwMDAwMDAwNCJ9")));
	}

}
