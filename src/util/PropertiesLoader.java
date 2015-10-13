package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {
	

	public static Properties load(String fileName) {
		InputStream input = null;
		Properties prop = new Properties();
		try {
			 
			input = PropertiesLoader.class.getClassLoader().getResourceAsStream(fileName);
	 
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
