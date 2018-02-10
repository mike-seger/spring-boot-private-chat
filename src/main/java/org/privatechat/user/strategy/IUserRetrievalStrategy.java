package org.privatechat.user.strategy;

import org.privatechat.model.User;

public interface IUserRetrievalStrategy<T> {
	public User getUser(T userIdentifier);
}