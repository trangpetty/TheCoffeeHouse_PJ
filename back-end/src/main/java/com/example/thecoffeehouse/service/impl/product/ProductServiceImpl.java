package com.example.thecoffeehouse.service.impl.product;

import java.util.List;
import java.util.stream.Collectors;

import com.example.thecoffeehouse.dto.ProductDetailDto;
import com.example.thecoffeehouse.dto.ProductImageDto;
import com.example.thecoffeehouse.dto.ProductToppingDto;
import com.example.thecoffeehouse.entity.product.*;
import com.example.thecoffeehouse.repository.product.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.thecoffeehouse.dto.ProductDto;
import com.example.thecoffeehouse.entity.mapper.ProductMapper;
import com.example.thecoffeehouse.service.product.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductDetailRepository productDetailRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductToppingRepository productToppingRepository;
    private final ToppingRepository toppingRepository;

    public ProductServiceImpl(ProductRepository productRepository, ProductDetailRepository productDetailRepository, ProductImageRepository productImageRepository, ProductToppingRepository productToppingRepository, ToppingRepository toppingRepository) {
        this.productRepository = productRepository;
        this.productDetailRepository = productDetailRepository;
        this.productImageRepository = productImageRepository;
        this.productToppingRepository = productToppingRepository;
        this.toppingRepository = toppingRepository;
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = ProductMapper.mapToProduct(productDto);
        Product savedProduct = productRepository.save(product);

        // Map and save product sizes to the saved product
        saveProductSizes(productDto.getProductSizes(), savedProduct);
        saveProductImages(productDto.getImages(), savedProduct);
        saveProductToppings(productDto.getToppings(), savedProduct);

        List<ProductDetailDto> productDetailDtos = productDto.getProductSizes();
        List<ProductImageDto> productImageDtos = productDto.getImages();
        List<ProductToppingDto> productToppings = productDto.getToppings();
        return ProductMapper.mapToProductDto(savedProduct, productDetailDtos, productImageDtos, productToppings);
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

    private void saveProductToppings(List<ProductToppingDto> productToppingDtos, Product product) {

        for (ProductToppingDto productToppingDto : productToppingDtos) {
            ProductTopping productTopping = new ProductTopping();
            productTopping.setProductID(product.getId());
            productTopping.setToppingID(productToppingDto.getToppingID());
            productToppingRepository.save(productTopping);
        }
    }

    @Override
    public ProductDto getProductById(Long id) {
        Product product = productRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Product does not exists"));
        List<Topping> toppings = toppingRepository.findAll();

        List<ProductDetail> productDetails = productDetailRepository.findAllByProductID(id);
        List<ProductImage> productImages = productImageRepository.findAllByProductID(id);
        List<ProductTopping> productToppings = productToppingRepository.findByProductID(id);

        // Map product details to DTOs
        List<ProductDetailDto> productDetailDtos = ProductMapper.mapToProductDetailDtoList(productDetails);
        List<ProductImageDto> productImageDtos = ProductMapper.mapProductImagesToDto(productImages);
        List<ProductToppingDto> productToppingDtos = ProductMapper.mapProductToppingsDto(productToppings, toppings);
        return ProductMapper.mapToProductDto(product, productDetailDtos, productImageDtos, productToppingDtos);
    }

    @Override
    public Page<ProductDto> getAllProducts(String name, Long typeID, Pageable pageable) {
        Page<Product> products = productRepository.getAllProducts(name, typeID, pageable);
        List<Topping> toppings = toppingRepository.findAll();

        return products.map(product -> {
            List<ProductDetail> productDetails = productDetailRepository.findAllByProductID(product.getId());
            List<ProductImage> productImages = productImageRepository.findAllByProductID(product.getId());
            List<ProductTopping> productToppings = productToppingRepository.findByProductID(product.getId());

            List<ProductDetailDto> productDetailDtos = ProductMapper.mapToProductDetailDtoList(productDetails);
            List<ProductImageDto> productImageDtos = ProductMapper.mapProductImagesToDto(productImages);
            List<ProductToppingDto> productToppingDtos = ProductMapper.mapProductToppingsDto(productToppings, toppings);

            return ProductMapper.mapToProductDto(product, productDetailDtos, productImageDtos, productToppingDtos);
        });
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product does not exist"));
        List<Topping> toppings = toppingRepository.findAll();

        // Update the fields of the Product entity
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setTypeID(productDto.getTypeID());
        product.setDescription(productDto.getDescription());

        // Save the updated Product entity
        Product updatedProduct = productRepository.save(product);

        // Save or update ProductDetail entities
        saveOrUpdateProductDetails(productDto.getProductSizes(), updatedProduct);

        // Save or update ProductImage entities
        saveOrUpdateProductImages(productDto.getImages(), updatedProduct);

        // Save or update ProductTopping entities
        saveOrUpdateProductToppings(productDto.getToppings(), updatedProduct);

        removeProductImages(productDto.getRemovedImages());
        removeProductDetails(productDto.getRemovedProductSizes());
        removeProductToppings(productDto.getRemovedToppings());

        // Retrieve updated ProductDetail entities
        List<ProductDetail> productDetails = productDetailRepository.findAllByProductID(id);
        // Map ProductDetail entities to DTOs
        List<ProductDetailDto> productDetailDtos = ProductMapper.mapToProductDetailDtoList(productDetails);

        // Retrieve updated ProductImage URLs
        List<ProductImage> productImages = productImageRepository.findAllByProductID(id);
        List<ProductImageDto> productImageDtos = ProductMapper.mapProductImagesToDto(productImages);

        // Retrieve updated ProductTopping entities
        List<ProductTopping> productToppings = productToppingRepository.findByProductID(id);
        List<ProductToppingDto> productToppingDtos = ProductMapper.mapProductToppingsDto(productToppings, toppings);


        // Map Product and associated entities to a ProductDto
        return ProductMapper.mapToProductDto(updatedProduct, productDetailDtos, productImageDtos, productToppingDtos);
    }

    private void saveOrUpdateProductDetails(List<ProductDetailDto> productDetailDtos, Product product) {
        for (ProductDetailDto productDetailDto : productDetailDtos) {
            ProductDetail productDetail = new ProductDetail();
            productDetail.setProductID(product.getId());
            productDetail.setSize(productDetailDto.getSize());
            productDetail.setSurcharge(productDetailDto.getSurcharge());

            if (productDetailDto.getId() != null) {
                productDetail.setId(productDetailDto.getId());
            }

            productDetailRepository.save(productDetail);

        }
    }

    private void saveOrUpdateProductImages(List<ProductImageDto> productImages, Product product) {
        for (ProductImageDto imageUrl : productImages) {
            ProductImage productImage = new ProductImage();
            productImage.setProductID(product.getId());
            productImage.setUrl(imageUrl.getUrl());

            if (imageUrl.getId() != null) {
                productImage.setId(imageUrl.getId());
            }

            productImageRepository.save(productImage);

        }
    }

    private void saveOrUpdateProductToppings(List<ProductToppingDto> productToppingDtos, Product product) {
        for (ProductToppingDto topping : productToppingDtos) {
            ProductTopping productTopping = new ProductTopping();
            productTopping.setProductID(product.getId());
            productTopping.setToppingID(topping.getToppingID());

            if (productTopping.getId() != null) {
                productTopping.setId(topping.getId());
            }

            productToppingRepository.save(productTopping);

        }
    }

    private void removeProductImages(List<ProductImageDto> removedImageDtos) {
        for (ProductImageDto image: removedImageDtos) {
            productImageRepository.deleteById(image.getId());
        }
    }

    private void removeProductToppings(List<ProductToppingDto> removedToppings) {
        for (ProductToppingDto productToppingDto: removedToppings) {
            productToppingRepository.deleteById(productToppingDto.getId());
        }
    }

    private void removeProductDetails(List<ProductDetailDto> removedDetailDtos) {
        for (ProductDetailDto size: removedDetailDtos) {
            productDetailRepository.deleteById(size.getId());
        }
    }


    @Override
    public List<ProductDto> getProductsByTypeID(Long typeID) {
        List<Product> products = productRepository.getProductsByTypeID(typeID);
        List<Topping> toppings = toppingRepository.findAll();

        return products.stream()
                .map(product -> {
                    List<ProductDetail> productDetails = productDetailRepository.findAllByProductID(product.getId());
                    List<ProductDetailDto> productDetailDtos = ProductMapper.mapToProductDetailDtoList(productDetails);

                    List<ProductImage> productImages = productImageRepository.findAllByProductID(product.getId());
                    List<ProductImageDto> productImageDtos = ProductMapper.mapProductImagesToDto(productImages);

                    List<ProductTopping> productToppings = productToppingRepository.findByProductID(product.getId());
                    List<ProductToppingDto> productToppingDtos = ProductMapper.mapProductToppingsDto(productToppings, toppings);
                    return ProductMapper.mapToProductDto(product, productDetailDtos, productImageDtos, productToppingDtos);
                })
                .collect(Collectors.toList());
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


}
