package com.example.thecoffeehouse.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillProductDto {
    private Long id;
    private Long productID;
    private String productSizeID;
    private String toppingID;
    private int quantity;
    private double cost;
}
