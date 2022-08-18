package com.arturoo404.NewsPage.repository;

import com.arturoo404.NewsPage.entity.article.Article;
import com.arturoo404.NewsPage.enums.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query(value = "FROM Article a WHERE a.id = ?1")
    Article findArticleById(Long id);
}
