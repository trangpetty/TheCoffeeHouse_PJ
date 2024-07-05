package com.example.thecoffeehouse.service.impl;

import com.example.thecoffeehouse.dto.user.*;
import com.example.thecoffeehouse.entity.user.User;
import com.example.thecoffeehouse.entity.mapper.UserMapper;
import com.example.thecoffeehouse.repository.UserRepository;
import com.example.thecoffeehouse.repository.UserRoleRepository;
import com.example.thecoffeehouse.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public UserServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userRepository.findByEmail(username);
            }
        };
    }

    public User saveOrUpdateGoogleUser(GoogleUserInfo userInfo) {
        // Find existing user by email
        User user = userRepository.findByEmail(userInfo.getEmail());

        if (user == null) {
            // If user does not exist, create a new user
            user = new User();
            user.setEmail(userInfo.getEmail());
            user.setFirstName(userInfo.getName());
            user.setAvatar(userInfo.getPicture());
            user.setGender(user.getGender()); // or set based on userInfo if available
            user.setPassword(""); // Set a blank password or a generated one
        } else {
            // If user exists, update existing user details
            user.setFirstName(userInfo.getName());
            user.setAvatar(userInfo.getPicture());
        }

        // Save the user in the database
        return userRepository.save(user);
    }

    @Override
    public UserDto getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));
        return UserMapper.mapToUserDto(user);
    }


    @Override
    public Page<UserDto> getUsers(String name, String phoneNumber, Pageable pageable) {
        try {
            log.info("Searching for users with name: '{}' and phoneNumber: '{}'", name, phoneNumber);
            Page<User> users = userRepository.getAllByNameAndPhoneNumber(name, phoneNumber, pageable);
            log.info("Found {} users", users.getTotalElements());
            return users.map(UserMapper::mapToUserDto);
        } catch (Exception e) {
            log.error("Error retrieving users: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve users", e);
        }
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setEmail(userDto.getEmail());
        user.setGender(userDto.getGender());
        user.setAvatar(userDto.getAvatar());

        User savedUser = userRepository.save(user);
        return UserMapper.mapToUserDto(savedUser);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));

        userRoleRepository.deleteByUserID(id);
        userRepository.deleteById(id);
    }

}
