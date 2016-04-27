package com.yuanbaopu;

import com.yuanbaopu.ip.IPoolFactory;
import com.yuanbaopu.proxy.ProxyStart;
import com.yuanbaopu.util.LogbackUtil;

public class ProxyMain {
	
	public static void appStart() throws Exception {
		LogbackUtil.init();
		IPoolFactory.init();
		ProxyStart.start();
	}
	
	public static void main(String[] args) throws Exception {  
	    appStart();  
	} 
}
