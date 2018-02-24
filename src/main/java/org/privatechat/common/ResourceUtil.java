package org.privatechat.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.springframework.web.client.ResourceAccessException;

public class ResourceUtil {
	public static String load(String location) {
		try (InputStream is = ResourceUtil.class.getResourceAsStream(location)) {
			if (is == null) {
				throw new IOException();
			}
			try (Scanner scanner = new Scanner(is)) {
				scanner.useDelimiter("\\Z");
				return scanner.next();
			}
		} catch (IOException e) {
			throw new ResourceAccessException("Cannot access resource at location: " + location, e);
		}
	}
}
