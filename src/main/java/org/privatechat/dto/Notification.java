package org.privatechat.dto;

public class NotificationDto {
	private String type;

	private String contents;

	private long fromUserId;

	public NotificationDto() {
	}

	public NotificationDto(String type, String contents, long fromUserId) {
		this.type = type;
		this.contents = contents;
		this.fromUserId = fromUserId;
	}

	public String getType() {
		return this.type;
	}

	public String getContent() {
		return this.contents;
	}

	public long getfromUserId() {
		return this.fromUserId;
	}
}