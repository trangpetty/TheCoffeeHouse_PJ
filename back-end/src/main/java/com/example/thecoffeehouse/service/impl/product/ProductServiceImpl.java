package com.example.thecoffeehouse.service.impl.product;

import java.util.List;
import java.util.stream.Collectors;

import com.example.thecoffeehouse.dto.product.ProductDetailDto;
import com.example.thecoffeehouse.dto.product.ProductImageDto;
import com.example.thecoffeehouse.dto.product.ProductToppingDto;
import com.example.thecoffeehouse.entity.product.*;
import com.example.thecoffeehouse.repository.product.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.thecoffeehouse.dto.product.ProductDto;
import com.example.thecoffeehouse.entity.mapper.ProductMapper;
import com.example.thecoffeehouse.service.product.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;
    private final ProductDetailRepository productDetailRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductToppingRepository productToppingRepository;
    private final ToppingRepository toppingRepository;
    private final UserProductRepository userProductRepository;

    public ProductServiceImpl(ProductRepository productRepository, ProductDetailRepository productDetailRepository, ProductImageRepository productImageRepository, ProductToppingRepository productToppingRepository, ToppingRepository toppingRepository, UserProductRepository userProductRepository) {
        this.productRepository = productRepository;
        this.productDetailRepository = productDetailRepository;
        this.productImageRepository = productImageRepository;
        this.productToppingRepository = productToppingRepository;
        this.toppingRepository = toppingRepository;
        this.userProductRepository = userProductRepository;
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = ProductMapper.mapToProduct(productDto);
        Product savedProduct = productRepository.save(product);

        saveOrUpdateProductDetails(productDto.getProductSizes(), savedProduct);
        saveOrUpdateProductImages(productDto.getImages(), savedProduct);
        saveOrUpdateProductToppings(productDto.getToppings(), savedProduct);
        return ProductMapper.mapToProductDto(savedProduct);
    }

    @Override
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product does not exist"));

        List<Topping> toppings = toppingRepository.findAll();
        List<ProductDetailDto> productDetailDtos = ProductMapper.mapToProductDetailDtoList(productDetailRepository.findAllByProductID(id));
        List<ProductImageDto> productImageDtos = ProductMapper.mapProductImagesToDto(productImageRepository.findAllByProductID(id));
        List<ProductToppingDto> productToppingDtos = ProductMapper.mapProductToppingsDto(productToppingRepository.findByProductID(id), toppings);

        return ProductMapper.mapToProductDtoWithDetails(product, productDetailDtos, productImageDtos, productToppingDtos);
    }

    @Override
    public Page<ProductDto> getAllProducts(String name, Long typeID, Pageable pageable) {
        Page<Product> products = productRepository.getAllProducts(name, typeID, pageable);
        List<Topping> toppings = toppingRepository.findAll();
        List<Long> userIds = userProductRepository.findDistinctUserIds();

        return products.map(product -> {
            List<ProductDetailDto> productDetailDtos = ProductMapper.mapToProductDetailDtoList(productDetailRepository.findAllByProductID(product.getId()));
            List<ProductImageDto> productImageDtos = ProductMapper.mapProductImagesToDto(productImageRepository.findAllByProductID(product.getId()));
            List<ProductToppingDto> productToppingDtos = ProductMapper.mapProductToppingsDto(productToppingRepository.findByProductID(product.getId()), toppings);

            ProductDto productDto = ProductMapper.mapToProductDtoWithDetails(product, productDetailDtos, productImageDtos, productToppingDtos);
            productDto.setLiked(userIds.stream().anyMatch(userId -> isProductLikedByUser(userId, productDto.getId())));

            return productDto;
        });
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product does not exist"));

        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setTypeID(productDto.getTypeID());
        product.setDescription(productDto.getDescription());

        Product updatedProduct = productRepository.save(product);

        saveOrUpdateProductDetails(productDto.getProductSizes(), updatedProduct);
        saveOrUpdateProductImages(productDto.getImages(), updatedProduct);
        saveOrUpdateProductToppings(productDto.getToppings(), updatedProduct);

        removeProductEntities(productDto.getRemovedProductSizes(), productDto.getRemovedImages(), productDto.getRemovedToppings());

        List<Topping> toppings = toppingRepository.findAll();
        List<ProductDetailDto> productDetailDtos = ProductMapper.mapToProductDetailDtoList(productDetailRepository.findAllByProductID(id));
        List<ProductImageDto> productImageDtos = ProductMapper.mapProductImagesToDto(productImageRepository.findAllByProductID(id));
        List<ProductToppingDto> productToppingDtos = ProductMapper.mapProductToppingsDto(productToppingRepository.findByProductID(id), toppings);

        return ProductMapper.mapToProductDtoWithDetails(updatedProduct, productDetailDtos, productImageDtos, productToppingDtos);
    }

    @Override
    public List<ProductDto> getProductsByTypeIDAndUserID(Long typeID, Long userID) {
        List<Product> products = productRepository.getProductsByTypeID(typeID);
        List<Topping> toppings = toppingRepository.findAll();

        return products.stream()
                .map(product -> {
                    List<ProductDetailDto> productDetailDtos = ProductMapper.mapToProductDetailDtoList(productDetailRepository.findAllByProductID(product.getId()));
                    List<ProductImageDto> productImageDtos = ProductMapper.mapProductImagesToDto(productImageRepository.findAllByProductID(product.getId()));
                    List<ProductToppingDto> productToppingDtos = ProductMapper.mapProductToppingsDto(productToppingRepository.findByProductID(product.getId()), toppings);

                    ProductDto productDto = ProductMapper.mapToProductDtoWithDetails(product, productDetailDtos, productImageDtos, productToppingDtos);
                    if (userID != null) {
                        productDto.setLiked(isProductLikedByUser(userID, productDto.getId()));
                    }

                    return productDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getProductsByTypeName(String name) {
        List<Product> products = productRepository.getProductsByTypeName(name);
        List<Topping> toppings = toppingRepository.findAll();

        return products.stream()
                .map(product -> {
                    List<ProductDetailDto> productDetailDtos = ProductMapper.mapToProductDetailDtoList(productDetailRepository.findAllByProductID(product.getId()));
                    List<ProductImageDto> productImageDtos = ProductMapper.mapProductImagesToDto(productImageRepository.findAllByProductID(product.getId()));
                    List<ProductToppingDto> productToppingDtos = ProductMapper.mapProductToppingsDto(productToppingRepository.findByProductID(product.getId()), toppings);

                    ProductDto productDto = ProductMapper.mapToProductDtoWithDetails(product, productDetailDtos, productImageDtos, productToppingDtos);

                    return productDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public UserProduct likeProduct(Long userId, Long productId) {
        UserProduct userProduct = userProductRepository.findByUserIdAndProductId(userId, productId);

        if (userProduct == null) {
            userProduct = new UserProduct();
            userProduct.setUserId(userId);
            userProduct.setProductId(productId);
            userProduct.setLiked(true); // Set liked status to true
        } else {
            userProduct.setLiked(!userProduct.getLiked()); // Toggle liked status
        }

        return userProductRepository.save(userProduct);
    }

    @Override
    public boolean isProductLikedByUser(Long userId, Long productId) {
        UserProduct userProduct = userProductRepository.findByUserIdAndProductId(userId, productId);
        return userProduct != null && userProduct.getLiked();
    }

    @Override
    public List<ProductDto> getMostFavoriteProducts() {
        List<Product> products = productRepository.getTotalLikes();
        List<Topping> toppings = toppingRepository.findAll();
        return products.stream().map(product -> {
            List<ProductDetailDto> productDetailDtos = ProductMapper.mapToProductDetailDtoList(productDetailRepository.findAllByProductID(product.getId()));
            List<ProductImageDto> productImageDtos = ProductMapper.mapProductImagesToDto(productImageRepository.findAllByProductID(product.getId()));
            List<ProductToppingDto> productToppingDtos = ProductMapper.mapProductToppingsDto(productToppingRepository.findByProductID(product.getId()), toppings);

            ProductDto productDto = ProductMapper.mapToProductDtoWithDetails(product, productDetailDtos, productImageDtos, productToppingDtos);
            return productDto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getBestSellingProducts() {
        List<Product> products = productRepository.getTotalQuantity();
        List<Topping> toppings = toppingRepository.findAll();
        return products.stream().map(product -> {
            List<ProductDetailDto> productDetailDtos = ProductMapper.mapToProductDetailDtoList(productDetailRepository.findAllByProductID(product.getId()));
            List<ProductImageDto> productImageDtos = ProductMapper.mapProductImagesToDto(productImageRepository.findAllByProductID(product.getId()));
            List<ProductToppingDto> productToppingDtos = ProductMapper.mapProductToppingsDto(productToppingRepository.findByProductID(product.getId()), toppings);

            ProductDto productDto = ProductMapper.mapToProductDtoWithDetails(product, productDetailDtos, productImageDtos, productToppingDtos);
            return productDto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getHighestRatedProducts() {
        List<Product> products = productRepository.getAvgRating();
        List<Topping> toppings = toppingRepository.findAll();
        return products.stream().map(product -> {
            List<ProductDetailDto> productDetailDtos = ProductMapper.mapToProductDetailDtoList(productDetailRepository.findAllByProductID(product.getId()));
            List<ProductImageDto> productImageDtos = ProductMapper.mapProductImagesToDto(productImageRepository.findAllByProductID(product.getId()));
            List<ProductToppingDto> productToppingDtos = ProductMapper.mapProductToppingsDto(productToppingRepository.findByProductID(product.getId()), toppings);

            ProductDto productDto = ProductMapper.mapToProductDtoWithDetails(product, productDetailDtos, productImageDtos, productToppingDtos);
            return productDto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getNewestProducts() {
        List<Product> products = productRepository.getNewestProducts();
        List<Topping> toppings = toppingRepository.findAll();
        return products.stream().map(product -> {
            List<ProductDetailDto> productDetailDtos = ProductMapper.mapToProductDetailDtoList(productDetailRepository.findAllByProductID(product.getId()));
            List<ProductImageDto> productImageDtos = ProductMapper.mapProductImagesToDto(productImageRepository.findAllByProductID(product.getId()));
            List<ProductToppingDto> productToppingDtos = ProductMapper.mapProductToppingsDto(productToppingRepository.findByProductID(product.getId()), toppings);

            ProductDto productDto = ProductMapper.mapToProductDtoWithDetails(product, productDetailDtos, productImageDtos, productToppingDtos);
            return productDto;
        }).collect(Collectors.toList());
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Product does not exists"));

        productDetailRepository.deleteByProductID(id);
        productImageRepository.deleteByProductID(id);
        productToppingRepository.deleteByProductID(id);
        productRepository.deleteById(id);
    }

    private void saveOrUpdateProductDetails(List<ProductDetailDto> productDetailDtos, Product product) {
        List<ProductDetail> existingDetails = productDetailRepository.findAllByProductID(product.getId());

        for (ProductDetailDto dto : productDetailDtos) {
            ProductDetail detail = existingDetails.stream()
                    .filter(d -> d.getId().equals(dto.getId()))
                    .findFirst()
                    .orElse(new ProductDetail());

            detail.setProductID(product.getId());
            detail.setSize(dto.getSize());
            detail.setSurcharge(dto.getSurcharge());

            productDetailRepository.save(detail);
        }

        List<Long> updatedDetailIds = productDetailDtos.stream()
                .map(ProductDetailDto::getId)
                .toList();
        existingDetails.stream()
                .filter(detail -> !updatedDetailIds.contains(detail.getId()))
                .forEach(productDetailRepository::delete);
    }

    private void saveOrUpdateProductImages(List<ProductImageDto> productImages, Product product) {
        List<ProductImage> existingImages = productImageRepository.findAllByProductID(product.getId());

        for (ProductImageDto dto : productImages) {
            ProductImage image = existingImages.stream()
                    .filter(i -> i.getId().equals(dto.getId()))
                    .findFirst()
                    .orElse(new ProductImage());

            image.setProductID(product.getId());
            image.setUrl(dto.getUrl());

            productImageRepository.save(image);
        }

        List<Long> updatedImageIds = productImages.stream()
                .map(ProductImageDto::getId)
                .toList();
        existingImages.stream()
                .filter(image -> !updatedImageIds.contains(image.getId()))
                .forEach(productImageRepository::delete);
    }

    private void saveOrUpdateProductToppings(List<ProductToppingDto> productToppingDtos, Product product) {
        List<ProductTopping> existingToppings = productToppingRepository.findByProductID(product.getId());

        for (ProductToppingDto dto : productToppingDtos) {
            ProductTopping topping = existingToppings.stream()
                    .filter(t -> t.getId().equals(dto.getId()))
                    .findFirst()
                    .orElse(new ProductTopping());

            topping.setProductID(product.getId());
            topping.setToppingID(dto.getToppingID());

            productToppingRepository.save(topping);
        }

        List<Long> updatedToppingIds = productToppingDtos.stream()
                .map(ProductToppingDto::getId)
                .toList();
        existingToppings.stream()
                .filter(topping -> !updatedToppingIds.contains(topping.getId()))
                .forEach(productToppingRepository::delete);
    }

    private void removeProductEntities(List<ProductDetailDto> removedProductSizes, List<ProductImageDto> removedImages, List<ProductToppingDto> removedToppings) {
        for (ProductDetailDto detail : removedProductSizes) {
            productDetailRepository.deleteById(detail.getId());
        }

        for (ProductImageDto image : removedImages) {
            productImageRepository.deleteById(image.getId());
        }

        for (ProductToppingDto topping : removedToppings) {
            productToppingRepository.deleteById(topping.getId());
        }
    }

}
