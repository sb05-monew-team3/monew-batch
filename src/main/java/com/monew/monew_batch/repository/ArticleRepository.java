package com.monew.monew_batch.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monew.monew_batch.entity.Article;

public interface ArticleRepository extends JpaRepository<Article, UUID> {
}
