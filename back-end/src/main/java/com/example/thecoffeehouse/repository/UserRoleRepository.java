package com.example.thecoffeehouse.repository;

import com.example.thecoffeehouse.entity.UserRole;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRoleRepository extends MongoRepository<UserRole, String> {

}
