package com.example.thecoffeehouse.service.impl;

import com.example.thecoffeehouse.dto.UserDto;
import com.example.thecoffeehouse.entity.User;
import com.example.thecoffeehouse.entity.UserRole;
import com.example.thecoffeehouse.entity.mapper.UserMapper;
import com.example.thecoffeehouse.repository.RoleRepository;
import com.example.thecoffeehouse.repository.UserRepository;
import com.example.thecoffeehouse.repository.UserRoleRepository;
import com.example.thecoffeehouse.service.UserService;
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

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public UserDto createUser(UserDto userDto, String password) {
        Long role_id = roleRepository.findIdByName(userDto.getRoleName());

        User user = UserMapper.mapToUser(userDto);
        user.setPassword(passwordEncoder.encode(password));

        User savedUser = userRepository.save(user);

        UserRole userRole = new UserRole();
        userRole.setUserID(savedUser.getId());
        userRole.setRoleID(role_id);
        userRoleRepository.save(userRole);
        return UserMapper.mapToUserDto(savedUser);
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
}
