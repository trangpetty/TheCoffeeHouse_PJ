package com.example.thecoffeehouse.entity.mapper;

import com.example.thecoffeehouse.dto.UserDto;
import com.example.thecoffeehouse.entity.User;

public class UserMapper {

    public static User mapToUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
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
        userDto.setName(user.getName());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setEmail(user.getEmail());
        userDto.setAvatar(user.getAvatar());
        userDto.setGender(user.getGender());
        userDto.setCreateTime(user.getCreateTime());
        userDto.setModifyTime(user.getModifyTime());
        return userDto;
    }
}
