package com.example.thecoffeehouse.controller.product;

import com.example.thecoffeehouse.dto.product.HashtagDto;
import com.example.thecoffeehouse.dto.product.ProductDto;
import com.example.thecoffeehouse.entity.product.Hashtag;
import com.example.thecoffeehouse.service.product.HashtagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hashtags")
public class HashtagController {

    private final HashtagService hashtagService;

    public HashtagController(HashtagService hashtagService) {
        this.hashtagService = hashtagService;
    }

    @PostMapping
    public ResponseEntity<Hashtag> createHashtag(@RequestBody HashtagDto hashtagDto) {
        return new ResponseEntity<>(hashtagService.createHashtag(hashtagDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<HashtagDto>> getAllHashtags() {
        return ResponseEntity.ok(hashtagService.getHashtagWithProducts());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHashtag(@PathVariable Long id) {
        hashtagService.deleteHashtag(id);
        return ResponseEntity.ok("success");
    }

    @PutMapping("/{id}")
    public ResponseEntity<HashtagDto> updateHashtag(@PathVariable Long id, @RequestBody HashtagDto hashtagDto) {
        return ResponseEntity.ok(hashtagService.updateHashtag(id, hashtagDto));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> searchHashtag(@RequestParam("name") String name) {
        return ResponseEntity.ok(hashtagService.getProductsWithHashtag(name));
    }
}
