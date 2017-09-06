package com.inveno.news.reformat.schema;

public class TestContentPacket {
	
	public static void main(String [] args) {
		ContentPacket cpack = new ContentPacket();
		System.out.println(cpack.toString());
		
		cpack.setStrategy("abc");
		System.out.println(cpack.toString());
	}

}
