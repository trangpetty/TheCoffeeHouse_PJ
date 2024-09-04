package com.example.thecoffeehouse.service.product;

import com.example.thecoffeehouse.dto.product.HashtagDto;
import com.example.thecoffeehouse.dto.product.ProductDto;
import com.example.thecoffeehouse.entity.product.Hashtag;

import java.util.List;

public interface HashtagService {
    Hashtag createHashtag(HashtagDto newHashtag);

    List<HashtagDto> getHashtagWithProducts();

    void deleteHashtag(Long id);

    HashtagDto updateHashtag(Long id, HashtagDto newHashtag);

    List<ProductDto> getProductsWithHashtag(String hashtag);
}
