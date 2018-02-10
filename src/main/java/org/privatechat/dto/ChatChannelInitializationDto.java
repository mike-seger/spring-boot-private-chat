package org.privatechat.dto;

public class ChatChannelInitializationDto {
	private long userIdOne;

	private long userIdTwo;

	public ChatChannelInitializationDto() {
	}

	public void setUserIdOne(long userIdOne) {
		this.userIdOne = userIdOne;
	}

	public void setUserIdTwo(long userIdTwo) {
		this.userIdTwo = userIdTwo;
	}

	public long getUserIdOne() {
		return this.userIdOne;
	}

	public long getUserIdTwo() {
		return userIdTwo;
	}
}
