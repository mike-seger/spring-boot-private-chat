package org.privatechat.shared.error;

import java.io.InputStream;
import java.util.Scanner;

import org.privatechat.shared.exceptions.ValidationException;
import org.privatechat.shared.http.JSONResponseHelper;
import org.privatechat.shared.interfaces.IErrorHandlerController;
import org.privatechat.user.exceptions.IsSameUserException;
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
public class ErrorHandlerController implements IErrorHandlerController {
	private static Logger logger = LoggerFactory.getLogger(ErrorHandlerController.class);

	@RequestMapping(value = "/error", method = RequestMethod.GET, produces = "text/html")
	@ResponseBody
	public ResponseEntity<String> error() {
		return new ResponseEntity<String>(routeToIndexFallBack(), HttpStatus.NOT_FOUND);
	}

	private String routeToIndexFallBack() {
		String location = "/static/index.html";
		try (InputStream is = getClass().getResourceAsStream(location); Scanner scanner = new Scanner(is)) {
				scanner.useDelimiter("\\Z");
				return scanner.next();
		} catch (Exception e) {
			logger.error("Error occurred loading {}", location, e);
			return "Error loading: " + location;
		}
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> exception(Exception exception) {
		logger.error("Error occurred", exception);
		if (isExceptionInWhiteList(exception)) {
			return JSONResponseHelper.createResponse(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return JSONResponseHelper.createResponse("Error. Contact your administrator", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// There's no way to iterate over exceptions with the 'instanceof' operator
	private Boolean isExceptionInWhiteList(Exception exception) {
		if (exception instanceof IsSameUserException)
			return true;
		if (exception instanceof ValidationException)
			return true;
		// TODO: if (exception instanceof UserNotFoundException) return true;

		return false;
	}
}