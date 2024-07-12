package com.example.thecoffeehouse.service.product;

import com.example.thecoffeehouse.entity.product.ProductType;

import java.util.List;

public interface ProductTypeService {
    List<ProductType> findAll();

    ProductType createProductType(ProductType productType);

    ProductType updateProductType(Long id, ProductType productType);

    ProductType getProductType(Long id);

    void deleteProductType(Long id);
}
