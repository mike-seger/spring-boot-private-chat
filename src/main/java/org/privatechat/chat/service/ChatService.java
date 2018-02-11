package org.privatechat.chat.service;

import java.util.List;
import java.util.stream.Collectors;

import org.privatechat.dto.ChatChannelInitialization;
import org.privatechat.dto.ChatMessageInfo;
import org.privatechat.dto.Notification;
import org.privatechat.exception.IsSameUserException;
import org.privatechat.exception.UserNotFoundException;
import org.privatechat.model.ChatChannel;
import org.privatechat.model.ChatMessage;
import org.privatechat.model.User;
import org.privatechat.repo.ChatChannelRepository;
import org.privatechat.repo.ChatMessageRepository;
import org.privatechat.user.service.UserService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
public class ChatService {
	@Autowired
	private ChatChannelRepository chatChannelRepository;
	@Autowired
	private ChatMessageRepository chatMessageRepository;
	@Autowired
	private UserService userService;

	private final int MAX_PAGABLE_CHAT_MESSAGES = 100;

	private String getExistingChannel(ChatChannelInitialization chatChannelInitializationDTO) {
		List<ChatChannel> channel = chatChannelRepository.findExistingChannel(
			chatChannelInitializationDTO.userIdOne, chatChannelInitializationDTO.userIdTwo);
		return (channel != null && !channel.isEmpty()) ? channel.get(0).getUuid() : null;
	}

	private String newChatSession(ChatChannelInitialization chatChannelInitialization)
			throws BeansException, UserNotFoundException {
		ChatChannel channel = new ChatChannel(userService.getUser(chatChannelInitialization.userIdOne),
				userService.getUser(chatChannelInitialization.userIdTwo));
		chatChannelRepository.save(channel);
		return channel.getUuid();
	}

	public String establishChatSession(ChatChannelInitialization chatChannelInitialization)
			throws IsSameUserException, BeansException, UserNotFoundException {
		if (chatChannelInitialization.userIdOne == chatChannelInitialization.userIdTwo) {
			throw new IsSameUserException();
		}
		String uuid = getExistingChannel(chatChannelInitialization);

		// If channel doesn't already exist, create a new one
		return (uuid != null) ? uuid : newChatSession(chatChannelInitialization);
	}

	public void submitMessage(ChatMessageInfo cmi) throws BeansException, UserNotFoundException {
		ChatMessage chatMessage = new ChatMessage(new User(cmi.fromUserId), new User(cmi.toUserId), cmi.contents);
		chatMessageRepository.save(chatMessage);
		User fromUser = userService.getUser(chatMessage.authorUser.id);
		User recipientUser = userService.getUser(chatMessage.recipientUser.id);
		userService.notifyUser(recipientUser, new Notification("ChatMessageNotification",
			fromUser.fullName + " has sent you a message", chatMessage.authorUser.id));
	}

	public List<ChatMessageInfo> getExistingChatMessages(String channelUuid) {
		ChatChannel channel = chatChannelRepository.getChannelDetails(channelUuid);
		List<ChatMessage> chatMessages = chatMessageRepository.getExistingChatMessages(channel.getUserOne().id,
			channel.getUserTwo().id, new PageRequest(0, MAX_PAGABLE_CHAT_MESSAGES));
		return Lists.reverse(chatMessages).stream().map(c -> new ChatMessageInfo(c)).collect(Collectors.toList());
	}
}