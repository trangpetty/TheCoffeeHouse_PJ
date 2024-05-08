package com.example.thecoffeehouse.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.thecoffeehouse.dto.ProductDto;
import com.example.thecoffeehouse.entity.Product;
import com.example.thecoffeehouse.entity.mapper.ProductMapper;
import com.example.thecoffeehouse.repository.ProductRepository;
import com.example.thecoffeehouse.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;


    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = ProductMapper.mapToProduct(productDto);
        Product savedProduct = productRepository.save(product);
        return ProductMapper.mapToProductDto(savedProduct);
    }

    @Override
    public ProductDto getProductById(String id) {
        Product product = productRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Product does not exists"));
        return ProductMapper.mapToProductDto(product);
    }

    @Override
    public Page<ProductDto> getAllProducts(String name, String typeID, Pageable pageable) {
        Page<Product> products = productRepository.getAllProducts(name, typeID, pageable);
        return products.map(ProductMapper::mapToProductDto);
    }

    @Override
    public ProductDto updateProduct(String id, ProductDto productDto) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Product does not exists")
        );

        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setSize(productDto.getSize());
        product.setTypeID(productDto.getTypeID());

        Product updatedProduct = productRepository.save(product);
        return ProductMapper.mapToProductDto(updatedProduct);
    }

    @Override
    public List<ProductDto> getProductsByTypeID(String typeID) {
        List<Product> products = productRepository.getProductsByTypeID(typeID);
        return products.stream()
                .map(ProductMapper::mapToProductDto)
                .collect(Collectors.toList());
    }


    @Override
    public void deleteProduct(String id) {
        Product product = productRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Product does not exists"));

        productRepository.deleteById(id);
    }

}
