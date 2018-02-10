package org.privatechat.dto;

public class ChatMessageDto {
	private String contents;

	private long fromUserId;

	private long toUserId;

	public ChatMessageDto() {
	}

	public ChatMessageDto(String contents, long fromUserId, long toUserId) {
		this.contents = contents;
		this.fromUserId = fromUserId;
		this.toUserId = toUserId;
	}

	public String getContents() {
		return this.contents;
	}

	public void setToUserId(long toUserId) {
		this.toUserId = toUserId;
	}

	public long getToUserId() {
		return this.toUserId;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public void setFromUserId(long userId) {
		this.fromUserId = userId;
	}

	public long getFromUserId() {
		return this.fromUserId;
	}
}
