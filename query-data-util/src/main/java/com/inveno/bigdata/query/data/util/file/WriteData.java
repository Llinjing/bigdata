package com.inveno.bigdata.query.data.util.file;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WriteData {

	private static final Logger logger = LoggerFactory.getLogger(WriteData.class);

	public static Object processMessage(String file_path, int msg) {
		BufferedWriter writer = null;

		try {
			writer = new BufferedWriter(new FileWriter(file_path, true));
			writer.write(Integer.valueOf(msg).toString());
			writer.newLine();
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return true;
	}

	public static Object processMessage(String file_path, String msg) {
		BufferedWriter writer = null;

		try {
			writer = new BufferedWriter(new FileWriter(file_path, true));
			writer.write(msg);
			writer.newLine();
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return true;
	}

	public static Object processMessage(String file_path, String msg, Boolean append) {
		BufferedWriter writer = null;

		try {
			writer = new BufferedWriter(new FileWriter(file_path, append));
			writer.write(msg);
			writer.newLine();
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return true;
	}

	public static Object processMessage(String file_path, Set<String> msg_list) {
		BufferedWriter writer = null;

		try {
			writer = new BufferedWriter(new FileWriter(file_path));
			for (String message : msg_list) {
				writer.write(message);
				writer.newLine();
			}
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return true;
	}

	public static Object processMessage(String file_path, Map<String, Integer> msg_map) {
		BufferedWriter writer = null;

		try {
			writer = new BufferedWriter(new FileWriter(file_path, true));

			List<Map.Entry<String, Integer>> msg_list = new ArrayList<Map.Entry<String, Integer>>(msg_map.entrySet());

			// sort
			Collections.sort(msg_list, new Comparator<Map.Entry<String, Integer>>() {
				public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
					// return (o2.getValue() - o1.getValue());
					return Integer.valueOf(o1.getKey()).compareTo(Integer.valueOf(o2.getKey()));
				}
			});

			for (Entry<String, Integer> message : msg_list) {
				writer.write(message.getKey() + " : " + message.getValue());
				writer.newLine();
			}
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return true;
	}

}
