package com.example.thecoffeehouse.repository.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.thecoffeehouse.entity.product.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>{
    @Query("SELECT p FROM Product p WHERE " +
            "(:name IS NULL OR lower(p.name) LIKE %:name%) AND " +
            "(:typeID IS NULL OR p.TypeID = :typeID)")
    Page<Product> getAllProducts(@Param("name") String name, @Param("typeID") Long typeID, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:name% AND p.TypeID = :typeID")
    List<Product> findByTypeAndName(@Param("name") String name, @Param("typeID") Long typeID);

    @Query("SELECT p FROM Product p WHERE p.name = :name")
    List<Product> findByName(@Param("name") String name);

    @Query("SELECT p FROM Product p WHERE p.TypeID = :typeID")
    List<Product> getProductsByTypeID(@Param("typeID") Long typeID);


}