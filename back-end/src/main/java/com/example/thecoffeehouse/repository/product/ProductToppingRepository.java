package com.example.thecoffeehouse.repository.product;

import com.example.thecoffeehouse.entity.product.ProductTopping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductToppingRepository extends JpaRepository<ProductTopping, Long> {
    @Query("SELECT pt FROM ProductTopping pt WHERE pt.productID = :productID")
    List<ProductTopping> findByProductID(@Param("productID") Long productID);

    @Modifying
    @Transactional
    @Query("DELETE FROM ProductTopping pt WHERE pt.productID = :id")
    void deleteByProductID(@Param("id") Long id);
}
