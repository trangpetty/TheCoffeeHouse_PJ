package com.example.thecoffeehouse.service.impl;

import com.example.thecoffeehouse.entity.Role;
import com.example.thecoffeehouse.repository.RoleRepository;
import com.example.thecoffeehouse.service.RoleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RolerServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RolerServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Page<Role> getAll(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }

    @Override
    public Role updateRole(Long id, Role updateRole) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role does not exists"));
        role.setName(updateRole.getName());
        role.setDescription(updateRole.getDescription());
        role.setRemovable(updateRole.getRemovable());
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(Long id) {
        roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role does not exists"));
        roleRepository.deleteById(id);
    }
}
