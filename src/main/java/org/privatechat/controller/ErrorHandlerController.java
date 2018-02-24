package org.privatechat.controller;

import org.privatechat.common.JSONResponseHelper;
import org.privatechat.exception.IsSameUserException;
import org.privatechat.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Controller
public class ErrorHandlerController {
	private static Logger logger = LoggerFactory.getLogger(ErrorHandlerController.class);

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> exception(Exception exception) {
		logger.error("Error occurred", exception);
		if (isExceptionInWhiteList(exception)) {
			return JSONResponseHelper.createResponse(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return JSONResponseHelper.createResponse("Error. Contact your administrator", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private Boolean isExceptionInWhiteList(Exception exception) {
		return exception instanceof IsSameUserException || exception instanceof ValidationException;
	}
}