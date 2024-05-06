package com.example.thecoffeehouse.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.thecoffeehouse.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>{
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:name% AND p.TypeID = :typeID")
    Page<Product> getAllProducts(@Param("name") String name, @Param("typeID") Long typeID, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.TypeID = :typeID")
    List<Product> getProductsByTypeID(@Param("typeID") Long typeID);
}
