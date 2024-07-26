package com.example.thecoffeehouse.service.product;


import com.example.thecoffeehouse.dto.product.ProductSalesDto;
import com.example.thecoffeehouse.entity.product.Product;
import com.example.thecoffeehouse.entity.product.UserProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.thecoffeehouse.dto.product.ProductDto;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface ProductService {
    ProductDto createProduct(ProductDto productDto);

    ProductDto getProductById(Long id);

    void deleteProduct(Long id);

    Page<ProductDto> getAllProducts(String name, Long typeID, Pageable pageable);

    ProductDto updateProduct(Long id, ProductDto productDto) throws IOException;

    List<ProductDto> getProductsByTypeIDAndUserID(Long typeID, Long userID);

    List<ProductDto> getProductsByTypeName(String name);

    UserProduct likeProduct(Long userId, Long productId);

    boolean isProductLikedByUser(Long userId, Long productId);

    List<ProductDto> getMostFavoriteProducts();

    List<ProductDto> getBestSellingProducts();

    List<ProductDto> getHighestRatedProducts();

    List<ProductDto> getNewestProducts();

    List<ProductSalesDto> getTopProducts(String reportType, int year, Integer period);

    List<ProductSalesDto> getTopProductsByMonthAndWeek(int month, int week);

    List<ProductSalesDto> getTopProductsByDate(LocalDate date);

}
