package org.privatechat.user.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

	@SendTo("/topic/user.notification.{userId}")
	public String notifications(@DestinationVariable long userId, String message) {
		return message;
	}
}