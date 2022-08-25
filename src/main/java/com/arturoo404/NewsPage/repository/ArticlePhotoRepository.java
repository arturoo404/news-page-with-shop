package com.arturoo404.NewsPage.repository;

import com.arturoo404.NewsPage.entity.photo.ArticlePhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticlePhotoRepository extends JpaRepository<ArticlePhoto, Long> {

    @Query(value = "FROM ArticlePhoto p WHERE p.content.article.id = ?1 AND  p.photoPosition = ?2")
    ArticlePhoto findByArticleIdAndPosition(Long articleId, Integer photoPosition);
}
