package com.inveno.bigdata.lockscreen.acs;

import java.util.List;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.panhongan.util.control.Lifecycleable;
import com.inveno.feeder.thrift.AcsService;
import com.inveno.feeder.thrift.Status;
import com.inveno.feeder.thrift.SysType;

public class AcsClient implements Lifecycleable {

	private static final String CLASS_NAME = AcsClient.class.getSimpleName();
	
	private static final Logger logger = LoggerFactory.getLogger(CLASS_NAME);

	private TTransport transport = null;
	
	private AcsService.Client client = null;
	
	private String server = null;
	
	private int port = -1;
	
	public AcsClient(String server, int port) {
		this.server = server;
		this.port = port;
	}
	
	@Override
	public boolean init() {
		boolean ret = false;
		
		try {  
            transport = new TFramedTransport(new TSocket(server, port, 5 * 1000));
            TProtocol protocol = new TBinaryProtocol(transport);  
            client = new AcsService.Client(protocol);  
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
	
	public void reset() {
		this.uninit();
		this.init();
	}
	
	public Status checkAndInsert(SysType type, String key, int timeout_usec) {
		Status status = null;
		
		try {
			if (client != null) {
				status = client.checkAndInsert(type, key, timeout_usec);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		}
		
		return status;
	}

    public List<Status> checkAndInsertMul(SysType type, List<String> keys, int timeout_usec) {
    	List<Status> status_list = null;
    	
    	try {
    		if (client != null) {
    			status_list = client.checkAndInsertMul(type, keys, timeout_usec);
    		}
    	} catch (Exception e) {
    		logger.warn(e.getMessage(), e);
    	}
    	
    	return status_list;
    }

    public List<Status> existMul(SysType type, List<String> keys, int timeout_usec) {
    	List<Status> status_list = null;
    	
    	try {
    		if (client != null) {
    			status_list = client.existMul(type, keys, timeout_usec);
    		}
    	} catch (Exception e) {
    		logger.warn(e.getMessage(), e);
    	}
    	
    	return status_list;
    }

}
