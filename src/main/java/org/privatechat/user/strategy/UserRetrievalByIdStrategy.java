package org.privatechat.user.strategy;

import org.privatechat.model.User;
import org.privatechat.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRetrievalByIdStrategy implements IUserRetrievalStrategy<Long> {
	private UserRepository userRepository;

	@Autowired
	public UserRetrievalByIdStrategy(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public User getUser(Long userIdentifier) {
		return userRepository.findById(userIdentifier);
	}
}
