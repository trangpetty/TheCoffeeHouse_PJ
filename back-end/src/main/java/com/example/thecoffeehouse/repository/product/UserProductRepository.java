package com.example.thecoffeehouse.repository.product;

import com.example.thecoffeehouse.entity.product.UserProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserProductRepository extends JpaRepository<UserProduct, Long> {

    UserProduct findByUserIdAndProductId(Long userId, Long productId);

    @Query("SELECT DISTINCT userId FROM UserProduct")
    List<Long> findDistinctUserIds();
}
