package org.privatechat.user.strategy;

import org.privatechat.model.User;
import org.privatechat.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRetrievalByEmailStrategy implements IUserRetrievalStrategy<String> {
	private UserRepository userRepository;

	@Autowired
	public UserRetrievalByEmailStrategy(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public User getUser(String userIdentifier) {
		return userRepository.findByEmail(userIdentifier);
	}
}