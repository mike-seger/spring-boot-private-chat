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
	private String uuid;

	@OneToOne
	@JoinColumn(name = "userIdOne")
	private User userOne;

	@OneToOne
	@JoinColumn(name = "userIdTwo")
	private User userTwo;

	public ChatChannel(User userOne, User userTwo) {
		this.uuid = UUID.randomUUID().toString();
		this.userOne = userOne;
		this.userTwo = userTwo;
	}

	public ChatChannel() {
	}

	public void setUserTwo(User user) {
		this.userTwo = user;
	}

	public void setUserOne(User user) {
		this.userOne = user;
	}

	public User getUserOne() {
		return this.userOne;
	}

	public User getUserTwo() {
		return this.userTwo;
	}

	public String getUuid() {
		return this.uuid;
	}
}