package com.example.thecoffeehouse.controller.product;

import com.example.thecoffeehouse.entity.product.ProductType;
import com.example.thecoffeehouse.service.product.ProductTypeService;
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

    @GetMapping("/{id}")
    public ResponseEntity<ProductType> getProductType(@PathVariable Long id) {
        ProductType productType = productTypeService.getProductType(id);
        return ResponseEntity.ok(productType);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductType> updateProductType(@PathVariable Long id, @RequestBody ProductType updateProductType) {
        ProductType productType = productTypeService.updateProductType(id, updateProductType);
        return ResponseEntity.ok(productType);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductType (@PathVariable Long id) {
        productTypeService.deleteProductType(id);
        return ResponseEntity.ok("success");
    }
}
