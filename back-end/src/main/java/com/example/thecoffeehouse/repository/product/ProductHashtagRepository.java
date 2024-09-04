package com.example.thecoffeehouse.repository.product;

import com.example.thecoffeehouse.entity.product.ProductHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductHashtagRepository extends JpaRepository<ProductHashtag, Long> {
    List<ProductHashtag> findByHashtagId(Long hashtagID);

    List<ProductHashtag> findByHashtagIdIn(List<Long> hashtagIds);
}
