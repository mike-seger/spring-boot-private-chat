package org.privatechat.chat.service;

import java.util.List;

import org.privatechat.chat.mapper.ChatMessageMapper;
import org.privatechat.dto.ChatChannelInitializationDto;
import org.privatechat.dto.ChatMessageDto;
import org.privatechat.dto.NotificationDto;
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
	private ChatChannelRepository chatChannelRepository;

	private ChatMessageRepository chatMessageRepository;

	private UserService userService;

	private final int MAX_PAGABLE_CHAT_MESSAGES = 100;

	@Autowired
	public ChatService(ChatChannelRepository chatChannelRepository, ChatMessageRepository chatMessageRepository,
			UserService userService) {
		this.chatChannelRepository = chatChannelRepository;
		this.chatMessageRepository = chatMessageRepository;
		this.userService = userService;
	}

	private String getExistingChannel(ChatChannelInitializationDto chatChannelInitializationDTO) {
		List<ChatChannel> channel = chatChannelRepository.findExistingChannel(
				chatChannelInitializationDTO.getUserIdOne(), chatChannelInitializationDTO.getUserIdTwo());

		return (channel != null && !channel.isEmpty()) ? channel.get(0).getUuid() : null;
	}

	private String newChatSession(ChatChannelInitializationDto chatChannelInitializationDTO)
			throws BeansException, UserNotFoundException {
		ChatChannel channel = new ChatChannel(userService.getUser(chatChannelInitializationDTO.getUserIdOne()),
				userService.getUser(chatChannelInitializationDTO.getUserIdTwo()));

		chatChannelRepository.save(channel);

		return channel.getUuid();
	}

	public String establishChatSession(ChatChannelInitializationDto chatChannelInitializationDTO)
			throws IsSameUserException, BeansException, UserNotFoundException {
		if (chatChannelInitializationDTO.getUserIdOne() == chatChannelInitializationDTO.getUserIdTwo()) {
			throw new IsSameUserException();
		}

		String uuid = getExistingChannel(chatChannelInitializationDTO);

		// If channel doesn't already exist, create a new one
		return (uuid != null) ? uuid : newChatSession(chatChannelInitializationDTO);
	}

	public void submitMessage(ChatMessageDto chatMessageDTO) throws BeansException, UserNotFoundException {
		ChatMessage chatMessage = ChatMessageMapper.mapChatDTOtoMessage(chatMessageDTO);

		chatMessageRepository.save(chatMessage);

		User fromUser = userService.getUser(chatMessage.getAuthorUser().getId());
		User recipientUser = userService.getUser(chatMessage.getRecipientUser().getId());

		userService.notifyUser(recipientUser, new NotificationDto("ChatMessageNotification",
				fromUser.getFullName() + " has sent you a message", chatMessage.getAuthorUser().getId()));
	}

	public List<ChatMessageDto> getExistingChatMessages(String channelUuid) {
		ChatChannel channel = chatChannelRepository.getChannelDetails(channelUuid);

		List<ChatMessage> chatMessages = chatMessageRepository.getExistingChatMessages(channel.getUserOne().getId(),
				channel.getUserTwo().getId(), new PageRequest(0, MAX_PAGABLE_CHAT_MESSAGES));

		// TODO: fix this
		List<ChatMessage> messagesByLatest = Lists.reverse(chatMessages);

		return ChatMessageMapper.mapMessagesToChatDTOs(messagesByLatest);
	}
}