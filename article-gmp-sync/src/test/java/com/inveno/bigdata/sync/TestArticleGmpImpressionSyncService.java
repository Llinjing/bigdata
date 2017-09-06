package com.inveno.bigdata.sync;

import com.github.panhongan.util.hash.SimplePartitioner;

public class TestArticleGmpImpressionSyncService {

	public static void main(String[] args) {
		String line = "(1041932727,[{\"noticias\":{\"impression\":510,\"click\":1.0231545,\"ctr\":0.031444725}},{\"-1\":{\"impression\":510,\"click\":1.0231545,\"ctr\":0.031444725}}]),1";
		line = line.replace("(", "");
	    line = line.replace(")", "");
		String [] arr = line.split(",");
		System.out.println(arr.length);
		for (String a : arr){
			System.out.println(a);
		}
		
		String hash_key = "article-gmp-spark-streaming-hash-key-" + SimplePartitioner.partition("1043679307", 100);
		System.out.println(hash_key);
	}

}
