package com.arturoo404.NewsPage.service;

import com.arturoo404.NewsPage.entity.article.Article;
import com.arturoo404.NewsPage.entity.article.dto.CreateArticleDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ArticleService {
    Article addArticle(CreateArticleDto createArticleDto);

    void addArticlePhoto(MultipartFile photo, Long id) throws IOException;

    Integer getNumberOfPhotos(Long articleId);
}
