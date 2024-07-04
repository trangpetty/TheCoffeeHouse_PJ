package com.example.thecoffeehouse.entity.mapper;

import com.example.thecoffeehouse.dto.user.UserDto;
import com.example.thecoffeehouse.entity.user.User;

public class UserMapper {

    public static User mapToUser(UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setEmail(userDto.getEmail());
        user.setAvatar(userDto.getAvatar());
        user.setGender(userDto.getGender());
        user.setCreateTime(userDto.getCreateTime());
        user.setModifyTime(userDto.getModifyTime());
        return user;
    }

    public static UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setEmail(user.getEmail());
        userDto.setAvatar(user.getAvatar());
        userDto.setGender(user.getGender());
        userDto.setCreateTime(user.getCreateTime());
        userDto.setModifyTime(user.getModifyTime());
        return userDto;
    }
}
