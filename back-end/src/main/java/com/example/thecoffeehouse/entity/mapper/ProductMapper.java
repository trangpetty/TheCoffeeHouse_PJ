package com.example.thecoffeehouse.entity.mapper;

import com.example.thecoffeehouse.dto.ProductDetailDto;
import com.example.thecoffeehouse.dto.ProductDto;
import com.example.thecoffeehouse.entity.Product;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ProductMapper {
    public static Product mapToProduct(ProductDto productDto) {
        Product product = new Product(
                productDto.getId(),
                productDto.getTypeID(),
                productDto.getName(),
                productDto.getImage(),
                productDto.getDescription(),
                productDto.getPrice(),
                productDto.getCreateTime(),
                productDto.getModifyTime()
        );

        return product;
    }

    public static ProductDto mapToProductDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setTypeID(product.getTypeID());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setCreateTime(product.getCreateTime());
        productDto.setModifyTime(product.getModifyTime());
        productDto.setImage(product.getImage());

        // Convert list of ProductDetail to list of ProductDetailDto
        List<ProductDetailDto> productDetailDtos = productDto.getProductSizes() == null ?
                Collections.emptyList()
                : productDto.getProductSizes().stream()
                .map(detail -> mapToProductDetailDto(detail))
                .collect(Collectors.toList());
        productDto.setProductSizes(productDetailDtos);

        return productDto;
    }

    private static ProductDetailDto mapToProductDetailDto(ProductDetailDto productDetail) {
        ProductDetailDto productDetailDto = new ProductDetailDto();
        productDetailDto.setSize(productDetail.getSize());
        productDetailDto.setSurcharge(productDetail.getSurcharge());

        return productDetailDto;
    }

//    public static ProductDto mapToProductDto (Product product) {
//        ProductDto productDto = new ProductDto();
//
//        productDto.setId(product.getId());
//        productDto.setName(product.getName());
//        productDto.setTypeID(product.getTypeID());
//        productDto.setDescription(product.getDescription());
//        productDto.setPrice(product.getPrice());
//        productDto.setCreateTime(product.getCreateTime());
//        productDto.setModifyTime(product.getModifyTime());
//        productDto.setImageUrl(product.getImage());
//
//        return productDto;
//    }
}
