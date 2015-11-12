package org.bpim.transformer.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.bpim.model.base.v1.ObjectFactory;
import org.bpim.model.base.v1.Server;

public class MetaDataHelper {
	
	public static Server createServer(){
		ObjectFactory objectFactory = new ObjectFactory(); 
		Server server = objectFactory.createServer();
		try {
			server.setAddress(InetAddress.getLocalHost().getHostAddress());
			server.setName(InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException e) {
			
			e.printStackTrace();
		}
		
		return server;
		
	}

}
