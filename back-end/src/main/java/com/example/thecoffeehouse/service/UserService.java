package com.example.thecoffeehouse.service;

import com.example.thecoffeehouse.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserDto createUser(UserDto userDto, String password);

    Page<UserDto> getUsers(String name, String phoneNumber, Pageable pageable);

    UserDto updateUser(Long id, UserDto userDto);

    void deleteUser(Long id);
}
