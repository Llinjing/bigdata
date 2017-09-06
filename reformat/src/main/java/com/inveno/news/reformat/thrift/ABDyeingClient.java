package com.inveno.news.reformat.thrift;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.panhongan.util.control.Lifecycleable;


public class ABDyeingClient implements Lifecycleable {
	
	private static final String CLASS_NAME = ABDyeingClient.class.getSimpleName();
	
	private static final Logger logger = LoggerFactory.getLogger(CLASS_NAME);
	
	private TTransport transport = null;
	
	private ABDyeingService.Client client = null;
	
	private String server = null;
	
	private int port = -1;
	
	private List<String> _types = new ArrayList<String>();
	
	public ABDyeingClient(String server, int port) {
		this.server = server;
		this.port = port;
		_types.clear();
		_types.add("ad");
	    _types.add("news");
	    _types.add("biz");
	}
	
	@Override
	public boolean init() {
		boolean ret = false;
		
		try {  
            transport = new TFramedTransport(new TSocket(server, port, 5 * 1000));
            TProtocol protocol = new TBinaryProtocol(transport);  
            client = new ABDyeingService.Client(protocol);  
            transport.open();
            logger.warn("ABDyeingClient init ok");
            
            ret = true;
        } catch (Exception e) {
        	logger.warn("ABDyeingClient init failed");
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
	
	public synchronized Map<String, String> getUpack(String uid, String product_id, String app_ver, String language, String platform) {
	    Map<String, String> config_map = new HashMap<String, String>();
	    
	    // try connect again
	    if (client==null || transport==null) {
	    	logger.warn("client or transport is null, init again");
	    	this.init();
	    }
	    
		if (client != null) {
			try {
				ABDyeingReply upack = null;
				/**
				 * struct ABDyeingLanRequest {
					    1: required string app,             // 渠道名 
					    2: required string app_ver,         // 渠道版本
					    3: required string app_lan,         // 语言
					    4: required list<string> _types,    // 请求的业务类型列表
					    5: optional string app_platform,    // ios or android
				   }
				*/
				ABDyeingLanRequest req = new ABDyeingLanRequest(product_id, app_ver, language, _types, platform);
				logger.warn("ABDyeingClient query upack, types size = {}", _types.size());
				
				upack = client.DyeingLan(uid, req);

				if (upack.status.equals(ab_status.RC_STATUS_OK)) {
					List<ABDyeingEntry> upack_data_list = upack._entries;
					
					Iterator<ABDyeingEntry> it = upack_data_list.iterator();
					while (it.hasNext()) {
						ABDyeingEntry config_tmp = it.next();
						config_map.put(config_tmp.getType()+"_configid", config_tmp.getConfig_id());
					}
					
				} else {
					logger.warn("query upack failed, server.status = {}", upack.status);
				}
			} catch (Exception e) {
				logger.warn(e.getMessage(), e);
				logger.warn("query upack failed, uid = {}, product_id = {}, app_ver = {}, language = {}, platform = {}", uid, product_id, app_ver, language, platform);
				
				this.uninit();
			}	
		}
		
		return config_map;
	}

}
