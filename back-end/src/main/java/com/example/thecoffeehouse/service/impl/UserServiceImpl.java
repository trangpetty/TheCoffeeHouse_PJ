package com.example.thecoffeehouse.service.impl;

import com.example.thecoffeehouse.dto.user.*;
import com.example.thecoffeehouse.entity.User;
import com.example.thecoffeehouse.entity.UserRole;
import com.example.thecoffeehouse.entity.mapper.UserMapper;
import com.example.thecoffeehouse.repository.RoleRepository;
import com.example.thecoffeehouse.repository.UserRepository;
import com.example.thecoffeehouse.repository.UserRoleRepository;
import com.example.thecoffeehouse.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public UserDto createUser(RegisterDto registerDto) {
        Long roleId = roleRepository.findIdByName(registerDto.getRoleName());

        // Create a new User entity and set its properties
        User user = new User();
        user.setName(registerDto.getName());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword())); // Encode the password
        user.setPhoneNumber(registerDto.getPhoneNumber());
        user.setEmail(registerDto.getEmail());
        user.setAvatar(registerDto.getAvatar());
        user.setGender(registerDto.getGender());

        // Save the user entity
        User savedUser = userRepository.save(user);

        // Create a new UserRole entity and set its properties
        UserRole userRole = new UserRole();
        userRole.setUserID(savedUser.getId());
        userRole.setRoleID(roleId);

        // Save the UserRole entity
        userRoleRepository.save(userRole);

        // Map the saved user entity to a UserDto and return
        return UserMapper.mapToUserDto(savedUser);
    }

    public User saveOrUpdateGoogleUser(GoogleUserInfo userInfo) {
        // Find existing user by email
        User user = userRepository.findByEmail(userInfo.getEmail());

        if (user == null) {
            // If user does not exist, create a new user
            user = new User();
            user.setEmail(userInfo.getEmail());
            user.setName(userInfo.getName());
            user.setAvatar(userInfo.getPicture());
            user.setProviderId(userInfo.getSub());
            user.setGender(user.getGender()); // or set based on userInfo if available
            user.setPassword(""); // Set a blank password or a generated one
        } else {
            // If user exists, update existing user details
            user.setName(userInfo.getName());
            user.setAvatar(userInfo.getPicture());
            user.setProviderId(userInfo.getSub());
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
        return userRepository.getAllByNameAndPhoneNumber(name, phoneNumber, pageable);
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));
        user.setName(userDto.getName());
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

    @Override
    public UserDto login(LoginDto loginDto) {
        // Find the user by email
        User user = userRepository.findByEmail(loginDto.getEmail());
        if (user != null) { // Check if user exists
            // Check if the provided password matches the encoded password in the database
            if (passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
                String roleName = roleRepository.getRoleNameByUserID(user.getId());
                UserDto userDto = UserMapper.mapToUserDto(user);
                userDto.setRoleName(roleName);
                return userDto;
//                return Login.of(user.getId(), accessTokenSecret, refreshTokenSecret);
            }
        }
        return null;
    }

}
