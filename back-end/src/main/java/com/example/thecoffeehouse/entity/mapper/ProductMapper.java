package com.example.thecoffeehouse.entity.mapper;

import com.example.thecoffeehouse.dto.product.ProductDetailDto;
import com.example.thecoffeehouse.dto.product.ProductDto;
import com.example.thecoffeehouse.dto.product.ProductImageDto;
import com.example.thecoffeehouse.dto.product.ProductToppingDto;
import com.example.thecoffeehouse.entity.product.*;
import com.example.thecoffeehouse.service.product.ProductService;

import java.util.List;
import java.util.stream.Collectors;
public class ProductMapper {
    public static ProductDto mapToProductDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setTypeID(product.getTypeID());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setCreateTime(product.getCreateTime());
        productDto.setModifyTime(product.getModifyTime());
        return productDto;
    }

    public static ProductDto mapToProductDtoWithDetails(Product product, List<ProductDetailDto> productDetailDtos, List<ProductImageDto> productImages, List<ProductToppingDto> productToppingDtos) {
        ProductDto productDto = mapToProductDto(product);
        productDto.setImages(productImages);
        productDto.setProductSizes(productDetailDtos);
        productDto.setToppings(productToppingDtos);
        return productDto;
    }

    public static Product mapToProduct(ProductDto productDto) {
        Product product = new Product();
        product.setTypeID(productDto.getTypeID());
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        return product;
    }

    public static ProductDetailDto mapToProductDetailDto(ProductDetail productDetail) {
        ProductDetailDto productDetailDto = new ProductDetailDto();
        productDetailDto.setId(productDetail.getId());
        productDetailDto.setSize(productDetail.getSize());
        productDetailDto.setSurcharge(productDetail.getSurcharge());
        return productDetailDto;
    }

    public static List<ProductDetailDto> mapToProductDetailDtoList(List<ProductDetail> productDetails) {
        return productDetails.stream()
                .map(ProductMapper::mapToProductDetailDto)
                .collect(Collectors.toList());
    }

    public static List<ProductImageDto> mapProductImagesToDto(List<ProductImage> productImages) {
        return productImages.stream()
                .map(ProductMapper::mapProductImageToDto)
                .collect(Collectors.toList());
    }

    public static ProductImageDto mapProductImageToDto(ProductImage productImage) {
        ProductImageDto productImageDto = new ProductImageDto();
        productImageDto.setId(productImage.getId());
        productImageDto.setUrl(productImage.getUrl());
        return productImageDto;
    }

    public static List<ProductToppingDto> mapProductToppingsDto(List<ProductTopping> productToppings, List<Topping> toppings) {
        return productToppings.stream()
                .map(productTopping -> {
                    Topping topping = toppings.stream()
                            .filter(t -> t.getId().equals(productTopping.getToppingID()))
                            .findFirst()
                            .orElse(null);
                    return mapProductToppingDto(productTopping, topping);
                })
                .collect(Collectors.toList());
    }

    public static ProductToppingDto mapProductToppingDto(ProductTopping productTopping, Topping topping) {
        ProductToppingDto productToppingDto = new ProductToppingDto();
        productToppingDto.setId(productTopping.getId());
        productToppingDto.setProductID(productTopping.getProductID());
        productToppingDto.setToppingID(productTopping.getToppingID());
        if(topping != null) {
            productToppingDto.setName(topping.getName());
            productToppingDto.setPrice(topping.getPrice());
            productToppingDto.setImage(topping.getImage());
        }
        return productToppingDto;
    }


}
