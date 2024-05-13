package com.example.thecoffeehouse.controller;

import com.example.thecoffeehouse.dto.ProductDetailDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.thecoffeehouse.dto.ProductDto;
import com.example.thecoffeehouse.service.ProductService;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.parser.Entity;
import java.io.IOException;
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
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        ProductDto productDto = productService.getProductById(id);
        return ResponseEntity.ok(productDto);
    }
    
    @GetMapping
    public ResponseEntity<Page<ProductDto>> getAllProducts(@RequestParam("name") String name,@RequestParam("typeID") Long typeID, @RequestParam("pageNo") int pageNo, @RequestParam(defaultValue = "10") int size) {
        if (pageNo > 0) {
            pageNo = pageNo - 1;
        }
        Pageable pageable = PageRequest.of(pageNo, size);
        return ResponseEntity.ok(productService.getAllProducts(name, typeID, pageable));
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<ProductDto> updateProduct(@PathVariable("id") Long id, @ModelAttribute ProductDto updateProduct) throws IOException {
        ProductDto productDto = productService.updateProduct(id, updateProduct);
        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("success");
    }

    @GetMapping("/type/{typeID}")
    public ResponseEntity<List<ProductDto>> getProductsByTypeID(@PathVariable Long typeID) {
        List<ProductDto> productDtos = productService.getProductsByTypeID(typeID);
        return ResponseEntity.ok(productDtos);
    }
}
