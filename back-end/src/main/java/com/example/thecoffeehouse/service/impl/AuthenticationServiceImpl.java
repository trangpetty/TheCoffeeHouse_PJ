package com.example.thecoffeehouse.service.impl;

import com.example.thecoffeehouse.Utils.exception.ApiException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.example.thecoffeehouse.dto.user.*;
import com.example.thecoffeehouse.entity.mapper.UserMapper;
import com.example.thecoffeehouse.entity.user.*;
import com.example.thecoffeehouse.repository.ContactDetailRepository;
import com.example.thecoffeehouse.repository.CustomerRepository;
import com.example.thecoffeehouse.repository.UserRepository;
import com.example.thecoffeehouse.service.AuthenticationService;
import com.example.thecoffeehouse.service.JWTService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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

    private final CustomerRepository customerRepository;

    private final ContactDetailRepository contactDetailRepository;

    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JWTService jwtService, CustomerRepository customerRepository, ContactDetailRepository contactDetailRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.customerRepository = customerRepository;
        this.contactDetailRepository = contactDetailRepository;
    }

    public UserDto signup(RegisterDto registerDto) {
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }

        if (userRepository.existsByPhoneNumber(registerDto.getPhoneNumber())) {
            throw new RuntimeException("Số điện thoại đã tồn tại");
        }

        Customer existingCustomer = customerRepository.findByPhoneNumber(registerDto.getPhoneNumber());

        User user = new User();

        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setPhoneNumber(registerDto.getPhoneNumber());
        user.setAvatar(registerDto.getAvatar());
        user.setGender(registerDto.getGender());
        user.setDob(registerDto.getDob());
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        if (existingCustomer != null) {
            // Nếu số điện thoại đã tồn tại trong Customer, điểm của User có thể là 0 hoặc thiết lập theo logic của bạn
            user.setPoint(existingCustomer.getPoint()); // Hoặc chuyển điểm từ Customer nếu cần thiết
        } else {
            user.setPoint(0); // Thiết lập điểm cho người dùng mới
        }

        userRepository.save(user);

        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        UserDto userDto = UserMapper.mapToUserDto(user);
        userDto.setRole(user.getRole().name());
        userDto.setToken(jwt);
        userDto.setRefreshToken(refreshToken);

        return userDto;
    }


    public UserDto signin(LoginDto loginDto) {
        try {
            // Xác thực email và mật khẩu
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
            );

            // Nếu xác thực thành công, tìm người dùng
            User user = userRepository.findByEmail(loginDto.getEmail());

            if (user == null) {
                log.info("User with this email does not exist.");
                throw new ApiException("User with this email does not exist.", HttpStatus.NOT_FOUND);
            }

            // Tạo JWT và Refresh Token
            var jwt = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

            // Tạo đối tượng UserDto để trả về
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
            userDto.setPoint(user.getPoint());
            userDto.setDob(user.getDob());

            ContactDetails contactDetails = contactDetailRepository.findLastByUserId(user.getId());
            if (contactDetails != null && contactDetails.getAddress() != null) {
                userDto.setAddress(contactDetails.getAddress());
            }

            return userDto;
        } catch (BadCredentialsException ex) {
            log.info("Invalid credentials.");
            throw new ApiException("Invalid email or password.", HttpStatus.UNAUTHORIZED);
        } catch (UsernameNotFoundException ex) {
            log.info("User not found.");
            throw new ApiException("User with this email does not exist.", HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            log.error("An error occurred during login.", ex);
            throw new ApiException("An error occurred during login.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
