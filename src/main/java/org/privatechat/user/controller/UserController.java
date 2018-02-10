package org.privatechat.user.controller;

import java.security.Principal;
import java.util.List;

import org.privatechat.common.JSONResponseHelper;
import org.privatechat.dto.RegistrationDto;
import org.privatechat.dto.UserDto;
import org.privatechat.exception.UserNotFoundException;
import org.privatechat.exception.ValidationException;
import org.privatechat.model.User;
import org.privatechat.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/api/user/register", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public ResponseEntity<String> register(@RequestBody RegistrationDto registeringUser) throws ValidationException {
		userService.addUser(registeringUser);

		return JSONResponseHelper.createResponse("", HttpStatus.OK);
	}

	// TODO: actually implement concept of a "friendslist"
	@RequestMapping(value = "/api/user/requesting/friendslist", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> retrieveRequestingUserFriendsList(Principal principal) throws UserNotFoundException {
		User requestingUser = userService.getUser(SecurityContextHolder.getContext());
		List<UserDto> friendslistUsers = userService.retrieveFriendsList(requestingUser);

		return JSONResponseHelper.createResponse(friendslistUsers, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/user/requesting/info", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> retrieveRequestUserInfo() throws UserNotFoundException {
		User requestingUser = userService.getUser(SecurityContextHolder.getContext());
		UserDto userDetails = userService.retrieveUserInfo(requestingUser);

		return JSONResponseHelper.createResponse(userDetails, HttpStatus.OK);
	}
}