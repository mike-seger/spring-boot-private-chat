package org.privatechat.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.google.gson.GsonBuilder;

public class JSONResponseHelper {
	public static <T> ResponseEntity<String> createResponse(T responseObj, HttpStatus stat) {
		return new ResponseEntity<String>(
				new GsonBuilder().disableHtmlEscaping().create().toJson(responseObj).toString(), stat);
	}
}