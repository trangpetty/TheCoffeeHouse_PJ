package com.example.thecoffeehouse.controller;

import com.example.thecoffeehouse.Utils.JwtUtils;
import com.example.thecoffeehouse.dto.user.*;
import com.example.thecoffeehouse.entity.User;
import com.example.thecoffeehouse.repository.RoleRepository;
import com.example.thecoffeehouse.repository.UserRepository;
import com.example.thecoffeehouse.repository.UserRoleRepository;
import com.example.thecoffeehouse.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    @Value("${jwt.secret}")
    private String jwtSecretKey;

    @Value("${jwt.issuer}")
    private String jwtIssuer;

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;

    public UserController(UserService userService, UserRepository userRepository, UserRoleRepository userRoleRepository, RoleRepository roleRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public ResponseEntity<Page<UserDto>> getUsers(@RequestParam("name") String name,
                                                  @RequestParam("phoneNumber") String phoneNumber,
                                                  @RequestParam("pageNo") int pageNo,
                                                  @RequestParam(defaultValue = "10") int pageSize) {
        if (pageNo > 0) {
            pageNo = pageNo - 1;
        }
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return ResponseEntity.ok(userService.getUsers(name, phoneNumber, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.updateUser(id, userDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("success");
    }

//    @PostMapping("/login/google")
//    public ResponseEntity<AuthResponse> loginWithGoogle(@RequestBody GoogleTokenRequest request) {
//        try {
//            // Step 1: Authenticate the Google token and extract user info
//            GoogleUserInfo userInfo = authenticateAndExtractUserInfo(request.getGoogleToken());
//
//            // Step 2: Generate a JWT token from the user info and return
//            String accessToken = jwtUtils.generateToken(userInfo.getEmail());
//
//            // Return the JWT token in the HTTP response
//            return ResponseEntity.ok(new AuthResponse(accessToken));
//        } catch (Exception e) {
//            // Handle any errors
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    // Your method to authenticate and extract user info
//    private GoogleUserInfo authenticateAndExtractUserInfo(String googleToken) {
//        // Implement this method to authenticate the Google token and extract user info
//        // For example, you can use Google's libraries to verify the token and get user info
//        // This method should return an instance of GoogleUserInfo
//        // Placeholder implementation:
//        GoogleUserInfo userInfo = new GoogleUserInfo();
//        userInfo.setSub("123456789");
//        userInfo.setName("John Doe");
//        userInfo.setGiven_name("John");
//        userInfo.setFamily_name("Doe");
//        userInfo.setPicture("https://example.com/picture.jpg");
//        userInfo.setEmail("john.doe@example.com");
//        userInfo.setEmail_verified(true);
//        return userInfo;
//    }

    @GetMapping("/detail")
    public UserDto user(HttpServletRequest request) {
        var user = (User) request.getAttribute("user");
        var userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());

        return userDto;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }
}
