package com.yuanbaopu.ip;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.yuanbaopu.ip.mayi.MayiIP;

public class IPoolFactory {
	
	private static Map<String, IPool> pools = new ConcurrentHashMap<String, IPool>();
	
	static {
		pools.put("mayi", new MayiIP());
		//pools.put("fengyun", new FengyunIP());
	}
	
	public static IPool getIPool() {
//		if(randomInt() > 4) {
//			return pools.get("fengyun");
//		}
		return pools.get("mayi");
	}
	
//	private static int randomInt() {
//		return new Long(Math.round(Math.random() * 10)).intValue();
//	}
	
	public static void init() {}
	
}
