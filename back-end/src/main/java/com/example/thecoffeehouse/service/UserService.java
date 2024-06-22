package com.example.thecoffeehouse.service;

import com.example.thecoffeehouse.dto.user.*;
import com.example.thecoffeehouse.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserDto createUser(RegisterDto registerDto);

    Page<UserDto> getUsers(String name, String phoneNumber, Pageable pageable);

    UserDto updateUser(Long id, UserDto userDto);

    void deleteUser(Long id);

    UserDto login(LoginDto loginDto);

    User saveOrUpdateGoogleUser(GoogleUserInfo userInfo);
}
