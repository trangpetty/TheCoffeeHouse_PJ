package com.example.thecoffeehouse.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSalesDto {
    private String productName;
    private Long totalQuantity;
}
