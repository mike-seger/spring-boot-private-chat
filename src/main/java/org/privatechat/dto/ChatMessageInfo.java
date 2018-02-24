package org.privatechat.dto;

import org.privatechat.model.ChatMessage;

public class ChatMessageInfo {
	public String contents;
	public long fromUserId;
	public long toUserId;

	public ChatMessageInfo() {
	}

	public ChatMessageInfo(ChatMessage chatMessage) {
		this.contents = chatMessage.contents;
		this.fromUserId = chatMessage.authorUser.id;
		this.toUserId = chatMessage.recipientUser.id;
	}
}
