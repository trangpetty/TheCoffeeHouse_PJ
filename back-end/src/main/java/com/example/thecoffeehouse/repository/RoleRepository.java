package com.example.thecoffeehouse.repository;

import com.example.thecoffeehouse.entity.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {

}
