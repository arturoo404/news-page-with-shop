package com.arturoo404.NewsPage.repository;

import com.arturoo404.NewsPage.entity.article.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
}
