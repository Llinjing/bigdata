package com.inveno.bigdata.lockscreen.tools;

import java.util.ArrayList;
import java.util.List;

import com.github.panhongan.util.FileUtil;
import com.inveno.bigdata.lockscreen.acs.AcsClient;
import com.inveno.feeder.thrift.Status;
import com.inveno.feeder.thrift.SysType;

public class WriteKeys {
	
	public static void usage() {
		System.out.println("WriteKeys <key_file> <acs_server> <acs_port>");
	}
	
	public static void write(AcsClient client, List<String> list) {
		List<Status> status_list = client.checkAndInsertMul(SysType.USER_READ, list, 2000);
		if (status_list != null) {
			for (Status status : status_list) {
				System.out.println(status);
			}
		} else {
			System.err.println("status_list is null");
		}
	}

	public static void main(String[] args) {
		if (args.length != 3) {
			usage();
			return;
		}
		
		List<String> key_list = FileUtil.readFile(args[0]);
		String server = args[1];
		int port = Integer.valueOf(args[2]);
		
		AcsClient client = new AcsClient(server, port);
		if (client.init()) {
			System.out.println("AcsClient init ok");
			
			List<String> subset = new ArrayList<String>();
			
			for (String key : key_list) {	
				subset.add(key);
				if (subset.size() == 100) {
					write(client, subset);
					subset.clear();
				}
			}
			
			if (!subset.isEmpty()) {
				write(client, subset);
			}
		} else {
			System.err.println("AcsClient init failed");
		}
		
		client.uninit();
	}

}
