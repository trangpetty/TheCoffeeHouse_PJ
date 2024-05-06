package com.example.thecoffeehouse.entity.mapper;

import com.example.thecoffeehouse.dto.ProductDto;
import com.example.thecoffeehouse.entity.Product;

public class ProductMapper {
    public static Product mapToProduct(ProductDto productDto) {
        Product product = new Product(
            productDto.getId(),
            productDto.getTypeID(),
            productDto.getName(),
            productDto.getImage(),
            productDto.getSize(),
            productDto.getPrice(),
            productDto.getCreateTime(),
            productDto.getModifyTime()
        );

        return product;
    }

    public static ProductDto mapToProductDto (Product product) {
        ProductDto productDto = new ProductDto(
            product.getId(),
            product.getTypeID(),
            product.getName(),
            product.getImage(),
            product.getSize(),
            product.getPrice(),
            product.getCreateTime(),
            product.getModifyTime() 
        );

        return productDto;
    }
}
