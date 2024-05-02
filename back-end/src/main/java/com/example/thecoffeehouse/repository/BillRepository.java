package com.example.thecoffeehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.thecoffeehouse.entity.Bill;

public interface BillRepository extends JpaRepository<Bill, Long>{

}
