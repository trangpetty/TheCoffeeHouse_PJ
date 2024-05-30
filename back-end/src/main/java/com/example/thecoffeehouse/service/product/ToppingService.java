package com.example.thecoffeehouse.service.product;

import com.example.thecoffeehouse.entity.product.Topping;

import java.util.List;

public interface ToppingService {
    List<Topping> findAll();

    Topping createTopping(Topping topping);

    Topping updateTopping(Long id, Topping topping);

    void deleteTopping(Long id);

    Topping findToppingById(Long id);
}
