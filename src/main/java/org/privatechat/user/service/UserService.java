package org.privatechat.user.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.privatechat.dto.Notification;
import org.privatechat.dto.Registration;
import org.privatechat.dto.UserInfo;
import org.privatechat.exception.UserNotFoundException;
import org.privatechat.exception.ValidationException;
import org.privatechat.model.User;
import org.privatechat.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserService implements UserDetailsService {
	private static Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	public User getUser(long userId) throws BeansException, UserNotFoundException {
		return userRepository.findById(userId);
	}

	public User getUser(String userEmail) throws BeansException, UserNotFoundException {
		return userRepository.findByEmail(userEmail);
	}

	public User getUser(SecurityContext userSecurityContext) throws BeansException, UserNotFoundException {
		org.springframework.security.core.userdetails.User userFromSecurityContext;
		userFromSecurityContext = (org.springframework.security.core.userdetails.User) userSecurityContext
				.getAuthentication().getPrincipal();
		return userRepository.findByEmail(userFromSecurityContext.getUsername());
	}

	@Override
	public UserDetails loadUserByUsername(String email) {
		User user = userRepository.findByEmail(email);

		if (user == null) {
			return null;
		}

		UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.email,
				user.password, AuthorityUtils.createAuthorityList(user.role));

		Authentication authentication = null;
		try {
			authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		} catch (Exception e) {
		}

		SecurityContextHolder.getContext().setAuthentication(authentication);

		return userDetails;
	}

	public boolean doesUserExist(String email) {
		User user = userRepository.findByEmail(email);

		return user != null;
	}

	public void addUser(Registration registration) throws ValidationException {
		if (this.doesUserExist(registration.email)) {
			throw new ValidationException("User already exists.");
		}

		String encryptedPassword = new BCryptPasswordEncoder().encode(registration.password);

		try {
			User user = new User(registration.email, registration.fullName, encryptedPassword,
					"STANDARD-ROLE");

			userRepository.save(user);
		} catch (ConstraintViolationException e) {
			throw new ValidationException(e.getConstraintViolations().iterator().next().getMessage());
		}
	}

	public List<UserInfo> retrieveFriendsList(User user) {
		List<User> users = userRepository.findFriendsListFor(user.email);
		return users.stream().map(u -> new UserInfo(u)).collect(Collectors.toList());
	}

	public UserInfo retrieveUserInfo(User user) {
		return new UserInfo(user);
	}

	// TODO: switch to a TINYINT field called "numOfConnections" to add/subtract
	// the total amount of user connections
	public void setIsPresent(User user, boolean isPresent) {
		user.isPresent=isPresent;
		userRepository.save(user);
	}

	public void notifyUser(User recipientUser, Notification notification) {
		if (recipientUser.isPresent) {
			simpMessagingTemplate.convertAndSend("/topic/user.notification." + recipientUser.id, notification);
		} else {
			logger.info("sending email notification to {}", recipientUser.fullName);
			// TODO: send email
		}
	}
}