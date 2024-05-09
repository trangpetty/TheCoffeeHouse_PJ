package com.example.thecoffeehouse.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.example.thecoffeehouse.service.FirebaseStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;
    private final FirebaseStorageService firebaseStorageService;

    public ProductServiceImpl(ProductRepository productRepository, FirebaseStorageService firebaseStorageService) {
        this.productRepository = productRepository;
        this.firebaseStorageService = firebaseStorageService;
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) throws IOException {
        String url = firebaseStorageService.uploadFile(productDto.getImage(), "products");
//        productDto.setImageUrl(url);
        Product product = ProductMapper.mapToProduct(productDto);
        product.setImage(url);
        Product savedProduct = productRepository.save(product);
        return ProductMapper.mapToProductDto(savedProduct);
    }

    @Override
    public ProductDto getProductById(Long id) {
        Product product = productRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Product does not exists"));
        return ProductMapper.mapToProductDto(product);
    }

    @Override
    public Page<ProductDto> getAllProducts(String name, Long typeID, Pageable pageable) {
        Page<Product> products = productRepository.getAllProducts(name, typeID, pageable);
        return products.map(ProductMapper::mapToProductDto);
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto) {
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
    public List<ProductDto> getProductsByTypeID(Long typeID) {
        List<Product> products = productRepository.getProductsByTypeID(typeID);
        return products.stream()
                .map(ProductMapper::mapToProductDto)
                .collect(Collectors.toList());
    }


    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Product does not exists"));

        productRepository.deleteById(id);
    }

}
