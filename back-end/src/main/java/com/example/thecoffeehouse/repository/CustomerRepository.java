package com.example.thecoffeehouse.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.thecoffeehouse.entity.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer, Long>{
    @Query("SELECT c FROM Customer c WHERE (:name IS NULL OR c.name LIKE %:name%) AND (:phoneNumber IS NULL OR c.phoneNumber LIKE %:phoneNumber%)")
    Page<Customer> getAllCustomers(@Param("name") String name, @Param("phoneNumber") String phoneNumber, Pageable pageable);
}
