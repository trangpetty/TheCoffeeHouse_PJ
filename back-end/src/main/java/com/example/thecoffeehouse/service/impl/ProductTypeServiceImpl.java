package com.example.thecoffeehouse.service.impl;

import com.example.thecoffeehouse.entity.ProductType;
import com.example.thecoffeehouse.repository.ProductTypeRepository;
import com.example.thecoffeehouse.service.ProductTypeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductTypeServiceImpl implements ProductTypeService {
    private final ProductTypeRepository productTypeRepository;

    public ProductTypeServiceImpl(ProductTypeRepository productTypeRepository) {
        this.productTypeRepository = productTypeRepository;
    }


    @Override
    public List<ProductType> findAll() {
        return productTypeRepository.findAll();
    }

    @Override
    public ProductType createProductType(ProductType productType) {
        productType.setId(UUID.randomUUID().toString().split("-")[0]);
        ProductType savedProductType = productTypeRepository.save(productType);
        return savedProductType;
    }

    @Override
    public ProductType updateProductType(String id, ProductType savedProductType) {
        ProductType productType = productTypeRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("ProductType does not exists"));

        productType.setName(savedProductType.getName());
        productType.setImage(savedProductType.getImage());
        ProductType updateProductType = productTypeRepository.save(productType);
        return updateProductType;
    }

    @Override
    public void deleteProductType(String id) {
        ProductType productType = productTypeRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("ProductType does not exists"));
        productTypeRepository.deleteById(id);
    }
}
