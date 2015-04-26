package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesLoader {
	

	public static Properties load(String fileName) {
		FileInputStream input = null;
		Properties prop = new Properties();
		try {
			 
			input = new FileInputStream("config.properties");
	 
			// load a properties file
			prop.load(input);
	 	 
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
					return prop;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
