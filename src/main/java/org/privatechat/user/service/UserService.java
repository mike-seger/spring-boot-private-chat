package org.privatechat.user.service;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.privatechat.dto.NotificationDto;
import org.privatechat.dto.RegistrationDto;
import org.privatechat.dto.UserDto;
import org.privatechat.exception.UserNotFoundException;
import org.privatechat.exception.ValidationException;
import org.privatechat.model.User;
import org.privatechat.repo.UserRepository;
import org.privatechat.user.mapper.UserMapper;
import org.privatechat.user.strategy.IUserRetrievalStrategy;
import org.privatechat.user.strategy.UserRetrievalByEmailStrategy;
import org.privatechat.user.strategy.UserRetrievalByIdStrategy;
import org.privatechat.user.strategy.UserRetrievalBySecurityContextStrategy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
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
	private UserRepository userRepository;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	private BeanFactory beanFactory;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	private <T> User getUser(T userIdentifier, IUserRetrievalStrategy<T> strategy) throws UserNotFoundException {
		User user = strategy.getUser(userIdentifier);

		if (user == null) {
			throw new UserNotFoundException("User not found: "+userIdentifier);
		}

		return user;
	}

	public User getUser(long userId) throws BeansException, UserNotFoundException {
		return this.getUser(userId, beanFactory.getBean(UserRetrievalByIdStrategy.class));
	}

	public User getUser(String userEmail) throws BeansException, UserNotFoundException {
		return this.getUser(userEmail, beanFactory.getBean(UserRetrievalByEmailStrategy.class));
	}

	public User getUser(SecurityContext userSecurityContext) throws BeansException, UserNotFoundException {
		return this.getUser(userSecurityContext, beanFactory.getBean(UserRetrievalBySecurityContextStrategy.class));
	}

	@Override
	public UserDetails loadUserByUsername(String email) {
		User user = userRepository.findByEmail(email);

		if (user == null) {
			return null;
		}

		UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getEmail(),
				user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole()));

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

	public void addUser(RegistrationDto registrationDTO) throws ValidationException {
		if (this.doesUserExist(registrationDTO.getEmail())) {
			throw new ValidationException("User already exists.");
		}

		String encryptedPassword = new BCryptPasswordEncoder().encode(registrationDTO.getPassword());

		try {
			User user = new User(registrationDTO.getEmail(), registrationDTO.getFullName(), encryptedPassword,
					"STANDARD-ROLE");

			userRepository.save(user);
		} catch (ConstraintViolationException e) {
			throw new ValidationException(e.getConstraintViolations().iterator().next().getMessage());
		}
	}

	public List<UserDto> retrieveFriendsList(User user) {
		List<User> users = userRepository.findFriendsListFor(user.getEmail());

		return UserMapper.mapUsersToUserDTOs(users);
	}

	public UserDto retrieveUserInfo(User user) {
		return new UserDto(user.getId(), user.getEmail(), user.getFullName());
	}

	// TODO: switch to a TINYINT field called "numOfConnections" to add/subtract
	// the total amount of user connections
	public void setIsPresent(User user, Boolean stat) {
		user.setIsPresent(stat);

		userRepository.save(user);
	}

	public Boolean isPresent(User user) {
		return user.getIsPresent();
	}

	public void notifyUser(User recipientUser, NotificationDto notification) {
		if (this.isPresent(recipientUser)) {
			simpMessagingTemplate.convertAndSend("/topic/user.notification." + recipientUser.getId(), notification);
		} else {
			System.out.println("sending email notification to " + recipientUser.getFullName());
			// TODO: send email
		}
	}
}