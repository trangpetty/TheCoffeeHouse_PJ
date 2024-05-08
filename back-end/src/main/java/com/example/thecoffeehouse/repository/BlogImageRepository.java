package com.example.thecoffeehouse.repository;

import com.example.thecoffeehouse.entity.BlogImage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BlogImageRepository extends MongoRepository<BlogImage, String> {

}
