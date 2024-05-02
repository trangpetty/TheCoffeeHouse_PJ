package com.example.thecoffeehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.thecoffeehouse.entity.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long>{

}
