package org.privatechat.user.mapper;

import java.util.ArrayList;
import java.util.List;

import org.privatechat.dto.UserDto;
import org.privatechat.model.User;

public class UserMapper {
	public static List<UserDto> mapUsersToUserDTOs(List<User> users) {
		List<UserDto> dtos = new ArrayList<UserDto>();

		for (User user : users) {
			dtos.add(new UserDto(user.getId(), user.getEmail(), user.getFullName()));
		}

		return dtos;
	}
}