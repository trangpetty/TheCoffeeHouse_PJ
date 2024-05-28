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
    private String productName;
    private String productSize;
    private int quantity;
    private double price;
}
