package com.example.thecoffeehouse.repository.product;

import com.example.thecoffeehouse.entity.product.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {
    @Query("SELECT p FROM ProductDetail p WHERE p.productID = :id")
    List<ProductDetail> findAllByProductID(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM ProductDetail p WHERE p.productID = :id")
    void deleteByProductID(@Param("id") Long id);
}
