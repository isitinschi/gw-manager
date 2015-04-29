package com.guesswhat.manager.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {

	public static String getProperty(String name) {
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("src/main/resources/server.properties");
			prop.load(input);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return prop.getProperty(name);
	}
}
