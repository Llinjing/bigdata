package com.inveno.bigdata.query.data.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.panhongan.util.conf.Config;
import com.inveno.bigdata.query.data.util.conf.UfsConfig;
import com.inveno.bigdata.query.data.util.file.WriteData;
import com.inveno.bigdata.query.data.util.thrift.ufs.UfsClient;

public class GetUfsThriftData {

	private static final Logger logger = LoggerFactory.getLogger(GetUfsThriftData.class);

	private static final String CLASS_NAME = GetUfsThriftData.class.getName();

	private static final String INTERFACE_GETWEIGHTEDCATEGORIES = "GetWeightedCategories";
	
	private static final String INTERFACE_GETWEIGHTEDTAGS = "GetWeightedTags";
	
	private static final String INTERFACE_GETLDATOPIC = "GetLdaTopic";
	
	private static UfsConfig ufs_config = UfsConfig.getInstance();

	private static Config config = null;

	private static UfsClient ufs_client = null;
	
	public boolean init() {
		boolean ret = false;

		config = ufs_config.getConfig();
		try{
			ufs_client = new UfsClient(config.getString("ufs.thrift.server.host"), config.getInt("ufs.thrift.server.port"));
			ret = ufs_client.init();
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		}	

		return ret;
	}

	public void uninit() {
		if (ufs_client != null){
			ufs_client.uninit();
		}
	}

	public static void usage() {
		System.out.println(CLASS_NAME + " <conf_file> <interface_name> <uid> <version>");
	}

	public static void main(String[] args) {
		if (args.length < 4) {
			usage();
			return;
		}

		// config
		String conf_file = args[0];
		if (!ufs_config.parse(conf_file)) {
			logger.warn("parse conf file failed : {}", conf_file);
			return;
		}

		logger.info(ufs_config.toString());

		if (!ufs_config.isValid()) {
			logger.warn("config is invalid");
			return;
		}

		String interface_name = args[1];
		String uid = args[2];
		String version = args[3];
		
		// request reformat service
		GetUfsThriftData service = new GetUfsThriftData();
		if (service.init()) {
			logger.info("{} init ok", CLASS_NAME);
			switch (interface_name) {
			case INTERFACE_GETWEIGHTEDCATEGORIES:
				String categories = ufs_client.getUserWeightedCategories(uid, version);
				if(!categories.isEmpty()) {
					WriteData.processMessage(config.getString("local.path.weighted.categories"), categories);
				}
				break;
			case INTERFACE_GETWEIGHTEDTAGS:
				String tags = ufs_client.getUserWeightedTags(uid, version);
				if(!tags.isEmpty()) {
					WriteData.processMessage(config.getString("local.path.weighted.tags"), tags);
				}
				break;
			case INTERFACE_GETLDATOPIC:
				String lda_topic = ufs_client.getLdaTopic(uid, version);
				if(!lda_topic.isEmpty()) {
					WriteData.processMessage(config.getString("local.path.lda.topic"), lda_topic);
				}
				break;
			default:
				System.out.println("illegal interface name , GetWeightedCategories or GetWeightedTags or GetLdaTopic");
				break;
			}
			
			service.uninit();
		} else {
			logger.warn("{} init failed", CLASS_NAME);
			service.uninit();
		}

	}

}
