package com.example.thecoffeehouse.service.impl;

import com.example.thecoffeehouse.entity.User;
import com.example.thecoffeehouse.repository.RoleRepository;
import com.example.thecoffeehouse.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AppUserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        String roleName = roleRepository.getRoleNameByUserID(user.getId());

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + roleName);

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), List.of(authority));
    }
}
