package com.example.thecoffeehouse.service.impl;

import com.example.thecoffeehouse.dto.user.*;
import com.example.thecoffeehouse.entity.mapper.ContactDetailMapper;
import com.example.thecoffeehouse.entity.user.ContactDetails;
import com.example.thecoffeehouse.entity.user.MembershipLevel;
import com.example.thecoffeehouse.entity.user.OwnerType;
import com.example.thecoffeehouse.entity.user.User;
import com.example.thecoffeehouse.entity.mapper.UserMapper;
import com.example.thecoffeehouse.repository.ContactDetailRepository;
import com.example.thecoffeehouse.repository.UserRepository;
import com.example.thecoffeehouse.repository.UserRoleRepository;
import com.example.thecoffeehouse.service.UserService;
import com.example.thecoffeehouse.service.VoucherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final VoucherService voucherService;
    private final PasswordEncoder passwordEncoder;
    private final ContactDetailRepository contactDetailRepository;

    public UserServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository, VoucherService voucherService, PasswordEncoder passwordEncoder, ContactDetailRepository contactDetailRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.voucherService = voucherService;
        this.passwordEncoder = passwordEncoder;
        this.contactDetailRepository = contactDetailRepository;
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
        user.setGender(userDto.getGender());
        user.setAvatar(userDto.getAvatar());
        user.setDob(userDto.getDob());

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
    public void updateMemberLevel(User user) {
        int points = user.getPoint();
        MembershipLevel newLevel = MembershipLevel.getLevel(points);
        user.setMembershipLevel(newLevel.getName());

        if (points == newLevel.getMinPoints()) {
            log.info("Condition met, assigning vouchers. Points: {}", points);
            voucherService.assignVouchersToUser(user.getId());
        } else {
            log.info("Condition not met. Points: {}", newLevel.getMinPoints());
        }

        userRepository.save(user);
    }

    @Override
    public Map<String, Long> getUserCountByAgeGroups() {
        LocalDate now = LocalDate.now();

        // Tạo các khoảng độ tuổi
        LocalDate startDateUnder18 = now.minusYears(18);
        LocalDate endDateUnder18 = now;

        LocalDate startDate18_24 = now.minusYears(24);
        LocalDate endDate18_24 = now.minusYears(18).minusDays(1);

        LocalDate startDate25_34 = now.minusYears(34);
        LocalDate endDate25_34 = now.minusYears(25).minusDays(1);

        LocalDate startDate35_44 = now.minusYears(44);
        LocalDate endDate35_44 = now.minusYears(35).minusDays(1);

        LocalDate startDate45_54 = now.minusYears(54);
        LocalDate endDate45_54 = now.minusYears(45).minusDays(1);

        LocalDate startDate55Plus = now.minusYears(120); // Chọn một năm xa để bao phủ toàn bộ tuổi 55+
        LocalDate endDate55Plus = now.minusYears(55).minusDays(1);

        Map<String, Long> ageGroupCounts = new HashMap<>();

        ageGroupCounts.put("Under 18", (long) userRepository.findUsersByAgeRange(startDateUnder18, endDateUnder18).size());
        ageGroupCounts.put("18-24", (long) userRepository.findUsersByAgeRange(startDate18_24, endDate18_24).size());
        ageGroupCounts.put("25-34", (long) userRepository.findUsersByAgeRange(startDate25_34, endDate25_34).size());
        ageGroupCounts.put("35-44", (long) userRepository.findUsersByAgeRange(startDate35_44, endDate35_44).size());
        ageGroupCounts.put("45-54", (long) userRepository.findUsersByAgeRange(startDate45_54, endDate45_54).size());
        ageGroupCounts.put("55+", (long) userRepository.findUsersByAgeRange(startDate55Plus, endDate55Plus).size());

        return ageGroupCounts;
    }

    @Override
    public void updatePassword(String email, String password) {
        User user = userRepository.findByEmail(email);
        if(user != null) {
            String encodedPassword = passwordEncoder.encode(password);
            user.setPassword(encodedPassword);
            userRepository.save(user);
        }
    }

    @Override
    public List<ContactDetailDto> getContactDetailsUserById(Long id) {
        List<ContactDetails> contactDetails = contactDetailRepository.findByOwnerIDAndOwnerType(id, OwnerType.USER);
        return contactDetails.stream().map(ContactDetailMapper::mapToContactDetailDto).collect(Collectors.toList());
    }

    @Override
    public List<Object[]> getTopUsersBuy() {
        Pageable pageable = PageRequest.of(0, 5); // Limit to top 10 products
        return userRepository.findTopUsersBuy(pageable);
    }

    @Override
    public List<Object[]> getTopUsersCancelOrder() {
        Pageable pageable = PageRequest.of(0, 5); // Limit to top 10 products
        return userRepository.findTopUsersCancelOrder(pageable);
    }

    public Map<String, List<Map<String, Object>>> getTopUsersSummary() {
        List<Object[]> topBuyers = getTopUsersBuy();
        List<Object[]> topCancelers = getTopUsersCancelOrder();

        // Xử lý kết quả để trả về một JSON response dễ đọc
        List<Map<String, Object>> buyersResponse = new ArrayList<>();
        for (Object[] result : topBuyers) {
            Map<String, Object> map = new HashMap<>();
            map.put("user", result[0]);  // Đối tượng User
            map.put("totalOrders", result[1]);
            map.put("success", result[2]);
            map.put("cancel", result[3]);
            map.put("fail", result[4]);
            buyersResponse.add(map);
        }

        List<Map<String, Object>> cancelersResponse = new ArrayList<>();
        for (Object[] result : topCancelers) {
            Map<String, Object> map = new HashMap<>();
            map.put("user", result[0]);  // Đối tượng User
            map.put("totalOrders", result[1]);
            map.put("success", result[2]);
            map.put("cancel", result[3]);
            map.put("fail", result[4]);
            cancelersResponse.add(map);
        }

        Map<String, List<Map<String, Object>>> response = new HashMap<>();
        response.put("topUsersBuy", buyersResponse);
        response.put("topUsersCancel", cancelersResponse);

        return response;
    }

}
