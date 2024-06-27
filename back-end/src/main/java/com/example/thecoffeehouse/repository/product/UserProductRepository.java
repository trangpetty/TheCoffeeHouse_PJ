package com.example.thecoffeehouse.repository.product;

import com.example.thecoffeehouse.entity.product.UserProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProductRepository extends JpaRepository<UserProduct, Long> {
}
