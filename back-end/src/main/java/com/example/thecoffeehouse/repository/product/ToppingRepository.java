package com.example.thecoffeehouse.repository.product;

import com.example.thecoffeehouse.entity.product.Topping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToppingRepository extends JpaRepository<Topping, Long> {
}
