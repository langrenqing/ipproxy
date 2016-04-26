package com.yuanbaopu.util;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

	/**
     * Load a properties file from the classpath
     *
     * @param propertiesName the properties file to load.
     * @return a properties instance loaded with the properties from
     * the file. If no file can be found it returns an empty instance.
     * @throws Exception
     */
    public Properties loadFromClassPath(final String propertiesName)
    	throws Exception {
        Properties properties = new Properties();
        ClassLoader classLoader = this.getClass().getClassLoader();

        InputStream inputStream = null;

        try {
    	    inputStream = classLoader.getResourceAsStream(propertiesName);
            properties.load(inputStream);
        }
        finally {
            if (inputStream != null) {
            	inputStream.close();
            }
        }
        return properties;
    }
	
}
