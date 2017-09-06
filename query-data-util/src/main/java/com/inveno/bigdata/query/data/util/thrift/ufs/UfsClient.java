package com.inveno.bigdata.query.data.util.thrift.ufs;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.panhongan.util.control.Lifecycleable;


public class UfsClient implements Lifecycleable {

	private static final String CLASS_NAME = UfsClient.class.getSimpleName();

	private static final Logger logger = LoggerFactory.getLogger(CLASS_NAME);

	private TTransport transport = null;

	private UfsService.Client client = null;

	private String server = null;

	private int port = -1;

	public UfsClient(String server, int port) {
		this.server = server;
		this.port = port;
	}

	@Override
	public boolean init() {
		boolean ret = false;

		try {  
			transport = new TFramedTransport(new TSocket(server, port, 5 * 1000));
			TProtocol protocol = new TBinaryProtocol(transport);  
			client = new UfsService.Client(protocol);  
			transport.open();

			ret = true;
		} catch (Exception e) {  
			logger.warn(e.getMessage(), e);
		}

		return ret;
	}

	@Override
	public void uninit() {
		if (transport != null) {
			try {
				transport.close();
			} catch (Exception e) {
				logger.warn(e.getMessage(), e);
			}

			transport = null;
			client = null;
		}
	}

	public String getUserWeightedCategories(String uid, String version) {
		String query_categories = null;

		if (client != null) {

			try {
				query_categories = client.GetWeightedCategories(uid, version);
			} catch (Exception e) {
				if (query_categories == null) {
					logger.warn("no categories, uid = {}", uid);
				} else {
					logger.warn("invalid categories, uid = {}, categories = {}", uid, query_categories);
				}
			}	
		}

		return query_categories;
	}

	public String getUserWeightedTags(String uid, String version) {
		String query_tags = null;

		if (client != null) {

			try {				
				query_tags = client.GetWeightedTags(uid, version);
			} catch (Exception e) {
				if (query_tags == null) {
					logger.warn("no tags, uid = {}", uid);
				} else {
					logger.warn("invalid tags, uid = {}, tags = {}", uid, query_tags);
				}
			}	
		}

		return query_tags;
	}
	
	public String getLdaTopic(String uid, String version) {
		String lda_topic = null;

		if (client != null) {

			try {
				lda_topic = client.GetLdaTopic(uid, version);
			} catch (Exception e) {
				if (lda_topic == null) {
					logger.warn("no lda_topic, uid = {}", uid);
				} else {
					logger.warn("invalid lda_topic, uid = {}, lda_topic = {}", uid, lda_topic);
				}
			}	
		}

		return lda_topic;
	}
}
