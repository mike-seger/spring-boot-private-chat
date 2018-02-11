package org.privatechat.dto;

public class EstablishedChatChannel {
	public String channelUuid;
	public String userOneFullName;
	public String userTwoFullName;

	public EstablishedChatChannel() {
	}

	public EstablishedChatChannel(String channelUuid, String userOneFullName, String userTwoFullName) {
		this.channelUuid = channelUuid;
		this.userOneFullName = userOneFullName;
		this.userTwoFullName = userTwoFullName;
	}
}