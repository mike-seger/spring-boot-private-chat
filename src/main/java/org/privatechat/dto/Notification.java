package org.privatechat.dto;

public class Notification {
	public String type;
	public String contents;
	public long fromUserId;

	public Notification() {
	}

	public Notification(String type, String contents, long fromUserId) {
		this.type = type;
		this.contents = contents;
		this.fromUserId = fromUserId;
	}
}