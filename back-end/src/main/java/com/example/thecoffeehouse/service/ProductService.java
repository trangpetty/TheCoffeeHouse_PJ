package com.example.thecoffeehouse.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.thecoffeehouse.dto.ProductDto;

public interface ProductService {
    ProductDto createProduct(ProductDto productDto);

    ProductDto getProductById(Long id);

    void deleteProduct(Long id);

    Page<ProductDto> getAllProducts(String name, Long typeID, Pageable pageable);

    ProductDto updateProduct(Long id, ProductDto productDto);
}
