package com.yuanbaopu.util;

import java.io.File;
import java.io.IOException;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

public class LogbackUtil {
	
	private final static String LOGNAME = "logback.xml";
	private final static String CONFIG = "prop.config";
	
	public static void init() {
		if(getConfigPath() != null) { // 从classpath之外加载logback.xml
			try {
				load(getConfigPath() + File.separator + LOGNAME);
			} catch (IOException | JoranException e) {
			}
		} else {
			
		}
	}
	
	private static String getConfigPath() {
		String configPath = System.getProperty(CONFIG);
        if (configPath == null || "".equals(configPath)) {
        	configPath = null;
        }
        return configPath;
	}

	public static void load(String externalConfigFileLocation)
			throws IOException, JoranException {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

		File externalConfigFile = new File(externalConfigFileLocation);
		if (!externalConfigFile.exists()) {
			throw new IOException(
					"Logback External Config File Parameter does not reference a file that exists");
		} else {
			if (!externalConfigFile.isFile()) {
				throw new IOException(
						"Logback External Config File Parameter exists, but does not reference a file");
			} else {
				if (!externalConfigFile.canRead()) {
					throw new IOException(
							"Logback External Config File exists and is a file, but cannot be read.");
				} else {
					JoranConfigurator configurator = new JoranConfigurator();
					configurator.setContext(lc);
					lc.reset();
					configurator.doConfigure(externalConfigFileLocation);
					StatusPrinter.printInCaseOfErrorsOrWarnings(lc);
				}
			}
		}
	}

}
