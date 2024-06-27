package com.example.thecoffeehouse.repository.product;

import com.example.thecoffeehouse.entity.product.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
}
