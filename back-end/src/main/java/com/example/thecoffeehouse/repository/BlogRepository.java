package com.example.thecoffeehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.thecoffeehouse.entity.Blog;

public interface BlogRepository extends JpaRepository<Blog, Long>{

}
