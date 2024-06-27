package com.example.thecoffeehouse.service.impl.product;

import com.example.thecoffeehouse.entity.ProductType;
import com.example.thecoffeehouse.repository.product.ProductTypeRepository;
import com.example.thecoffeehouse.service.product.ProductTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return productTypeRepository.save(productType);
    }

    @Override
    public ProductType updateProductType(Long id, ProductType savedProductType) {
        ProductType productType = productTypeRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("ProductType does not exists"));

        productType.setName(savedProductType.getName());
        productType.setImage(savedProductType.getImage());
        return productTypeRepository.save(productType);
    }

    @Override
    public void deleteProductType(Long id) {
        ProductType productType = productTypeRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("ProductType does not exists"));
        productTypeRepository.deleteById(id);
    }
}
