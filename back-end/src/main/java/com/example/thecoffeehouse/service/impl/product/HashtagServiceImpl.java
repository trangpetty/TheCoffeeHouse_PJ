package com.example.thecoffeehouse.service.impl.product;

import com.example.thecoffeehouse.dto.product.*;
import com.example.thecoffeehouse.entity.mapper.ProductMapper;
import com.example.thecoffeehouse.entity.product.Hashtag;
import com.example.thecoffeehouse.entity.product.Product;
import com.example.thecoffeehouse.entity.product.ProductHashtag;
import com.example.thecoffeehouse.entity.product.Topping;
import com.example.thecoffeehouse.repository.product.*;
import com.example.thecoffeehouse.service.product.HashtagService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HashtagServiceImpl implements HashtagService {

    private final HashtagRepository hashtagRepository;

    private final ProductRepository productRepository;

    private final ProductHashtagRepository productHashtagRepository;

    private final ProductDetailRepository productDetailRepository;

    private final ProductImageRepository productImageRepository;

    private final ProductToppingRepository productToppingRepository;

    private final ToppingRepository toppingRepository;

    public HashtagServiceImpl(HashtagRepository hashtagRepository, ProductRepository productRepository, ProductHashtagRepository productHashtagRepository, ProductDetailRepository productDetailRepository, ProductImageRepository productImageRepository, ProductToppingRepository productToppingRepository, ToppingRepository toppingRepository) {
        this.hashtagRepository = hashtagRepository;
        this.productRepository = productRepository;
        this.productHashtagRepository = productHashtagRepository;
        this.productDetailRepository = productDetailRepository;
        this.productImageRepository = productImageRepository;
        this.productToppingRepository = productToppingRepository;
        this.toppingRepository = toppingRepository;
    }

    @Override
    public Hashtag createHashtag(HashtagDto hashtagDto) {
        Hashtag hashtag = new Hashtag();
        hashtag.setName(hashtagDto.getName());
        Hashtag newHashtag = hashtagRepository.save(hashtag);
        List<Long> productIDs = hashtagDto.getProducts();
        if (productIDs != null && !productIDs.isEmpty()) {
            List<ProductHashtag> productHashtags = productIDs.stream()
                    .map(productID -> new ProductHashtag(productID, newHashtag.getId()))
                    .toList();

            // Lưu tất cả các ProductHashtag liên kết với hashtag mới
            productHashtagRepository.saveAll(productHashtags);
        }
        return newHashtag;
    }

    @Override
    public List<HashtagDto> getHashtagWithProducts() {
        List<Hashtag> hashtags = hashtagRepository.findAll();
        return hashtags.stream()
                .map(hashtag -> {
            HashtagDto hashtagDto = new HashtagDto();
            hashtagDto.setId(hashtag.getId());
            hashtagDto.setName(hashtag.getName());
            List<ProductHashtag> productHashtags = productHashtagRepository.findByHashtagId(hashtag.getId());
            if(productHashtags != null && !productHashtags.isEmpty()) {
                List<Long> productIDs = new ArrayList<>();
                for (ProductHashtag productHashtag : productHashtags) {
                    productIDs.add(productHashtag.getProductId());
                }
                hashtagDto.setProducts(productIDs);
            }
            return hashtagDto;
        })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteHashtag(Long id) {
        Hashtag hashtag = hashtagRepository.findById(id)
                .orElseThrow();
        hashtagRepository.deleteById(id);
    }

    @Override
    public HashtagDto updateHashtag(Long id, HashtagDto newHashtag) {
        // Tìm hashtag cần cập nhật, nếu không có thì ném ngoại lệ
        Hashtag hashtag = hashtagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hashtag not found"));

        // Cập nhật tên của hashtag
        hashtag.setName(newHashtag.getName());
        hashtagRepository.save(hashtag);

        // Khởi tạo HashtagDto và gán tên mới
        HashtagDto hashtagDto = new HashtagDto();
        hashtagDto.setId(hashtag.getId());
        hashtagDto.setName(newHashtag.getName());

        // Lấy danh sách các sản phẩm hiện tại có liên kết với hashtag
        List<ProductHashtag> existingProductHashtags = productHashtagRepository.findByHashtagId(id);

        // Tạo danh sách các sản phẩm mới để thêm
        List<Long> newProductIDs = newHashtag.getProducts();

        // Lọc ra những productID chưa tồn tại trong danh sách hiện tại
        if (newProductIDs != null && !newProductIDs.isEmpty()) {
            List<Long> existingProductIDs = existingProductHashtags.stream()
                    .map(ProductHashtag::getProductId)
                    .toList();

            List<Long> productsToAdd = newProductIDs.stream()
                    .filter(productID -> !existingProductIDs.contains(productID))
                    .toList();

            // Thêm những sản phẩm mới
            for (Long productID : productsToAdd) {
                ProductHashtag productHashtag = new ProductHashtag();
                productHashtag.setHashtagId(id);
                productHashtag.setProductId(productID);
                productHashtagRepository.save(productHashtag);
            }

            // Gán danh sách productID vào HashtagDto
            hashtagDto.setProducts(newProductIDs);
        }

        return hashtagDto;
    }

    @Override
    public List<ProductDto> getProductsWithHashtag(String hashtag) {
        List<Hashtag> hashtags = hashtagRepository.findByNameContains(hashtag);
        if (hashtags.isEmpty()) {
            return List.of(); // Không có hashtag nào khớp
        }
        // Lấy danh sách hashtagIds
        List<Long> hashtagIds = hashtags.stream()
                .map(Hashtag::getId)
                .collect(Collectors.toList());

        // Lấy tất cả ProductHashtag dựa trên hashtagIds
        List<ProductHashtag> productHashtags = productHashtagRepository.findByHashtagIdIn(hashtagIds);

        if (productHashtags.isEmpty()) {
            return List.of(); // Không có sản phẩm nào khớp với các hashtags
        }

        // Lấy danh sách productId từ productHashtags
        List<Long> productIds = productHashtags.stream()
                .map(ProductHashtag::getProductId)
                .collect(Collectors.toList());

        // Tìm danh sách sản phẩm theo productIds
        List<Product> products = productRepository.findAllById(productIds);
        List<Topping> toppings = toppingRepository.findAll();

        // Chuyển đổi các Product entity thành ProductDto
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


}
