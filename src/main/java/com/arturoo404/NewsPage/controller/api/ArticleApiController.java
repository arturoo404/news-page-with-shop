package com.arturoo404.NewsPage.controller.api;

import com.arturoo404.NewsPage.service.ArticleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/article")
public class ArticleApiController {

    private final ArticleService articleService;

    public ArticleApiController(ArticleService articleService) {
        this.articleService = articleService;
    }
}
