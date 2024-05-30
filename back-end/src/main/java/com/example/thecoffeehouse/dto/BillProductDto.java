package com.example.thecoffeehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillProductDto {
    private Long id;
    private Long productID;
    private Long productSizeID;
    private Long toppingID;
    private String productName;
    private String productSize;
    private String toppingName;
    private int quantityProduct;
    private int quantityTopping;
    private double cost;
}
