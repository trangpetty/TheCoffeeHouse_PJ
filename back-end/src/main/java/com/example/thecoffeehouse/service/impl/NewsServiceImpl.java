package com.example.thecoffeehouse.service.impl;

import com.example.thecoffeehouse.entity.News;
import com.example.thecoffeehouse.repository.NewsRepository;
import com.example.thecoffeehouse.service.NewsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;

    public NewsServiceImpl(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @Override
    public News createNews(News news) {
        return newsRepository.save(news);
    }

    @Override
    public Page<News> getAllNews(String type, Pageable pageable) {
        return newsRepository.findByType(type, pageable);
    }

    @Override
    public News updateNews(Long id, News updateNews) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News does not exists"));
        news.setUserID(updateNews.getUserID());
        news.setTitle(updateNews.getTitle());
        news.setContent(updateNews.getContent());
        news.setType(updateNews.getType());
        news.setImage(updateNews.getImage());

        return newsRepository.save(news);
    }

    @Override
    public void deleteNews(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News does not exists"));
        newsRepository.deleteById(id);

    }

    @Override
    public List<News> getListNews(String type) {
        return newsRepository.findListByType(type);
    }

    @Override
    public Optional<News> getNewsById(Long id) {
        return newsRepository.findById(id);
    }

    @Override
    public List<News> getNewestNews() {
        return newsRepository.getNewestNews();
    }
}
