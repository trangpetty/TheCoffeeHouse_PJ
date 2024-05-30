package com.example.thecoffeehouse.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private Long TypeID;
    private String name;
    private List<ProductImageDto> images;
    private List<ProductImageDto> removedImages;
    private String description;
    private double price;
    private List<ProductDetailDto> productSizes;
    private List<ProductDetailDto> removedProductSizes;
    private List<ProductToppingDto> toppings;
    private List<ProductToppingDto> removedToppings;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
}
