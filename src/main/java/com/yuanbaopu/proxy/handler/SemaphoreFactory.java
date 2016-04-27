package com.yuanbaopu.proxy.handler;

import com.yuanbaopu.proxy.helper.ProxyHelper;

public class SemaphoreFactory {
	
	public static ProxySemaphore createSemaphore(int limit) {
		if(ProxyHelper.getBoolean("proxy.distributed")) {
			return new ZKSemaphore(limit);
		}
		return new JDKSemaphore(limit);
	}
}
