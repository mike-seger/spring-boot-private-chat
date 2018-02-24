package org.privatechat.controller;

import java.util.List;

import org.privatechat.chat.service.ChatService;
import org.privatechat.common.JSONResponseHelper;
import org.privatechat.dto.ChatChannelInitialization;
import org.privatechat.dto.ChatMessageInfo;
import org.privatechat.dto.EstablishedChatChannel;
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
	public ChatMessageInfo chatMessage(@DestinationVariable String channelId, ChatMessageInfo message)
			throws BeansException, UserNotFoundException {
		chatService.submitMessage(message);

		return message;
	}

	@RequestMapping(value = "/api/private-chat/channel", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
	public ResponseEntity<String> establishChatChannel(
			@RequestBody ChatChannelInitialization chatChannelInitialization)
			throws IsSameUserException, UserNotFoundException {
		String channelUuid = chatService.establishChatSession(chatChannelInitialization);
		User userOne = userService.getUser(chatChannelInitialization.userIdOne);
		User userTwo = userService.getUser(chatChannelInitialization.userIdTwo);

		EstablishedChatChannel establishedChatChannel = new EstablishedChatChannel(channelUuid,
				userOne.fullName, userTwo.fullName);

		return JSONResponseHelper.createResponse(establishedChatChannel, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/private-chat/channel/{channelUuid}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getExistingChatMessages(@PathVariable("channelUuid") String channelUuid) {
		List<ChatMessageInfo> messages = chatService.getExistingChatMessages(channelUuid);

		return JSONResponseHelper.createResponse(messages, HttpStatus.OK);
	}
}