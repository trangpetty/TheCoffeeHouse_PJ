package com.example.thecoffeehouse.controller;

import com.example.thecoffeehouse.entity.News;
import com.example.thecoffeehouse.service.NewsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
public class NewsController {
    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @PostMapping
    public ResponseEntity<News> save(@RequestBody News news) {
        return new ResponseEntity<>(newsService.createNews(news), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<News>> getAll(@RequestParam(required = false) String type,
                                             @RequestParam(defaultValue = "0") int pageNo,
                                             @RequestParam(defaultValue = "10") int pageSize) {
        if (pageNo > 0) {
            pageNo = pageNo - 1;
        }
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return ResponseEntity.ok(newsService.getAllNews(type, pageable));
    }

    @GetMapping("/all")
    public ResponseEntity<List<News>> getList(@RequestParam(required = false) String type) {
        return ResponseEntity.ok(newsService.getListNews(type));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(newsService.getNewsById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<News> update(@PathVariable Long id, @RequestBody News news) {
        return ResponseEntity.ok(newsService.updateNews(id, news));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        newsService.deleteNews(id);
        return ResponseEntity.ok("success");
    }
}
