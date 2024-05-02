package com.example.thecoffeehouse.repository;

import com.example.thecoffeehouse.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.thecoffeehouse.entity.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer, Long>{
    @Query("SELECT c FROM Customer c WHERE c.name LIKE %:name% AND c.phoneNumber LIKE %:phoneNumber%")
    Page<Customer> getAllCustomers(@Param("name") String name, @Param("phoneNumber") String phoneNumber, Pageable pageable);
}
