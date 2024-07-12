package com.example.thecoffeehouse.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.thecoffeehouse.entity.product.ProductType;

public interface ProductTypeRepository extends JpaRepository<ProductType, Long>{

}
