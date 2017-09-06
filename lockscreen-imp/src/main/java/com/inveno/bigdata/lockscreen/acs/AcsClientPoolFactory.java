package com.inveno.bigdata.lockscreen.acs;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import com.github.panhongan.util.conf.Config;
import com.inveno.feeder.thrift.AcsService;

public class AcsClientPoolFactory extends BasePooledObjectFactory<TServiceClient> {

	private String host;

	private int port;

	private int timeout;

	public AcsClientPoolFactory(Config config){
		String[] acs_arr = config.getString("acs.server").split(":");

		this.host = acs_arr[0];
		this.port = Integer.valueOf(acs_arr[1]).intValue();
		this.timeout = config.getInt("acs.timeout.ms", 2000);
	}
	
	@Override
	public TServiceClient create() throws Exception {
		TTransport transport = new TSocket(host, port, timeout);
		TProtocol protocol = new TBinaryProtocol(new TFramedTransport(transport));
		transport.open();
		AcsService.Client client = new AcsService.Client(protocol);
		return client;
	}

	@Override
	public void destroyObject(PooledObject<TServiceClient> p) throws Exception {
		TServiceClient client = p.getObject();
		TTransport transport = client.getInputProtocol().getTransport();
		transport.close();
	}

	@Override
	public boolean validateObject(PooledObject<TServiceClient> p) {
		TServiceClient client = p.getObject();
		TTransport transport = client.getInputProtocol().getTransport();
		return transport.isOpen();
	}

	@Override
	public PooledObject<TServiceClient> wrap(TServiceClient arg0) {
		return new DefaultPooledObject<TServiceClient>(arg0);
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

}

// private ObjectPool<TServiceClient> thriftPoool;
// factory = new ClientPooledThreadFactory(host, port, timeout);
// poolConfig = new GenericObjectPoolConfig();
// thriftPool = new GenericObjectPool(factory, poolConfig);
// client = thriftPool.borrowObject();
