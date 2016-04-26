package com.yuanbaopu.proxy.helper;

import java.util.Properties;

import com.yuanbaopu.util.PropertiesUtil;

public class ProxyHelper {
	
	private static ProxyHelper ph;
	
	private ProxyHelper() {}
	
	private Properties ps;

	private final static String PROPERTIESNAME = "application.properties";
	
	public static ProxyHelper getInstance() {
		synchronized(ProxyHelper.class) {
			if(ph == null) {
				ph = new ProxyHelper();
			}
			ph.loadFromClassPath();
		}
		return ph;
	}
	
	
	private void loadFromClassPath() {
		try {
			ps = new PropertiesUtil().loadFromClassPath(PROPERTIESNAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String doGetProperty(String key) {
		return ps.getProperty(key);
	}
	
	public static String getProperty(String key) {
		return ProxyHelper.getInstance().doGetProperty(key);
	}
	
	public static int getPropertyInt(String key) {
		return Integer.valueOf(ProxyHelper.getInstance().doGetProperty(key));
	}
	
}
