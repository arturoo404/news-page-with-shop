package com.arturoo404.NewsPage.service.impl;

import com.arturoo404.NewsPage.repository.ArticleRepository;
import com.arturoo404.NewsPage.service.ArticleService;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }
}
