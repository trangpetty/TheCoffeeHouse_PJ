package com.example.thecoffeehouse.repository.product;

import com.example.thecoffeehouse.entity.product.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    List<Hashtag> findByNameContains(String name);

}
