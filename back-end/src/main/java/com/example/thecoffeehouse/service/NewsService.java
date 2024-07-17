package com.example.thecoffeehouse.service;

import com.example.thecoffeehouse.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface NewsService {
    News createNews(News news);

    Page<News> getAllNews(String type, Pageable pageable);

    News updateNews(Long id, News News);

    void deleteNews(Long id);

    List<News> getListNews(String type);

    Optional<News> getNewsById(Long id);

    List<News> getNewestNews();
}
