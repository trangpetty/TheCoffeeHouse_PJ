package com.example.thecoffeehouse.repository;

import com.example.thecoffeehouse.entity.ProductType;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductTypeRepository extends MongoRepository<ProductType, String> {

}
