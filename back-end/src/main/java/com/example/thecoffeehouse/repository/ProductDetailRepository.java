package com.example.thecoffeehouse.repository;

import com.example.thecoffeehouse.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {
}
