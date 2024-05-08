package com.example.thecoffeehouse.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.thecoffeehouse.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    @Query("{ 'name' : { $regex: ?0, $options: 'i' }, 'TypeID' : ?1 }")
    Page<Product> getAllProducts(String name, String typeID, Pageable pageable);

    @Query("{ 'typeID' : ?0 }")
    List<Product> getProductsByTypeID(String typeID);
}
