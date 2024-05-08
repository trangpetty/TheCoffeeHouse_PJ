package com.example.thecoffeehouse.repository;

import com.example.thecoffeehouse.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

}
