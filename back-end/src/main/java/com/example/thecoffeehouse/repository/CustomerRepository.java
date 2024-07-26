package com.example.thecoffeehouse.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.thecoffeehouse.entity.user.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface CustomerRepository extends JpaRepository<Customer, Long>{
    @Query("SELECT c FROM Customer c WHERE (:name IS NULL OR c.defaultName LIKE %:name%) AND (:phoneNumber IS NULL OR c.phoneNumber LIKE %:phoneNumber%)")
    Page<Customer> getAllCustomers(@Param("name") String name, @Param("phoneNumber") String phoneNumber, Pageable pageable);

    Customer findByPhoneNumber(String phoneNumber);

    @Query("SELECT COUNT(c) FROM Customer c WHERE c.createTime BETWEEN :startOfDay AND :endOfDay")
    Integer findNewCustomersToday(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);
}
