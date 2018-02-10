package org.privatechat.common;

import java.io.InputStream;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceUtil {
	private static Logger logger = LoggerFactory.getLogger(ResourceUtil.class);

	public static String load(String location) {
		try (
				InputStream is = ResourceUtil.class.getResourceAsStream(location); 
				Scanner scanner = new Scanner(is)) {
			scanner.useDelimiter("\\Z");
			return scanner.next();
		} catch (Exception e) {
			logger.error("Error occurred loading {}", location, e);
			return "Error loading: " + location;
		}
	}

}
