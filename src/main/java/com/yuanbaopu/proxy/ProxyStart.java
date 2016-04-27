package com.yuanbaopu.proxy;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerWrapper;

import com.yuanbaopu.proxy.handler.DefaultHandler;
import com.yuanbaopu.proxy.handler.LimitHandler;
import com.yuanbaopu.proxy.helper.ProxyHelper;
import com.yuanbaopu.proxy.helper.ZkHelper;

public class ProxyStart {

	public static void start() throws Exception {
		Server server = new Server();
		ServerConnector connector = new ServerConnector(server);
		connector.setPort(ProxyHelper.getInt("server.port"));
		server.addConnector(connector);
		
		// Setup proxy handler to handle CONNECT methods
		Handler proxy = new DefaultHandler();
		HandlerWrapper hander = new LimitHandler(ProxyHelper.getInt("proxy.limit"));
		hander.setHandler(proxy);
		server.setHandler(hander);
		server.start(); 
		
		if(ProxyHelper.getBoolean("proxy.distributed")) {
			ZkHelper.asWorker(getLocalIP() + ":" + ProxyHelper.getInt("server.port"));
		}
	}
	
	private static String getLocalIP() {
		InetAddress addr;
		try {
			addr = InetAddress.getLocalHost();
			return addr.getHostAddress().toString();//获得本机IP
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return "127.0.0.1";
	}
	
}
