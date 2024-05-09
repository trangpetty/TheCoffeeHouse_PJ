package com.example.thecoffeehouse.entity.mapper;

import com.example.thecoffeehouse.dto.ProductDto;
import com.example.thecoffeehouse.entity.Product;

public class ProductMapper {
    public static Product mapToProduct(ProductDto productDto) {
        Product product = new Product(
                productDto.getId(),
                productDto.getTypeID(),
                productDto.getName(),
                productDto.getImageUrl(),
                productDto.getSize(),
                productDto.getPrice(),
                productDto.getCreateTime(),
                productDto.getModifyTime()
        );

        return product;
    }

    public static ProductDto mapToProductDto (Product product) {
        ProductDto productDto = new ProductDto();

        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setTypeID(product.getTypeID());
        productDto.setSize(product.getSize());
        productDto.setPrice(product.getPrice());
        productDto.setCreateTime(product.getCreateTime());
        productDto.setModifyTime(product.getModifyTime());
        productDto.setImageUrl(product.getImage());

        return productDto;
    }
}
