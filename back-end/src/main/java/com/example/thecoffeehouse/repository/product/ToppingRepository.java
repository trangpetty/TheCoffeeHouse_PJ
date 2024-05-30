package com.example.thecoffeehouse.repository.product;

import com.example.thecoffeehouse.entity.product.Topping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ToppingRepository extends JpaRepository<Topping, Long> {
    @Query("SELECT t FROM Topping t WHERE t.id = :id")
    Topping findToppingById(@Param("id") Long id);
}
