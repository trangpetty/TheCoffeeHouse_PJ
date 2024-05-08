package com.example.thecoffeehouse.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.thecoffeehouse.entity.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends MongoRepository<Customer, String> {
    @Query("{ 'name' : { $regex: ?0, $options: 'i' }, 'phoneNumber' : { $regex: ?1, $options: 'i' } }")
    Page<Customer> getAllCustomers(String name, String phoneNumber, Pageable pageable);
}
