package com.example.thecoffeehouse.service.product;


import com.example.thecoffeehouse.entity.product.UserProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.thecoffeehouse.dto.product.ProductDto;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    ProductDto createProduct(ProductDto productDto);

    ProductDto getProductById(Long id);

    void deleteProduct(Long id);

    Page<ProductDto> getAllProducts(String name, Long typeID, Pageable pageable);

    ProductDto updateProduct(Long id, ProductDto productDto) throws IOException;

    List<ProductDto> getProductsByTypeID(Long typeID);

    UserProduct likeProduct(Long userId, Long productId);

    boolean isProductLikedByUser(Long userId, Long productId);

    ProductDto getMostFavoriteProduct();

    ProductDto getBestSellingProduct();

    ProductDto getHighestRatedProduct();
}
