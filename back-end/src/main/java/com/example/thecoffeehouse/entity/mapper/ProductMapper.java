package com.example.thecoffeehouse.entity.mapper;

import com.example.thecoffeehouse.dto.ProductDetailDto;
import com.example.thecoffeehouse.dto.ProductDto;
import com.example.thecoffeehouse.dto.ProductImageDto;
import com.example.thecoffeehouse.entity.product.Product;
import com.example.thecoffeehouse.entity.product.ProductDetail;
import com.example.thecoffeehouse.entity.product.ProductImage;

import java.util.List;
import java.util.stream.Collectors;
public class ProductMapper {

    public static ProductDto mapToProductDto(Product product, List<ProductDetailDto> productDetailDtos, List<ProductImageDto> productImages) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setTypeID(product.getTypeID());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setCreateTime(product.getCreateTime());
        productDto.setModifyTime(product.getModifyTime());
        productDto.setImages(productImages);
        productDto.setProductSizes(productDetailDtos);
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


}
