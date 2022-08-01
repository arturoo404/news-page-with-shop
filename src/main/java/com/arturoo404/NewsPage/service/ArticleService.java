package com.arturoo404.NewsPage.service;

import com.arturoo404.NewsPage.entity.article.Article;
import com.arturoo404.NewsPage.entity.article.dto.CreateArticleDto;

public interface ArticleService {
    Article addArticle(CreateArticleDto createArticleDto);
}
