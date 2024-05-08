package com.example.thecoffeehouse.service;

import com.example.thecoffeehouse.entity.ProductType;

import java.util.List;

public interface ProductTypeService {
    List<ProductType> findAll();

    ProductType createProductType(ProductType productType);

    ProductType updateProductType(String id, ProductType productType);

    void deleteProductType(String id);
}
