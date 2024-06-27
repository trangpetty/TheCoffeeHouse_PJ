package com.example.thecoffeehouse.dto.product;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductToppingDto {
    private Long id;
    private Long productID;
    private Long toppingID;
    private String name;
    private double price;
    private String image;
}
