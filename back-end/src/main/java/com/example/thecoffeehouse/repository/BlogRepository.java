package com.example.thecoffeehouse.repository;

import com.example.thecoffeehouse.entity.Blog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BlogRepository extends MongoRepository<Blog, String> {

}
