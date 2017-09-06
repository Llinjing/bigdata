package com.inveno.bigdata.query.data.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * 
 * @author huanghaifeng
 * $JAVA_HOME/bin/java -Djava.ext.dirs=./ com.inveno.bigdata.query.data.util.LookingForUnuseImpression result.data > output_1
 */

public class LookingForUnuseImpression {

	public static void main(String[] args) {

		try {
			FileReader reader = new FileReader(args[0]);
			BufferedReader br = new BufferedReader(reader);
			String str = null;
			StringBuffer sb = new StringBuffer();

			while ((str = br.readLine()) != null) {
				String[] str_arr = str.split("\t+|\\s+");
				int length = str_arr.length;

				if (length > 2) {
					boolean bfilter = false;
					int count = 0;
					String uid = str_arr[0];
					for (int i = 1; i < length; i++) {
						if (str_arr[i].contains("click")) {
							bfilter = true;
							count = 0;
							sb = new StringBuffer();
						} else if (str_arr[i].contains("impression")) {
							if (bfilter && count < 4) {
								sb.append(uid + "\t" + str_arr[i] + "\n");
								count++;
							}
						} else if (str_arr[i].contains("event")) {
							bfilter = false;
							sb = new StringBuffer();
						} else if (str_arr[i].contains("dwelltime")) {
							bfilter = false;
							if (sb.length() > 0) {
								System.out.println(sb.toString().trim());
								sb = new StringBuffer();
							}
						}
					}
				}
			}
			br.close();
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}