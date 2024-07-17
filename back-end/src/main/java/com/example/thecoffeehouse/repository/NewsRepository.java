package com.example.thecoffeehouse.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.thecoffeehouse.entity.News;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long>{
    @Query("SELECT n FROM News n WHERE (:type IS NULL OR n.type LIKE %:type%)")
    Page<News> findByType(@Param("type") String type, Pageable pageable);

    @Query("SELECT n FROM News n WHERE (:type IS NULL OR n.type LIKE %:type%)")
    List<News> findListByType(@Param("type") String type);

    @Query("SELECT n " +
            "FROM News n " +
            "ORDER BY n.createTime DESC LIMIT 4")
    List<News> getNewestNews();
}
