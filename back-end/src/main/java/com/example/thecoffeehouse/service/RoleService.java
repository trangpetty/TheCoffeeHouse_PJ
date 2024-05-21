package com.example.thecoffeehouse.service;

import com.example.thecoffeehouse.entity.Role;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

public interface RoleService {
    Role createRole(Role role);

    Page<Role> getAll(Pageable pageable);

    Role updateRole(Long id, Role role);

    void deleteRole(Long id);
}
