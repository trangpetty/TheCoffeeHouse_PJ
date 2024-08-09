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

    @Query("SELECT pd.id FROM ProductDetail pd WHERE pd.productID = :productID AND pd.size = :size")
    Long getProductDetailIdByProductIDAndSize(@Param("productID") Long productID, @Param("size") String size);

    @Query("SELECT pd.size FROM ProductDetail pd WHERE pd.id = :productDetailID")
    String getSizeByProductDetailID(@Param("productDetailID") Long productDetailID);

    @Query("SELECT pd.productID FROM ProductDetail pd WHERE pd.id = :productDetailID")
    Long getProductIdByProductDetailID(@Param("productDetailID") Long productDetailID);
}
