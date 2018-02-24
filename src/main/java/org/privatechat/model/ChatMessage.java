package org.privatechat.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "chatMessage")
public class ChatMessage {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long id;

	@OneToOne
	@JoinColumn(name = "authorUserId")
	public User authorUser;

	@OneToOne
	@JoinColumn(name = "recipientUserId")
	public User recipientUser;

	@NotNull
	public Date timeSent;

	@NotNull
	public String contents;

	public ChatMessage() {
	}

	public ChatMessage(User authorUser, User recipientUser, String contents) {
		this.authorUser = authorUser;
		this.recipientUser = recipientUser;
		this.contents = contents;
		this.timeSent = new Date();
	}
}