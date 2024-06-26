package com.example.thecoffeehouse.controller;

import com.example.thecoffeehouse.entity.ProductType;
import com.example.thecoffeehouse.service.ProductTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-type")
public class ProductTypeController {
    private final ProductTypeService productTypeService;

    public ProductTypeController(ProductTypeService productTypeService) {
        this.productTypeService = productTypeService;
    }

    @GetMapping
    public ResponseEntity<List<ProductType>> getAllProductTypes() {
        List<ProductType> productTypes = productTypeService.findAll();
        return ResponseEntity.ok(productTypes);
    }

    @PostMapping
    public ResponseEntity<ProductType> createProductType(@RequestBody ProductType productType) {
        return new ResponseEntity<>(productTypeService.createProductType(productType), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductType> updateProductType(@PathVariable String id, @RequestBody ProductType updateProductType) {
        ProductType productType = productTypeService.updateProductType(id, updateProductType);
        return ResponseEntity.ok(productType);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductType (@PathVariable String id) {
        productTypeService.deleteProductType(id);
        return ResponseEntity.ok("success");
    }
}
