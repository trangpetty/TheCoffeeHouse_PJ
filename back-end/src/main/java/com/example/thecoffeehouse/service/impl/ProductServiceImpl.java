package com.example.thecoffeehouse.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.thecoffeehouse.dto.ProductDetailDto;
import com.example.thecoffeehouse.dto.ProductImageDto;
import com.example.thecoffeehouse.entity.product.ProductDetail;
import com.example.thecoffeehouse.entity.product.ProductImage;
import com.example.thecoffeehouse.repository.product.ProductDetailRepository;
import com.example.thecoffeehouse.repository.product.ProductImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.thecoffeehouse.dto.ProductDto;
import com.example.thecoffeehouse.entity.product.Product;
import com.example.thecoffeehouse.entity.mapper.ProductMapper;
import com.example.thecoffeehouse.repository.product.ProductRepository;
import com.example.thecoffeehouse.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;
    private final ProductDetailRepository productDetailRepository;
    private final ProductImageRepository productImageRepository;

    public ProductServiceImpl(ProductRepository productRepository, ProductDetailRepository productDetailRepository, ProductImageRepository productImageRepository) {
        this.productRepository = productRepository;
        this.productDetailRepository = productDetailRepository;
        this.productImageRepository = productImageRepository;
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = ProductMapper.mapToProduct(productDto);
        Product savedProduct = productRepository.save(product);
        // Map and save product sizes to the saved product
        saveProductSizes(productDto.getProductSizes(), savedProduct);
        saveProductImages(productDto.getImages(), savedProduct);
        List<ProductDetailDto> productDetailDtos = productDto.getProductSizes();
        List<ProductImageDto> productImageDtos = productDto.getImages();
        return ProductMapper.mapToProductDto(savedProduct, productDetailDtos, productImageDtos);
    }

    private void saveProductSizes(List<ProductDetailDto> productSizes, Product product) {

        for (ProductDetailDto sizeDto : productSizes) {
            ProductDetail productDetail = new ProductDetail();
            productDetail.setProductID(product.getId());
            productDetail.setSize(sizeDto.getSize());
            productDetail.setSurcharge(sizeDto.getSurcharge());
            productDetailRepository.save(productDetail);
        }
    }

    private void saveProductImages(List<ProductImageDto> productImages, Product product) {
        for (ProductImageDto imageDto : productImages) {
            ProductImage productImage = new ProductImage();
            productImage.setProductID(product.getId());
            productImage.setUrl(imageDto.getUrl());
            productImageRepository.save(productImage);
        }
    }

    @Override
    public ProductDto getProductById(Long id) {
        Product product = productRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Product does not exists"));
        List<ProductDetail> productDetails = productDetailRepository.findAllByProductID(id);
        List<ProductImage> productImages = productImageRepository.findAllByProductID(id);

        // Map product details to DTOs
        List<ProductDetailDto> productDetailDtos = ProductMapper.mapToProductDetailDtoList(productDetails);
        List<ProductImageDto> productImageDtos = ProductMapper.mapProductImagesToDto(productImages);
        return ProductMapper.mapToProductDto(product, productDetailDtos, productImageDtos);
    }

    @Override
    public Page<ProductDto> getAllProducts(String name, Long typeID, Pageable pageable) {
        Page<Product> products = productRepository.getAllProducts(name, typeID, pageable);
        return products.map(product -> {
            List<ProductDetail> productDetails = productDetailRepository.findAllByProductID(product.getId());
            List<ProductImage> productImages = productImageRepository.findAllByProductID(product.getId());
            List<ProductDetailDto> productDetailDtos = ProductMapper.mapToProductDetailDtoList(productDetails);
            List<ProductImageDto> productImageDtos = ProductMapper.mapProductImagesToDto(productImages);
            return ProductMapper.mapToProductDto(product, productDetailDtos, productImageDtos);
        });
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product does not exist"));

        // Update the fields of the Product entity
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setTypeID(productDto.getTypeID());

        // Save the updated Product entity
        Product updatedProduct = productRepository.save(product);

        // Save or update ProductDetail entities
        saveOrUpdateProductDetails(productDto.getProductSizes(), updatedProduct);

        // Save or update ProductImage entities
        saveOrUpdateProductImages(productDto.getImages(), updatedProduct);

        removeProductImages(productDto.getRemovedImages());
        removeProductDetails(productDto.getRemovedProductSizes());

        // Retrieve updated ProductDetail entities
        List<ProductDetail> productDetails = productDetailRepository.findAllByProductID(id);
        // Map ProductDetail entities to DTOs
        List<ProductDetailDto> productDetailDtos = ProductMapper.mapToProductDetailDtoList(productDetails);

        // Retrieve updated ProductImage URLs
        List<ProductImage> productImages = productImageRepository.findAllByProductID(id);
        List<ProductImageDto> productImageDtos = ProductMapper.mapProductImagesToDto(productImages);


        // Map Product and associated entities to a ProductDto
        return ProductMapper.mapToProductDto(updatedProduct, productDetailDtos, productImageDtos);
    }

    private void saveOrUpdateProductDetails(List<ProductDetailDto> productDetailDtos, Product product) {
        for (ProductDetailDto productDetailDto : productDetailDtos) {
            if(productDetailDto.getId() != null) {
                 ProductDetail productDetail = productDetailRepository.findById(productDetailDto.getId())
                         .orElseGet(ProductDetail::new);

                 productDetail.setProductID(product.getId());
                 productDetail.setSize(productDetailDto.getSize());
                 productDetail.setSurcharge(productDetailDto.getSurcharge());
                 productDetailRepository.save(productDetail);
            }
            else {
                ProductDetail productDetail = new ProductDetail();
                productDetail.setProductID(product.getId());
                productDetail.setSize(productDetailDto.getSize());
                productDetail.setSurcharge(productDetailDto.getSurcharge());
                productDetailRepository.save(productDetail);
            }
        }
    }

    private void saveOrUpdateProductImages(List<ProductImageDto> productImages, Product product) {
        // Logic to update or save ProductImage entities based on changes in the ProductDto
        for (ProductImageDto imageUrl : productImages) {
            if(imageUrl.getId() != null) {
                ProductImage productImage = productImageRepository.findById(imageUrl.getId())
                        .orElseGet(ProductImage::new);

                productImage.setProductID(product.getId());
                productImage.setUrl(imageUrl.getUrl());
                productImageRepository.save(productImage);
            }
            else {
                ProductImage productImage = new ProductImage();
                productImage.setProductID(product.getId());
                productImage.setUrl(imageUrl.getUrl());
                productImageRepository.save(productImage);
            }
        }
    }

    private void removeProductImages(List<ProductImageDto> removedImageDtos) {
        for (ProductImageDto image: removedImageDtos) {
            productImageRepository.deleteById(image.getId());
        }
    }

    private void removeProductDetails(List<ProductDetailDto> removedDetailDtos) {
        for (ProductDetailDto size: removedDetailDtos) {
            log.info("Removing product detail {}", size.getId());
            productDetailRepository.deleteById(size.getId());
        }
    }


    @Override
    public List<ProductDto> getProductsByTypeID(Long typeID) {
        List<Product> products = productRepository.getProductsByTypeID(typeID);
        return products.stream()
                .map(product -> {
                    List<ProductDetail> productDetails = productDetailRepository.findAllByProductID(product.getId());
                    List<ProductDetailDto> productDetailDtos = ProductMapper.mapToProductDetailDtoList(productDetails);
                    List<ProductImage> productImages = productImageRepository.findAllByProductID(product.getId());
                    List<ProductImageDto> productImageDtos = ProductMapper.mapProductImagesToDto(productImages);
                    return ProductMapper.mapToProductDto(product, productDetailDtos, productImageDtos);
                })
                .collect(Collectors.toList());
    }


    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Product does not exists"));

        productDetailRepository.deleteByProductID(id);
        productImageRepository.deleteByProductID(id);
        productRepository.deleteById(id);
    }


}
