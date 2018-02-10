package org.privatechat.chat.mapper;

import java.util.ArrayList;
import java.util.List;

import org.privatechat.dto.ChatMessageDto;
import org.privatechat.model.ChatMessage;
import org.privatechat.model.User;

public class ChatMessageMapper {
	public static List<ChatMessageDto> mapMessagesToChatDTOs(List<ChatMessage> chatMessages) {
		List<ChatMessageDto> dtos = new ArrayList<ChatMessageDto>();

		for (ChatMessage chatMessage : chatMessages) {
			dtos.add(new ChatMessageDto(chatMessage.getContents(), chatMessage.getAuthorUser().getId(),
					chatMessage.getRecipientUser().getId()));
		}

		return dtos;
	}

	public static ChatMessage mapChatDTOtoMessage(ChatMessageDto dto) {
		return new ChatMessage(

				// only need the id for mapping
				new User(dto.getFromUserId()), new User(dto.getToUserId()),

				dto.getContents());
	}
}
