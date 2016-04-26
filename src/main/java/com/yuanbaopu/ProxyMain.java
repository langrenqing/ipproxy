package com.yuanbaopu;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerWrapper;

import com.yuanbaopu.ip.IPoolFactory;
import com.yuanbaopu.proxy.handler.DefaultHandler;
import com.yuanbaopu.proxy.handler.LimitHandler;
import com.yuanbaopu.proxy.helper.ProxyHelper;

public class ProxyMain {
	
	public static void appStart() throws Exception {
		Server server = new Server();
		ServerConnector connector = new ServerConnector(server);
		connector.setPort(ProxyHelper.getPropertyInt("server.port"));
		server.addConnector(connector);
		
		IPoolFactory.init();
		
		// Setup proxy handler to handle CONNECT methods
		Handler proxy = new DefaultHandler();
		HandlerWrapper hander = new LimitHandler(ProxyHelper.getPropertyInt("proxy.limit"));
		hander.setHandler(proxy);
		server.setHandler(hander);
		server.start(); 
	}
	
	public static void main(String[] args) throws Exception {  
	    appStart();  
	} 
}
