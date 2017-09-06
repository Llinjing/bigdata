package com.inveno.news.reformat.constant;

public class TestNetwork {
	
	public static void main(String [] args) {
		System.out.println(Network.getNetwork(Network.NetworkValue.UNKNOWN.getValue()));
		System.out.println(Network.getNetwork(Network.NetworkValue.NETWORK_2G.getValue()));
		System.out.println(Network.getNetwork(Network.NetworkValue.WIFI.getValue()));
		System.out.println(Network.getNetwork(Network.NetworkValue.NETWORK_3G.getValue()));
		System.out.println(Network.getNetwork(Network.NetworkValue.NETWORK_4G.getValue()));
	}

}
