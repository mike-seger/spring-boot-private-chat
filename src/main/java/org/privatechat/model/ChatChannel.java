package org.privatechat.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "chatChannel")
public class ChatChannel {

	@Id
	@NotNull
	public String uuid;

	@OneToOne
	@JoinColumn(name = "userIdOne")
	public User userOne;

	@OneToOne
	@JoinColumn(name = "userIdTwo")
	public User userTwo;

	public ChatChannel(User userOne, User userTwo) {
		this.uuid = UUID.randomUUID().toString();
		this.userOne = userOne;
		this.userTwo = userTwo;
	}

	public ChatChannel() {
	}
}