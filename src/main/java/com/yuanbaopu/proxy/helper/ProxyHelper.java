package com.yuanbaopu.proxy.helper;

import java.io.File;
import java.util.Properties;

import com.yuanbaopu.util.PropertiesUtil;

public class ProxyHelper {
	
	private static ProxyHelper ph;
	
	private ProxyHelper() {}
	
	private Properties ps;

	private final static String PROPERTIESNAME = "application.properties";
	private final static String CONFIG = "prop.config";
	
	public static ProxyHelper getInstance() {
		synchronized(ProxyHelper.class) {
			if(ph == null) {
				ph = new ProxyHelper();
				ph.loadFromPath();
			}
		}
		return ph;
	}
	
	/**
	 * 从classpath 或者外部配置路径位置加载配置文件
	 */
	private void loadFromPath() {
		try {
			if(getConfigPath() != null) {
				System.out.println(getConfigPath() + File.separator + PROPERTIESNAME);
				ps = new PropertiesUtil().loadFromPath(getConfigPath() + File.separator + PROPERTIESNAME);
			} else {
				ps = new PropertiesUtil().loadFromClassPath(PROPERTIESNAME);
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("ERROR:获取配置文件失败");
		}
	}
	
	private String getConfigPath() {
		String configPath = System.getProperty(CONFIG);
		System.out.println("configPath:" + configPath);
        if (configPath == null || "".equals(configPath)) {
        	configPath = null;
        }
        return configPath;
	}
	
	private String doGetProperty(String key) {
		return ps.getProperty(key);
	}
	
	public static String getString(String key) {
		return ProxyHelper.getInstance().doGetProperty(key);
	}
	
	public static boolean getBoolean(String key) {
		return Boolean.valueOf(ProxyHelper.getInstance().doGetProperty(key));
	}
	
	public static int getInt(String key) {
		return Integer.valueOf(ProxyHelper.getInstance().doGetProperty(key));
	}
	
}
