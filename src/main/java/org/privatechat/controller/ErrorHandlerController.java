package org.privatechat.controller;

import org.privatechat.common.JSONResponseHelper;
import org.privatechat.common.ResourceUtil;
import org.privatechat.exception.IsSameUserException;
import org.privatechat.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class ErrorHandlerController {
	private static Logger logger = LoggerFactory.getLogger(ErrorHandlerController.class);

	@RequestMapping(value = "/error", method = RequestMethod.GET, produces = "text/html")
	@ResponseBody
	public ResponseEntity<String> error() {
		return new ResponseEntity<String>(ResourceUtil.load("/static/index.html"), HttpStatus.NOT_FOUND);
	}

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