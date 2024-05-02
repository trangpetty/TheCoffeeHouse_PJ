package com.example.thecoffeehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.thecoffeehouse.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

}
