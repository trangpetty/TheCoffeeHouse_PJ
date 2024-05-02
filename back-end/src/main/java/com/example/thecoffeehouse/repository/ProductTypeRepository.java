package com.example.thecoffeehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.thecoffeehouse.entity.ProductType;

public interface ProductTypeRepository extends JpaRepository<ProductType, Long>{

}
