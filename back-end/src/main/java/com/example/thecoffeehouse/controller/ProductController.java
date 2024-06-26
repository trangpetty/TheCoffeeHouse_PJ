package com.example.thecoffeehouse.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.thecoffeehouse.dto.ProductDto;
import com.example.thecoffeehouse.service.ProductService;

import javax.swing.text.html.parser.Entity;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto productDto) {
        return new ResponseEntity<>(productService.createProduct(productDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable String id) {
        ProductDto productDto = productService.getProductById(id);
        return ResponseEntity.ok(productDto);
    }
    
    @GetMapping
    public ResponseEntity<Page<ProductDto>> getAllProducts(@RequestParam("name") String name,@RequestParam("typeID") String typeID, @RequestParam("pageNo") int pageNo, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(pageNo, size);
        return ResponseEntity.ok(productService.getAllProducts(name, typeID, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable("id") String id, @RequestBody ProductDto updateProduct) {
        ProductDto productDto = productService.updateProduct(id, updateProduct);
        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("success");
    }

    @GetMapping("/type/{typeID}")
    public ResponseEntity<List<ProductDto>> getProductsByTypeID(@PathVariable String typeID) {
        List<ProductDto> productDtos = productService.getProductsByTypeID(typeID);
        return ResponseEntity.ok(productDtos);
    }
}
