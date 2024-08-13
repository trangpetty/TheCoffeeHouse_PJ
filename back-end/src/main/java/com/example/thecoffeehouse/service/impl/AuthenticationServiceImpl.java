package com.example.thecoffeehouse.service.impl;

import com.example.thecoffeehouse.Utils.exception.ApiException;
import com.example.thecoffeehouse.service.UserService;
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

    private final UserService userService;

    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JWTService jwtService, CustomerRepository customerRepository, ContactDetailRepository contactDetailRepository, UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.customerRepository = customerRepository;
        this.contactDetailRepository = contactDetailRepository;
        this.userService = userService;
    }

    public UserDto signup(RegisterDto registerDto) {
        // Kiểm tra xem email có tồn tại không
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }

        // Kiểm tra xem số điện thoại có tồn tại không
        if (userRepository.existsByPhoneNumber(registerDto.getPhoneNumber())) {
            throw new RuntimeException("Số điện thoại đã tồn tại");
        }

        // Kiểm tra xem số điện thoại có tồn tại trong bảng Customer không
        Customer existingCustomer = customerRepository.findByPhoneNumber(registerDto.getPhoneNumber());
        User newUser = new User();

        if (existingCustomer != null) {
            // Cập nhật thông tin người dùng từ Customer
            newUser.setPhoneNumber(existingCustomer.getPhoneNumber());
            newUser.setFirstName(registerDto.getFirstName());
            newUser.setLastName(registerDto.getLastName());
            newUser.setMembershipLevel(existingCustomer.getMembershipLevel());
            newUser.setPoint(existingCustomer.getPoint());

            ContactDetails contactDetails = contactDetailRepository.findByPhoneNumberAndOwnerType(existingCustomer.getPhoneNumber(), OwnerType.CUSTOMER);
            contactDetails.setOwnerType(OwnerType.USER);
            contactDetails.setOwnerID(newUser.getId());
            contactDetailRepository.save(contactDetails);

            // Xóa bản ghi khách hàng sau khi chuyển thông tin
            customerRepository.delete(existingCustomer);
        } else {
            // Thiết lập thông tin người dùng mới không có điểm và hạng chuyển giao
            newUser.setPhoneNumber(registerDto.getPhoneNumber());
            newUser.setFirstName(registerDto.getFirstName());
            newUser.setLastName(registerDto.getLastName());
            newUser.setMembershipLevel(MembershipLevel.BRONZE.getName()); // Hạng mặc định
            newUser.setPoint(0); // Điểm khởi tạo
        }

        // Cập nhật các thuộc tính còn lại cho người dùng
        newUser.setEmail(registerDto.getEmail());
        newUser.setAvatar(registerDto.getAvatar());
        newUser.setGender(registerDto.getGender());
        newUser.setDob(registerDto.getDob());
        newUser.setCode(registerDto.getCode());
        newUser.setRole(Role.USER);
        newUser.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        userService.updateMemberLevel(newUser);
        // Lưu người dùng vào cơ sở dữ liệu
        userRepository.save(newUser);

        // Tạo JWT và Refresh Token
        var jwt = jwtService.generateToken(newUser);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), newUser);

        // Chuyển đổi người dùng thành DTO và trả về
        UserDto userDto = UserMapper.mapToUserDto(newUser);
        userDto.setRole(newUser.getRole().name());
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
            userDto.setCode(user.getCode());
            userDto.setMembershipLevel(user.getMembershipLevel());

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

//    @Override
//    public JwtAuthenticationResponse changePassword(ChangePasswordRequest changePasswordRequest) {
//        try {
//            // Xác thực mật khẩu cũ
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            SecurityContextHolder.getContext().getAuthentication().getName(),
//                            changePasswordRequest.getOldPassword()
//                    )
//            );
//
//            // Thực hiện đổi mật khẩu và nhận phản hồi từ service
//            userService.updatePassword(authentication.getName(), changePasswordRequest.getNewPassword());
//
//            // Trả về phản hồi thành công
//            return ResponseEntity.ok(new JwtAuthenticationResponse("Mật khẩu đã được cập nhật thành công"));
//        } catch (BadCredentialsException e) {
//            // Mật khẩu cũ không chính xác
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Mật khẩu cũ không chính xác");
//        }
//    }


}
