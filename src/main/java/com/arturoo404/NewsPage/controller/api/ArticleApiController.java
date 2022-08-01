package com.arturoo404.NewsPage.controller.api;

import com.arturoo404.NewsPage.entity.article.dto.CreateArticleDto;
import com.arturoo404.NewsPage.service.ArticleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/article")
public class ArticleApiController {

    private final ArticleService articleService;

    public ArticleApiController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping(path = "/add")
    ResponseEntity<Object> addArticle(@RequestBody CreateArticleDto createArticleDto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(articleService.addArticle(createArticleDto));
    }
}
