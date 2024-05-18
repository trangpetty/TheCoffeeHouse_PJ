package com.example.thecoffeehouse.repository.product;

import com.example.thecoffeehouse.entity.product.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    @Query("SELECT pi FROM ProductImage pi WHERE pi.productID = :productID")
    List<ProductImage> findAllByProductID(@Param("productID") Long productID);

    @Modifying
    @Transactional
    @Query("DELETE FROM ProductImage pi WHERE pi.productID = :productID")
    void deleteByProductID(@Param("productID") Long productID);

    @Query("SELECT pi FROM ProductImage pi WHERE pi.url = :imageUrl")
    ProductImage findByUrl(@Param("imageUrl") String imageUrl);
}
