package com.example.thecoffeehouse.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.thecoffeehouse.dto.ProductDto;

import java.util.List;

public interface ProductService {
    ProductDto createProduct(ProductDto productDto);

    ProductDto getProductById(String id);

    void deleteProduct(String id);

    Page<ProductDto> getAllProducts(String name, String typeID, Pageable pageable);

    ProductDto updateProduct(String id, ProductDto productDto);

    List<ProductDto> getProductsByTypeID(String typeID);
}
