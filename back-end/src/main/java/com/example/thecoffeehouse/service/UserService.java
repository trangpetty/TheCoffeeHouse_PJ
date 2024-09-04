package com.example.thecoffeehouse.service;

import com.example.thecoffeehouse.dto.user.*;
import com.example.thecoffeehouse.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Map;

public interface UserService {
    UserDetailsService userDetailsService();

    Page<UserDto> getUsers(String name, String phoneNumber, Pageable pageable);

    UserDto updateUser(Long id, UserDto userDto);

    void deleteUser(Long id);

    User saveOrUpdateGoogleUser(GoogleUserInfo userInfo);

    UserDto getUser(Long id);

    void updateMemberLevel(User user);

    Map<String, Long> getUserCountByAgeGroups();

    void updatePassword(String email, String password);

    List<ContactDetailDto> getContactDetailsUserById(Long id);

    List<Object[]> getTopUsersBuy();

    List<Object[]> getTopUsersCancelOrder();

    Map<String, List<Map<String, Object>>> getTopUsersSummary();

}
