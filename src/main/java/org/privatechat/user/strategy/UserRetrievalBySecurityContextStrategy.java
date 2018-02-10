package org.privatechat.user.strategy;

import org.privatechat.model.User;
import org.privatechat.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;

@Service
public class UserRetrievalBySecurityContextStrategy implements IUserRetrievalStrategy<SecurityContext> {
	private UserRepository userRepository;

	@Autowired
	public UserRetrievalBySecurityContextStrategy(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public User getUser(SecurityContext securityContext) {
		org.springframework.security.core.userdetails.User userFromSecurityContext;

		userFromSecurityContext = (org.springframework.security.core.userdetails.User) securityContext
				.getAuthentication().getPrincipal();

		return userRepository.findByEmail(userFromSecurityContext.getUsername());
	}
}