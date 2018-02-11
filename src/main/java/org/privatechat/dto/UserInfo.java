package org.privatechat.dto;

import org.privatechat.model.User;

public class UserInfo {
	public long id;
	public String email;
	public String fullName;
	
	public UserInfo() {}
	public UserInfo(User user) {
		this.id = user.id;
		this.email = user.email;
		this.fullName = user.fullName;
	}
}