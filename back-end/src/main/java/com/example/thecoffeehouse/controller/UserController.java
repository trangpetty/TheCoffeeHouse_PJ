package com.example.thecoffeehouse.controller;

import com.example.thecoffeehouse.dto.bill.BillDto;
import com.example.thecoffeehouse.dto.user.*;
import com.example.thecoffeehouse.entity.user.ContactDetails;
import com.example.thecoffeehouse.entity.user.User;
import com.example.thecoffeehouse.repository.ContactDetailRepository;
import com.example.thecoffeehouse.repository.UserAddressRepository;
import com.example.thecoffeehouse.service.UserService;
import com.example.thecoffeehouse.service.bill.BillService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    private final BillService billService;

    private final ContactDetailRepository contactDetailRepository;

    public UserController(UserService userService, BillService billService, ContactDetailRepository contactDetailRepository) {
        this.userService = userService;
        this.billService = billService;
        this.contactDetailRepository = contactDetailRepository;
    }

    @GetMapping
    public ResponseEntity<Page<UserDto>> getUsers(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(name = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {

        // Adjust pageNo for 0-based indexing
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

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
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());

        return userDto;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @GetMapping("/address/{id}")
    public ResponseEntity<List<ContactDetails>> getAddresses(@PathVariable Long id) {
        return ResponseEntity.ok(contactDetailRepository.findByUserId(id));
    }

    @GetMapping("/bills/{id}")
    public ResponseEntity<List<BillDto>> getBills(@PathVariable Long id) {
        return ResponseEntity.ok(billService.getBillsByUserId(id));
    }

    @GetMapping("/bill-total/{id}")
    public ResponseEntity<Double> getTotalBill(@PathVariable Long id) {
        return ResponseEntity.ok(billService.totalValueByUserIDForCurrentYear(id));
    }
}
