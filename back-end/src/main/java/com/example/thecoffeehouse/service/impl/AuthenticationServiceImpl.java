package com.example.thecoffeehouse.service.impl;

import com.example.thecoffeehouse.dto.user.*;
import com.example.thecoffeehouse.entity.mapper.UserMapper;
import com.example.thecoffeehouse.entity.user.Role;
import com.example.thecoffeehouse.entity.user.User;
import com.example.thecoffeehouse.repository.UserRepository;
import com.example.thecoffeehouse.service.AuthenticationService;
import com.example.thecoffeehouse.service.JWTService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JWTService jwtService;

    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JWTService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public UserDto signup(RegisterDto registerDto) {
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }

        if (userRepository.existsByPhoneNumber(registerDto.getPhoneNumber())) {
            throw new RuntimeException("Số điện thoại đã tồn tại");
        }

        User user = new User();

        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setPhoneNumber(registerDto.getPhoneNumber());
        user.setAvatar(registerDto.getAvatar());
        user.setGender(registerDto.getGender());
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        UserDto userDto = UserMapper.mapToUserDto(user);
        userDto.setRole(user.getRole().name());
        userDto.setToken(jwt);
        userDto.setRefreshToken(refreshToken);

        return userDto;
    }


    public UserDto signin(LoginDto loginDto){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        var user = userRepository.findByEmail(loginDto.getEmail());
        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

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
        userDto.setRole(user.getRole().name());
        userDto.setToken(jwt);
        userDto.setRefreshToken(refreshToken);

        return userDto;
    }

    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String userEmail;
        try {
            userEmail = jwtService.extractUsername(refreshTokenRequest.getToken());
        } catch (Exception e) {
            throw new RuntimeException("Invalid token");
        }

        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (jwtService.isTokenValid(refreshTokenRequest.getToken(), user)) {
            var jwt = jwtService.generateToken(user);

            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
            jwtAuthenticationResponse.setToken(jwt);
            jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());

            return jwtAuthenticationResponse;
        } else {
            throw new RuntimeException("Token is not valid or expired");
        }
    }

}
