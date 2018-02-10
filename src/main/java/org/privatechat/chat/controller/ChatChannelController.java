package org.privatechat.chat.controller;

import java.util.List;

import org.privatechat.chat.service.ChatService;
import org.privatechat.common.JSONResponseHelper;
import org.privatechat.dto.ChatChannelInitializationDto;
import org.privatechat.dto.ChatMessageDto;
import org.privatechat.dto.EstablishedChatChannelDto;
import org.privatechat.exception.IsSameUserException;
import org.privatechat.exception.UserNotFoundException;
import org.privatechat.model.User;
import org.privatechat.user.service.UserService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ChatChannelController {
	@Autowired
	private ChatService chatService;

	@Autowired
	private UserService userService;

	@MessageMapping("/private.chat.{channelId}")
	@SendTo("/topic/private.chat.{channelId}")
	public ChatMessageDto chatMessage(@DestinationVariable String channelId, ChatMessageDto message)
			throws BeansException, UserNotFoundException {
		chatService.submitMessage(message);

		return message;
	}

	@RequestMapping(value = "/api/private-chat/channel", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
	public ResponseEntity<String> establishChatChannel(
			@RequestBody ChatChannelInitializationDto chatChannelInitialization)
			throws IsSameUserException, UserNotFoundException {
		String channelUuid = chatService.establishChatSession(chatChannelInitialization);
		User userOne = userService.getUser(chatChannelInitialization.getUserIdOne());
		User userTwo = userService.getUser(chatChannelInitialization.getUserIdTwo());

		EstablishedChatChannelDto establishedChatChannel = new EstablishedChatChannelDto(channelUuid,
				userOne.getFullName(), userTwo.getFullName());

		return JSONResponseHelper.createResponse(establishedChatChannel, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/private-chat/channel/{channelUuid}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getExistingChatMessages(@PathVariable("channelUuid") String channelUuid) {
		List<ChatMessageDto> messages = chatService.getExistingChatMessages(channelUuid);

		return JSONResponseHelper.createResponse(messages, HttpStatus.OK);
	}
}