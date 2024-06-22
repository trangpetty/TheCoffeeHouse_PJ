package com.example.thecoffeehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.thecoffeehouse.entity.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleRepository extends JpaRepository<Role, Long>{
    @Query("SELECT r.id FROM Role r WHERE r.name = :name")
    Long findIdByName(@Param("name") String name);
}
