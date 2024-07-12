package com.example.thecoffeehouse.controller.product;

import com.example.thecoffeehouse.dto.LikeProductRequest;
import com.example.thecoffeehouse.entity.product.ProductReview;
import com.example.thecoffeehouse.entity.product.UserProduct;
import com.example.thecoffeehouse.repository.product.ProductReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.thecoffeehouse.dto.product.ProductDto;
import com.example.thecoffeehouse.service.product.ProductService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;
    private final ProductReviewRepository productReviewRepository;

    public ProductController(ProductService productService, ProductReviewRepository productReviewRepository) {
        this.productService = productService;
        this.productReviewRepository = productReviewRepository;
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
    public ResponseEntity<Page<ProductDto>> getAllProducts(@RequestParam(required = false) String name,@RequestParam(required = false) Long typeID, @RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int size) {
        if (pageNo > 0) {
            pageNo = pageNo - 1;
        }
        Pageable pageable = PageRequest.of(pageNo, size);
        return ResponseEntity.ok(productService.getAllProducts(name, typeID, pageable));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable("id") Long id, @RequestBody ProductDto updateProduct) throws IOException {
        ProductDto productDto = productService.updateProduct(id, updateProduct);
        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("success");
    }

    @GetMapping("/type/{typeID}")
    public ResponseEntity<List<ProductDto>> getProductsByTypeID(@PathVariable Long typeID, @RequestParam(name = "userID", required = false) Optional<Long> userID) {
        Long userIdValue = userID.orElse(null);
        List<ProductDto> productDtos = productService.getProductsByTypeIDAndUserID(typeID, userIdValue);
        return ResponseEntity.ok(productDtos);
    }


    @PostMapping("/rate")
    public ResponseEntity<ProductReview> createProductReview(@RequestBody ProductReview productReview) {
        return new ResponseEntity<>(productReviewRepository.save(productReview), HttpStatus.CREATED);
    }

    @PostMapping("/like")
    public UserProduct likeProduct(@RequestBody LikeProductRequest request) {
        return productService.likeProduct(request.getUserId(), request.getProductId());
    }

    @GetMapping("/most-like")
    public ResponseEntity<List<ProductDto>> getMostFavoriteProducts() {
        return ResponseEntity.ok(productService.getMostFavoriteProducts());
    }

    @GetMapping("/best-seller")
    public ResponseEntity<List<ProductDto>> getBestSellingProducts() {
        return ResponseEntity.ok(productService.getBestSellingProducts());
    }

    @GetMapping("/highest-rate")
    public ResponseEntity<List<ProductDto>> getHighestRatedProducts() {
        return ResponseEntity.ok(productService.getHighestRatedProducts());
    }

    @GetMapping("/newest")
    public ResponseEntity<List<ProductDto>> getNewestProducts() {
        return ResponseEntity.ok(productService.getNewestProducts());
    }
}
